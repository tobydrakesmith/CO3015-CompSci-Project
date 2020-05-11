package com.recyclerviewadapters.theatreticketsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.activities.theatreticketsapp.R;
import com.classfiles.theatreticketsapp.BasketBooking;
import com.classfiles.theatreticketsapp.Ticket;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyBasketRecyclerViewAdapter extends RecyclerView.Adapter<MyBasketRecyclerViewAdapter.ViewHolder> {


    private ArrayList<BasketBooking> mBookings;
    private LayoutInflater mInflater;
    private MyBasketRecyclerViewAdapter.OnTicketClickListener mOnTicketListener;

    public MyBasketRecyclerViewAdapter(Context context,
                                       OnTicketClickListener onClickListener, ArrayList<BasketBooking> bookings) {
        this.mInflater = LayoutInflater.from(context);
        this.mOnTicketListener = onClickListener;
        this.mBookings = bookings;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_basket_row, parent, false);
        return new ViewHolder(view, mOnTicketListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String showName = mBookings.get(position).getShowName();
        ArrayList<Ticket> tickets = mBookings.get(position).getTickets();

        holder.showNameText.setText(showName);
        holder.date.setText(mBookings.get(position).getDate() + " " + mBookings.get(position).getStartTime());
        holder.numberOfTickets.setText("x" + (tickets.size()));
        holder.cost.setText("£"+mBookings.get(position).getCost());

    }

    @Override
    public int getItemCount() {
        return mBookings.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView showNameText, numberOfTickets, date, cost;
        Button btnDelete;
        OnTicketClickListener onTicketClickListener;

        ViewHolder(View view, OnTicketClickListener onTicketClickListener) {
            super(view);
            this.onTicketClickListener = onTicketClickListener;
            showNameText = view.findViewById(R.id.showName);
            numberOfTickets = view.findViewById(R.id.numberOfTickets);
            date = view.findViewById(R.id.date);
            btnDelete = view.findViewById(R.id.btnCancel);
            cost = view.findViewById(R.id.cost);

            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTicketClickListener.onTicketClick(getAdapterPosition());
        }

    }

    public interface OnTicketClickListener {
        void onTicketClick(int position);
    }
}

