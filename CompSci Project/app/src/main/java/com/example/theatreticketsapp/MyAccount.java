package com.example.theatreticketsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyAccount extends AppCompatActivity {

    //TODO: Implement features (reset password, view reviews etc.)

    Basket basket;
    User user;
    Button changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        basket = intent.getParcelableExtra("basket");
        user = intent.getParcelableExtra("user");

        changePassword = findViewById(R.id.buttonPassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ChangePassword.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });


        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);

        navView.setSelectedItemId(R.id.navigation_myaccount);

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
                intent.putExtra("user", user);
                startActivity(intent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent (this, Homepage.class);
        intent.putExtra("basket", basket);
        intent.putExtra("user", user);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                    switch (menuItem.getItemId()){

                        case R.id.navigation_mybookings:

                            Intent intent1 = new Intent (MyAccount.this, MyBookings.class);
                            intent1.putExtra("basket", basket);
                            intent1.putExtra("user", user);
                            startActivity(intent1);
                            overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
                            break;

                        case R.id.navigation_home:

                            Intent intent2 = new Intent(MyAccount.this, Homepage.class);
                            intent2.putExtra("basket", basket);
                            intent2.putExtra("user", user);
                            startActivity(intent2);
                            overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
                            break;


                    }
                    return true;
                }
            };
}
