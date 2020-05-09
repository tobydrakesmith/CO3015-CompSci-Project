package com.example.theatreticketsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ViewBooking extends AppCompatActivity {

    private Booking booking;
    private TextView venueName, ticketDetails, bookingCost;
    private Venue venue;
    private RequestQueue requestQueue;
    private Date date;
    private ArrayList<Ticket> mTickets;
    private String jsonTickets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        mTickets = new ArrayList<>();
        jsonTickets = "";

        requestQueue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        booking = intent.getParcelableExtra("booking");

        date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(booking.getDate());

            String strDate = new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(date);

            date = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.UK).parse(strDate+ " " + booking.getShowTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        FloatingActionButton calendar = findViewById(R.id.fabCalendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncCalendar();
            }
        });
        
        final FloatingActionButton tickets = findViewById(R.id.fabTix);
        tickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewBooking.this, ViewTickets.class);
                i.putExtra("booking", booking);
                i.putExtra("tickets", jsonTickets);
                startActivity(i);
            }
        });
        
        FloatingActionButton navigate = findViewById(R.id.fabMap);
        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent venueIntent = new Intent(ViewBooking.this, MapsActivity.class);
                venueIntent.putExtra("venue", venue);
                startActivity(venueIntent);
            }
        });

        TextView showName = findViewById(R.id.showName);
        TextView showDateTime = findViewById(R.id.showTime);
        venueName = findViewById(R.id.venueName);
        ticketDetails = findViewById(R.id.ticketDetails);
        bookingCost = findViewById(R.id.bookingCost);

        showName.setText(booking.getShowName());
        showDateTime.setText(date.toString());

        getVenueInfo();
        loadTickets();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private void loadTickets(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_TICKETS + booking.getBookingID(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    jsonTickets = jsonArray.toString();
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonTicket = (JSONObject) jsonArray.get(i);
                        Ticket ticket = new Ticket(jsonTicket.getInt("price"), jsonTicket.getString("priceBand"));
                        mTickets.add(ticket);
                    }

                    setTicketLabel();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);

    }

    private void setTicketLabel(){
        StringBuilder toDisplay = new StringBuilder("Your tickets: \n");
        int totalCost=0;
        for (Ticket ticket : mTickets){
            toDisplay.append("Price Band: ").append(ticket.getPriceBand()).append(", Price: £").append(ticket.getPrice()).append("\n");
            totalCost += ticket.getPrice();
        }
        ticketDetails.setText(toDisplay.toString());
        bookingCost.setText("Total cost £" + (totalCost));
    }

    private void getVenueInfo(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_VENUE_INFO_BOOKING+booking.getShowInstanceID(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject venueInfo = new JSONObject(response);
                    String name = venueInfo.getString("name");
                    venueName.setText(name);
                    String description = venueInfo.getString("venueDescription");
                    String postcode = venueInfo.getString("postcode");
                    String city = venueInfo.getString("city");
                    venue = new Venue(name, postcode, description, city);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);
    }

    private void syncCalendar(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_RUNNING_TIME+booking.getShowName(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int runningTime = jsonObject.getInt("runningTime");
                    openCalendar(runningTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(stringRequest);

    }

    private void openCalendar(int runningTime){
        Intent insertCalendarIntent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI);
        insertCalendarIntent.putExtra(CalendarContract.Events.TITLE, booking.getShowName());
        insertCalendarIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
        insertCalendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date.getTime());
        insertCalendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, date.getTime() + runningTime*60000);
        insertCalendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, venue.getStrLocation());
        startActivity(insertCalendarIntent);
    }
}
