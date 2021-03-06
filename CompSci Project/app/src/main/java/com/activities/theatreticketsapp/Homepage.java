package com.activities.theatreticketsapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.classfiles.theatreticketsapp.Basket;
import com.classfiles.theatreticketsapp.Show;
import com.classfiles.theatreticketsapp.User;
import com.classfiles.theatreticketsapp.Venue;
import com.configfiles.theatreticketsapp.DatabaseAPI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.recyclerviewadapters.theatreticketsapp.RecyclerViewTouchListener;
import com.recyclerviewadapters.theatreticketsapp.ShowRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Homepage extends AppCompatActivity implements ShowRecyclerViewAdapter.OnShowClickListener {

    private Basket basket;
    private ArrayList<Show> mShows, fullList;
    private ArrayList<Venue> mVenues;
    private ShowRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SeekBar seekBarLocation, seekBarPrice;
    private RequestQueue requestQueue;
    private Location userLoc;
    private Button filterBtn;
    private BottomNavigationView navView;
    private User user;
    private RadioGroup radioGroup;
    private int distanceFilter = 100, maxPrice = 0, filterPrice, selectedRadioBtn=-1;
    private SharedPreferences sharedPreferences;
    SearchView searchView;

    //TODO: TIDY CODE

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        //Request device's location
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        //Get user's current location
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null)
                    userLoc = new Location(location);
                else {
                    Toast.makeText(Homepage.this, "Please turn location on", Toast.LENGTH_SHORT).show();
                    userLoc = null;
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
        user = intent.getParcelableExtra("user");

        mShows = new ArrayList<>();
        fullList = new ArrayList<>();
        mVenues = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //filter list via adapter
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        filterBtn = findViewById(R.id.filter);
        setUpFilterMenu();


        progressBar = findViewById(R.id.progressBar);

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);
        navView.setSelectedItemId(R.id.navigation_home);

        recyclerView = findViewById((R.id.liveShowsView));
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, recyclerView, new RecyclerViewTouchListener.OnTouchActionListener() {
            @Override
            public void onLeftSwipe(View view) {

            }

            @Override
            public void onRightSwipe(View view) {
                navView.setSelectedItemId(R.id.navigation_mybookings);
            }

            @Override
            public void onClick(View view) {

            }

            @Override
            public void onScroll(View view) {

            }
        }));

        loadShows();



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.activity_basket) {
            Intent intent = new Intent(this, MyBasket.class);
            intent.putExtra("basket", basket);
            intent.putExtra("user", user);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homepage_top_nav, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            //User has granted permission to device's location

            //Get device location
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {

                        userLoc = new Location(location);
                        if (adapter != null) {
                            adapter.setUserLocation(userLoc);
                            adapter.notifyDataSetChanged(); //add location to the adapter to display distance
                        }
                    } else {
                        Toast.makeText(Homepage.this, "Please turn location on", Toast.LENGTH_SHORT).show();
                        userLoc = null;
                    }
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });


        }else{
            Toast.makeText(this, "Without location permissions we can not let you know how far the venues are from you", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {

                        case R.id.navigation_mybookings:

                            Intent intent1 = new Intent(Homepage.this, MyBookings.class);
                            intent1.putExtra("basket", basket);
                            intent1.putExtra("user", user);
                            startActivity(intent1);

                            overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);

                            break;

                        case R.id.navigation_myaccount:

                            showPopup(findViewById(R.id.navigation_myaccount));
                            break;
                    }

                    return true;
                }
            };

    public void showPopup(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.change_password:
                        Intent i = new Intent(getApplicationContext(), ChangePassword.class);
                        i.putExtra("user", user);
                        startActivity(i);
                        break;

                    case R.id.view_reviews:
                        Intent reviews = new Intent(getApplicationContext(), MyReviews.class);
                        reviews.putExtra("user", user);
                        startActivity(reviews);
                        break;

                    case R.id.update_preferences:
                        Intent preferences = new Intent(getApplicationContext(), NotificationPreferences.class);
                        startActivity(preferences);
                        break;

                    case R.id.log_out:
                        Intent logOut = new Intent(getApplicationContext(), Login.class);
                        sharedPreferences.edit().remove("loggedon").apply();
                        startActivity(logOut);
                        break;
                }

                return false;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                navView.setSelectedItemId(R.id.navigation_home);
            }
        });
        popupMenu.inflate(R.menu.my_account_pop_up);

        popupMenu.show();
    }


    //Load all shows from the database
    private void loadShows() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_LIVE_SHOWS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONArray shows = new JSONArray(response);
                            for (int i = 0; i < shows.length(); i++) {
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
                                                    String city = jsonVenue.getString("city");

                                                    Venue venue =
                                                            getVenue(showObject.getString("venueName"), venueDesc, postcode, city);

                                                    final Show show = createShowObject(showObject, venue);

                                                    mShows.add(show);

                                                    if (mShows.size() == shows.length()) {
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

    //Set venues to relevant shows
    private Venue getVenue(String venueName, String description, String postcode, String city) throws IOException {

        for (Venue venue : mVenues)
            if (venue.getVenueName().equals(venueName)) return venue;

        Venue venue = new Venue(venueName, postcode, description, city);
        mVenues.add(venue);

        Geocoder geocoder = new Geocoder(Homepage.this);
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



    //Show selected from recycler view
    @Override
    public void onShowClick(int position) {
        Show show = mShows.get(position);
        Intent intent = new Intent(this, ShowInformation.class);
        intent.putExtra("live_show", show);
        intent.putExtra("basket", basket);
        intent.putExtra("user", user);
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

        int friMat = show.getInt("friMat");
        int friEve = show.getInt("friEve");

        int satMat = show.getInt("satMat");
        int satEve = show.getInt("satEve");

        int sunMat = show.getInt("sunMat");
        int sunEve = show.getInt("sunEve");

        int priceBandAPrices = show.getInt("bandAPrice");
        if (priceBandAPrices > maxPrice) {
            maxPrice = priceBandAPrices;
            filterPrice = maxPrice;
        }

        int priceBandBPrices = show.getInt("bandBPrice");
        int priceBandCPrices = show.getInt("bandCPrice");
        int priceBandDPrices = show.getInt("bandDPrice");

        int numberTixPBA = show.getInt("bandANumberOfTickets");
        int numberTixPBB = show.getInt("bandBNumberOfTickets");
        int numberTixPBC = show.getInt("bandCNumberOfTickets");
        int numberTixPBD = show.getInt("bandDNumberOfTickets");

        String dateAdded = show.getString("dateAdded");

        return new Show(id, showName, venueName, startDate, endDate, matineeStart, eveningStart, monMat, monEve, tueMat, tueEve, wedMat, wedEve, thuMat, thuEve, friMat, friEve,
                satMat, satEve, sunMat, sunEve, priceBandAPrices, priceBandBPrices, priceBandCPrices, priceBandDPrices, numberTixPBA, numberTixPBB, numberTixPBC, numberTixPBD, venue, dateAdded);

    }


    //FILTER MENUS AND FUNCTIONS

    private void setUpFilterMenu() {
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final View view = View.inflate(Homepage.this, R.layout.dialog_filter_location, null);

                final TextView seekBarLblLocation = view.findViewById(R.id.filterDistance);
                seekBarLocation = view.findViewById(R.id.seekBarLocation);
                if (userLoc == null)
                    seekBarLocation.setEnabled(false);

                seekBarLocation.setProgress(distanceFilter);

                if (distanceFilter == 100)
                    seekBarLblLocation.setText(String.format("%skm+", Integer.toString(distanceFilter)));
                else
                    seekBarLblLocation.setText(String.format("%skm", Integer.toString(distanceFilter)));

                seekBarLocation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        distanceFilter = progress;
                        if (distanceFilter < seekBar.getMax())
                            seekBarLblLocation.setText(String.format("%skm", Integer.toString(distanceFilter)));
                        else
                            seekBarLblLocation.setText(String.format("%skm+", Integer.toString(distanceFilter)));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                final TextView seekBarLblPrice = view.findViewById(R.id.filterPrice);
                seekBarPrice = view.findViewById(R.id.seekBarPrice);
                seekBarPrice.setMax(maxPrice);
                seekBarPrice.setProgress(filterPrice);
                seekBarLblPrice.setText(String.format(Locale.ENGLISH, "%s%d", getString(R.string.pound_sign), filterPrice));

                seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        filterPrice = progress;
                        seekBarLblPrice.setText(String.format(Locale.ENGLISH, "%s%d", getString(R.string.pound_sign), filterPrice));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                radioGroup = view.findViewById(R.id.radioGroup);

                if (selectedRadioBtn == -1) radioGroup.check(R.id.allShowsRB);
                else radioGroup.check(selectedRadioBtn);


                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        selectedRadioBtn = checkedId;

                    }
                });

                AlertDialog.Builder dialog = new AlertDialog.Builder(Homepage.this);
                dialog.setTitle("Filter");
                dialog.setView(view);


                dialog.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RadioButton radioButton = view.findViewById(selectedRadioBtn);

                        Toast.makeText(Homepage.this, radioButton.getText().toString(), Toast.LENGTH_SHORT).show();

                        /*
                        Search view works for searching a filtered list of shows, however there are
                        issues related to the combination of the two.
                        */


                        if (userLoc != null)
                            filterLocation();

                        filterPrice();
                        filterDate(radioButton);


                    }
                });
                dialog.setNeutralButton("Clear filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mShows.clear();
                        mShows.addAll(fullList);
                        adapter.setFullList(mShows);
                        adapter.notifyDataSetChanged();

                        radioGroup.check(R.id.allShowsRB);
                        filterPrice = maxPrice;
                        distanceFilter = 100;
                        searchView.setQuery("", false);
                    }
                });
                dialog.show();

            }
        });
    }

    private void filterLocation() {

        if (distanceFilter == seekBarLocation.getMax()) {
            mShows.clear();
            mShows.addAll(fullList);
        } else {
            ArrayList<Show> filterList = new ArrayList<>();

            for (Show show : fullList) {
                if (Float.parseFloat(show.getUserDistanceFromVenue(userLoc)) < distanceFilter)
                    filterList.add(show);
            }

            mShows.clear();
            mShows.addAll(filterList);
        }

    }

    private void filterPrice() {
        if (filterPrice < seekBarPrice.getMax()) {

            ArrayList<Show> filterList = new ArrayList<>();
            if (userLoc != null) {
                for (Show show : fullList) {
                    if (show.getPricePbD() <= filterPrice && mShows.contains(show))
                        filterList.add(show);
                }
            }else{
                for (Show show : fullList) {
                    if (show.getPricePbD() <= filterPrice)
                        filterList.add(show);
                }
            }
            mShows.clear();
            mShows.addAll(filterList);
        }

    }

    private void filterDate(RadioButton radioButton) {

        ArrayList<Show> filterList = new ArrayList<>();

        if (radioButton.getText().toString().equals("All shows")) {
            for (Show show : fullList) {
                if (mShows.contains(show))
                    filterList.add(show);
            }
        } else {
            for (Show s : fullList) {
                if (mShows.contains(s) && (s.getSDate().before(new Date())))
                    filterList.add(s);
            }
        }
        mShows.clear();
        mShows.addAll(filterList);

        adapter.notifyDataSetChanged();

        adapter.setFullList(mShows);

    }



}
