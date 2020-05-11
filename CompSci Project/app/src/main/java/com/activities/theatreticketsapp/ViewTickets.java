package com.activities.theatreticketsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.classfiles.theatreticketsapp.Booking;
import com.classfiles.theatreticketsapp.Venue;
import com.configfiles.theatreticketsapp.QRCodeHelper;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ViewTickets extends AppCompatActivity {

    private ArrayList<JSONObject> tickets;
    private ImageView qrCodeImageView;
    private TextView ticketLbl;
    private Bitmap bitmap;
    private int position;
    private GestureDetector mGestureDetector;

    private static final int SWIPE_MIN_DISTANCE = 175;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tickets);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Intent intent = getIntent();
        Booking booking = intent.getParcelableExtra("booking");
        tickets = new ArrayList<>();

        Venue venue = intent.getParcelableExtra("venue");

        TextView venueName = findViewById(R.id.venueName);
        venueName.setText(venue.getVenueName());

        TextView priceBand = findViewById(R.id.priceBand);


        String jsonTickets = intent.getStringExtra("tickets");
        try {
            JSONArray jsonArray = new JSONArray(jsonTickets);
            priceBand.setText("Price band: " +jsonArray.getJSONObject(0).getString("priceBand"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonTicket = jsonArray.getJSONObject(i);
                tickets.add(jsonTicket);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        position = 0;

        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        ticketLbl = findViewById(R.id.ticketNumberLbl);


        TextView dateLbl = findViewById(R.id.dateLbl);
        TextView showNameLbl = findViewById(R.id.showNameLbl);

        Button right = findViewById(R.id.buttonRight);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveRight();
            }
        });
        Button left = findViewById(R.id.buttonLeft);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLeft();
            }
        });

        bitmap = QRCodeHelper
                .newInstance(ViewTickets.this)
                .setContent(tickets.get(0).toString())
                .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                .setMargin(2)
                .getQRCOde();
        qrCodeImageView.setImageBitmap(bitmap);


        mGestureDetector = new GestureDetector(this, new GestureListener());

        qrCodeImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        right.setVisibility(View.VISIBLE);
        left.setVisibility(View.VISIBLE);

        updateLabel();

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(booking.getDate());


            assert date != null;
            String strDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(date);

            date = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.ENGLISH).parse(strDate + " " + booking.getShowTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        showNameLbl.setText(booking.getShowName());
        assert date != null;
        dateLbl.setText(date.toString());


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

    private void moveLeft() {
        if (position > 0) {
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

    private void moveRight() {
        if (position < tickets.size() - 1) {
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

    private void updateLabel() {
        ticketLbl.setText("Ticket number " + (position + 1) + " of " + tickets.size());
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
                moveRight();
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
                moveLeft();

            return false;
        }
    }

}





