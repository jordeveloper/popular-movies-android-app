package com.example.android.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Movie {
    private long voteCount;
    private long id;
    private boolean isVideo;
    private double voteAverage;
    private String title;
    private double popularity;
    private String posterPath;
    private String originalLanguage;
    private String originalTitle;
    private int[] genreIDs;
    private String backdropPath;
    private boolean isAdult;
    private String overview;
    private String releaseDate;

    public Movie (JSONObject json){
        /*
        {
            "vote_count": 1702,
            "id": 383498,
            "video": false,
            "vote_average": 7.9,
            "title": "Deadpool 2",
            "popularity": 311.839988,
            "poster_path": "/to0spRl1CMDvyUbOnbb4fTk3VAd.jpg",
            "original_language": "en",
            "original_title": "Deadpool 2",
            "genre_ids": [
            28,
            35,
            878
            ],
            "backdrop_path": "/j0BtDE8M4Q2sJANrQjCosU8N7ji.jpg",
            "adult": false,
            "overview": "Wisecracking mercenary Deadpool battles the evil and powerful Cable and other bad guys to save a boy's life.",
            "release_date": "2018-05-15"
          }
         */
        try {
            this.voteCount = json.getLong("vote_count");
            this.id = json.getLong("id");
            this.isVideo = json.getBoolean("video");
            this.voteAverage = json.getDouble("vote_average");
            this.title = json.getString("title");
            this.popularity = json.getDouble("popularity");
            this.posterPath = json.getString("poster_path");
            this.originalLanguage = json.getString("original_language");
            this.originalTitle = json.getString("original_title");
            JSONArray genres = json.getJSONArray("genre_ids");
            this.genreIDs = new int[genres.length()];
            for (int i = 0; i < genres.length(); i ++) {
                this.genreIDs[i] = genres.getInt(i);
            }
            this.backdropPath = json.getString("backdrop_path");
            this.isAdult = json.getBoolean("adult");
            this.overview = json.getString("overview");
            this.releaseDate = json.getString("release_date");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public long getVoteCount() {
        return voteCount;
    }

    public long getId() {
        return id;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public int[] getGenreIDs() {
        return genreIDs;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
