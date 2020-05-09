package com.example.theatreticketsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

public class MyReviews extends AppCompatActivity implements ReviewsRecyclerViewAdapter.OnReviewClickListener{

    private User user;
    private ArrayList<Review> mReviews;
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private ReviewsRecyclerViewAdapter reviewsRecyclerViewAdapter;
    private boolean editActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reviews);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Intent i = getIntent();
        user = i.getParcelableExtra("user");
        requestQueue = Volley.newRequestQueue(this);
        mReviews = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadReviews();
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

    private void loadReviews() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_USER_REVIEWS+user.getId(), new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
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
                        int reviewid = review.getInt("reviewid");

                        mReviews.add(new Review(reviewText,rating, date, showName, user.getId(),
                                bookingID, reviewid));

                    }

                    reviewsRecyclerViewAdapter = new ReviewsRecyclerViewAdapter(MyReviews.this, MyReviews.this, mReviews, true);
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

        final Review review = mReviews.get(position);

        View view = View.inflate(this, R.layout.dialog_edit_review,null);

        final RatingBar rating = view.findViewById(R.id.ratingBar);
        final EditText reviewText = view.findViewById(R.id.reviewText);
        final TextView date = view.findViewById(R.id.reviewDate);
        final ImageButton edit = view.findViewById(R.id.editReviewBtn);

        rating.setRating(review.getRating());
        rating.setIsIndicator(true);
        reviewText.setText(review.getReviewTxt());
        date.setText(review.getDate());


        reviewText.setFocusable(false);
        reviewText.setFocusableInTouchMode(false);
        reviewText.setClickable(false);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editActive = !editActive;

                if (editActive)
                    edit.setImageResource(R.drawable.ic_edit_selected);
                else
                    edit.setImageResource(R.drawable.ic_edit);

                rating.setIsIndicator(!editActive);

                reviewText.setFocusable(editActive);
                reviewText.setFocusableInTouchMode(editActive);
                reviewText.setClickable(editActive);
                reviewText.setSelected(editActive);
            }
        });

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("Submit changes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                review.setRating((int) rating.getRating());
                review.setReviewTxt(reviewText.getText().toString());
                editReview(review);


            }
        });
        dialog.setView(view);
        dialog.show();
    }

    private void editReview(final Review review) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseAPI.URL_EDIT_USER_REVIEW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                reviewsRecyclerViewAdapter.notifyDataSetChanged();
                System.out.println(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("reviewid", Integer.toString(review.getReviewid()));
                params.put("rating", Integer.toString(review.getRating()));
                params.put("review", review.getReviewTxt());
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }
}
