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

    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.Date;
    import java.util.List;
    import java.util.Locale;

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

        void setFullList(ArrayList<Show> filteredShows){
            showListFull.clear();
            showListFull.addAll(filteredShows);
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
            holder.venueNameTextView.setText(show.getVenueName()+",");
            holder.venueCity.setText(show.getVenue().getCity());
            holder.imageView.setImageResource(R.drawable.ic_theaters_black_24dp);

            Date date = null;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_WEEK, -3);


            try{
                date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).parse(mShows.get(position).getDateAdded());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.newImage.setVisibility(date.after(calendar.getTime()) ? View.VISIBLE : View.INVISIBLE);

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
        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView showNameTextView;
            TextView venueNameTextView, venueCity;
            TextView distance;
            ImageView imageView, newImage;
            OnShowClickListener onShowClickListener;

            ViewHolder(View itemView, OnShowClickListener onShowClickListener) {
                super(itemView);
                this.onShowClickListener = onShowClickListener;
                showNameTextView = itemView.findViewById(R.id.showName);
                venueNameTextView = itemView.findViewById(R.id.venue);
                venueCity = itemView.findViewById(R.id.venueCity);
                imageView = itemView.findViewById(R.id.imageView);
                distance = itemView.findViewById(R.id.distance);
                newImage = itemView.findViewById(R.id.newImage);

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
            return filter;
        }

        private Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {



                List<Show> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0)
                    filteredList.addAll(showListFull);
                else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Show show : showListFull){
                        if (show.getShowName().toLowerCase().contains(filterPattern)
                                || show.getVenueName().toLowerCase().contains(filterPattern)
                                || show.getVenue().getCity().toLowerCase().contains(filterPattern))

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
