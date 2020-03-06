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
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_review);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        booking = intent.getParcelableExtra("booking");
        userID = intent.getIntExtra("userid",0);
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

                RatingBar ratingBar = findViewById(R.id.ratingBar);
                EditText review = findViewById(R.id.reviewText);

                HashMap<String, String> params = new HashMap<>();
                params.put("bookingID", Integer.toString(booking.getBookingID()));
                params.put("userID", Integer.toString(userID));
                params.put("showName", booking.getShowName());
                params.put("showInstanceID", Integer.toString(booking.getShowInstanceID()));
                params.put("rating", Integer.toString(ratingBar.getNumStars()));
                params.put("review", review.getText().toString());

                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

}
