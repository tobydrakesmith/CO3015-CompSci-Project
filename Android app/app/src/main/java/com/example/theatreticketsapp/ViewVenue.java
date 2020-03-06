package com.example.theatreticketsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ViewVenue extends AppCompatActivity {

    Venue venue;
    TextView venueNameText, venueDescriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_venue);
        venueNameText = findViewById(R.id.venueName);
        venueDescriptionText = findViewById(R.id.venueDescription);

        Intent intent = getIntent();
        venue = intent.getParcelableExtra("venue");

        venueNameText.setText(venue.getVenueName());
        venueDescriptionText.setText(venue.getDescription());


    }

    public void onNavigateClick(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("venue", venue);
        startActivity(intent);
    }
}
