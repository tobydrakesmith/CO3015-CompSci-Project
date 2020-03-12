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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ChooseTickets extends AppCompatActivity {

    private Basket basket;

    private TextView numberTixLbl, tixLeftPBA, tixLeftPBB, tixLeftPBC, tixLeftPBD;
    private int numberTix;
    private Show mShow;
    private String strDate;
    private int userID;
    private boolean matinee;

    RadioButton pba, pbb, pbc, pbd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_tickets);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        numberTix = 0;

        Intent intent = getIntent();
        mShow = intent.getParcelableExtra("live_show");
        strDate = intent.getStringExtra("date");
        basket = intent.getParcelableExtra("basket");
        userID = intent.getIntExtra("userid", -1);
        matinee = intent.getBooleanExtra("matinee", false);

        mShow.setNumTicketsPbA(3);

        getSales();


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

        switch (item.getItemId()) {
            case R.id.activity_basket:
                Intent intent = new Intent(this, MyBasket.class);
                intent.putExtra("basket", basket);
                intent.putExtra("live_show", mShow);
                intent.putExtra("date", strDate);
                intent.putExtra("userid", userID);
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
        intent.putExtra("userid", userID);
        startActivity(intent);
    }


    public void onClickIncrease(View view) {

        if (pba.isChecked()) {
            if (numberTix<9 && numberTix < mShow.getNumTicketsPbA() ) {
                numberTix++;
                updateLabel();
                changeNumberTickets();
            }
        }
        else if (pbb.isChecked()){
            if (numberTix<9 && numberTix < mShow.getNumTicketsPbB() ) {
                numberTix++;
                updateLabel();
                changeNumberTickets();
            }

        }
        else if (pbc.isChecked()){
            if (numberTix<9 && numberTix < mShow.getNumTicketsPbC() ) {
                numberTix++;
                updateLabel();
                changeNumberTickets();
            }
        }
        else if (pbd.isChecked()) {
            if (numberTix<9 && numberTix < mShow.getNumTicketsPbD() ) {
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
        pba.setClickable(numberTix <= mShow.getNumTicketsPbA());
        pbb.setClickable(numberTix <= mShow.getNumTicketsPbB());
        pbc.setClickable(numberTix <= mShow.getNumTicketsPbC());
        pbd.setClickable(numberTix <= mShow.getNumTicketsPbD());
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

        return null;

    }

    private void updateLabel() {
        numberTixLbl = findViewById(R.id.numberOfTix);
        numberTixLbl.setText(Integer.toString(numberTix));
    }

    private void updateAvail(){
        tixLeftPBA.setText("Tickets left: " + (mShow.getNumTicketsPbA()));
        tixLeftPBB.setText("Tickets left: " + (mShow.getNumTicketsPbB()));
        tixLeftPBC.setText("Tickets left: " + (mShow.getNumTicketsPbC()));
        tixLeftPBD.setText("Tickets left: " + (mShow.getNumTicketsPbD()));
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
            addToBasket(getSelectedPrice());
    }

    private void addToBasket(int price) {

        BasketBooking basketBooking =
                new BasketBooking(strDate, matinee ? mShow.getMatineeStart() : mShow.getEveningStart(), mShow);

        String priceBand = getPriceBand();

        for (int i = 0; i < numberTix; i++) {
            Ticket t = new Ticket(price, mShow, priceBand);
            basketBooking.addTicket(t);
        }
        basket.addBooking(basketBooking);


        AlertDialog alertDialog = new AlertDialog.Builder(ChooseTickets.this).create();
        alertDialog.setTitle("Success");
        alertDialog.setMessage("Your tickets have been added to your basket, the tickets will release after 10 minutes.");
        System.out.println("BASKET EMPTY? " + basket.isEmpty());
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Take me to my basket",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ChooseTickets.this, MyBasket.class);
                        intent.putExtra("basket", basket);
                        intent.putExtra("live_show", mShow);
                        intent.putExtra("userid", userID);
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
                        intent.putExtra("userid", userID);
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
    }

    private void getSales() {
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
                        System.out.println(priceBand);
                        switch (priceBand){
                            case "A":
                                mShow.reduceNumberTixPBA();
                            break;
                            case "B":
                                mShow.reduceNumberTixPBB();
                                break;
                            case "C":
                                mShow.reduceNumberTixPBC();
                                break;
                            case "d":
                                mShow.reduceNumberTixPBD();
                                break;
                        }
                    }
                    updateAvail();
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
        Volley.newRequestQueue(this).add(stringRequest);
    }

}
