package com.example.theatreticketsapp;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SelectedShow extends AppCompatActivity {

    Show mShow;
    Basket basket;
    ProgressBar progressBar;
    long milliLeft;
    int userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_show);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progressBar);

        Intent i = getIntent();
        mShow = i.getParcelableExtra("live_show");
        basket = i.getParcelableExtra("basket");
        userID = i.getIntExtra("userid", -1);

        milliLeft = basket.getMilliLeft();

        CountDownTimer countDownTimer = new CountDownTimer(milliLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                basket.setMilliLeft(millisUntilFinished);
            }
            @Override
            public void onFinish() {
                basket.releaseTickets();
            }
        };

        if (!basket.isEmpty()) {
            System.out.println("Basket not empty - TIMER START");
            countDownTimer.start();
        }


        loadShow();



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectedShow.this, BookShow.class);
                intent.putExtra("live_show", mShow);
                intent.putExtra("basket", basket);
                intent.putExtra("userid", userID);
                startActivity(intent);
                overridePendingTransition(R.transition.slide_in_down, R.transition.slide_out_up);

            }
        });


    }

    @Override //TODO: Add animation
    public void onBackPressed() {
        Intent intent = new Intent (this, Homepage.class);
        intent.putExtra("basket", basket);
        intent.putExtra("live_show", mShow);
        intent.putExtra("userid", userID);
        startActivity(intent);
    }

    private void loadShow(){
        StringRequest request = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_SHOW_INFO+mShow.getShowName(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject show = new JSONObject(response);
                    mShow.setShowDescription(show.getString("showDesc"));

                    progressBar.setVisibility(View.INVISIBLE);

                    TextView showName = findViewById(R.id.showName);
                    TextView venueName = findViewById(R.id.venueName);
                    TextView showDesc = findViewById(R.id.showDescription);
                   // TextView startDate = findViewById(R.id.startDate);
                    TextView endDate = findViewById(R.id.endDate);
                    ImageView image = findViewById(R.id.showImage);

                    showName.setText(mShow.getShowName());
                    showName.setVisibility(View.VISIBLE);
                    venueName.setText("Venue name: " + mShow.getVenueName());
                    venueName.setVisibility(View.VISIBLE);
                    showDesc.setVisibility(View.VISIBLE);
                    //showDesc.setText("Show description: " + mShow.getShowDescription());
                   // startDate.setText("Start date: " + mShow.getStartDate());
                    endDate.setText("Booking until: " + mShow.getEndDate());
                    endDate.setVisibility(View.VISIBLE);
                    image.setVisibility(View.VISIBLE);



                } catch (JSONException e) {
                    Toast.makeText(SelectedShow.this, e.getMessage(), Toast.LENGTH_LONG);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelectedShow.this, "Error", Toast.LENGTH_LONG);
            }
        });

        Volley.newRequestQueue(this).add(request);

    }

}
