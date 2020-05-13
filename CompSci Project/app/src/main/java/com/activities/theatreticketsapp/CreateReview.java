package com.activities.theatreticketsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.classfiles.theatreticketsapp.Booking;
import com.classfiles.theatreticketsapp.User;
import com.configfiles.theatreticketsapp.DatabaseAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateReview extends AppCompatActivity {

    private Booking booking;
    private User user;

    private RatingBar ratingBar;
    private EditText review;
    private RequestQueue requestQueue;
    private TextView characterLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_review);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        requestQueue = Volley.newRequestQueue(this);

        characterLimit = findViewById(R.id.characterLimit);
        ratingBar = findViewById(R.id.ratingBar);
        review = findViewById(R.id.reviewText);
        review.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //display the remaining character limit
                characterLimit.setText(Integer.toString(200 - s.length()));

                if (200 - s.length() < 20)
                    characterLimit.setTextColor(Color.RED);
                else
                    characterLimit.setTextColor(Color.BLACK);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Intent intent = getIntent();
        booking = intent.getParcelableExtra("booking");
        user = intent.getParcelableExtra("user");
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

    public void onClick(View view) {
        createReview();
    }

    public void onSuccess() {
        AlertDialog alertDialog = new AlertDialog.Builder(CreateReview.this).create();
        alertDialog.setTitle("Success");
        alertDialog.setMessage("Your review has been added");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        alertDialog.show();
    }


    private void createReview() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseAPI.URL_CREATE_REVIEW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {
                        onSuccess();
                    } else {
                        Toast.makeText(CreateReview.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        System.out.println("Review text: '" + (review.getText().toString()) + "'");
                        System.out.println(review.getText().toString().length() == 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                HashMap<String, String> params = new HashMap<>();
                params.put("bookingID", Integer.toString(booking.getBookingID()));
                params.put("userID", Integer.toString(user.getId()));
                params.put("showName", booking.getShowName());
                params.put("showInstanceID", Integer.toString(booking.getShowInstanceID()));
                params.put("rating", Float.toString(ratingBar.getRating()));
                params.put("review", review.getText().toString());

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

}
