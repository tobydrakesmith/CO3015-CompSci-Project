package com.example.scannerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.classfiles.scannerapp.Show;
import com.classfiles.scannerapp.Venue;
import com.configfiles.scannerapp.DatabaseAPI;
import com.recyclerviewadapters.scannerapp.VenueRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Configure extends AppCompatActivity {

    private RequestQueue requestQueue;
    private ArrayList<Venue> mVenues;
    private ArrayList<Show> mShows;
    private VenueRecyclerViewAdapter venueAdapter;
    private RecyclerView venueRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);

        requestQueue = Volley.newRequestQueue(this);
        mVenues = new ArrayList<>();
        mShows = new ArrayList<>();

        venueRecyclerView = findViewById(R.id.venueRecyclerView);
        venueRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        loadVenues();

        Button confirm = findViewById(R.id.confirmBtn);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (venueAdapter.getSelected() != -1)
                    loadShows();
                else
                    Toast.makeText(Configure.this, "Select venue", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadVenues() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_VENUES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONArray venues = new JSONArray(response);
                    for (int i=0; i<venues.length(); i++){
                        JSONObject venue = venues.getJSONObject(i);
                        int id = venue.getInt("id");
                        String name = venue.getString("name");
                        String postcode = venue.getString("postcode");
                        String city = venue.getString("city");
                        mVenues.add(new Venue(id, name, postcode, city));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                venueAdapter = new VenueRecyclerViewAdapter(Configure.this, mVenues);
                venueRecyclerView.setAdapter(venueAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(stringRequest);

    }

    private void loadShows(){

        Venue venue = mVenues.get(venueAdapter.getSelected());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_INSTANCES+venue.getName(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray shows = new JSONArray(response);
                    for (int i=0; i<shows.length(); i++){
                        JSONObject show = shows.getJSONObject(i);
                        String name = show.getString("name");
                        boolean mondayMat = show.getInt("mondayMat") == 1;
                        boolean mondayEve = show.getInt("mondayEve") == 1;
                        boolean tuesdayMat = show.getInt("tuesdayMat") == 1;
                        boolean tuesdayEve = show.getInt("tuesdayEve") == 1;
                        boolean wednesdayMat = show.getInt("wednesdayMat") == 1;
                        boolean wednesdayEve = show.getInt("wednesdayEve") == 1;
                        boolean thursdayMat = show.getInt("thursdayMat") == 1;
                        boolean thursdayEve = show.getInt("thursdayEve") == 1;
                        boolean fridayMat = show.getInt("fridayMat") == 1;
                        boolean fridayEve = show.getInt("fridayEve") == 1;
                        boolean saturdayMat = show.getInt("saturdayMat") == 1;
                        boolean saturdayEve = show.getInt("saturdayEve") == 1;
                        boolean sundayMat = show.getInt("sundayMat") == 1;
                        boolean sundayEve = show.getInt("sundayEve") == 1;
                        String matStart = show.getString("matStart");
                        String eveStart = show.getString("eveStart");

                        mShows.add(new Show(name, mondayMat, mondayEve, tuesdayMat, tuesdayEve,
                                wednesdayMat, wednesdayEve, thursdayMat, thursdayEve, fridayMat, fridayEve,
                                saturdayMat, saturdayEve, sundayMat, sundayEve, matStart, eveStart));

                    }

                }catch (JSONException e){
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
}
