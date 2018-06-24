package com.example.android.popularmovies;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieReview {
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
}
