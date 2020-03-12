package com.example.theatreticketsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewReviews extends AppCompatActivity  {

    Show mShow;
    ArrayList<Review> mReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);
        Intent intent = getIntent();

        mShow = intent.getParcelableExtra("live_show");
        mReviews = new ArrayList<>();

        loadReviews();
    }


    private void loadReviews(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_REVIEWS_DETAILED+mShow.getShowName(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray reviews = new JSONArray(response);

                    for (int i=0; i<reviews.length(); i++){
                        JSONObject review = reviews.getJSONObject(i);
                        int rating = review.getInt("rating");
                        String reviewText = review.getString("reviewText");
                        int userID = review.getInt("userID");
                        int bookingID = review.getInt("bookingID");
                        String date = review.getString("date");

                        mReviews.add(new Review(rating, reviewText, date, mShow.getShowName(), userID,
                                bookingID, mShow.getId()));
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

        Volley.newRequestQueue(this).add(stringRequest);

    }
}
