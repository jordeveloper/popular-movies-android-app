package com.example.android.popularmovies;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieTrailer {
    private String key;
    private String name;
    private String site;
    private String type;

    public MovieTrailer(JSONObject json){
        try {
            this.key = json.getString("key");
            this.name = json.getString("name");
            this.site = json.getString("site");
            this.type = json.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getKey(){
        return key;
    }

    public String getName(){
        return name;
    }

    public String getSite(){
        return site;
    }

    public String getType(){
        return type;
    }
}
