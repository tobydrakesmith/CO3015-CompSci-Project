package com.example.theatreticketsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Review> mReviews;
    private LayoutInflater mInflater;
    private ReviewsRecyclerViewAdapter.OnReviewClickListener mOnReviewClickListener;
    private boolean userReviews;

    ReviewsRecyclerViewAdapter(Context context, ReviewsRecyclerViewAdapter.OnReviewClickListener onReviewClickListener,
                               ArrayList<Review> reviews, boolean userReviews){
        this.mInflater = LayoutInflater.from(context);
        this.mOnReviewClickListener = onReviewClickListener;
        this.mReviews = reviews;
        this.userReviews = userReviews;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.recyclerview_reviews, parent, false);
        return new ViewHolder(view, mOnReviewClickListener);

    }


    @Override
    public void onBindViewHolder(ReviewsRecyclerViewAdapter.ViewHolder holder, int position){

        Review review = mReviews.get(position);

        holder.ratingBar.setRating(review.getRating());
        holder.date.setText(review.getDate());
        holder.reviewText.setText(review.getReviewTxt());
        if (userReviews) holder.userName.setText(review.getShowName());
        else holder.userName.setText("Username" + review.getUserid());


    }

    @Override
    public int getItemCount () {
        return mReviews.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        RatingBar ratingBar;
        TextView reviewText, date, userName;

        ReviewsRecyclerViewAdapter.OnReviewClickListener onReviewClickListener;

        ViewHolder(View view, ReviewsRecyclerViewAdapter.OnReviewClickListener onReviewClickListener){
            super(view);

            this.onReviewClickListener = onReviewClickListener;

            ratingBar = view.findViewById(R.id.ratingBar);
            reviewText = view.findViewById(R.id.reviewText);
            date = view.findViewById(R.id.date);
            userName = view.findViewById(R.id.showName);

            view.setOnClickListener(this);



        }

        @Override
        public void onClick(View view){
            onReviewClickListener.onReviewClick(getAdapterPosition());
        }

    }

    public interface OnReviewClickListener{
        void onReviewClick(int position);
    }
}
