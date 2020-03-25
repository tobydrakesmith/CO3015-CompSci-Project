package com.example.theatreticketsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Review> mReviews;
    private LayoutInflater mInflater;
    private ReviewsRecyclerViewAdapter.OnReviewClickListener mOnReviewClickListener;

    ReviewsRecyclerViewAdapter(Context context, ReviewsRecyclerViewAdapter.OnReviewClickListener onReviewClickListener,
                               ArrayList<Review> reviews){
        this.mInflater = LayoutInflater.from(context);
        this.mOnReviewClickListener = onReviewClickListener;
        this.mReviews = reviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.recyclerview_reviews, parent, false);
        return new ViewHolder(view, mOnReviewClickListener);

    }


    @Override
    public void onBindViewHolder(ReviewsRecyclerViewAdapter.ViewHolder holder, int position){

        Review review = mReviews.get(position);

        holder.rating.setText(Integer.toString(review.getRating())+ "/5");
        holder.date.setText(review.getDate());
        holder.reviewText.setText(review.getRatingTxt());


    }

    @Override
    public int getItemCount () {
        return mReviews.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView rating, reviewText, date, userName;

        ReviewsRecyclerViewAdapter.OnReviewClickListener onReviewClickListener;

        ViewHolder(View view, ReviewsRecyclerViewAdapter.OnReviewClickListener onReviewClickListener){
            super(view);

            rating = view.findViewById(R.id.rating);
            reviewText = view.findViewById(R.id.reviewText);
            date = view.findViewById(R.id.date);



        }

        @Override
        public void onClick(View view){

        }

    }

    public interface OnReviewClickListener{

    }
}
