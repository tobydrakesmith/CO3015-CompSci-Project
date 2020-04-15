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
import android.os.Parcelable;
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

    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        requestQueue = Volley.newRequestQueue(this);

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
                if (!basket.isEmpty()) {
                    Intent intent = new Intent(MyBasket.this, Checkout.class);
                    intent.putExtra("basket", basket);
                    intent.putExtra("userid", userID);
                    startActivity(intent);
                }
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
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, Homepage.class);
            intent.putExtra("basket", basket);
            intent.putExtra("userid", userID);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    public void onTicketClick(int position){

        StringRequest stringRequest =  new StringRequest(Request.Method.DELETE,
                DatabaseAPI.URL_DELETE_BASKET_BOOKING+basket.getBookings().get(position).getTempID(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(stringRequest);

        basket.releaseTickets(basket.getBookings().get(position));
        numberTix.setText("Number of tickets in basket: " + basket.size());
        totalCost.setText("Total cost £" + (basket.getTotalCost()));
        adapter.notifyDataSetChanged();

        if(basket.isEmpty()){
            timeLeft.setText("Seconds left: 0");
            countDownTimer.cancel();
        }
    }









}



