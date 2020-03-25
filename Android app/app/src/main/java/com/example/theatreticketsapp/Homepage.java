package com.example.theatreticketsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class  Homepage extends AppCompatActivity implements  MyRecyclerViewAdapter.OnShowClickListener{

    private Basket basket;


    private ArrayList<Show> mShows = new ArrayList<>();
    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Intent intent = getIntent();
        basket = intent.getParcelableExtra("basket");
        userID = intent.getIntExtra("userid", -1);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);
        navView.setSelectedItemId(R.id.navigation_home);

        recyclerView = findViewById((R.id.liveShowsView));
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));
        recyclerView.setAdapter(adapter);
        loadShows();

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homepage_top_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.activity_basket:
                Intent intent = new Intent(this, MyBasket.class);
                intent.putExtra("basket", basket);
                intent.putExtra("userid", userID);
                startActivity(intent);

                return true;

        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent (this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){

                        case R.id.navigation_explore:

                            Intent intent = new Intent(Homepage.this, Explore.class);
                            intent.putExtra("basket", basket);
                            intent.putExtra("userid", userID);
                            startActivity(intent);
                            overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);

                            break;

                        case R.id.navigation_mybookings:

                            Intent intent1 = new Intent (Homepage.this, MyBookings.class);
                            intent1.putExtra("basket", basket);
                            intent1.putExtra("userid", userID);
                            startActivity(intent1);

                            overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);

                            break;

                        case R.id.navigation_myaccount:

                            Intent intent2 = new Intent(Homepage.this, MyAccount.class);
                            intent2.putExtra("basket", basket);
                            intent2.putExtra("userid", userID);
                            startActivity(intent2);
                            overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);

                            break;
                    }

                    return true;
                }
            };

    private void loadShows(){
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_LIVE_SHOWS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray shows = new JSONArray(response);
                            for (int i=0;i< shows.length(); i++) {
                                JSONObject showObject = shows.getJSONObject(i);

                                Show show = createShowObject(showObject);

                                mShows.add(show);
                            }

                            progressBar.setVisibility(View.INVISIBLE);
                            adapter = new MyRecyclerViewAdapter(Homepage.this, mShows, Homepage.this);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            Toast.makeText(Homepage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            progressBar.setVisibility((View.INVISIBLE));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Homepage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility((View.INVISIBLE));
            }
        });

    Volley.newRequestQueue(this).add(stringRequest);
    }


    @Override
    public void onShowClick(int position) {
        Show liveShow = mShows.get(position);
        Intent intent = new Intent(this, ShowInformation.class);
        intent.putExtra("live_show", liveShow);
        intent.putExtra("basket", basket);
        intent.putExtra("userid", userID);
        startActivity(intent);

    }

    private Show createShowObject(JSONObject show) throws JSONException {
        int id = show.getInt("id");
        String showName = show.getString("showName");
        String venueName = show.getString("venueName");
        String startDate = show.getString("startDate");
        String endDate = show.getString("endDate");
        String matineeStart = show.getString("matTime");
        String eveningStart = show.getString("eveTime");

        int monMat = show.getInt("monMat");
        int monEve = show.getInt("monEve");

        int tueMat = show.getInt("tueMat");
        int tueEve = show.getInt("tueEve");

        int wedMat = show.getInt("wedMat");
        int wedEve = show.getInt("wedEve");

        int thuMat = show.getInt("thuMat");
        int thuEve = show.getInt("thuEve");

        int friMat= show.getInt("friMat");
        int friEve = show.getInt("friEve");

        int satMat = show.getInt("satMat");
        int satEve = show.getInt("satEve");

        int sunMat = show.getInt("sunMat");
        int sunEve = show.getInt("sunEve");

        int priceBandAPrices = show.getInt("bandAPrice");
        int priceBandBPrices = show.getInt("bandBPrice");
        int priceBandCPrices = show.getInt("bandCPrice");
        int priceBandDPrices = show.getInt("bandDPrice");

        int numberTixPBA = show.getInt("bandANumberOfTickets");
        int numberTixPBB = show.getInt("bandBNumberOfTickets");
        int numberTixPBC = show.getInt("bandCNumberOfTickets");
        int numberTixPBD = show.getInt("bandDNumberOfTickets");

        Show newShow = new Show(id, showName, venueName, startDate, endDate, matineeStart, eveningStart, monMat, monEve, tueMat, tueEve, wedMat, wedEve, thuMat, thuEve, friMat, friEve,
                satMat, satEve, sunMat, sunEve, priceBandAPrices, priceBandBPrices, priceBandCPrices, priceBandDPrices, numberTixPBA, numberTixPBB, numberTixPBC, numberTixPBD);

        return newShow;

    }
}
