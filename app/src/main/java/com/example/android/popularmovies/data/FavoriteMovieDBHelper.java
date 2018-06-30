package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteMovieDBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = FavoriteMovieDBHelper.class.getSimpleName();

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movies.db";

    public FavoriteMovieDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE =  "CREATE TABLE " + MovieContract.FavoriteMovieEntry.TABLE_NAME + " ("
                + MovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.FavoriteMovieEntry.COLUMN_NAME_MOVIE_ID + " INTEGER NOT NULL, "
                + MovieContract.FavoriteMovieEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Don't need yet
    }
}
