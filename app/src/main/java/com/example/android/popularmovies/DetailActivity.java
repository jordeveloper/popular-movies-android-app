package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.utilities.APIUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieExtras>, MovieTrailerAdapter.ItemClickListener{
    private final int LOADER_ID = 101;
    private final String LOG_NAME = "DetailActivity";
    private TextView mMovieTitleTextView;
    private TextView mMovieReleaseDateTextView;
    private TextView mMovieSummaryTextView;
    private TextView mMovieVoteAverageTextView;
    private ImageView mMoviePosterImageView;
    private TextView mNoReviewsTextView;
    private TextView mNoTrailersTextView;
    private RecyclerView mReviewsRecyclerView;
    private RecyclerView mTrailersRecyclerView;
    private MovieReviewAdapter mReviewAdapter;
    private MovieTrailerAdapter mTrailerAdapter;
    private LinearLayoutManager mReviewLayoutManager;
    private LinearLayoutManager mTrailerLayoutManager;
    private MovieExtras mMovieExtras;
    private long mMovieID;
    private String mMovieTitle;
    private boolean mIsFavorite;
    private ToggleButton mToggleButton;
    private static final String MOVIE_TRAILERS_KEY = "trailers_key";
    private static final String MOVIE_REVIEWS_KEY = "reviews_key";
    private static final String FAVORITES_TOGGLE_KEY = "favorite_toggle";
    private static final String SCROLL_COORDINATES_KEY = "scroll_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mToggleButton = findViewById(R.id.favorite_button);
        mReviewsRecyclerView = findViewById(R.id.movie_review_rv);
        mTrailersRecyclerView = findViewById(R.id.movie_trailer_rv);
        mReviewsRecyclerView.setHasFixedSize(true);
        mTrailersRecyclerView.setHasFixedSize(true);

        mReviewLayoutManager = new LinearLayoutManager(this);
        mReviewsRecyclerView.setLayoutManager(mReviewLayoutManager);
        mTrailerLayoutManager = new LinearLayoutManager(this);
        mTrailersRecyclerView.setLayoutManager(mTrailerLayoutManager);

        // set up adapter and fill later with the data
        mReviewAdapter = new MovieReviewAdapter();
        mTrailerAdapter = new MovieTrailerAdapter(this);
        mReviewsRecyclerView.setAdapter(mReviewAdapter);
        mTrailersRecyclerView.setAdapter(mTrailerAdapter);

        mMoviePosterImageView = findViewById(R.id.movie_poster);
        mMovieTitleTextView = findViewById(R.id.movie_title);
        mMovieReleaseDateTextView = findViewById(R.id.movie_release_date);
        mMovieSummaryTextView = findViewById(R.id.movie_summary);
        mMovieVoteAverageTextView = findViewById(R.id.movie_vote_average);
        mNoReviewsTextView = findViewById(R.id.reviews_unavailable);
        mNoTrailersTextView = findViewById(R.id.trailers_unavailable);

        Intent intent = getIntent();
        Picasso.get().load(APIUtils.buildImageUrl(intent.getStringExtra(MainActivity.MOVIE_POSTER_URL_DATA))
                .toString()).into(mMoviePosterImageView);
        this.mMovieTitle = intent.getStringExtra(MainActivity.MOVIE_TITLE_DATA);
        mMovieTitleTextView.setText(this.mMovieTitle);
        mMovieReleaseDateTextView.setText(intent.getStringExtra(MainActivity.MOVIE_RELEASE_DATA));
        mMovieSummaryTextView.setText(intent.getStringExtra(MainActivity.MOVIE_SUMMARY_DATA));
        mMovieVoteAverageTextView.setText(String.valueOf(intent.getDoubleExtra(MainActivity.MOVIE_VOTE_AVERAGE_DATA, 0.0)));

        // Check for internet connection
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected){
            this.mMovieID = intent.getLongExtra(MainActivity.MOVIE_ID, 0);
            LoaderManager loaderManager = getLoaderManager();
            Bundle args = new Bundle();
            args.putLong(MainActivity.MOVIE_ID, this.mMovieID);
            loaderManager.initLoader(LOADER_ID, args, this);
        } else {
            mNoReviewsTextView.setVisibility(View.VISIBLE);
            mNoTrailersTextView.setVisibility(View.VISIBLE);
            mReviewsRecyclerView.setVisibility(View.GONE);
        }

        // determine if the movie is in the favorites
        Uri favoriteMovieCheck = MovieContract.FavoriteMovieEntry.CONTENT_URI
                .buildUpon().appendPath(String.valueOf(this.mMovieID)).build();
        Cursor favoriteMoviesCursor = this.getContentResolver().query(favoriteMovieCheck,
                null, null, null, null);
        this.mIsFavorite = favoriteMoviesCursor.getCount() != 0;
        this.mToggleButton.setChecked(this.mIsFavorite);
        favoriteMoviesCursor.close();
    }

    @Override
    public Loader<MovieExtras> onCreateLoader(int id, Bundle args) {
        return new DetailsAsyncTaskLoader(this, args.getLong(MainActivity.MOVIE_ID));
    }

    @Override
    public void onLoadFinished(Loader<MovieExtras> loader, MovieExtras data) {
        mMovieExtras = data;
        if (data != null) {
            View[] views = {mNoReviewsTextView, mNoTrailersTextView, mReviewsRecyclerView};
            hideViews(views);
            List<MovieReview> reviews = data.getReviews();
            List<MovieTrailer> trailers = data.getTrailers();
            mReviewAdapter.updateData(reviews);
            mTrailerAdapter.updateData(trailers);
            if (reviews == null || reviews.size() == 0) {
                mReviewsRecyclerView.setVisibility(View.GONE);
                mNoReviewsTextView.setVisibility(View.VISIBLE);
            } else{
                mReviewsRecyclerView.setVisibility(View.VISIBLE);
            }
            if (trailers == null || trailers.size() == 0) {
                mTrailersRecyclerView.setVisibility(View.GONE);
                mNoTrailersTextView.setVisibility(View.VISIBLE);
            } else {
                mTrailersRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            View[] viewsToHide = {mReviewsRecyclerView, mTrailersRecyclerView};
            TextView[] viewsToShow = {mNoReviewsTextView, mNoTrailersTextView};
            hideViews(viewsToHide);
            showViews(viewsToShow);
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieExtras> loader) {
        mReviewAdapter.updateData(null);
        mTrailerAdapter.updateData(null);
    }

    private void hideViews(View[] views){
        for(View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    private void showViews(View[] views){
        for(View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(int clickedItemIndex) {
        String trailerKey = mMovieExtras.getTrailers().get(clickedItemIndex).getKey();
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerKey));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailerKey));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        ArrayList<MovieReview> reviews = savedInstanceState.getParcelableArrayList(MOVIE_REVIEWS_KEY);
        ArrayList<MovieTrailer> trailers = savedInstanceState.getParcelableArrayList(MOVIE_TRAILERS_KEY);
        this.mMovieExtras = new MovieExtras(reviews, trailers);
        this.mReviewAdapter.updateData(reviews);
        this.mTrailerAdapter.updateData(trailers);
        this.mIsFavorite = savedInstanceState.getBoolean(FAVORITES_TOGGLE_KEY);
        this.mToggleButton.setChecked(this.mIsFavorite);

        final ScrollView sv = findViewById(R.id.details_scroll_view);
        final int[] scrollCoordinates = savedInstanceState.getIntArray(SCROLL_COORDINATES_KEY);
        if(scrollCoordinates != null) {
            sv.post(new Runnable() {
                public void run() {
                    sv.scrollTo(scrollCoordinates[0], scrollCoordinates[1]);
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_TRAILERS_KEY, (ArrayList<MovieTrailer>) this.mMovieExtras.getTrailers());
        outState.putParcelableArrayList(MOVIE_REVIEWS_KEY, (ArrayList<MovieReview>) this.mMovieExtras.getReviews());
        outState.putBoolean(FAVORITES_TOGGLE_KEY, this.mIsFavorite);

        ScrollView sv = findViewById(R.id.details_scroll_view);
        outState.putIntArray(SCROLL_COORDINATES_KEY, new int[]{ sv.getScrollX(), sv.getScrollY()});

        super.onSaveInstanceState(outState);
    }

    public void onFavoriteToggled(View view) {
        this.mIsFavorite = ((ToggleButton) view).isChecked();
        if(this.mIsFavorite) {
            // add the movie to the favorites table
            ContentValues cv = new ContentValues();
            cv.put(MovieContract.FavoriteMovieEntry.COLUMN_NAME_MOVIE_ID, this.mMovieID);
            cv.put(MovieContract.FavoriteMovieEntry.COLUMN_NAME_TITLE, this.mMovieTitle);
            this.getContentResolver().insert(MovieContract.FavoriteMovieEntry.CONTENT_URI, cv);
        } else {
            // remove the movie from the favorites DB
            Uri deleteMovieURI = MovieContract.FavoriteMovieEntry.CONTENT_URI
                    .buildUpon().appendPath(String.valueOf(this.mMovieID)).build();
            boolean successfullyDeleted = this.getContentResolver().delete(deleteMovieURI,null, null) == 1;
            if (successfullyDeleted) {
                Log.v(LOG_NAME, "Successfully deleted favorite movie!");
            } else {
                Log.v(LOG_NAME, "Unable to remove movie from favorites.");
            }
        }
    }
}
