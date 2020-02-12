package com.example.theatreticketsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;



public class ChooseTickets extends AppCompatActivity {

    Basket basket;

    TextView numberTixLbl;
    int numberTix;
    Show mShow;
    String strDate;
    long milliLeft;
    int userID;


    RadioButton pba, pbb, pbc, pbd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_tickets);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        numberTix = 0;

        Intent i = getIntent();
        mShow = i.getParcelableExtra("live_show");
        strDate = i.getStringExtra("date");
        basket = i.getParcelableExtra("basket");
        userID = i.getIntExtra("userid", -1);


        milliLeft = basket.getMilliLeft();

        CountDownTimer countDownTimer = new CountDownTimer(milliLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                basket.setMilliLeft(millisUntilFinished);
            }
            @Override
            public void onFinish() {
                basket.releaseTickets();
            }
        };

        if (!basket.isEmpty()) {
            System.out.println("Basket not empty - TIMER START");
            countDownTimer.start();
        }


        pba = findViewById(R.id.priceBandA);
        pbb = findViewById(R.id.priceBandB);
        pbc = findViewById(R.id.priceBandC);
        pbd = findViewById(R.id.priceBandD);

        pba.setText("£" + mShow.getPricePbA());
        pbb.setText("£" + mShow.getPricePbB());
        pbc.setText("£" + mShow.getPricePbC());
        pbd.setText("£" + mShow.getPricePbD());

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
        Intent intent = new Intent (this, BookShow.class);
        intent.putExtra("basket", basket);
        intent.putExtra("live_show", mShow);
        intent.putExtra("userid", userID);
        startActivity(intent);
    }




    public void onClickIncrease(View view){
        numberTix ++;
        updateLabel();
    }


    public void onClickDecrease(View view){
        if(numberTix>0) {
            numberTix--;
            updateLabel();
        }
    }

    private int getSelectedPrice(){

        if (pba.isChecked()) return mShow.getPricePbA();
        if (pbb.isChecked()) return mShow.getPricePbB();
        if (pbc.isChecked()) return mShow.getPricePbC();
        if (pbd.isChecked()) return mShow.getPricePbD();

        return -1;
    }

    private void updateLabel(){
        numberTixLbl = findViewById(R.id.numberOfTix);
        numberTixLbl.setText(Integer.toString(numberTix));
    }

    public void onClickAddToBasket(View view){
        if (numberTix == 0)
            Toast.makeText(this, "You have not selected the number of tickets you would like", Toast.LENGTH_SHORT).show();

        else if (getSelectedPrice() == -1)
            Toast.makeText(this, "Please select a price to continue", Toast.LENGTH_SHORT).show();

        else
            addToBasket(getSelectedPrice());
    }

    private void addToBasket(int price){
        for (int i=0; i<numberTix; i++){
            Ticket t = new Ticket(price, strDate, mShow);
            basket.addTicket(t);
        }
        AlertDialog alertDialog = new AlertDialog.Builder(ChooseTickets.this).create();
        alertDialog.setTitle("Success");
        alertDialog.setMessage("Your tickets have been added to your basket, the tickets will release after 10 minutes.");
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
}
