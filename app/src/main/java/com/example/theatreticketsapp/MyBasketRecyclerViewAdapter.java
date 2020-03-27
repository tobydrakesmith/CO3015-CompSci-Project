package com.example.theatreticketsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyBasketRecyclerViewAdapter extends RecyclerView.Adapter<MyBasketRecyclerViewAdapter.ViewHolder> {


    private ArrayList<BasketBooking> mBookings;
    private LayoutInflater mInflater;
    private MyBasketRecyclerViewAdapter.OnTicketClickListener mOnTicketListener;

    MyBasketRecyclerViewAdapter(Context context,
                                OnTicketClickListener onClickListener, ArrayList<BasketBooking> bookings){
        this.mInflater = LayoutInflater.from(context);
        this.mOnTicketListener = onClickListener;
        this.mBookings = bookings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.recyclerview_basket_row, parent, false);
        return new ViewHolder(view, mOnTicketListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        String showName = mBookings.get(position).getShowName();
        ArrayList<Ticket> tickets = mBookings.get(position).getTickets();

        holder.showNameText.setText(showName);
        holder.date.setText(mBookings.get(position).getDate() + " " + mBookings.get(position).getStartTime());
        holder.numberOfTickets.setText("x" + (tickets.size()));

    }

    @Override
    public int getItemCount () {
        return mBookings.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView showNameText;
        TextView numberOfTickets;
        TextView date;
        Button btnDelete;
        OnTicketClickListener onTicketClickListener;

        ViewHolder(View view, OnTicketClickListener onTicketClickListener){
            super(view);
            this.onTicketClickListener = onTicketClickListener;
            showNameText = view.findViewById(R.id.showName);
            numberOfTickets = view.findViewById(R.id.numberOfTickets);
            date = view.findViewById(R.id.date);
            btnDelete = view.findViewById(R.id.btnCancel);

            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            onTicketClickListener.onTicketClick(getAdapterPosition());
        }

    }

    public interface OnTicketClickListener{
        void onTicketClick(int position);
    }
}

