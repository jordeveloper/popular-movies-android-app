package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class APIUtils {

    // base URLs
    public final static String API_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";

    // endpoints for sorted data
    public final static String POPULAR_ENDPOINT = "popular";
    public final static String TOP_RATED_ENDPOINT = "top_rated";
    public final static String MOVIE_DETAILS_ENDPOINT = "";

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
                .appendPath(imagePath.substring(1)) // remove leading slash
                .build();

        URL url = null;
        try {
            url = new URL(queryUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v("Image URL created", url.toString());
        return url;
    }

    /**
     * From Udacity Sunshine Project Solution
     *
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /*
    Method to make a request to the popular movies endpoint and return a JSON
     */
    public static JSONArray getPopularMovies(String apiKey) {
        URL url = APIUtils.buildQueryUrl(APIUtils.POPULAR_ENDPOINT, apiKey);
        try {
            String results = APIUtils.getResponseFromHttpUrl(url);
            JSONObject json = new JSONObject(results);
            return json.getJSONArray("results");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    Method to make a request to the top rated movies endpoint and return a JSON
     */
    public static JSONArray getTopRatedMovies(String apiKey) {
        URL url = APIUtils.buildQueryUrl(APIUtils.TOP_RATED_ENDPOINT, apiKey);
        try {
            String results = APIUtils.getResponseFromHttpUrl(url);
            JSONObject json = new JSONObject(results);
            return json.getJSONArray("results");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    Method to make a request to the movie endpoint with a specific movie ID and return a JSON
     */
    public static JSONObject getMovieDetails(int movie_id, String apiKey) {
        URL url = APIUtils.buildQueryUrl(APIUtils.MOVIE_DETAILS_ENDPOINT + movie_id, apiKey);
        try {
            String results = APIUtils.getResponseFromHttpUrl(url);
            JSONObject json = new JSONObject(results);
            return json;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
