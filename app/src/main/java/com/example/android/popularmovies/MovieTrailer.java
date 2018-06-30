package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieTrailer implements Parcelable{
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

    public MovieTrailer(Parcel parcel){
        this.key = parcel.readString();
        this.name = parcel.readString();
        this.site = parcel.readString();
        this.type = parcel.readString();
    }

    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[0];
        }

    };

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

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(type);
    }
}
