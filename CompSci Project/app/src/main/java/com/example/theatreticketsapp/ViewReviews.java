package com.example.theatreticketsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class ViewReviews extends AppCompatActivity implements ReviewsRecyclerViewAdapter.OnReviewClickListener {

    private Show mShow;
    private ArrayList<Review> mReviews, fullList;
    private RecyclerView recyclerViewReviews;
    private ReviewsRecyclerViewAdapter reviewsRecyclerViewAdapter;
    private RequestQueue requestQueue;
    private ImageButton sort;
    private int selectedRadioButton=-1;
    private TextView numberOfReviewsLbl;

    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);
        Intent intent = getIntent();

        mShow = intent.getParcelableExtra("live_show");

        mReviews = new ArrayList<>();
        fullList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);

        recyclerViewReviews = findViewById(R.id.reviewView);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));

        TextView reviewTitle = findViewById(R.id.reviewTitle);
        reviewTitle.setText("Reviews for " + mShow.getShowName());

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating((float) mShow.getRating());

        numberOfReviewsLbl = findViewById(R.id.numberOfReviews);


        ImageButton filter = findViewById(R.id.reviewFilter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View view = View.inflate(ViewReviews.this, R.layout.dialog_review_filter, null);

                radioGroup = view.findViewById(R.id.radioGroup);

                if (selectedRadioButton != -1){
                    RadioButton radioButton = view.findViewById(selectedRadioButton);
                    radioButton.setChecked(true);
                }

                AlertDialog.Builder dialog = new AlertDialog.Builder(ViewReviews.this);
                dialog.setTitle("Filter by rating");
                dialog.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        selectedRadioButton = radioGroup.getCheckedRadioButtonId();
                        if (selectedRadioButton != -1) {
                            RadioButton radioButton = view.findViewById(selectedRadioButton);

                            String selected = radioButton.getText().toString();
                            filterRating(selected);
                        }
                        else Toast.makeText(ViewReviews.this, "Select a rating", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //clear filter
                        selectedRadioButton = -1;
                        mReviews.clear();
                        mReviews.addAll(fullList);
                        reviewsRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });
                dialog.setView(view);
                dialog.show();
            }
        });

        sort = findViewById(R.id.reviewSort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ViewReviews.this, sort);
                popupMenu.getMenuInflater().inflate(R.menu.sort_menu_reviews, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        String name = item.getTitle().toString();

                        switch (name) {
                            case "Sort by date (oldest first)":
                                sortDateAsc();
                                break;
                            case "Sort by date (newest first)":
                                sortDateDesc();
                                break;
                            case "Sort by rating":
                                sortRating();
                                break;
                        }


                        return false;
                    }
                });
                popupMenu.show();
            }
        });

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

                    fullList.addAll(mReviews);

                    String numberOfReviews = "Number of reviews: " + mReviews.size();

                    numberOfReviewsLbl.setText(numberOfReviews);

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

    private void filterRating(String selected){

        int intSelected = Integer.parseInt(selected);

        ArrayList<Review> toDisplay = new ArrayList<>();

        for (Review review : fullList)
            if (review.getRating() == intSelected)
                toDisplay.add(review);

        mReviews.clear();
        mReviews.addAll(toDisplay);
        reviewsRecyclerViewAdapter.notifyDataSetChanged();

    }

    private void sortDateAsc(){
        Collections.sort(mReviews, new SortByDateAsc());
        reviewsRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void sortDateDesc(){
        Collections.sort(mReviews, new SortByDateDesc());
        reviewsRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void sortRating(){
        Collections.sort(mReviews, new SortByRating());
        reviewsRecyclerViewAdapter.notifyDataSetChanged();
    }

}

class SortByDateAsc implements Comparator<Review>{

    @Override
    public int compare(Review r1, Review r2) {
        Date d1 = new Date();
        Date d2 = new Date();

        try{
            d1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).parse(r1.getDate());
            d2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).parse(r2.getDate());
        } catch (ParseException e){
            e.printStackTrace();
        }

        assert d1 != null;
        return d1.compareTo(d2);
    }
}

class SortByDateDesc implements Comparator<Review>{

    @Override
    public int compare(Review r1, Review r2) {

        Date d1 = new Date();
        Date d2 = new Date();

        try{
            d1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).parse(r1.getDate());
            d2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).parse(r2.getDate());
        } catch (ParseException e){
            e.printStackTrace();
        }

        assert d2 != null;
        return d2.compareTo(d1);
    }
}

class SortByRating implements Comparator<Review>{

    @Override
    public int compare(Review r1, Review r2){
        return r2.getRating() - r1.getRating();
    }
}
