package com.example.theatreticketsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewReviews extends AppCompatActivity implements ReviewsRecyclerViewAdapter.OnReviewClickListener {

    Show mShow;
    ArrayList<Review> mReviews;
    RecyclerView recyclerViewReviews;
    ReviewsRecyclerViewAdapter reviewsRecyclerViewAdapter;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);
        Intent intent = getIntent();

        mShow = intent.getParcelableExtra("live_show");
        mReviews = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);


        recyclerViewReviews = findViewById(R.id.reviewView);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));


        loadReviews();

        System.out.println(mReviews.size());
    }


    private void loadReviews() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_REVIEWS_DETAILED + mShow.getShowName(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray reviews = new JSONArray(response);

                    for (int i = 0; i < reviews.length(); i++) {
                        JSONObject review = reviews.getJSONObject(i);
                        int rating = review.getInt("rating");
                        String reviewText = review.getString("reviewText");
                        int userID = review.getInt("userID");
                        int bookingID = review.getInt("bookingID");
                        String date = review.getString("date");

                        mReviews.add(new Review(rating, reviewText, date, mShow.getShowName(), userID,
                                bookingID, mShow.getId()));
                    }

                    reviewsRecyclerViewAdapter = new ReviewsRecyclerViewAdapter(ViewReviews.this, ViewReviews.this, mReviews);
                    recyclerViewReviews.setAdapter(reviewsRecyclerViewAdapter);

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
