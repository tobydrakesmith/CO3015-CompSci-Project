    package com.example.theatreticketsapp;

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.recyclerview.widget.RecyclerView;

    import java.util.ArrayList;
    import java.util.List;

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        private List<Show> mShows;
        private LayoutInflater mInflater;
        private OnShowClickListener mOnShowListener;



        // data is passed into the constructor
        MyRecyclerViewAdapter(Context context, ArrayList<Show> shows, OnShowClickListener onShowClickListener) {
            this.mInflater = LayoutInflater.from(context);
            this.mShows = shows;
            this.mOnShowListener = onShowClickListener;
        }

        // inflates the row layout from xml when needed
         @Override
         public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
            return new ViewHolder(view, mOnShowListener);
         }

         // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder (ViewHolder holder,int position){
            Show show = mShows.get(position);
            holder.showNameTextView.setText(show.getShowName());
            holder.venueNameTextView.setText(show.getVenueName());
            holder.imageView.setImageResource(R.drawable.ic_theaters_black_24dp);
        }

        // total number of rows
        @Override
        public int getItemCount () {
            return mShows.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView showNameTextView;
            TextView venueNameTextView;
            ImageView imageView;
            OnShowClickListener onShowClickListener;

            ViewHolder(View itemView, OnShowClickListener onShowClickListener) {
                super(itemView);
                this.onShowClickListener = onShowClickListener;
                showNameTextView = itemView.findViewById(R.id.userName);
                venueNameTextView = itemView.findViewById(R.id.venue);
                imageView = itemView.findViewById(R.id.imageView);
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

   }
