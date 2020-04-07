package com.example.theatreticketsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyBookings extends AppCompatActivity implements MyBookingsRecyclerViewAdapter.OnBookingClickListener, MyPastBookingsRecyclerViewAdapter.OnReviewClickListener {

    Basket basket;
    int userID;
    ArrayList<Booking> mFutureBookings = new ArrayList<>();
    ArrayList<Booking> mPastBookings = new ArrayList<>();

    RecyclerView upcomingBookingsRecyclerView, pastBookingsRecyclerView;
    MyBookingsRecyclerViewAdapter myBookingsRecyclerViewAdapter;
    MyPastBookingsRecyclerViewAdapter myPastBookingsRecyclerViewAdapter;
    ProgressBar progressBar;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        Intent intent = getIntent();
        basket = intent.getParcelableExtra("basket");
        userID = intent.getIntExtra("userid", -1);

        progressBar = findViewById(R.id.progressBar);
        requestQueue = Volley.newRequestQueue(this);

        loadFutureBookings();
        loadPastBookings();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);

        navView.setSelectedItemId(R.id.navigation_mybookings);
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
        Intent intent = new Intent (this, Homepage.class);
        intent.putExtra("basket", basket);
        intent.putExtra("userid", userID);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                        case R.id.navigation_explore:

                            Intent intent = new Intent(MyBookings.this, Explore.class);
                            intent.putExtra("basket", basket);
                            intent.putExtra("userid", userID);
                            startActivity(intent);
                            overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
                            break;

                        case R.id.navigation_myaccount:

                            Intent intent1 = new Intent(MyBookings.this, MyAccount.class);
                            intent1.putExtra("basket", basket);
                            intent1.putExtra("userid", userID);
                            startActivity(intent1);
                            overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
                            break;

                        case R.id.navigation_home:

                            Intent intent2 = new Intent(MyBookings.this, Homepage.class);
                            intent2.putExtra("basket", basket);
                            intent2.putExtra("userid", userID);
                            startActivity(intent2);
                            overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
                            break;
                    }
                    return true;
                }
            };


    private void loadFutureBookings(){


        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_BOOKINGS + userID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try{
                    JSONArray bookings = new JSONArray(response);
                    for (int i=0; i<bookings.length(); i++){
                        JSONObject bookingsJSONObject = bookings.getJSONObject(i);
                        final Booking booking = createBookingObject(bookingsJSONObject);

                        mFutureBookings.add(booking);
                    }
                    myBookingsRecyclerViewAdapter = new MyBookingsRecyclerViewAdapter(MyBookings.this, MyBookings.this, mFutureBookings);

                    upcomingBookingsRecyclerView = findViewById(R.id.futureBookingsView);
                    upcomingBookingsRecyclerView.setLayoutManager(new LinearLayoutManager(MyBookings.this));
                    upcomingBookingsRecyclerView.setAdapter(myBookingsRecyclerViewAdapter);



                }catch (JSONException e){
                    try {
                        JSONObject message = new JSONObject(response);
                        System.out.println(message.getString("message"));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }

                progressBar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });
        requestQueue.add(stringRequest);

    }

    private void loadPastBookings(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_PAST_BOOKINGS + userID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try{
                    JSONArray bookings = new JSONArray(response);
                    for (int i=0; i<bookings.length(); i++){
                        JSONObject bookingsJSONObject = bookings.getJSONObject(i);
                        final Booking booking = createBookingObject(bookingsJSONObject);

                        mPastBookings.add(booking);
                    }

                    myPastBookingsRecyclerViewAdapter = new MyPastBookingsRecyclerViewAdapter(MyBookings.this, MyBookings.this, mPastBookings);

                    pastBookingsRecyclerView = findViewById(R.id.pastBookingsView);
                    pastBookingsRecyclerView.setLayoutManager(new LinearLayoutManager(MyBookings.this));

                    pastBookingsRecyclerView.setAdapter(myPastBookingsRecyclerViewAdapter);

                }catch (JSONException e){
                    try {
                        JSONObject message = new JSONObject(response);
                        System.out.println(message.getString("message"));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });

        requestQueue.add(stringRequest);
    }



    private Booking createBookingObject(JSONObject jsonObject) throws JSONException{

        int bookingID = jsonObject.getInt("bookingID");
        int showInstanceID = jsonObject.getInt("showInstanceID");
        int numberOfTickets = jsonObject.getInt("numberOfTickets");
        String date = jsonObject.getString("bookingDate");
        String showTime = jsonObject.getString("showTime");
        String showName = jsonObject.getString("showName");


        return new Booking(bookingID, showInstanceID, numberOfTickets, userID, date, showName, showTime);
    }




    @Override
    public void onBookingClick(int position){
        Intent intent = new Intent(this, ViewBooking.class);
        intent.putExtra("booking", mFutureBookings.get(position));
        startActivity(intent);

    }




    @Override
    public void onReviewClick(final int position) {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_CHECK_REVIEW + mPastBookings.get(position).getBookingID(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String reviewResponse = jsonObject.getString("reviewLeft");
                    if (reviewResponse.equals("true")){
                        Toast.makeText(MyBookings.this, "You have already left a review for this booking", Toast.LENGTH_SHORT).show();

                    }else{
                        Intent intent = new Intent(MyBookings.this, CreateReview.class);
                        intent.putExtra("booking", mPastBookings.get(position));
                        intent.putExtra("userid", userID);
                        startActivity(intent);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(this).add(stringRequest);

    }

}
