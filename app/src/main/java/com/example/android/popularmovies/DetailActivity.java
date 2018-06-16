package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.APIUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private TextView mMovieTitleTextView;
    private TextView mMovieReleaseDateTextView;
    private TextView mMovieSummaryTextView;
    private TextView mMovieVoteAverageTextView;
    private ImageView mMoviePosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mMoviePosterImageView = findViewById(R.id.movie_poster);
        mMovieTitleTextView = findViewById(R.id.movie_title);
        mMovieReleaseDateTextView = findViewById(R.id.movie_release_date);
        mMovieSummaryTextView = findViewById(R.id.movie_summary);
        mMovieVoteAverageTextView = findViewById(R.id.movie_vote_average);

        Intent intent = getIntent();
        Picasso.get().load(APIUtils.buildImageUrl(intent.getStringExtra(MainActivity.MOVIE_POSTER_URL_DATA))
                .toString()).into(mMoviePosterImageView);
        mMovieTitleTextView.setText(intent.getStringExtra(MainActivity.MOVIE_TITLE_DATA));
        mMovieReleaseDateTextView.setText(intent.getStringExtra(MainActivity.MOVIE_RELEASE_DATA));
        mMovieSummaryTextView.setText(intent.getStringExtra(MainActivity.MOVIE_SUMMARY_DATA));
        mMovieVoteAverageTextView.setText(String.valueOf(intent.getDoubleExtra(MainActivity.MOVIE_VOTE_AVERAGE_DATA, 0.0)));
    }

}
