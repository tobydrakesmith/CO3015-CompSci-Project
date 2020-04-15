    package com.example.theatreticketsapp;

    import android.content.Context;
    import android.location.Location;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Filter;
    import android.widget.Filterable;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.recyclerview.widget.RecyclerView;

    import java.util.ArrayList;
    import java.util.List;

    public class ShowRecyclerViewAdapter extends RecyclerView.Adapter<ShowRecyclerViewAdapter.ViewHolder> implements Filterable {

        private List<Show> mShows, showListFull;
        private LayoutInflater mInflater;
        private OnShowClickListener mOnShowListener;
        private Location userLocation;



        // data is passed into the constructor
        ShowRecyclerViewAdapter(Context context, ArrayList<Show> shows, OnShowClickListener onShowClickListener, Location userLocation) {
            this.mInflater = LayoutInflater.from(context);
            this.mShows = shows;
            this.mOnShowListener = onShowClickListener;
            this.showListFull = new ArrayList<>(shows);
            this.userLocation = userLocation;
        }

        // inflates the row layout from xml when needed
         @Override
         public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
            return new ViewHolder(view, mOnShowListener);
         }

         // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder (ViewHolder holder, int position) {
            Show show = mShows.get(position);
            holder.showNameTextView.setText(show.getShowName());
            holder.venueNameTextView.setText(show.getVenueName());
            holder.imageView.setImageResource(R.drawable.ic_theaters_black_24dp);
            try {
                holder.distance.setText(show.getUserDistanceFromVenue(userLocation) + " km");
            } catch (NullPointerException e){
                holder.distance.setText("");
            }
        }


        @Override
        public int getItemCount () {
            return mShows.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView showNameTextView;
            TextView venueNameTextView;
            TextView distance;
            ImageView imageView;
            OnShowClickListener onShowClickListener;

            ViewHolder(View itemView, OnShowClickListener onShowClickListener) {
                super(itemView);
                this.onShowClickListener = onShowClickListener;
                showNameTextView = itemView.findViewById(R.id.userName);
                venueNameTextView = itemView.findViewById(R.id.venue);
                imageView = itemView.findViewById(R.id.imageView);
                distance = itemView.findViewById(R.id.distance);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                onShowClickListener.onShowClick(getAdapterPosition());

            }
        }

        public interface OnShowClickListener{
            void onShowClick(int position);
        }

        @Override
        public Filter getFilter() {
            return exampleFilter;
        }

        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                List<Show> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0)
                    filteredList.addAll(showListFull);
                else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Show show : showListFull){
                        if (show.getShowName().toLowerCase().contains(filterPattern)
                                || show.getVenueName().toLowerCase().contains(filterPattern))
                            filteredList.add(show);

                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mShows.clear();
                mShows.addAll((List) results.values);
                notifyDataSetChanged();

            }
        };
    }
