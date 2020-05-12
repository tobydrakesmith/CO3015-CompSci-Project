package com.activities.theatreticketsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.classfiles.theatreticketsapp.Basket;
import com.classfiles.theatreticketsapp.Booking;
import com.classfiles.theatreticketsapp.User;
import com.configfiles.theatreticketsapp.DatabaseAPI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.recyclerviewadapters.theatreticketsapp.MyBookingsRecyclerViewAdapter;
import com.recyclerviewadapters.theatreticketsapp.MyPastBookingsRecyclerViewAdapter;
import com.recyclerviewadapters.theatreticketsapp.RecyclerViewTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class MyBookings extends AppCompatActivity implements MyBookingsRecyclerViewAdapter.OnBookingClickListener, MyPastBookingsRecyclerViewAdapter.OnReviewClickListener {

    private Basket basket;
    private User user;

    private ArrayList<Booking> mFutureBookings = new ArrayList<>();
    private ArrayList<Booking> mPastBookings = new ArrayList<>();

    private RecyclerView bookingsRecyclerView;
    private MyBookingsRecyclerViewAdapter myBookingsRecyclerViewAdapter;
    private MyPastBookingsRecyclerViewAdapter myPastBookingsRecyclerViewAdapter;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;
    private BottomNavigationView navView;
    private TabLayout tabLayout;
    private TextView noBookings;
    private SharedPreferences sharedPreferences;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);


        Intent intent = getIntent();
        basket = intent.getParcelableExtra("basket");
        user = intent.getParcelableExtra("user");

        progressBar = findViewById(R.id.progressBar);
        requestQueue = Volley.newRequestQueue(this);

        bookingsRecyclerView = findViewById(R.id.bookingsView);
        bookingsRecyclerView.setAdapter(myBookingsRecyclerViewAdapter);
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        bookingsRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, bookingsRecyclerView, new RecyclerViewTouchListener.OnTouchActionListener() {
            @Override
            public void onLeftSwipe(View view) {
                if (tabLayout.getSelectedTabPosition() == 1) {
                    tabLayout.selectTab(tabLayout.getTabAt(0), true);
                } else {
                    navView.setSelectedItemId(R.id.navigation_home);
                }
            }

            @Override
            public void onRightSwipe(View view) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    tabLayout.selectTab(tabLayout.getTabAt(1), true);
                } else {
                    navView.setSelectedItemId(R.id.navigation_myaccount);
                }
            }

            @Override
            public void onClick(View view) {

            }

            @Override
            public void onScroll(View view) {

            }
        }));


        loadBookings();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);

        navView.setSelectedItemId(R.id.navigation_mybookings);
        noBookings = findViewById(R.id.noBookings);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getText().toString().equals("Your upcoming bookings")) {
                    if (mFutureBookings.isEmpty()) {
                        noBookings.setText(R.string.no_bookings);
                        noBookings.setVisibility(View.VISIBLE);
                    } else {
                        bookingsRecyclerView.setAdapter(myBookingsRecyclerViewAdapter);
                        noBookings.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (mPastBookings.isEmpty()) {
                        noBookings.setText(R.string.no_past_bookings);
                        noBookings.setVisibility(View.VISIBLE);
                    } else {
                        bookingsRecyclerView.setAdapter(myPastBookingsRecyclerViewAdapter);
                        noBookings.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }


    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.homepage_top_nav, menu);
        return true;
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
        navView.setSelectedItemId(R.id.navigation_home);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {

                        case R.id.navigation_myaccount:

                            showPopup(findViewById(R.id.navigation_myaccount));

                            break;

                        case R.id.navigation_home:
                            finish();
                            overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
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
                        Intent password = new Intent(getApplicationContext(), ChangePassword.class);
                        password.putExtra("user", user);
                        startActivity(password);
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
                navView.setSelectedItemId(R.id.navigation_mybookings);
            }
        });
        popupMenu.inflate(R.menu.my_account_pop_up);

        popupMenu.show();
    }

    private void loadBookings() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_BOOKINGS + user.getId(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray bookings = new JSONArray(response);
                    for (int i = 0; i < bookings.length(); i++) {
                        JSONObject bookingsJSONObject = bookings.getJSONObject(i);
                        final Booking booking = createBookingObject(bookingsJSONObject);

                        Date date = booking.getParsedDate();

                        if (date.before(new Date()))
                            mPastBookings.add(booking);
                        else
                            mFutureBookings.add(booking);

                    }
                    myBookingsRecyclerViewAdapter = new MyBookingsRecyclerViewAdapter(MyBookings.this, MyBookings.this, mFutureBookings);
                    myPastBookingsRecyclerViewAdapter = new MyPastBookingsRecyclerViewAdapter(MyBookings.this, MyBookings.this, mPastBookings);

                    bookingsRecyclerView.setAdapter(myBookingsRecyclerViewAdapter);


                } catch (JSONException e) {
                    try {
                        JSONObject message = new JSONObject(response);
                        Toast.makeText(MyBookings.this, message.getString("message"), Toast.LENGTH_SHORT).show();
                        noBookings.setVisibility(View.VISIBLE);
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


    private Booking createBookingObject(JSONObject jsonObject) throws JSONException {

        int bookingID = jsonObject.getInt("bookingID");
        int showInstanceID = jsonObject.getInt("showInstanceID");
        int numberOfTickets = jsonObject.getInt("numberOfTickets");
        String date = jsonObject.getString("bookingDate");
        String showTime = jsonObject.getString("showTime");
        String showName = jsonObject.getString("showName");
        int reviewLeft = jsonObject.getInt("reviewLeft");


        return new Booking(bookingID, showInstanceID, numberOfTickets, user.getId(), date, showName, showTime, reviewLeft);
    }


    @Override
    public void onBookingClick(int position) {
        Intent intent = new Intent(this, ViewBooking.class);
        intent.putExtra("booking", mFutureBookings.get(position));
        startActivity(intent);

    }


    @Override
    public void onReviewClick(final int position) {
        Intent intent = new Intent(MyBookings.this, CreateReview.class);
        intent.putExtra("booking", mPastBookings.get(position));
        intent.putExtra("user", user);
        startActivity(intent);
    }

}