package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.APIUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>{
    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check for internet connection
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // TODO Set up empty view for the list view
//        this.mEmptyStateTextView = findViewById(R.id.empty_view);
//        movieListView.setEmptyView(this.mEmptyStateTextView);

        if (isConnected){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(100, null, this);
            Log.v(LOG_TAG, "Loader created!");
        } else {
            // TODO Hide loading indicator because the data has been loaded
            //View loadingIndicator = findViewById(R.id.progress_indicator);
            //loadingIndicator.setVisibility(View.GONE);

            // TODO Set empty state text to display "No internet connection."
            //mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        // TODO get the sorted preference from the bundle and use that one instead of always POPULAR

        return new MovieAsyncTaskLoader(this, APIUtils.POPULAR_ENDPOINT);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        TextView tv = findViewById(R.id.test);
        tv.setText(data.get(0).getOriginalTitle());

        ImageView iv = findViewById(R.id.poster);
        Picasso.get().load(APIUtils.buildImageUrl(data.get(0).getPosterPath()).toString()).into(iv);

        // TODO hide the loading indicator and the no internet text view; clear the adapter
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        // TODO clear the adapter
    }
}
