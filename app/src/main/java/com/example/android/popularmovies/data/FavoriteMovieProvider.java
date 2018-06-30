package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class FavoriteMovieProvider extends ContentProvider {
    public static final String LOG_TAG = FavoriteMovieProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the favorite movies table */
    private static final int MOVIES = 100;

    /** URI matcher code for the content URI for a single movie in the favorite movies table */
    private static final int MOVIE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVORITE_MOVIES, MOVIES);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVORITE_MOVIES + "/#", MOVIE_ID);
    }

    private FavoriteMovieDBHelper mDBHelper;

    @Override
    public boolean onCreate() {
        mDBHelper = new FavoriteMovieDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDBHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                cursor = database.query(MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case MOVIE_ID:
                selection = MovieContract.FavoriteMovieEntry.COLUMN_NAME_MOVIE_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return insertFavoriteMovie(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertFavoriteMovie(Uri uri, ContentValues values) {
        // Check that the name is not null
        String title = values.getAsString(MovieContract.FavoriteMovieEntry.COLUMN_NAME_TITLE);
        if (title == null) {
            throw new IllegalArgumentException("Movie must have a title!");
        }

        Integer movieID = values.getAsInteger(MovieContract.FavoriteMovieEntry.COLUMN_NAME_MOVIE_ID);
        if (movieID == null) {
            throw new IllegalArgumentException("Movie must have an ID!");
        }

        SQLiteDatabase database = mDBHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(MovieContract.FavoriteMovieEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the favorite movies content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return updateFavoriteMovies(uri, contentValues, selection, selectionArgs);
            case MOVIE_ID:
                selection = MovieContract.FavoriteMovieEntry.COLUMN_NAME_MOVIE_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateFavoriteMovies(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateFavoriteMovies(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(MovieContract.FavoriteMovieEntry.COLUMN_NAME_TITLE)) {
            String title = values.getAsString(MovieContract.FavoriteMovieEntry.COLUMN_NAME_TITLE);
            if (title == null) {
                throw new IllegalArgumentException("Movie must have a title!");
            }
        }

        if (values.containsKey(MovieContract.FavoriteMovieEntry.COLUMN_NAME_MOVIE_ID)) {
            Integer movieID = values.getAsInteger(MovieContract.FavoriteMovieEntry.COLUMN_NAME_MOVIE_ID);
            if (movieID == null) {
                throw new IllegalArgumentException("Movie must have an ID!");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDBHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(MovieContract.FavoriteMovieEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDBHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(MovieContract.FavoriteMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_ID:
                // Delete a single row given by the ID in the URI
                selection = MovieContract.FavoriteMovieEntry.COLUMN_NAME_MOVIE_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(MovieContract.FavoriteMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return MovieContract.FavoriteMovieEntry.CONTENT_LIST_TYPE;
            case MOVIE_ID:
                return MovieContract.FavoriteMovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
