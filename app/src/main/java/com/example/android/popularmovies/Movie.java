package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Property;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Movie implements Parcelable {
    private long voteCount;
    private long id;
    private double voteAverage;
    private String title;
    private double popularity;
    private String posterPath;
    private String originalLanguage;
    private String originalTitle;
    private String backdropPath;
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
            this.voteAverage = json.getDouble("vote_average");
            this.title = json.getString("title");
            this.popularity = json.getDouble("popularity");
            this.posterPath = json.getString("poster_path");
            this.originalLanguage = json.getString("original_language");
            this.originalTitle = json.getString("original_title");
            this.backdropPath = json.getString("backdrop_path");
            this.overview = json.getString("overview");
            this.releaseDate = json.getString("release_date");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Movie(Parcel parcel){
        voteCount = parcel.readLong();
        id = parcel.readLong();
        voteAverage = parcel.readDouble();
        title = parcel.readString();
        popularity = parcel.readDouble();
        posterPath = parcel.readString();
        originalLanguage = parcel.readString();
        originalTitle = parcel.readString();
        backdropPath = parcel.readString();
        overview = parcel.readString();
        releaseDate = parcel.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }

    };

    public long getVoteCount() {
        return voteCount;
    }

    public long getId() {
        return id;
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

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(voteCount);
        dest.writeLong(id);
        dest.writeDouble(voteAverage);
        dest.writeString(title);
        dest.writeDouble(popularity);
        dest.writeString(posterPath);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(backdropPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
    }


}
