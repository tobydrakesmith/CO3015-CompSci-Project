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
import android.provider.CalendarContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Locale;
import java.util.Map;

public class Checkout extends AppCompatActivity implements CheckoutRecyclerViewAdapter.OnCalendarClick{


    //TODO forced error for paypal

    private Basket basket;
    private User user;
    private RequestQueue requestQueue;
    private String subject, content;
    private ArrayList<BasketBooking> bookings = new ArrayList<>();

    private static PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, PayPalService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        TextView totalCost = findViewById(R.id.totalBookingCost);


        Intent intent = getIntent();
        basket = intent.getParcelableExtra("basket");
        user = intent.getParcelableExtra("user");
        
        bookings.addAll(basket.getBookings());

        CheckoutRecyclerViewAdapter checkoutRecyclerViewAdapter = new CheckoutRecyclerViewAdapter(this, this, bookings);

        if (basket.numberOfBookings() > 1)
            subject = "Your booking to see " + basket.numberOfBookings() + " shows";
        else
            subject = "Your booking to see " + basket.getBookings().get(0).getShowName();

        requestQueue = Volley.newRequestQueue(this);


        Intent payPalService = new Intent(this, PayPalService.class);
        payPalService.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        startService(payPalService);

        RecyclerView recyclerView = findViewById(R.id.checkoutRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(checkoutRecyclerViewAdapter);

        totalCost.setText("Total cost: £"+basket.getTotalCost());


        processPayment();
    }
    
    @Override
    public void onCalendarClick(int position){
        calendarSync(bookings.get(position));
    }

    private void calendarSync(BasketBooking booking) {

        Intent insertCalendarIntent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI);
        insertCalendarIntent.putExtra(CalendarContract.Events.TITLE, booking.getShowName());
        insertCalendarIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
        insertCalendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, booking.getActualDate().getTime());
        insertCalendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, (booking.getActualDate().getTime()) + ((booking.getShow().getRunningTime())*60000));
        insertCalendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, booking.getShow().getVenue().getStrLocation());
        startActivity(insertCalendarIntent);
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


        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseAPI.URL_SEND_BOOKING_EMAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", user.getEmail());
                params.put("subject", subject);
                params.put("content", content);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7171){
            if (resultCode == RESULT_OK){
                assert data != null;
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

        final String NEWLINE = "newline";
        final String POUNDSYMBOL = "poundsymbol";


        StringBuilder toDisplay = new StringBuilder();
        ArrayList<BasketBooking> bookings;
        bookings = basket.getBookings();

        Date date;

        for (BasketBooking booking : bookings) {
            date = formatDate(booking);
            toDisplay.append(booking.getShowName()).append(NEWLINE).append(booking.getShow().getVenue().getVenueName())
                    .append(NEWLINE).append(date.toString()).append(" x")
                    .append(booking.getNumberOfTickets()).append(NEWLINE + POUNDSYMBOL).append(booking.getTickets().get(0).getPrice()).append(" each")
                    .append(NEWLINE + NEWLINE);
        }

        toDisplay.append("Total cost: "+POUNDSYMBOL).append(basket.getTotalCost()).append(NEWLINE);

        content = "Dear " + user.getFirstName() + ", " + NEWLINE + NEWLINE +
                "Thank you for your booking. Please see the details below: " + NEWLINE + NEWLINE +
                toDisplay.toString() + NEWLINE + "You can access your tickets in the My Bookings section of the app." +
                NEWLINE + NEWLINE + "Many thanks, " + NEWLINE + "Theatre Tickets App";

        basketToDb();

    }

    private Date formatDate(BasketBooking booking){
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(booking.getDate());

            assert date != null;
            String strDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(date);

            date = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.ENGLISH).parse(strDate+ " " + booking.getStartTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        booking.setActualDate(date);
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
