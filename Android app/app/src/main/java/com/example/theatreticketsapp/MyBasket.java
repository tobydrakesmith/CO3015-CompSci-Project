package com.example.theatreticketsapp;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyBasket extends AppCompatActivity implements MyBasketRecyclerViewAdapter.OnTicketClickListener{

    Basket basket;
    Show show;
    TextView numberTix, timeLeft, totalCost;

    private CountDownTimer countDownTimer;
    int userID;

    MyBasketRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        Intent i = getIntent();
        basket = i.getParcelableExtra("basket");
        show = i.getParcelableExtra("live_show");
        userID = i.getIntExtra("userid", -1);


        adapter = new MyBasketRecyclerViewAdapter(MyBasket.this,MyBasket.this, basket.getBookings());

        countDownTimer =  new CountDownTimer(basket.getTimeOut() - System.currentTimeMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft.setText("Seconds left: " + ((basket.getTimeOut() - System.currentTimeMillis())/1000));
            }

            @Override
            public void onFinish() {
                basket.releaseTickets();
                numberTix.setText("Number of tickets in basket: " + basket.size());
                totalCost.setText("Total cost £" + (basket.getTotalCost()));
                adapter.notifyDataSetChanged();
            }
        };

        numberTix = findViewById(R.id.numberOfTickets);
        timeLeft = findViewById(R.id.timeLeftLbl);
        totalCost = findViewById(R.id.totalCostLbl);

        if (!basket.isEmpty()) {
            countDownTimer.start();
        }

        recyclerView = findViewById(R.id.basketView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        totalCost.setText("Total cost £" + (basket.getTotalCost()));
        numberTix.setText("Number of tickets in basket: " + basket.size());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basketToDb();
            }
        });
    }



    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, Homepage.class);
        intent.putExtra("basket", basket);
        intent.putExtra("userid", userID);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, Homepage.class);
                intent.putExtra("basket", basket);
                intent.putExtra("userid", userID);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    public void onTicketClick(int position){

        basket.releaseTickets(basket.getBookings().get(position));
        numberTix.setText("Number of tickets in basket: " + basket.size());
        totalCost.setText("Total cost £" + (basket.getTotalCost()));
        adapter.notifyDataSetChanged();

        if(basket.isEmpty()){
            timeLeft.setText("Seconds left: 0");
            countDownTimer.cancel();
        }
    }

    public void basketToDb() {

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
                        return params;
                    }
                };

                queue.add(stringRequest);


            }
        }
    }

    public void ticketsToDb(final BasketBooking booking) {

        final int bookingID = booking.getBookingID();

        final ArrayList<Ticket> tickets = basket.getTickets(bookingID);
        basket.releaseTickets(booking);

        for (int i=0; i<tickets.size(); i++){
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
                  params.put("price", Integer.toString(tickets.get(0).getPrice()));

                  return params;
              }

            };

            Volley.newRequestQueue(this).add(stringRequest);

        }



    }







}



