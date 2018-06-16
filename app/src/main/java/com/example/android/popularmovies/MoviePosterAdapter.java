package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.utilities.APIUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MovieViewHolder>{
    private int numMovies;
    private ArrayList<Movie> movies;
    final private GridItemClickListener mOnClickListener;

    public MoviePosterAdapter(GridItemClickListener listener) {
        this.movies = null;
        this.numMovies = 0;
        this.mOnClickListener = listener;
    }

    void updateData(ArrayList<Movie> movieList) {
        if (movieList == null) {
            this.movies = null;
            this.numMovies = 0;
        } else {
            this.movies = movieList;
            this.numMovies = movieList.size();
        }
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView movieHolder;
        public MovieViewHolder(View v) {
            super(v);
            movieHolder = v.findViewById(R.id.movie_list_item);
            v.setOnClickListener(this);
        }

        public void bind(ArrayList<Movie> movies, int position) {
            if (movies != null && position < movies.size()){
                Picasso.get().load(APIUtils.buildImageUrl(movies.get(position).getPosterPath()).toString()).into(movieHolder);
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onGridItemClick(clickedPosition);
        }
    }

    @NonNull
    @Override
    public MoviePosterAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachImmediately = false;

        View view = inflater.inflate(layoutID, parent, attachImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MoviePosterAdapter.MovieViewHolder holder, int position) {
        holder.bind(this.movies, position);
    }

    @Override
    public int getItemCount() {
        return this.numMovies;
    }

    public interface GridItemClickListener {
        void onGridItemClick(int clickedItemIndex);
    }
}
