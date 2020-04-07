package com.example.theatreticketsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewTickets extends AppCompatActivity {

    ArrayList<JSONObject> tickets;
    Booking booking;
    ImageView qrCodeImageView;
    TextView ticketLbl, dateLbl, showNameLbl;
    Bitmap bitmap;
    Button left, right;
    int position = 0;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tickets);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        booking = intent.getParcelableExtra("booking");
        tickets = new ArrayList<>();

        String jsonTickets = intent.getStringExtra("tickets");
        try {
            JSONArray jsonArray = new JSONArray(jsonTickets);
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonTicket = jsonArray.getJSONObject(i);
                tickets.add(jsonTicket);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        requestQueue = Volley.newRequestQueue(this);

        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        ticketLbl = findViewById(R.id.ticketNumberLbl);
        dateLbl = findViewById(R.id.dateLbl);
        showNameLbl = findViewById(R.id.showNameLbl);
        right = findViewById(R.id.buttonRight);
        left = findViewById(R.id.buttonLeft);

        bitmap = QRCodeHelper
                .newInstance(ViewTickets.this)
                .setContent(tickets.get(0).toString())
                .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                .setMargin(2)
                .getQRCOde();
        qrCodeImageView.setImageBitmap(bitmap);

        right.setVisibility(View.VISIBLE);
        left.setVisibility(View.VISIBLE);

        updateLabel();


        //loadTickets();

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(booking.getDate());


            String strDate = new SimpleDateFormat("dd-MM-yyyy").format(date);

            date = new SimpleDateFormat("dd-MM-yyyy hh:mm").parse(strDate+ " " + booking.getShowTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        showNameLbl.setText(booking.getShowName());
        dateLbl.setText(date.toString());


    }

    private void loadTickets(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_TICKETS + booking.getBookingID(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonTicket = (JSONObject) jsonArray.get(i);
                        tickets.add(jsonTicket);
                    }
                    bitmap = QRCodeHelper
                            .newInstance(ViewTickets.this)
                            .setContent(tickets.get(0).toString())
                            .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                            .setMargin(2)
                            .getQRCOde();
                    qrCodeImageView.setImageBitmap(bitmap);

                    right.setVisibility(View.VISIBLE);
                    left.setVisibility(View.VISIBLE);

                    updateLabel();


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

    public void onClickRight(View view) {


        if(position<tickets.size()-1){
            bitmap = QRCodeHelper
                    .newInstance(ViewTickets.this)
                    .setContent(tickets.get(++position).toString())
                    .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                    .setMargin(2)
                    .getQRCOde();
            qrCodeImageView.setImageBitmap(bitmap);
            updateLabel();
        }

    }

    public void onClickLeft(View view) {


        if(position>0){
            bitmap = QRCodeHelper
                    .newInstance(ViewTickets.this)
                    .setContent(tickets.get(--position).toString())
                    .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                    .setMargin(2)
                    .getQRCOde();
            qrCodeImageView.setImageBitmap(bitmap);
            updateLabel();
        }

    }

    private void updateLabel(){
        ticketLbl.setText("Ticket number " + (position+1)  + " of " + tickets.size());
    }



}




