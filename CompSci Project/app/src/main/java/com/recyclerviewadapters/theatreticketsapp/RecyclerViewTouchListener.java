package com.recyclerviewadapters.theatreticketsapp;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {

    private static final int SWIPE_MIN_DISTANCE = 175;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private OnTouchActionListener mOnTouchActionListener;
    private GestureDetectorCompat mGestureDetector;

    public interface OnTouchActionListener {
        void onLeftSwipe(View view);

        void onRightSwipe(View view);

        void onClick(View view);

        void onScroll(View view);
    }

    public RecyclerViewTouchListener(Context context, final RecyclerView recyclerView,
                                     OnTouchActionListener onTouchActionListener) {

        mOnTouchActionListener = onTouchActionListener;
        mGestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {

                try {

                    // right to left swipe
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                        if (mOnTouchActionListener != null)
                            mOnTouchActionListener.onRightSwipe(recyclerView);

                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                        if (mOnTouchActionListener != null)
                            mOnTouchActionListener.onLeftSwipe(recyclerView);
                    }
                } catch (Exception e) {
                    // nothing
                }

                return false;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
