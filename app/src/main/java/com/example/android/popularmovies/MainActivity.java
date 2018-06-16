package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.APIUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>{
    private static final String LOG_TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private MoviePosterAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.movie_poster_rv);
        mRecyclerView.setHasFixedSize(true);

        // use a grid layout manager with 2 columns
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // set up adapter and fill later with the data
        mAdapter = new MoviePosterAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // Check for internet connection
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(100, null, this);
            Log.v(LOG_TAG, "Loader created!");
        } else {
            // DONE Hide loading indicator because the data has been loaded
            View loadingIndicator = findViewById(R.id.progress_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // DONE Display "No internet connection."
            TextView noInternetTextView = findViewById(R.id.no_internet_notice);
            noInternetTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        // TODO get the sorted preference from the bundle and use that one instead of always POPULAR

        return new MovieAsyncTaskLoader(this, APIUtils.POPULAR_ENDPOINT);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        // DONE hide the loading indicator and the no internet text view
        View loadingIndicator = findViewById(R.id.progress_indicator);
        loadingIndicator.setVisibility(View.GONE);
        TextView noInternetTextView = findViewById(R.id.no_internet_notice);
        noInternetTextView.setVisibility(View.GONE);

        // DONE fill in data into layout
        if (data != null) {
            mAdapter.updateData((ArrayList<Movie>) data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        // DONE clear the adapter
        mAdapter.updateData(null);
    }
}
