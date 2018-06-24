package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.APIUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ReviewViewHolder>{
    private int numReviews;
    private List<MovieReview> reviews;

    public MovieReviewAdapter() {
        this.reviews = null;
        this.numReviews = 0;
    }

    void updateData(List<MovieReview> reviews) {
        if (reviews == null) {
            this.reviews = null;
            this.numReviews = 0;
        } else {
            this.reviews = reviews;
            this.numReviews = reviews.size();
        }
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        public TextView reviewContentHolder;
        public TextView reviewAuthorHolder;
        public ReviewViewHolder(View v) {
            super(v);
            reviewContentHolder = v.findViewById(R.id.review_content);
            reviewAuthorHolder = v.findViewById(R.id.review_author);
        }

        public void bind(List<MovieReview> reviews, int position) {
            if (reviews != null && position < reviews.size()){
                reviewContentHolder.setText(reviews.get(position).getContent());
                reviewAuthorHolder.setText(reviews.get(position).getAuthor());
            }
        }
    }

    @NonNull
    @Override
    public MovieReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.movie_review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachImmediately = false;

        View view = inflater.inflate(layoutID, parent, attachImmediately);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewAdapter.ReviewViewHolder holder, int position) {
        holder.bind(this.reviews, position);
    }

    @Override
    public int getItemCount() {
        return this.numReviews;
    }

}
