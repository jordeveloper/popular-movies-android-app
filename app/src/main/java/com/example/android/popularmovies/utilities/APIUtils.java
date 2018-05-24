package com.example.android.popularmovies.utilities;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class APIUtils {

    // base URLs
    public final static String API_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";

    // endpoints for sorted data
    public final static String POPULAR_ENDPOINT = "popular";
    public final static String TOP_RATED_ENDPOINT = "top_rated";

    // query string
    public final static String QUERY_PARAM_API_KEY = "api_key";

    /**
     * Builds the URL used to query the API.
     *
     * @param endpoint String for the endpoint being used
     * @param apiKey String for the API key to request the data.
     * @return The URL to request the movie data.
     */
    public static URL buildQueryUrl(String endpoint, String apiKey) {
        Uri queryUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath(endpoint)
                .appendQueryParameter(QUERY_PARAM_API_KEY, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(queryUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds the URL used to get the movie posters.
     *
     * @param imagePath String path to get the movie poster
     * @return The URL for the movie poster.
     */
    public static URL buildImageUrl(String imagePath) {
        Uri queryUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(imagePath)
                .build();

        URL url = null;
        try {
            url = new URL(queryUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /*
    TODO Method to make a request to the popular movies endpoint and return a JSON
     */

    /*
    TODO Method to make a request to the top rated movies endpoint and return a JSON
     */

    /*
    TODO Method to make a request to the movie endpoint with a specific movie ID and return a JSON
     */
}
