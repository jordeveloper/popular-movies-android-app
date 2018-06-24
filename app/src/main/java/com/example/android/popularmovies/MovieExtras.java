package com.example.android.popularmovies;

import java.util.List;

public class MovieExtras {
    private List<MovieReview> reviews;
    private List<MovieTrailer> trailers;

    public MovieExtras(List<MovieReview> reviews, List<MovieTrailer> trailers) {
        this.reviews = reviews;
        this.trailers = trailers;
    }

    public List<MovieReview> getReviews(){
        return this.reviews;
    }

    public List<MovieTrailer> getTrailers(){
        return this.trailers;
    }
}
