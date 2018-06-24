package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.APIUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieExtras>, MovieTrailerAdapter.ItemClickListener{
    private final int LOADER_ID = 101;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
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
        mMovieTitleTextView.setText(intent.getStringExtra(MainActivity.MOVIE_TITLE_DATA));
        mMovieReleaseDateTextView.setText(intent.getStringExtra(MainActivity.MOVIE_RELEASE_DATA));
        mMovieSummaryTextView.setText(intent.getStringExtra(MainActivity.MOVIE_SUMMARY_DATA));
        mMovieVoteAverageTextView.setText(String.valueOf(intent.getDoubleExtra(MainActivity.MOVIE_VOTE_AVERAGE_DATA, 0.0)));

        // Check for internet connection
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected){
            LoaderManager loaderManager = getLoaderManager();
            Bundle args = new Bundle();
            args.putLong(MainActivity.MOVIE_ID, intent.getLongExtra(MainActivity.MOVIE_ID, 0));
            loaderManager.initLoader(LOADER_ID, args, this);
        } else {
            mNoReviewsTextView.setVisibility(View.VISIBLE);
            mNoTrailersTextView.setVisibility(View.VISIBLE);
            mReviewsRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public Loader<MovieExtras> onCreateLoader(int id, Bundle args) {
        // TODO get the sorted preference from the bundle and use that one instead of always POPULAR
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
            mReviewsRecyclerView.setVisibility(View.VISIBLE);
            mTrailersRecyclerView.setVisibility(View.VISIBLE);
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
}
