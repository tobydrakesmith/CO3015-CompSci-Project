package com.example.theatreticketsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateReview extends AppCompatActivity {

    Booking booking;
    User user;

    RatingBar ratingBar;
    EditText review;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_review);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        requestQueue = Volley.newRequestQueue(this);

        ratingBar = findViewById(R.id.ratingBar);
        review = findViewById(R.id.reviewText);

        Intent intent = getIntent();
        booking = intent.getParcelableExtra("booking");
        user = intent.getParcelableExtra("user");
    }

    public void onClick(View view){
        createReview();
    }

    public void onSuccess(){
        AlertDialog alertDialog = new AlertDialog.Builder(CreateReview.this).create();
        alertDialog.setTitle("Success");
        alertDialog.setMessage("Your review has been edited");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        alertDialog.show();
    }


    private void createReview(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseAPI.URL_CREATE_REVIEW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")){
                        Toast.makeText(CreateReview.this, "Success", Toast.LENGTH_SHORT).show();
                        onSuccess();
                    }
                    else {
                        Toast.makeText(CreateReview.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        System.out.println("Review text: '" + (review.getText().toString()) + "'");
                        System.out.println(review.getText().toString().length() == 0);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams(){



                HashMap<String, String> params = new HashMap<>();
                params.put("bookingID", Integer.toString(booking.getBookingID()));
                params.put("userID", Integer.toString(user.getId()));
                params.put("showName", booking.getShowName());
                params.put("showInstanceID", Integer.toString(booking.getShowInstanceID()));
                params.put("rating", Float.toString(ratingBar.getRating()));
                params.put("review", (review.getText().toString().length() == 0) ? "Empty" : review.getText().toString());

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

}
