package com.example.theatreticketsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Checkout extends AppCompatActivity {

    //TODO: ADD cost for each booking and total cost to display & generally improve the UI and what is displayed/sent in email

    Basket basket;
    int userID;
    TextView bookingDetails;
    RequestQueue requestQueue;
    String subject, content;

    private static PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, PayPalService.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);


        Intent intent = getIntent();
        basket = intent.getParcelableExtra("basket");
        userID = intent.getIntExtra("userid", -1);


        ImageView sendMail = findViewById(R.id.emailBtn);
        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

        if (basket.numberOfBookings() > 1){
            subject = "Your booking to see " + basket.numberOfBookings() + " shows";
        }else{
            subject = "Your booking to see " + basket.getBookings().get(0).getShowName();
        }




        requestQueue = Volley.newRequestQueue(this);

        bookingDetails = findViewById(R.id.bookingSummary);

        Intent payPalService = new Intent(this, PayPalService.class);
        payPalService.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        startService(payPalService);

        processPayment();
    }

    private void processPayment(){

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(basket.getTotalCost()), "GBP",
                "Theatre booking", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent process = new Intent(this, PaymentActivity.class);
        process.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        process.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(process, 7171);
    }



    private void sendMail(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(EmailConfig.email,
                            EmailConfig.password);
                    sender.sendMail(subject, content,
                            EmailConfig.email, "toby_drakesmith95@hotmail.com");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("On result");
        System.out.println("Basket size: " + basket.size());
        if (requestCode == 7171){
            if (resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null){
                    try {
                        JSONObject paymentDetails = confirmation.toJSONObject().getJSONObject("response");

                        if (paymentDetails.getString("state").equals("approved"))
                            displayBooking();
                        else
                            Toast.makeText(this, "Not approved", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID)
                System.out.println("ERROR");
        }
    }

    private void displayBooking(){

        String toDisplay = "";
        ArrayList<BasketBooking> bookings;
        bookings = basket.getBookings();
        BasketBooking booking = bookings.get(0);

        Date date = formatDate(booking);

        toDisplay = booking.getShowName() + " " + date.toString() +
                " x" + booking.getNumberOfTickets();

        for (int i = 1; i < basket.numberOfBookings(); i++) {
            booking = bookings.get(i);
            date = formatDate(booking);
            toDisplay += "\n\n" + booking.getShowName() + " " + date.toString() +
                    " x" + booking.getNumberOfTickets();
        }

        bookingDetails.setText(toDisplay);
        content = toDisplay;

        basketToDb();

    }

    private Date formatDate(BasketBooking booking){
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(booking.getDate());

            String strDate = new SimpleDateFormat("dd-MM-yyyy").format(date);

            date = new SimpleDateFormat("dd-MM-yyyy hh:mm").parse(strDate+ " " + booking.getStartTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private void basketToDb() {

        RequestQueue queue = Volley.newRequestQueue(this);

        if (!basket.isEmpty()) {
            for (int i = 0; i < basket.getBookings().size(); i++) {

                final BasketBooking booking = basket.getBookings().get(i);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseAPI.URL_CREATE_BOOKING, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getString("error").equals("false")){
                                int bookingID = jsonResponse.getInt("bookingid");
                                booking.setBookingID(bookingID);

                                ticketsToDb(booking);

                            } else {
                                System.out.println("ERROR: " + jsonResponse.getString("message"));
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("instanceID", Integer.toString(booking.getShow().getId()));
                        params.put("userID", Integer.toString(userID));
                        params.put("numberOfTickets", Integer.toString(booking.getNumberOfTickets()));
                        params.put("date", booking.getDate());
                        params.put("showTime", booking.getStartTime());
                        params.put("showName", booking.getShowName());
                        params.put("tempID", Integer.toString(booking.getTempID()));
                        return params;
                    }
                };

                queue.add(stringRequest);


            }
        }
    }

    private void ticketsToDb(final BasketBooking booking) {

        final int bookingID = booking.getBookingID();

        final ArrayList<Ticket> tickets = basket.getTickets(bookingID);
        basket.releaseTickets(booking);

        for (int i=0; i<tickets.size(); i++){
            final int x = i;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseAPI.URL_CREATE_TICKET, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();

                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("bookingID", Integer.toString(bookingID));
                    params.put("price", Integer.toString(tickets.get(x).getPrice()));
                    params.put("priceBand", tickets.get(x).getPriceBand());

                    return params;
                }

            };

            requestQueue.add(stringRequest);

        }
    }
}
