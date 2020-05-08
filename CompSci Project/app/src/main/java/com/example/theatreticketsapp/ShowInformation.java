package com.example.theatreticketsapp;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowInformation extends AppCompatActivity {

    private Show mShow;
    private Venue venue;
    private Basket basket;
    private ProgressBar progressBar;
    private TextView showDescription, showName, venueName, endDate, rating;
    private User user;
    private String previousActivity;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_show);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progressBar);
        showDescription = findViewById(R.id.showDescription);
        showDescription.setMovementMethod(new ScrollingMovementMethod());

        requestQueue = Volley.newRequestQueue(this);

        Intent i = getIntent();
        mShow = i.getParcelableExtra("live_show");
        basket = i.getParcelableExtra("basket");
        user = i.getParcelableExtra("user");
        previousActivity = i.getStringExtra("previous_activity");


        venue = mShow.getVenue();


        showName = findViewById(R.id.showName);
        venueName = findViewById(R.id.venueName);
        endDate = findViewById(R.id.endDate);
        rating = findViewById(R.id.showRating);

        loadShow();
        loadRating();




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowInformation.this, ChooseDate.class);
                intent.putExtra("live_show", mShow);
                intent.putExtra("basket", basket);
                intent.putExtra("user", user);
                startActivity(intent);
                overridePendingTransition(R.transition.slide_in_down, R.transition.slide_out_up);

            }
        });


    }

    public void onBackPressed() {

        if (previousActivity != null)
            finish();
        else {
            Intent intent = new Intent(this, Homepage.class);
            intent.putExtra("basket", basket);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }
    public void onVenueClick(View view){
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            intent.putExtra("venue", venue);
            startActivity(intent);
    }

    public void viewReviews(View view){
        Intent intent = new Intent(this, ViewReviews.class);
        intent.putExtra("live_show", mShow);
        startActivity(intent);
    }

    private void loadShow(){
        StringRequest request = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_SHOW_INFO+mShow.getShowName(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println(response);
                try {
                    JSONObject show = new JSONObject(response);
                    mShow.setShowDescription(show.getString("showDesc"));
                    mShow.setRunningTime(show.getInt("runningTime"));

                    progressBar.setVisibility(View.INVISIBLE);

                    showName.setText(mShow.getShowName());
                    showName.setVisibility(View.VISIBLE);
                    venueName.setText(mShow.getVenueName());
                    venueName.setVisibility(View.VISIBLE);
                    showDescription.setVisibility(View.VISIBLE);
                    endDate.setText("Booking until: " + mShow.getEndDate());
                    endDate.setVisibility(View.VISIBLE);



                } catch (JSONException e) {
                    Toast.makeText(ShowInformation.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShowInformation.this, "Error", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);

    }


    private void loadRating(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseAPI.URL_GET_REVIEWS + mShow.getShowName(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray reviews = new JSONArray(response);
                    double total=0.0;
                    for(int i=0; i<reviews.length(); i++){
                        JSONObject review = reviews.getJSONObject(i);
                        total += review.getInt("rating");
                    }
                    try {
                        mShow.setRating(total / reviews.length());
                        rating.setText(Double.toString(mShow.getRating()));
                    }catch(ArithmeticException e){
                        rating.setText(R.string.string_not_enough_reviews);
                    }

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

}
