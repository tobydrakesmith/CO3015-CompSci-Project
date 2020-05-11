package com.recyclerviewadapters.scannerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.classfiles.scannerapp.Show;
import com.example.scannerapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ShowRecyclerViewAdapter extends RecyclerView.Adapter<ShowRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<Show> mShows;
    private ShowRecyclerViewAdapter.OnShowClick onShowClick;
    private int selected = RecyclerView.NO_POSITION;



    public ShowRecyclerViewAdapter(Context context, ArrayList<Show> shows, ShowRecyclerViewAdapter.OnShowClick onShowClick) {
        this.mInflater = LayoutInflater.from(context);
        this.mShows = shows;
        this.onShowClick = onShowClick;
    }

    @NotNull
    @Override
    public ShowRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row_show, parent, false);
        return new ViewHolder(view, onShowClick);
    }

    public void setSelected(int selected){
        this.selected = selected;
    }

    @Override
    public void onBindViewHolder(ShowRecyclerViewAdapter.ViewHolder holder, final int position) {


        Show show = mShows.get(position);

        holder.name.setText(show.getName());

        holder.itemView.setSelected(selected == position);

    }


    @Override
    public int getItemCount() {
        return mShows.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        OnShowClick onShowClick;

        ViewHolder(View view, OnShowClick onShowClick) {
            super(view);

            this.onShowClick = onShowClick;
            name = view.findViewById(R.id.showName);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onShowClick.onShowClick(getAdapterPosition(), v);
        }
    }

    public interface OnShowClick{
        void onShowClick(int pos, View view);
    }


}
