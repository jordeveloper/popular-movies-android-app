package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.APIUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>, MoviePosterAdapter.GridItemClickListener{
    private static final String LOG_TAG = "MainActivity";
    private final int LOADER_ID = 100;
    private RecyclerView mRecyclerView;
    private MoviePosterAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Movie> mMovies;
    public static final String MOVIE_TITLE_DATA = "title";
    public static final String MOVIE_RELEASE_DATA = "release date";
    public static final String MOVIE_POSTER_URL_DATA = "movie poster url";
    public static final String MOVIE_VOTE_AVERAGE_DATA = "vote average";
    public static final String MOVIE_SUMMARY_DATA = "plot synopsis";
    public static final String MOVIE_ID = "movie ID";
    public String mSortType;
    public static final String MOVIE_LIST_KEY = "movies";
    public static final String SORT_TYPE_KEY = "sort";
    private boolean isConnected;
    private static final String LAYOUT_POSITION = "layout_position_key";

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
        mAdapter = new MoviePosterAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        // set which type of data to pull
        if (((RadioButton) findViewById(R.id.radio_popular)).isChecked()) {
            this.mSortType = APIUtils.POPULAR_ENDPOINT;
        } else if (((RadioButton) findViewById(R.id.radio_top_rated)).isChecked()){
            this.mSortType = APIUtils.TOP_RATED_ENDPOINT;
        } else {
            this.mSortType = APIUtils.MOVIE_ENDPOINT;
        }

        // Check for internet connection
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        this.isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (this.isConnected){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
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
        return new MovieAsyncTaskLoader(this, this.mSortType);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        // DONE hide the loading indicator and the no internet text view
        View loadingIndicator = findViewById(R.id.progress_indicator);
        loadingIndicator.setVisibility(View.GONE);
        TextView noInternetTextView = findViewById(R.id.no_internet_notice);
        RecyclerView rv = findViewById(R.id.movie_poster_rv);
        TextView noFavoritesTextView = findViewById(R.id.no_favorites_notice);

        mMovies = data;
        mAdapter.updateData((ArrayList<Movie>) mMovies);

        // DONE fill in data into layout
        if (mMovies != null) {
            noInternetTextView.setVisibility(View.GONE);
            noFavoritesTextView.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        } else {
            if (this.isConnected && this.mSortType == APIUtils.MOVIE_ENDPOINT) {
                noFavoritesTextView.setVisibility(View.VISIBLE);
            } else {
                noInternetTextView.setVisibility(View.VISIBLE);
            }
            rv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        // DONE clear the adapter
        mAdapter.updateData(null);
    }

    @Override
    public void onGridItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, DetailActivity.class);
        Movie movie = mMovies.get(clickedItemIndex);
        intent.putExtra(this.MOVIE_TITLE_DATA, movie.getTitle());
        intent.putExtra(this.MOVIE_RELEASE_DATA, movie.getReleaseDate());
        intent.putExtra(this.MOVIE_POSTER_URL_DATA, movie.getPosterPath());
        intent.putExtra(this.MOVIE_VOTE_AVERAGE_DATA, movie.getVoteAverage());
        intent.putExtra(this.MOVIE_SUMMARY_DATA, movie.getOverview());
        intent.putExtra(this.MOVIE_ID, movie.getId());
        startActivity(intent);
    }

    // Buttons for which search type
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_popular:
                if (checked) {
                    // Get data for popular movies
                    this.mSortType = APIUtils.POPULAR_ENDPOINT;
                    getLoaderManager().restartLoader(LOADER_ID, null, this);
                    break;
                }
            case R.id.radio_top_rated:
                if (checked) {
                    // Get data for top rated movies
                    this.mSortType = APIUtils.TOP_RATED_ENDPOINT;
                    getLoaderManager().restartLoader(LOADER_ID, null, this);
                    break;
                }
            case R.id.radio_favorites:
                if (checked){
                    // Get data for the favorite movies
                    this.mSortType = APIUtils.MOVIE_ENDPOINT;
                    getLoaderManager().restartLoader(LOADER_ID, null, this);
                    break;
                }
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        this.mMovies = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
        this.mSortType = savedInstanceState.getString(SORT_TYPE_KEY);
        this.mAdapter.updateData((ArrayList<Movie>) this.mMovies);
        this.mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_POSITION));

        restoreRadioButtonState();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_LIST_KEY, (ArrayList<Movie>) this.mMovies);
        outState.putString(SORT_TYPE_KEY, this.mSortType);
        outState.putParcelable(LAYOUT_POSITION, this.mLayoutManager.onSaveInstanceState());

        super.onSaveInstanceState(outState);
    }

    private void restoreRadioButtonState(){
        switch (this.mSortType) {
            case APIUtils.POPULAR_ENDPOINT:
                ((RadioButton) findViewById(R.id.radio_popular)).setChecked(true);
                break;
            case APIUtils.TOP_RATED_ENDPOINT:
                ((RadioButton) findViewById(R.id.radio_top_rated)).setChecked(true);
                break;
            case APIUtils.MOVIE_ENDPOINT:
                ((RadioButton) findViewById(R.id.radio_favorites)).setChecked(true);
                break;
            default:
                break;
        }
    }
}
