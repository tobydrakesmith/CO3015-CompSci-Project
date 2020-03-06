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
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowInformation extends AppCompatActivity {

    Show mShow;
    Venue venue;
    Basket basket;
    ProgressBar progressBar;
    TextView showDescription;
    int userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_show);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progressBar);
        showDescription = findViewById(R.id.showDescription);
        showDescription.setMovementMethod(new ScrollingMovementMethod());

        Intent i = getIntent();
        mShow = i.getParcelableExtra("live_show");
        basket = i.getParcelableExtra("basket");
        userID = i.getIntExtra("userid", -1);



        loadShow();



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowInformation.this, ChooseDate.class);
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

    public void onVenueClick(View view){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_VENUE_INFO + mShow.getVenueName(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String postcode = jsonObject.getString("postcode");
                    String description = jsonObject.getString("venueDescription");
                    venue = new Venue(mShow.getVenueName(), postcode, description);
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("venue", venue);
                    startActivity(intent);
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

        Volley.newRequestQueue(this).add(stringRequest);
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

                    showName.setText(mShow.getShowName());
                    showName.setVisibility(View.VISIBLE);
                    venueName.setText(mShow.getVenueName());
                    venueName.setVisibility(View.VISIBLE);
                    showDesc.setVisibility(View.VISIBLE);
                    //showDesc.setText("Show description: " + mShow.getShowDescription());
                   // startDate.setText("Start date: " + mShow.getStartDate());
                    endDate.setText("Booking until: " + mShow.getEndDate());
                    endDate.setVisibility(View.VISIBLE);



                } catch (JSONException e) {
                    Toast.makeText(ShowInformation.this, e.getMessage(), Toast.LENGTH_LONG);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShowInformation.this, "Error", Toast.LENGTH_LONG);
            }
        });

        Volley.newRequestQueue(this).add(request);

    }

}
