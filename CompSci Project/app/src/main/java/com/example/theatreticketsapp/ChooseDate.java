package com.example.theatreticketsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



public class ChooseDate extends AppCompatActivity {

    Show mShow;
    Basket basket;

    TextView matineeStart, eveningStart;
    Button matineeBtn, eveningBtn;

    CalendarView calendarView;
    Calendar calendar;

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_show);




        Intent i = getIntent();
        mShow = i.getParcelableExtra("live_show");
        basket = i.getParcelableExtra("basket");
        user = i.getParcelableExtra("user");

        calendar = Calendar.getInstance();

        matineeStart = findViewById(R.id.matineeTime);
        eveningStart = findViewById(R.id.eveningTime);
        matineeBtn = findViewById(R.id.matineeBook);
        eveningBtn = findViewById(R.id.eveningBook);

        matineeStart.setText(mShow.getMatineeStart());
        eveningStart.setText(mShow.getEveningStart());

        calendarView = findViewById(R.id.calendarView);

        try{
            Date startDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(mShow.getStartDate());
            assert startDate != null;
            if (startDate.after(new Date())) {
                calendarView.setMinDate(startDate.getTime());
            }else {
                calendarView.setMinDate(new Date().getTime());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                getDayOfWeek(dayOfWeek);
                getShowTimes(calendar.getTime(), new Date());
            }
        } catch (ParseException e){
            e.printStackTrace();
        }

        try {
            Date endDate = new SimpleDateFormat("yyyy-MM-dd", Locale.UK).parse(mShow.getEndDate());
            assert endDate != null;
            calendarView.setMaxDate(endDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }






        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                getDayOfWeek(dayOfWeek);
                getShowTimes(calendar.getTime(), new Date());
            }
        });
    }

    private void getShowTimes(Date date, Date date2) {
        if (date.compareTo(date2) <= 0){
            try {
                String strDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(date);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.ENGLISH);

                date = sdf.parse(strDate + " " + mShow.getMatineeStart());
                assert date != null;
                matineeBtn.setEnabled(date.after(new Date()));


                date = sdf.parse(strDate + " " + mShow.getEveningStart());
                assert date != null;
                eveningBtn.setEnabled(date.after(new Date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else {
            eveningBtn.setEnabled(true);
            matineeBtn.setEnabled(true);
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent (this, ShowInformation.class);
        intent.putExtra("basket", basket);
        intent.putExtra("live_show", mShow);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void getDayOfWeek(int day){

        switch (day){

            case 1:
                matineeStart.setVisibility(mShow.isSunMat() ? View.VISIBLE : View.GONE);
                eveningStart.setVisibility(mShow.isSunEve() ? View.VISIBLE : View.GONE);

                matineeBtn.setVisibility(mShow.isSunMat() ? View.VISIBLE : View.GONE);
                eveningBtn.setVisibility(mShow.isSunEve() ? View.VISIBLE : View.GONE);
                break;
            case 2:
                matineeStart.setVisibility(mShow.isMonMat() ? View.VISIBLE : View.GONE);
                eveningStart.setVisibility(mShow.isMonEve() ? View.VISIBLE : View.GONE);

                matineeBtn.setVisibility(mShow.isMonMat() ? View.VISIBLE : View.GONE);
                eveningBtn.setVisibility(mShow.isMonEve() ? View.VISIBLE : View.GONE);
                break;
            case 3:
                matineeStart.setVisibility(mShow.isTueMat() ? View.VISIBLE : View.GONE);
                eveningStart.setVisibility(mShow.isTueEve() ? View.VISIBLE : View.GONE);

                matineeBtn.setVisibility(mShow.isTueMat() ? View.VISIBLE : View.GONE);
                eveningBtn.setVisibility(mShow.isTueEve() ? View.VISIBLE : View.GONE);
                break;
            case 4:
                matineeStart.setVisibility(mShow.isWedMat() ? View.VISIBLE : View.GONE);
                eveningStart.setVisibility(mShow.isWedEve() ? View.VISIBLE : View.GONE);

                matineeBtn.setVisibility(mShow.isWedMat() ? View.VISIBLE : View.GONE);
                eveningBtn.setVisibility(mShow.isWedEve() ? View.VISIBLE : View.GONE);
                break;

            case 5:
                matineeStart.setVisibility(mShow.isThuMat() ? View.VISIBLE : View.GONE);
                eveningStart.setVisibility(mShow.isThuEve() ? View.VISIBLE : View.GONE);

                matineeBtn.setVisibility(mShow.isThuMat() ? View.VISIBLE : View.GONE);
                eveningBtn.setVisibility(mShow.isThuEve() ? View.VISIBLE : View.GONE);
                break;

            case 6:
                matineeStart.setVisibility(mShow.isFriMat() ? View.VISIBLE : View.GONE);
                eveningStart.setVisibility(mShow.isFriEve() ? View.VISIBLE : View.GONE);

                matineeBtn.setVisibility(mShow.isFriMat() ? View.VISIBLE : View.GONE);
                eveningBtn.setVisibility(mShow.isFriEve() ? View.VISIBLE : View.GONE);
                break;

            case 7:
                matineeStart.setVisibility(mShow.isSatMat() ? View.VISIBLE : View.GONE);
                eveningStart.setVisibility(mShow.isSatEve() ? View.VISIBLE : View.GONE);

                matineeBtn.setVisibility(mShow.isSatMat() ? View.VISIBLE : View.GONE);
                eveningBtn.setVisibility(mShow.isSatEve() ? View.VISIBLE : View.GONE);
                break;

        }


    


    }

    public void matineeBtnClicked(View view){

        Date date = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        String parcelDate = dateFormat.format(date);

        Intent intent = new Intent(this, ChooseTickets.class);
        intent.putExtra("live_show", mShow);
        intent.putExtra("date", parcelDate);
        intent.putExtra("basket", basket);
        intent.putExtra("user", user);
        intent.putExtra("matinee", true);
        startActivity(intent);
    }


    public void eveningBtnClicked(View view){

        Date date = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        String parcelDate = dateFormat.format(date);

        Intent intent = new Intent(this, ChooseTickets.class);
        intent.putExtra("live_show", mShow);
        intent.putExtra("date", parcelDate);
        intent.putExtra("basket", basket);
        intent.putExtra("user", user);
        intent.putExtra("matinee", false);
        startActivity(intent);

    }
}
