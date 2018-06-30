package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieReview implements Parcelable {
    private String author;
    private String content;
    private String id;
    private String url;

    public MovieReview(JSONObject json) {
        try {
            this.author = json.getString("author");
            this.content = json.getString("content");
            this.id = json.getString("id");
            this.url = json.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public MovieReview(Parcel parcel){
        this.author = parcel.readString();
        this.content = parcel.readString();
        this.id = parcel.readString();
        this.url = parcel.readString();
    }

    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[0];
        }

    };

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getID() {
        return id;
    }

    public String getURL() {
        return url;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(id);
        dest.writeString(url);
    }
}
