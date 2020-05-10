package com.activities.theatreticketsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.classfiles.theatreticketsapp.Basket;
import com.classfiles.theatreticketsapp.Show;
import com.classfiles.theatreticketsapp.User;
import com.classfiles.theatreticketsapp.Venue;
import com.configfiles.theatreticketsapp.DatabaseAPI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ShowInformation extends AppCompatActivity {

    private Show mShow;
    private Venue venue;
    private Basket basket;
    private ProgressBar progressBar;
    private TextView showDescription, showName, venueName, startDate, endDate, runningTime;
    private User user;
    private RequestQueue requestQueue;
    private RatingBar rating;
    private Button viewReviews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_show);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        progressBar = findViewById(R.id.progressBar);
        showDescription = findViewById(R.id.showDescription);

        requestQueue = Volley.newRequestQueue(this);

        Intent i = getIntent();
        mShow = i.getParcelableExtra("live_show");
        basket = i.getParcelableExtra("basket");
        user = i.getParcelableExtra("user");

        setTitle(mShow.getShowName());

        venue = mShow.getVenue();
        showName = findViewById(R.id.showName);
        venueName = findViewById(R.id.venueName);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        runningTime = findViewById(R.id.runningTime);
        rating = findViewById(R.id.ratingBar);

        viewReviews = findViewById(R.id.viewReviewsBtn);
        viewReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewReviews();
            }
        });

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

    public void onVenueClick(View view) {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("venue", venue);
        startActivity(intent);
    }

    public void viewReviews() {
        Intent intent = new Intent(this, ViewReviews.class);
        intent.putExtra("live_show", mShow);
        startActivity(intent);
    }

    private void loadShow() {
        StringRequest request = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_SHOW_INFO + mShow.getShowName(), new Response.Listener<String>() {
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
                    showDescription.setText(show.getString("showDesc"));
                    showDescription.setVisibility(View.VISIBLE);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                    Date startDateParsed = sdf.parse(mShow.getStartDate());
                    Date endDateParsed = sdf.parse(mShow.getEndDate());

                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

                    String sd = sdf2.format(startDateParsed);
                    String ed = sdf2.format(endDateParsed);

                    startDate.setText(getString(R.string.start_date) + sd);
                    startDate.setVisibility(View.VISIBLE);
                    endDate.setText(getString(R.string.end_date) + ed);
                    endDate.setVisibility(View.VISIBLE);

                    runningTime.setText("Running time: " + mShow.getRunningTime() + " minutes");
                    runningTime.setVisibility(View.VISIBLE);


                } catch (JSONException | ParseException e) {
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


    private void loadRating() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseAPI.URL_GET_REVIEWS + mShow.getShowName(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                if (!response.equals("false")) {
                    try {
                        JSONArray reviews = new JSONArray(response);
                        double total = 0.0;
                        for (int i = 0; i < reviews.length(); i++) {
                            JSONObject review = reviews.getJSONObject(i);
                            total += review.getInt("rating");
                        }
                        try {
                            mShow.setRating(total / reviews.length());
                            rating.setRating((float) mShow.getRating());
                            rating.setVisibility(View.VISIBLE);
                            viewReviews.setVisibility(View.VISIBLE);
                        } catch (ArithmeticException ignored) {
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
