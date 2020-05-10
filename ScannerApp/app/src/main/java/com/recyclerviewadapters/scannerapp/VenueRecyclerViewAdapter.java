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
    private int selected = RecyclerView.NO_POSITION;



    public VenueRecyclerViewAdapter(Context context, ArrayList<Venue> venues) {
        this.mInflater = LayoutInflater.from(context);
        this.mVenues = venues;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row_venue, parent, false);
        return new ViewHolder(view);
    }

    public int getSelected() {
        return selected;
    }

    @Override
    public void onBindViewHolder(VenueRecyclerViewAdapter.ViewHolder holder, final int position) {


        Venue venue = mVenues.get(position);

        holder.name.setText(venue.getName());
        holder.city.setText(venue.getCity());
        holder.postcode.setText(venue.getPostcode());

        holder.itemView.setSelected(selected == position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == position){
                    selected = RecyclerView.NO_POSITION;
                    notifyDataSetChanged();
                }
                selected = position;
                notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemCount() {
        return mVenues.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, postcode, city;

        ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.venueName);
            postcode = view.findViewById(R.id.postcode);
            city = view.findViewById(R.id.city);


        }


    }


}
