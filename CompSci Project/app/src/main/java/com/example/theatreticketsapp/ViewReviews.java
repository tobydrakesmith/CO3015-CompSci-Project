package com.example.theatreticketsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView reviewTitle, showName;
    RatingBar ratingBar;


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

        reviewTitle = findViewById(R.id.reviewTitle);
        reviewTitle.setText(R.string.reviews_title);

        showName = findViewById(R.id.showNameLbl);
        showName.setText(mShow.getShowName());

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating((float) mShow.getRating());

        loadReviews();
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

    @Override
    public void onReviewClick(int position){

        Review review = mReviews.get(position);
        
        View view = View.inflate(ViewReviews.this, R.layout.dialog_review,null);

        RatingBar rating = view.findViewById(R.id.ratingBar);
        TextView username = view.findViewById(R.id.username);
        TextView reviewText = view.findViewById(R.id.reviewText);
        TextView date = view.findViewById(R.id.reviewDate);

        rating.setRating(review.getRating());
        username.setText("Username"+review.getUserid());
        reviewText.setText(review.getRatingTxt());
        date.setText(review.getDate());

        AlertDialog.Builder dialog = new AlertDialog.Builder(ViewReviews.this);
        dialog.setTitle("View review");
        dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

}
