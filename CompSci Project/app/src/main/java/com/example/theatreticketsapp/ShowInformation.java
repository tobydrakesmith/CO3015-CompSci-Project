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

    Show mShow;
    Venue venue;
    Basket basket;
    ProgressBar progressBar;
    TextView showDescription, showName, venueName, showDesc, endDate, rating;
    int userID;

    RequestQueue requestQueue;


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
        userID = i.getIntExtra("userid", -1);


        showName = findViewById(R.id.showName);
        venueName = findViewById(R.id.venueName);
        showDesc = findViewById(R.id.showDescription);
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

        requestQueue.add(stringRequest);
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

                    progressBar.setVisibility(View.INVISIBLE);

                    showName.setText(mShow.getShowName());
                    showName.setVisibility(View.VISIBLE);
                    venueName.setText(mShow.getVenueName());
                    venueName.setVisibility(View.VISIBLE);
                    showDesc.setVisibility(View.VISIBLE);
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
                        rating.setText("Not enough reviews for this to be displayed yet!");
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
