package com.example.theatreticketsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

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


    Basket basket;
    User user;
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Homepage.class);
        intent.putExtra("basket", basket);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, Homepage.class);
            intent.putExtra("basket", basket);
            intent.putExtra("user", user);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_home);
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        basket = intent.getParcelableExtra("basket");
        user = intent.getParcelableExtra("user");

        if (basket.numberOfBookings() > 1)
            subject = "Your booking to see " + basket.numberOfBookings() + " shows";
        else
            subject = "Your booking to see " + basket.getBookings().get(0).getShowName();





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
                            EmailConfig.email, user.getEmail());
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7171){
            if (resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null){
                    try {
                        JSONObject paymentDetails = confirmation.toJSONObject().getJSONObject("response");

                        if (paymentDetails.getString("state").equals("approved")) {
                            displayBooking();
                            sendMail();
                        }else
                            Toast.makeText(this, "Not approved", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private void displayBooking(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Payment successful");

        View view = View.inflate(this, R.layout.dialog_booking_complete, null);
        dialog.setView(view);
        dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();


        StringBuilder toDisplay = new StringBuilder();
        ArrayList<BasketBooking> bookings;
        bookings = basket.getBookings();
        BasketBooking booking;

        Date date;

        for (int i = 0; i < basket.numberOfBookings(); i++) {
            booking = bookings.get(i);
            date = formatDate(booking);
            toDisplay.append(booking.getShowName()).append("\n").append(date.toString()).append(" x")
                    .append(booking.getNumberOfTickets()).append("\n£").append(booking.getCost()).append("\n\n");
        }

        toDisplay.append("Total cost: £").append(basket.getTotalCost());

        bookingDetails.setText(toDisplay.toString());

        content = "Dear " + user.getFirstName() + ",\n \n" +
                "Thank you for your booking. Please see the details below:\n \n" +
                toDisplay.toString() + "\n \nYou can access your tickets in the My Bookings section of the app.\nMany thanks";

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
                        params.put("userID", Integer.toString(user.getId()));
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
