package com.example.android.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.example.android.popularmovies.utilities.APIUtils;

import org.json.JSONArray;
import org.json.JSONException;

public class DetailsAsyncTaskLoader extends AsyncTaskLoader<MovieExtras> {
    private static final String LOG_TAG = DetailsAsyncTaskLoader.class.getName();
    private long movieID;

    public DetailsAsyncTaskLoader(Context context, long movieID) {
        super(context);
        this.movieID = movieID;
        Log.v(LOG_TAG, "Creating a MovieAsyncTaskLoader instance");
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG, "onStartLoading() happening now");
        forceLoad();
    }

    @Override
    public MovieExtras loadInBackground() {
        Log.v(LOG_TAG, "Running background task...");

        // Check for internet connection
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected){
            JSONArray movieReviews = APIUtils.getMovieReviews(this.movieID, getContext().getString(R.string.API_KEY));
            List<MovieReview> reviewList = new ArrayList<>();
            if (movieReviews != null) {
                for (int i = 0; i < movieReviews.length(); i++) {
                    try {
                        reviewList.add(new MovieReview(movieReviews.getJSONObject(i)));
                    } catch (JSONException e) {
                        Log.d(LOG_TAG, "Not a valid movie.");
                        e.printStackTrace();
                    }
                }
            } else {
                reviewList = null;
            }
            JSONArray movieTrailers = APIUtils.getMovieTrailers(this.movieID, getContext().getString(R.string.API_KEY));
            List<MovieTrailer> trailerList = new ArrayList<>();
            if (movieTrailers != null) {
                for (int i = 0; i < movieTrailers.length(); i++) {
                    try {
                        trailerList.add(new MovieTrailer(movieTrailers.getJSONObject(i)));
                    } catch (JSONException e) {
                        Log.d(LOG_TAG, "Not a valid movie.");
                        e.printStackTrace();
                    }
                }
            } else {
                trailerList = null;
            }
            return new MovieExtras(reviewList, trailerList);
        } else {
            return null;
        }


    }

}
