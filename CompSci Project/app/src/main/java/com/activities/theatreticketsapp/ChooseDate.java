package com.activities.theatreticketsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.classfiles.theatreticketsapp.Basket;
import com.classfiles.theatreticketsapp.Show;
import com.classfiles.theatreticketsapp.User;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ChooseDate extends AppCompatActivity {

    private Show mShow;
    private Basket basket;

    private TextView matineeStart, eveningStart, noShows;
    private Button matineeBtn, eveningBtn;

    private Calendar calendar;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        Intent i = getIntent();
        mShow = i.getParcelableExtra("live_show");
        basket = i.getParcelableExtra("basket");
        user = i.getParcelableExtra("user");

        setTitle(mShow.getShowName());

        calendar = Calendar.getInstance();

        matineeStart = findViewById(R.id.matineeTime);
        eveningStart = findViewById(R.id.eveningTime);
        matineeBtn = findViewById(R.id.matineeBook);
        eveningBtn = findViewById(R.id.eveningBook);
        noShows = findViewById(R.id.noShowsLbl);

        matineeStart.setText(mShow.getMatineeStart());
        eveningStart.setText(mShow.getEveningStart());

        CalendarView calendarView = findViewById(R.id.calendarView);

        try {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(mShow.getStartDate());
            assert startDate != null;
            if (startDate.after(new Date())) {
                calendarView.setMinDate(startDate.getTime());
            } else {
                calendarView.setMinDate(new Date().getTime());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                getDayOfWeek(dayOfWeek);
                getShowTimes(calendar.getTime(), new Date());
            }
        } catch (ParseException e) {
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
        if (date.compareTo(date2) <= 0) {
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

        } else {
            eveningBtn.setEnabled(true);
            matineeBtn.setEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void getDayOfWeek(int day) {

        switch (day) {

            case 1:
                matineeBtn.setVisibility(mShow.isSunMat() ? View.VISIBLE : View.GONE);
                eveningBtn.setVisibility(mShow.isSunEve() ? View.VISIBLE : View.GONE);
                break;

            case 2:
                matineeBtn.setVisibility(mShow.isMonMat() ? View.VISIBLE : View.GONE);
                eveningBtn.setVisibility(mShow.isMonEve() ? View.VISIBLE : View.GONE);
                break;

            case 3:
                matineeBtn.setVisibility(mShow.isTueMat() ? View.VISIBLE : View.GONE);
                eveningBtn.setVisibility(mShow.isTueEve() ? View.VISIBLE : View.GONE);
                break;

            case 4:
                matineeBtn.setVisibility(mShow.isWedMat() ? View.VISIBLE : View.GONE);
                eveningBtn.setVisibility(mShow.isWedEve() ? View.VISIBLE : View.GONE);
                break;

            case 5:
                matineeBtn.setVisibility(mShow.isThuMat() ? View.VISIBLE : View.GONE);
                eveningBtn.setVisibility(mShow.isThuEve() ? View.VISIBLE : View.GONE);
                break;

            case 6:
                matineeBtn.setVisibility(mShow.isFriMat() ? View.VISIBLE : View.GONE);
                eveningBtn.setVisibility(mShow.isFriEve() ? View.VISIBLE : View.GONE);
                break;

            case 7:
                matineeBtn.setVisibility(mShow.isSatMat() ? View.VISIBLE : View.GONE);
                eveningBtn.setVisibility(mShow.isSatEve() ? View.VISIBLE : View.GONE);
                break;

        }


        boolean mat = matineeBtn.getVisibility() == View.VISIBLE;
        boolean eve = eveningBtn.getVisibility() == View.VISIBLE;

        matineeStart.setVisibility(mat ? View.VISIBLE : View.GONE);
        eveningStart.setVisibility(eve ? View.VISIBLE : View.GONE);

        noShows.setVisibility((!mat && !eve) ? View.VISIBLE : View.INVISIBLE);


    }

    public void matineeBtnClicked(View view) {

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


    public void eveningBtnClicked(View view) {

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
