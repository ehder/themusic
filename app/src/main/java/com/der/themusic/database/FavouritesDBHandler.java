package com.der.themusic.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FavouritesDBHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public static final String FAVOURITE_SONG_TABLE         = "favorites";
    public static final String COLUMN_TITLE                 = "title";
    public static final String COLUMN_ARTIST_NAME              = "artistName";
    public static final String COLUMN_PATH                  = "songPath";
    public static final String COLUMN_FAV                  = "favSong";
    public static final String COLUMN_ALBUM_NAME                  = "albumName";
    public static final String COLUMN_DURATION                  = "duration";
    public static final String COLUMN_ARTIST_ID                  = "artistId";
    public static final String COLUMN_DATE_ADD                  = "dateAdd";

    private static final String CREATE_TABLE ="CREATE TABLE " + FAVOURITE_SONG_TABLE + " ("
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_ARTIST_NAME + " TEXT, "
            + COLUMN_FAV + " INTEGER, "
            + COLUMN_ALBUM_NAME + " TEXT, "
            + COLUMN_DURATION + " TEXT, "
            + COLUMN_ARTIST_ID + " TEXT, "
            + COLUMN_DATE_ADD + " TEXT, "
            + COLUMN_PATH + " TEXT PRIMARY KEY " + ")";

    public FavouritesDBHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sql) {
        sql.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sql, int i, int i1) {
        sql.execSQL("DROP TABLE IF EXISTS " + FAVOURITE_SONG_TABLE);
        sql.execSQL(CREATE_TABLE);
    }
}
