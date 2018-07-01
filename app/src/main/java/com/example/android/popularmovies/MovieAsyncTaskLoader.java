package com.example.android.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.example.android.popularmovies.data.FavoriteMovieProvider;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.utilities.APIUtils;

import org.json.JSONArray;
import org.json.JSONException;

public class MovieAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {
    private static final String LOG_TAG = MovieAsyncTaskLoader.class.getName();
    private String endpoint;

    public MovieAsyncTaskLoader(Context context, String endpoint) {
        super(context);
        this.endpoint = endpoint;
        Log.v(LOG_TAG, "Creating a MovieAsyncTaskLoader instance");
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG, "onStartLoading() happening now");
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        Log.v(LOG_TAG, "Running background task...");

        // Check for internet connection
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected){
            if (this.endpoint == null) {
                return null;
            }
            JSONArray movies;
            switch (this.endpoint) {
                case APIUtils.POPULAR_ENDPOINT:
                    movies = APIUtils.getPopularMovies(getContext().getString(R.string.API_KEY));
                    break;
                case APIUtils.TOP_RATED_ENDPOINT:
                    movies = APIUtils.getTopRatedMovies(getContext().getString(R.string.API_KEY));
                    break;
                case APIUtils.MOVIE_ENDPOINT:
                    // get all the favorite movies from the DB
                    Cursor favorites = getContext().getContentResolver()
                            .query(MovieContract.FavoriteMovieEntry.CONTENT_URI,
                                    null, null, null, null);

                    // then make requests for each of their details assembling the JSONArray
                    if (favorites != null && favorites.getCount() > 0) {
                        movies = new JSONArray();
                        int idCol = favorites.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_NAME_MOVIE_ID);
                        for (int i = 0; i < favorites.getCount(); i++) {
                            favorites.moveToPosition(i);
                            movies.put(APIUtils.getMovieDetails(
                                    favorites.getInt(idCol),
                                    getContext().getString(R.string.API_KEY)));
                        }
                    } else {
                        movies = null;
                    }
                    favorites.close();
                    break;
                default:
                    movies = null;
                    break;
            }
            List<Movie> movieList = new ArrayList<>();
            if (movies != null) {
                for (int i = 0; i < movies.length(); i++) {
                    try {
                        movieList.add(new Movie(movies.getJSONObject(i)));
                    } catch (JSONException e) {
                        Log.d(LOG_TAG, "Not a valid movie.");
                        e.printStackTrace();
                    }
                }
            } else {
                movieList = null;
            }
            return movieList;
        } else {
            return null;
        }


    }
}
