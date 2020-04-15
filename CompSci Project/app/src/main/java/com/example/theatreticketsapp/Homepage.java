package com.example.theatreticketsapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class  Homepage extends AppCompatActivity implements  ShowRecyclerViewAdapter.OnShowClickListener{

    private Basket basket;
    private ArrayList<Show> mShows, fullList;
    private ArrayList<Venue> mVenues;
    private ShowRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SeekBar seekBar;
    private TextView seekBarLbl;
    private int userID;
    private RequestQueue requestQueue;
    private Button filterBtn;
    private Location userLoc;
    private Geocoder geocoder;

    private int distanceFilter;

    //TODO: TIDY CODE

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            //permission denied

        }

        //TODO wait for user response to permission


        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null)
                    userLoc = new Location(location);
                else {
                    System.out.println("Null location");
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

        Intent intent = getIntent();
        basket = intent.getParcelableExtra("basket");
        userID = intent.getIntExtra("userid", -1);

        mShows = new ArrayList<>();
        fullList = new ArrayList<>();
        mVenues = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        geocoder = new Geocoder(Homepage.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        filterBtn = findViewById(R.id.filter);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Homepage.this, filterBtn);
                popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(Homepage.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                        String title = item.getTitle().toString();

                        switch (title){
                            case "Location":

                                LinearLayout layout = new LinearLayout(Homepage.this);
                                layout.setOrientation(LinearLayout.VERTICAL);

                                AlertDialog.Builder dialog = new AlertDialog.Builder(Homepage.this);
                                dialog.setTitle("Filter by location");

                                seekBarLbl = new TextView(Homepage.this);

                                seekBar = new SeekBar(Homepage.this);
                                layout.addView(seekBar);

                                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                        seekBarLbl.setText(String.format("%skm", Integer.toString(seekBar.getProgress())));
                                        distanceFilter = seekBar.getProgress();
                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {
                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {
                                    }
                                });

                                seekBar.setProgress(seekBar.getMax()/2);


                                layout.addView(seekBarLbl);

                                dialog.setView(layout);

                                dialog.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        filterLocation(distanceFilter);
                                    }
                                });
                                dialog.show();


                                break;
                            case "Rating":
                                break;
                            case "Date":
                                break;
                            case "Price":
                                break;
                        }

                        return false;
                    }
                });

                popupMenu.show();
            }
        });

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

    private void filterLocation(int distance){
        ArrayList<Show> filterList = new ArrayList<>();
        for (Show show : fullList){
            if (Float.parseFloat(show.getUserDistanceFromVenue(userLoc)) < distance){
                filterList.add(show);
            }
        }


        mShows.clear();
        mShows.addAll(filterList);


/*        adapter = new ShowRecyclerViewAdapter(Homepage.this, mShows,
                Homepage.this, userLoc);
        recyclerView.setAdapter(adapter);*/
        adapter.notifyDataSetChanged();

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


    private Venue getVenue(String venueName, String description, String postcode) throws IOException {

        for (Venue venue : mVenues)
            if (venue.getVenueName().equals(venueName)) return venue;

        Venue venue = new Venue(venueName, description, postcode);
        mVenues.add(venue);
        List<Address> address =
                geocoder.getFromLocationName(venue.getStrLocation(), 1);
        double latitude = address.get(0).getLatitude();
        double longitude = address.get(0).getLongitude();

        Location venueLocation = new Location(LocationManager.GPS_PROVIDER);
        venueLocation.setLatitude(latitude);
        venueLocation.setLongitude(longitude);

        venue.setLocation(venueLocation);
        return venue;
    }

    private void loadShows(){
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_LIVE_SHOWS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONArray shows = new JSONArray(response);
                            for (int i=0;i< shows.length(); i++) {
                                final JSONObject showObject = shows.getJSONObject(i);
                                StringRequest venueInfo = new StringRequest(Request.Method.GET,
                                        DatabaseAPI.URL_GET_VENUE_INFO + showObject.getString("venueName"),
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonVenue = new JSONObject(response);
                                                    String postcode = jsonVenue.getString("postcode");
                                                    String venueDesc = jsonVenue.getString("venueDescription");

                                                    Venue venue =
                                                            getVenue(showObject.getString("venueName"), venueDesc, postcode);

                                                    final Show show = createShowObject(showObject,venue);

                                                    mShows.add(show);

                                                    if (mShows.size() == shows.length()){
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        adapter = new ShowRecyclerViewAdapter(Homepage.this, mShows,
                                                                        Homepage.this, userLoc);
                                                        recyclerView.setAdapter(adapter);
                                                        fullList.addAll(mShows);
                                                    }

                                                } catch (JSONException | IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                    }
                                });
                                requestQueue.add(venueInfo);
                            }

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

        requestQueue.add(stringRequest);
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

    private Show createShowObject(JSONObject show, Venue venue) throws JSONException {
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
                satMat, satEve, sunMat, sunEve, priceBandAPrices, priceBandBPrices, priceBandCPrices, priceBandDPrices, numberTixPBA, numberTixPBB, numberTixPBC, numberTixPBD, venue);

        return newShow;

    }

}
