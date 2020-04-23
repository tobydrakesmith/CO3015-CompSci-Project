package com.example.theatreticketsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CheckoutRecyclerViewAdapter extends RecyclerView.Adapter<CheckoutRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private CheckoutRecyclerViewAdapter.OnCalendarClick onCalendarClick;
    ArrayList<BasketBooking> mBookings;

    CheckoutRecyclerViewAdapter(Context context,
                                OnCalendarClick onClickListener, ArrayList<BasketBooking> bookings){
        this.mInflater = LayoutInflater.from(context);
        this.onCalendarClick = onClickListener;
        this.mBookings = bookings;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.recyclerview_checkout_row, parent, false);
        return new ViewHolder(view, onCalendarClick);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        BasketBooking booking = mBookings.get(position);

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(booking.getDate());

            String strDate = new SimpleDateFormat("dd-MM-yyyy").format(date);

            date = new SimpleDateFormat("dd-MM-yyyy hh:mm").parse(strDate+ " " + booking.getStartTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.showName.setText(booking.getShowName());
        holder.venueName.setText(booking.getShow().getVenue().getVenueName());
        holder.numberOfTickets.setText("Number of tickets: "+booking.getNumberOfTickets());
        holder.bookingCost.setText("£"+booking.getCost());
        holder.date.setText(date.toString());
    }

    @Override
    public int getItemCount () {
        return mBookings.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView showName, venueName, bookingCost, numberOfTickets, date;
        FloatingActionButton fabCalendar;
        OnCalendarClick calendarClick;

        ViewHolder(View view, OnCalendarClick onCalendarClick){
            super(view);
            this.calendarClick = onCalendarClick;

            showName = view.findViewById(R.id.showName);
            venueName = view.findViewById(R.id.venueName);
            date = view.findViewById(R.id.bookingDate);
            bookingCost = view.findViewById(R.id.bookingCost);
            numberOfTickets = view.findViewById(R.id.numberTickets);

            fabCalendar = view.findViewById(R.id.fabCalendar);
            fabCalendar.setOnClickListener(this);

        }

        @Override
        public void onClick(View view){
            calendarClick.onCalendarClick(getAdapterPosition());
        }

    }

    public interface OnCalendarClick{
        void onCalendarClick(int position);
    }

}

