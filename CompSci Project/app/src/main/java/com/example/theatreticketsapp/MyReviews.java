package com.example.theatreticketsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyReviews extends AppCompatActivity implements ReviewsRecyclerViewAdapter.OnReviewClickListener{

    private User user;
    private ArrayList<Review> mReviews;
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private ReviewsRecyclerViewAdapter reviewsRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reviews);
        Intent i = getIntent();
        user = i.getParcelableExtra("user");
        requestQueue = Volley.newRequestQueue(this);
        mReviews = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadReviews();
    }

    private void loadReviews() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_USER_REVIEWS+user.getId(), new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try{
                    JSONArray reviews = new JSONArray(response);
                    for (int i = 0; i < reviews.length(); i++) {
                        System.out.println("IN FOR LOOP");
                        JSONObject review = reviews.getJSONObject(i);
                        int rating = review.getInt("rating");
                        String reviewText = review.getString("reviewText");
                        String showName = review.getString("showName");
                        int bookingID = review.getInt("bookingID");
                        String date = review.getString("date");

                        mReviews.add(new Review(rating, reviewText, date, showName, user.getId(),
                                bookingID));

                        //System.out.println("REVIEWS SIZE:" + mReviews.size());
                    }

                    reviewsRecyclerViewAdapter = new ReviewsRecyclerViewAdapter(MyReviews.this, MyReviews.this, mReviews);
                    recyclerView.setAdapter(reviewsRecyclerViewAdapter);

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

    @Override
    public void onReviewClick(int position) {

        Toast.makeText(this, "Review click", Toast.LENGTH_SHORT).show();
    }
}
