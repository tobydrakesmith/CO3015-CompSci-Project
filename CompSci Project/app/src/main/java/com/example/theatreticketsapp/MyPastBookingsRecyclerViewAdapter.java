package com.example.theatreticketsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyPastBookingsRecyclerViewAdapter extends RecyclerView.Adapter<MyPastBookingsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Booking> mBookings;
    private LayoutInflater mInflater;
    private MyPastBookingsRecyclerViewAdapter.OnReviewClickListener mOnReviewClickListener;

    MyPastBookingsRecyclerViewAdapter(Context context, MyPastBookingsRecyclerViewAdapter.OnReviewClickListener onClickListener, ArrayList<Booking> bookings){
        this.mInflater = LayoutInflater.from(context);
        this.mOnReviewClickListener = onClickListener;
        this.mBookings = bookings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.recyclerview_my_past_bookings_row, parent, false);
        return new ViewHolder(view, mOnReviewClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Booking booking = mBookings.get(position);

        holder.showName.setText(booking.getShowName());
        holder.date.setText(booking.getDate());
        holder.numberOfTickets.setText("No. tickets: " + booking.getNumberOfTickets());
    }

    @Override
    public int getItemCount () {
        return mBookings.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView showName, date, numberOfTickets;
        Button leaveAReview;


        MyPastBookingsRecyclerViewAdapter.OnReviewClickListener onReviewClickListener;

        ViewHolder(View view, MyPastBookingsRecyclerViewAdapter.OnReviewClickListener onReviewClickListener){
            super(view);
            this.onReviewClickListener = onReviewClickListener;

            showName = view.findViewById(R.id.showName);
            date = view.findViewById(R.id.date);
            numberOfTickets = view.findViewById(R.id.numberOfTickets);
            leaveAReview = view.findViewById(R.id.btnViewBooking);

            leaveAReview.setOnClickListener(this);

        }

        @Override
        public void onClick(View view){
            onReviewClickListener.onReviewClick(getAdapterPosition());
        }

    }

    public interface OnReviewClickListener{
        void onReviewClick(int position);
    }
}
