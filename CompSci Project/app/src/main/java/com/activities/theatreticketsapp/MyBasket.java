package com.activities.theatreticketsapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import com.configfiles.theatreticketsapp.DatabaseAPI;
import com.configfiles.theatreticketsapp.PayPalConfig;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.recyclerviewadapters.theatreticketsapp.MyBasketRecyclerViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class MyBasket extends AppCompatActivity implements MyBasketRecyclerViewAdapter.OnTicketClickListener {

    private Basket basket;
    private TextView numberTix, timeLeft, totalCost;
    private CountDownTimer countDownTimer;
    private User user;
    private MyBasketRecyclerViewAdapter adapter;
    private RequestQueue requestQueue;

    private static PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        requestQueue = Volley.newRequestQueue(this);

        Toolbar toolbar = findViewById(R.id.toolbar);

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_home);
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);


        Intent i = getIntent();
        basket = i.getParcelableExtra("basket");
        user = i.getParcelableExtra("user");


        adapter = new MyBasketRecyclerViewAdapter(MyBasket.this, MyBasket.this, basket.getBookings());

        //get the remaining time on the timer
        countDownTimer = new CountDownTimer(basket.getTimeOut() - System.currentTimeMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long milliLeft = basket.getTimeOut()-System.currentTimeMillis();
                String formattedTime = String.format(Locale.ENGLISH,"%02d min, %02d sec",
                        TimeUnit.MILLISECONDS.toMinutes(milliLeft),
                        TimeUnit.MILLISECONDS.toSeconds(milliLeft) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliLeft))
                );
                timeLeft.setText(String.format(Locale.ENGLISH,"%s%s", getString(R.string.time_left), formattedTime));
            }

            @Override
            public void onFinish() {
                basket.releaseTickets();
                numberTix.setText(String.format(Locale.ENGLISH,"%s%d", getString(R.string.number_tickets), basket.size()));
                totalCost.setText(String.format(Locale.ENGLISH, "%s%d", getString(R.string.total_cost), basket.getTotalCost()));
                adapter.notifyDataSetChanged();
            }
        };

        numberTix = findViewById(R.id.numberOfTickets);
        timeLeft = findViewById(R.id.timeLeftLbl);
        totalCost = findViewById(R.id.totalCostLbl);

        if (!basket.isEmpty())
            countDownTimer.start();


        RecyclerView recyclerView = findViewById(R.id.basketView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        numberTix.setText(String.format(Locale.ENGLISH,"%s%d", getString(R.string.number_tickets), basket.size()));
        totalCost.setText(String.format(Locale.ENGLISH, "%s%d", getString(R.string.total_cost), basket.getTotalCost()));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!basket.isEmpty()) {

                    Intent payPalService = new Intent(MyBasket.this, PayPalService.class);
                    payPalService.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
                    startService(payPalService);

                    PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(basket.getTotalCost()), "GBP",
                            "Theatre booking", PayPalPayment.PAYMENT_INTENT_SALE);

                    Intent process = new Intent(MyBasket.this, PaymentActivity.class);
                    process.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
                    process.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                    startActivityForResult(process, 7171);

                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, PayPalService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7171) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        JSONObject paymentDetails = confirmation.toJSONObject().getJSONObject("response");
                        //If the payment was successful, start new activity and display booking
                        if (paymentDetails.getString("state").equals("approved")) {
                            Intent intent = new Intent(MyBasket.this, Checkout.class);
                            intent.putExtra("basket", basket);
                            intent.putExtra("user", user);
                            startActivity(intent);
                            finish();
                        } else
                            Toast.makeText(this, "Not approved", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Homepage.class);
        intent.putExtra("basket", basket);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, Homepage.class);
            intent.putExtra("basket", basket);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    public void onTicketClick(int position) {

        //remove tickets from the basket and from the database
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE,
                DatabaseAPI.URL_DELETE_BASKET_BOOKING + basket.getBookings().get(position).getTempID(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(stringRequest);

        basket.releaseTickets(basket.getBookings().get(position));
        numberTix.setText(String.format(Locale.ENGLISH,"%s%d", getString(R.string.number_tickets), basket.size()));
        totalCost.setText(String.format(Locale.ENGLISH, "%s%d", getString(R.string.total_cost), basket.getTotalCost()));
        adapter.notifyDataSetChanged();

        if (basket.isEmpty()) {
            timeLeft.setVisibility(View.INVISIBLE);
            countDownTimer.cancel();
        }
    }

}



