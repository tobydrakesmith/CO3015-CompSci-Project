package com.example.theatreticketsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyBookingsRecyclerViewAdapter extends RecyclerView.Adapter<MyBookingsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Booking> mBookings;
    private LayoutInflater mInflater;
    private MyBookingsRecyclerViewAdapter.OnBookingClickListener mOnBookingClickListener;

    MyBookingsRecyclerViewAdapter(Context context,
                                  MyBookingsRecyclerViewAdapter.OnBookingClickListener onClickListener,
                                  ArrayList<Booking> bookings){

        this.mInflater = LayoutInflater.from(context);
        this.mOnBookingClickListener = onClickListener;
        this.mBookings = bookings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.recyclerview_my_bookings_row, parent, false);

        return new ViewHolder(view, mOnBookingClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        Booking booking = mBookings.get(position);

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(booking.getDate());

            assert date != null;
            String strDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(date);

            date = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.ENGLISH).parse(strDate+ " " + booking.getShowTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.showName.setText(booking.getShowName());
        assert date != null;
        holder.date.setText(date.toString());
        holder.numberOfTickets.setText("No. tickets: " + booking.getNumberOfTickets());
    }

    @Override
    public int getItemCount () {
        return mBookings.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView showName, date, numberOfTickets;
        Button viewTickets;


        MyBookingsRecyclerViewAdapter.OnBookingClickListener onBookingClickListener;

        ViewHolder(View view, MyBookingsRecyclerViewAdapter.OnBookingClickListener onBookingClickListener){
            super(view);
            this.onBookingClickListener = onBookingClickListener;

            showName = view.findViewById(R.id.showName);
            date = view.findViewById(R.id.date);
            numberOfTickets = view.findViewById(R.id.numberOfTickets);
            viewTickets = view.findViewById(R.id.btnViewBooking);

            viewTickets.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            if (view.getId() == R.id.btnViewBooking)
                onBookingClickListener.onBookingClick(getAdapterPosition());
        }

    }

    public interface OnBookingClickListener{
        void onBookingClick(int position);
    }
}
