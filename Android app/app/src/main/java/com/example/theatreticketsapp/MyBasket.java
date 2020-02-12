package com.example.theatreticketsapp;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyBasket extends AppCompatActivity implements BasketRecyclerViewAdapter.OnTicketClickListener{

    HashMap<String,ArrayList<Ticket>> basketMap = new HashMap<>();
    ArrayList<String> showNames;

    Basket  basket;
    Show show;
    String strDate = "";
    TextView numberTix, timeLeft, totalCost;
    long milliseconds;

    private CountDownTimer countDownTimer;
    int userID;

    BasketRecyclerViewAdapter adapter;
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
        strDate = i.getStringExtra("date");
        userID = i.getIntExtra("userid", -1);

        milliseconds = basket.getMilliLeft();

        countDownTimer =  new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft.setText("Seconds left: " + millisUntilFinished/1000);
                basket.setMilliLeft(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                basket.releaseTickets();
                numberTix.setText("Number of tickets in basket: " + basket.size());
                totalCost.setText("Total cost £" + (basket.getTotalCost()));
                basketMap.clear();
                adapter.notifyDataSetChanged();
            }
        };

        numberTix = findViewById(R.id.numberOfTickets);
        timeLeft = findViewById(R.id.timeLeftLbl);
        totalCost = findViewById(R.id.totalCostLbl);



        if (!basket.isEmpty()) {
            countDownTimer.start();
            categoriseTickets();
            showNames = getShowNames();
        }

        adapter = new BasketRecyclerViewAdapter(MyBasket.this, basketMap,MyBasket.this, showNames);

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

    public void categoriseTickets(){

        for(int i = 0; i<basket.size(); i++){
            Ticket ticket = basket.getTicket(i);
            String show = ticket.getShow().getShowName();

            if (basketMap.containsKey(show)){
                ArrayList<Ticket> list = basketMap.get(show);
                list.add(ticket);
            }else{
                ArrayList<Ticket> list = new ArrayList<>();
                list.add(ticket);
                basketMap.put(show, list);
            }
        }
    }

    public ArrayList<String> getShowNames(){

        ArrayList<String> toRet = new ArrayList<>();

        for(Map.Entry<String, ArrayList<Ticket>> entry : basketMap.entrySet())
            toRet.add(entry.getKey());

        return toRet;

    }
    

    @Override
    public void onTicketClick(int position){

        basketMap.remove(showNames.get(position));
        basket.releaseTickets(showNames.get(position));
        showNames.remove(position);
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
            for (int i = 0; i < showNames.size(); i++) {

                String showName = showNames.get(i);

                final Show show = basketMap.get(showName).get(i).getShow();
                final int size = basketMap.get(showName).size();

                Map<String, String> params = new HashMap<>();
                params.put("instanceID", Integer.toString(show.getId()));
                params.put("userID", Integer.toString(userID));
                params.put("numberOfTickets", Integer.toString(size));
                params.put("date", strDate);
                params.put("showName", showName);

                CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, DatabaseAPI.URL_CREATE_BOOKING, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("error").equals("false")){
                                int bookingID = response.getInt("bookingid");
                                System.out.println(bookingID);
                                ticketsToDb(bookingID);
                            }
                            else
                                System.out.println("ERROR: " + response.getString("message"));

                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                    }
                });

                queue.add(jsObjRequest);


            }
        }
    }

    public void ticketsToDb(final int bookingID){

        RequestQueue queue = Volley.newRequestQueue(this);

        for (int i=0; i<showNames.size(); i++){
            final String showName = showNames.get(i);
            for (int j = 0; i < basketMap.get(showName).size(); i++) {

                final int x = j;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseAPI.URL_CREATE_TICKET, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("bookingID", Integer.toString(bookingID));
                        params.put("price", Integer.toString(basketMap.get(showName).get(x).getPrice()));
                        return params;
                    }
                };

                queue.add(stringRequest);

            }
        }
    }








}



