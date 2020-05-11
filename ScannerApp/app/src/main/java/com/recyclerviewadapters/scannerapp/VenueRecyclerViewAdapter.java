package com.recyclerviewadapters.scannerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.classfiles.scannerapp.Venue;
import com.example.scannerapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class VenueRecyclerViewAdapter extends RecyclerView.Adapter<VenueRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<Venue> mVenues;
    private VenueRecyclerViewAdapter.OnVenueClick onVenueClick;
    private int selected = RecyclerView.NO_POSITION;





    public VenueRecyclerViewAdapter(Context context, ArrayList<Venue> venues,
                                    VenueRecyclerViewAdapter.OnVenueClick onVenueClick) {
        this.mInflater = LayoutInflater.from(context);
        this.mVenues = venues;
        this.onVenueClick = onVenueClick;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row_venue, parent, false);
        return new ViewHolder(view, onVenueClick);
    }

    public void setSelected(int s){
        this.selected = s;
    }



    @Override
    public void onBindViewHolder(VenueRecyclerViewAdapter.ViewHolder holder, int position) {

        Venue venue = mVenues.get(position);

        holder.name.setText(venue.getName());
        holder.city.setText(venue.getCity());
        holder.postcode.setText(venue.getPostcode());

        holder.itemView.setSelected(selected == position);


    }


    @Override
    public int getItemCount() {
        return mVenues.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, postcode, city;
        OnVenueClick venueClick;
        int selected = RecyclerView.NO_POSITION;

        ViewHolder(View view, OnVenueClick onVenueClick) {
            super(view);
            venueClick = onVenueClick;
            name = view.findViewById(R.id.venueName);
            postcode = view.findViewById(R.id.postcode);
            city = view.findViewById(R.id.city);
            view.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {
            v.setSelected(true);
            selected = getAdapterPosition();
            venueClick.onVenueClick(getAdapterPosition(), v);
        }
    }

    public interface OnVenueClick{
        void onVenueClick(int position, View view);
    }


}
