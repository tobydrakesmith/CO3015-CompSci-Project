package com.example.theatreticketsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class BasketRecyclerViewAdapter extends RecyclerView.Adapter<BasketRecyclerViewAdapter.ViewHolder> {

    private HashMap<String, ArrayList<Ticket>> mTickets;
    private ArrayList<String> mShowNames;
    private LayoutInflater mInflater;
    private BasketRecyclerViewAdapter.OnTicketClickListener mOnTicketListener;

    BasketRecyclerViewAdapter(Context context, HashMap<String, ArrayList<Ticket>> tickets,
                              OnTicketClickListener onClickListener, ArrayList<String> showNames){
        this.mInflater = LayoutInflater.from(context);
        this.mTickets = tickets;
        this.mOnTicketListener = onClickListener;
        this.mShowNames = showNames;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.recyclerview_basket_row, parent, false);
        return new ViewHolder(view, mOnTicketListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        String showName = mShowNames.get(position);
        ArrayList<Ticket> tickets = mTickets.get(showName);

        holder.showNameText.setText(showName);
        holder.date.setText(tickets.get(0).getDate());
        holder.numberOfTickets.setText("x" + (tickets.size()));

    }

    @Override
    public int getItemCount () {
        return mTickets.size();
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

