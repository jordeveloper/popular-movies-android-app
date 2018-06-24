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

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.TrailerViewHolder>{
    private int numTrailers;
    private List<MovieTrailer> trailers;

    public MovieTrailerAdapter() {
        this.trailers = null;
        this.numTrailers = 0;
    }

    void updateData(List<MovieTrailer> trailers) {
        if (trailers == null) {
            this.trailers = null;
            this.numTrailers = 0;
        } else {
            this.trailers = trailers;
            this.numTrailers = trailers.size();
        }
        notifyDataSetChanged();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder{
        public TextView trailerHolder;
        public TrailerViewHolder(View v) {
            super(v);
            trailerHolder = v.findViewById(R.id.trailer_name);
        }

        public void bind(List<MovieTrailer> trailers, int position) {
            if (trailers != null && position < trailers.size()){
                trailerHolder.setText(trailers.get(position).getName());
            }
        }
    }

    @NonNull
    @Override
    public MovieTrailerAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.movie_trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachImmediately = false;

        View view = inflater.inflate(layoutID, parent, attachImmediately);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerAdapter.TrailerViewHolder holder, int position) {
        holder.bind(this.trailers, position);
    }

    @Override
    public int getItemCount() {
        return this.numTrailers;
    }

}
