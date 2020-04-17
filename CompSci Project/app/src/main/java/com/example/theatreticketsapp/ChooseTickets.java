package com.example.theatreticketsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//TODO: I think that this code can be re-written better

public class ChooseTickets extends AppCompatActivity {

    private Basket basket;

    private TextView tixLeftPBA, tixLeftPBB, tixLeftPBC, tixLeftPBD;
    private int numberTix;
    private Show mShow;
    private String strDate;
    private User user;
    private boolean matinee;
    private int pbaLeft, pbbLeft, pbcLeft, pbdLeft;

    RequestQueue requestQueue;

    RadioGroup radioGroup;
    RadioButton pba, pbb, pbc, pbd;

    //TODO: see if using a radio group can reduce code

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_tickets);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestQueue = Volley.newRequestQueue(this);

        numberTix = 0;

        Intent intent = getIntent();
        mShow = intent.getParcelableExtra("live_show");
        strDate = intent.getStringExtra("date");
        basket = intent.getParcelableExtra("basket");
        user = intent.getParcelableExtra("user");
        matinee = intent.getBooleanExtra("matinee", false);

        getSales();

        radioGroup = findViewById(R.id.radioGroup);

        pba = findViewById(R.id.priceBandA);
        pbb = findViewById(R.id.priceBandB);
        pbc = findViewById(R.id.priceBandC);
        pbd = findViewById(R.id.priceBandD);


        pba.setText("£" + mShow.getPricePbA());
        pbb.setText("£" + mShow.getPricePbB());
        pbc.setText("£" + mShow.getPricePbC());
        pbd.setText("£" + mShow.getPricePbD());

        tixLeftPBA = findViewById(R.id.tixLeftPBA);
        tixLeftPBB = findViewById(R.id.tixLeftPBB);
        tixLeftPBC = findViewById(R.id.tixLeftPBC);
        tixLeftPBD = findViewById(R.id.tixLeftPBD);

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
            intent.putExtra("live_show", mShow);
            intent.putExtra("date", strDate);
            intent.putExtra("user", user);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override //TODO: Add animation
    public void onBackPressed() {
        Intent intent = new Intent(this, ChooseDate.class);
        intent.putExtra("basket", basket);
        intent.putExtra("live_show", mShow);
        intent.putExtra("user", user);
        startActivity(intent);
    }


    public void onClickIncrease(View view) {


        if (pba.isChecked()) {
            if (numberTix<9 && numberTix < pbaLeft ) {
                numberTix++;
                updateLabel();
                changeNumberTickets();
            }
        }
        else if (pbb.isChecked()){
            if (numberTix<9 && numberTix < pbbLeft ) {
                numberTix++;
                updateLabel();
                changeNumberTickets();
            }

        }
        else if (pbc.isChecked()){
            if (numberTix<9 && numberTix < pbcLeft) {
                numberTix++;
                updateLabel();
                changeNumberTickets();
            }
        }
        else if (pbd.isChecked()) {
            if (numberTix<9 && numberTix < pbdLeft ) {
                numberTix++;
                updateLabel();
                changeNumberTickets();
            }

        }
        else{
            numberTix++;
            updateLabel();
            changeNumberTickets();
        }


    }


    public void onClickDecrease(View view) {
        if (numberTix > 0) {
            numberTix--;
            updateLabel();
            changeNumberTickets();
        }
    }

    public void changeNumberTickets(){
        pba.setClickable(numberTix <= pbaLeft);
        pbb.setClickable(numberTix <= pbbLeft);
        pbc.setClickable(numberTix <= pbcLeft);
        pbd.setClickable(numberTix <= pbdLeft);
    }

    private int getSelectedPrice() {

        if (pba.isChecked()) return mShow.getPricePbA();
        if (pbb.isChecked()) return mShow.getPricePbB();
        if (pbc.isChecked()) return mShow.getPricePbC();
        if (pbd.isChecked()) return mShow.getPricePbD();

        return -1;
    }

    private String getPriceBand() {

        if (pba.isChecked()) return "A";
        if (pbb.isChecked()) return "B";
        if (pbc.isChecked()) return "C";
        if (pbd.isChecked()) return "D";

        return "";

    }

    private void updateLabel() {
        TextView numberTixLbl = findViewById(R.id.numberOfTix);
        numberTixLbl.setText(Integer.toString(numberTix));
    }

    private void updateAvail(){
        tixLeftPBA.setText("Tickets left: " + pbaLeft);
        tixLeftPBB.setText("Tickets left: " + pbbLeft);
        tixLeftPBC.setText("Tickets left: " + pbcLeft);
        tixLeftPBD.setText("Tickets left: " + pbdLeft);
    }

    public void onClickAddToBasket(View view) {

        if (numberTix == 0)
            Toast.makeText(this,
                    "You have not selected the number of tickets you would like",
                    Toast.LENGTH_SHORT).show();

        else if (getSelectedPrice() == -1)
            Toast.makeText(this, "Please select a price to continue",
                    Toast.LENGTH_SHORT).show();
        else
            getSales();

    }



    private void addToBasket(final int price) {

        String date = "&date=" + strDate;
        String time = "&time=" + (matinee ? mShow.getMatineeStart() : mShow.getEveningStart());
        String pb = "&priceBand=" + getPriceBand();
        String uid = "&userID=" + user.getId();
        String numTix = "&numberOfTickets=" + numberTix;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseAPI.URL_CREATE_BASKET_BOOKING+mShow.getId()+date+time+pb+uid+numTix, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    final int tempID = jsonObject.getInt("id");
                    BasketBooking basketBooking =
                            new BasketBooking(strDate, matinee ? mShow.getMatineeStart() : mShow.getEveningStart(),
                                    mShow, tempID);

                    String priceBand = getPriceBand();

                    for (int i = 0; i < numberTix; i++) {
                        Ticket t = new Ticket(price, mShow, priceBand);
                        basketBooking.addTicket(t);
                    }
                    basket.addBooking(basketBooking);


                    AlertDialog alertDialog = new AlertDialog.Builder(ChooseTickets.this).create();
                    alertDialog.setTitle("Success");
                    alertDialog.setMessage("Your tickets have been added to your basket, the tickets will release after 10 minutes.");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Take me to my basket",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ChooseTickets.this, MyBasket.class);
                                    intent.putExtra("basket", basket);
                                    intent.putExtra("live_show", mShow);
                                    intent.putExtra("user", user);
                                    intent.putExtra("date", strDate);
                                    startActivity(intent);
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Back to home",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ChooseTickets.this, Homepage.class);
                                    intent.putExtra("basket", basket);
                                    intent.putExtra("live_show", mShow);
                                    intent.putExtra("user", user);
                                    intent.putExtra("date", strDate);
                                    startActivity(intent);

                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);


    }

    private void getSales() {

        pbaLeft = mShow.getNumTicketsPbA();
        pbbLeft = mShow.getNumTicketsPbB();
        pbcLeft = mShow.getNumTicketsPbC();
        pbdLeft = mShow.getNumTicketsPbD();


        String date = "&date=" + strDate;
        String showTime = "&time=" + (matinee ? mShow.getMatineeStart() : mShow.getEveningStart());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_GET_SALES + mShow.getId() + date + showTime, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String priceBand = jsonObject.getString("priceBand");
                        switch (priceBand){
                            case "A":
                                pbaLeft--;
                                break;
                            case "B":
                                pbbLeft--;
                                break;
                            case "C":
                                pbcLeft--;
                                break;
                            case "D":
                                pbdLeft--;
                                break;
                        }
                    }
                    updateAvail();
                    String pb = getPriceBand();

                    switch (pb){
                        case "A":

                            if ((pbaLeft - numberTix) >= 0) addToBasket(getSelectedPrice());
                            else Toast.makeText(ChooseTickets.this, "Sorry, these tickets are no longer available", Toast.LENGTH_SHORT).show();
                            break;

                        case "B":
                            if ((pbbLeft - numberTix) >=0) addToBasket(getSelectedPrice());
                            else Toast.makeText(ChooseTickets.this, "Sorry, these tickets are no longer available", Toast.LENGTH_SHORT).show();
                            break;

                        case "C":
                            if ((pbcLeft - numberTix) >=0) addToBasket(getSelectedPrice());
                            else Toast.makeText(ChooseTickets.this, "Sorry, these tickets are no longer available", Toast.LENGTH_SHORT).show();
                            break;

                        case "D":
                            if ((pbdLeft - numberTix) >=0) addToBasket(getSelectedPrice());
                            else Toast.makeText(ChooseTickets.this, "Sorry, these tickets are no longer available", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    updateAvail();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                updateAvail();
            }
        });
        requestQueue.add(stringRequest);
    }

}
