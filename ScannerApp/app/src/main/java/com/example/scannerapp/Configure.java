package com.example.scannerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.classfiles.scannerapp.Show;
import com.classfiles.scannerapp.Venue;
import com.configfiles.scannerapp.DatabaseAPI;
import com.recyclerviewadapters.scannerapp.ShowRecyclerViewAdapter;
import com.recyclerviewadapters.scannerapp.VenueRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Configure extends AppCompatActivity implements VenueRecyclerViewAdapter.OnVenueClick, ShowRecyclerViewAdapter.OnShowClick {

    private RequestQueue requestQueue;
    private ArrayList<Venue> mVenues;
    private ArrayList<Show> mShows;
    private VenueRecyclerViewAdapter venueAdapter;
    private ShowRecyclerViewAdapter showAdapter;
    private RecyclerView venueRecyclerView, showRecyclerView;
    private ProgressBar progressBar;
    private TextView showTxtView, performancesTxtView, noPerformancesTxtView;
    private int selectedVenue = RecyclerView.NO_POSITION, selectedShow = RecyclerView.NO_POSITION;
    private RadioGroup radioGroup;
    private RadioButton matineeRB, eveningRB;
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);

        requestQueue = Volley.newRequestQueue(this);
        mVenues = new ArrayList<>();
        mShows = new ArrayList<>();

        venueRecyclerView = findViewById(R.id.venueRecyclerView);
        venueRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        showRecyclerView = findViewById(R.id.showRecyclerView);
        showRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBar);
        showTxtView = findViewById(R.id.selectShowLbl);
        performancesTxtView = findViewById(R.id.todaysShowsLbl);
        noPerformancesTxtView = findViewById(R.id.noPerformancesLbl);
        matineeRB = findViewById(R.id.matinee);
        eveningRB = findViewById(R.id.evening);
        confirmBtn = findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Show show = mShows.get(selectedShow);
                String showTime;
                int id = radioGroup.getCheckedRadioButtonId();
                if (id == matineeRB.getId())
                    showTime = show.getMatStart();
                else
                    showTime = show.getEveStart();

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String d = sdf.format(date);


                Intent scan = new Intent(Configure.this, MainActivity.class);
                scan.putExtra("time", showTime);
                scan.putExtra("name", show.getName());
                scan.putExtra("date", d);
                startActivity(scan);
            }
        });

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                confirmBtn.setVisibility(View.VISIBLE);
            }
        });


        loadVenues();



    }

    private void loadVenues() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_VENUES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

                venueAdapter = new VenueRecyclerViewAdapter(Configure.this, mVenues, Configure.this);
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

        mShows.clear();
        Venue venue = mVenues.get(selectedVenue);

        String url = DatabaseAPI.URL_GET_INSTANCES+venue.getName();
        url = url.replace(" ", "%20");

        progressBar.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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



                    progressBar.setVisibility(View.INVISIBLE);
                    showTxtView.setVisibility(mShows.isEmpty() ? View.INVISIBLE : View.VISIBLE);

                    showAdapter = new ShowRecyclerViewAdapter(Configure.this, mShows, Configure.this);
                    showRecyclerView.setAdapter(showAdapter);

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

    private void showTimes(){
        Show show = mShows.get(selectedShow);
        matineeRB.setText(show.getMatStart());
        eveningRB.setText(show.getEveStart());
        performancesTxtView.setVisibility(View.VISIBLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day){
            case 1:
                matineeRB.setVisibility(show.isSunMat() ? View.VISIBLE : View.GONE);
                eveningRB.setVisibility(show.isSunEve() ? View.VISIBLE : View.GONE);
                break;

            case 2:
                matineeRB.setVisibility(show.isMonMat() ? View.VISIBLE : View.GONE);
                eveningRB.setVisibility(show.isMonEve() ? View.VISIBLE : View.GONE);
                break;

            case 3:
                matineeRB.setVisibility(show.isTueMat() ? View.VISIBLE : View.GONE);
                eveningRB.setVisibility(show.isTueEve() ? View.VISIBLE : View.GONE);
                break;

            case 4:
                matineeRB.setVisibility(show.isWedMat() ? View.VISIBLE : View.GONE);
                eveningRB.setVisibility(show.isWedEve() ? View.VISIBLE : View.GONE);
                break;

            case 5:
                matineeRB.setVisibility(show.isThuMat() ? View.VISIBLE : View.GONE);
                eveningRB.setVisibility(show.isThuEve() ? View.VISIBLE : View.GONE);
                break;

            case 6:
                matineeRB.setVisibility(show.isFriMat() ? View.VISIBLE : View.GONE);
                eveningRB.setVisibility(show.isFriEve() ? View.VISIBLE : View.GONE);
                break;

            case 7:
                matineeRB.setVisibility(show.isSatMat() ? View.VISIBLE : View.GONE);
                eveningRB.setVisibility(show.isSatEve() ? View.VISIBLE : View.GONE);
                break;
        }

        noPerformancesTxtView.setVisibility(matineeRB.getVisibility() == View.GONE &&
                eveningRB.getVisibility() == View.GONE ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onVenueClick(int position, View view) {

        performancesTxtView.setVisibility(View.INVISIBLE);
        noPerformancesTxtView.setVisibility(View.INVISIBLE);
        matineeRB.setVisibility(View.INVISIBLE);
        eveningRB.setVisibility(View.INVISIBLE);
        radioGroup.clearCheck();
        confirmBtn.setVisibility(View.INVISIBLE);
        venueAdapter.setSelected(position);
        venueAdapter.notifyDataSetChanged();
        selectedVenue = position;
        loadShows();
    }

    @Override
    public void onShowClick(int pos, View view) {
        showAdapter.setSelected(pos);
        showAdapter.notifyDataSetChanged();
        selectedShow = pos;
        radioGroup.clearCheck();
        confirmBtn.setVisibility(View.INVISIBLE);
        showTimes();
    }
}
