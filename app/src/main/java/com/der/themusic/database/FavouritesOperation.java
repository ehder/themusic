package com.der.themusic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.der.themusic.models.Song;

import java.util.ArrayList;

public class FavouritesOperation implements SongService{

    SQLiteOpenHelper dbHandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            FavouritesDBHandler.COLUMN_TITLE,
            FavouritesDBHandler.COLUMN_ARTIST_NAME,
            FavouritesDBHandler.COLUMN_PATH,
            FavouritesDBHandler.COLUMN_FAV,
            FavouritesDBHandler.COLUMN_ALBUM_NAME,
            FavouritesDBHandler.COLUMN_DURATION,
            FavouritesDBHandler.COLUMN_DATE_ADD,
            FavouritesDBHandler.COLUMN_ARTIST_ID
    };

    public FavouritesOperation(Context context) {
        dbHandler = new FavouritesDBHandler(context);
    }

    @Override
    public void insert(Song song) {
        open();

        ContentValues values =new ContentValues();
        values.put(FavouritesDBHandler.COLUMN_TITLE, song.getTitle());
        values.put(FavouritesDBHandler.COLUMN_ALBUM_NAME, song.getAlbumName());
        values.put(FavouritesDBHandler.COLUMN_PATH, song.getPath());
        values.put(FavouritesDBHandler.COLUMN_ARTIST_NAME, song.getArtistName());
        values.put(FavouritesDBHandler.COLUMN_ARTIST_ID, song.getArtistID());
        values.put(FavouritesDBHandler.COLUMN_DURATION, song.getDuration());
        values.put(FavouritesDBHandler.COLUMN_DATE_ADD, song.getDateAdd());
        values.put(FavouritesDBHandler.COLUMN_FAV, song.isFavSong());

        database.insertWithOnConflict(FavouritesDBHandler.FAVOURITE_SONG_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        //close();
    }

    @Override
    public ArrayList<Song> getAll() {
        open();

        Cursor cursor = database.query(FavouritesDBHandler.FAVOURITE_SONG_TABLE, allColumns, null, null, null, null, null);
        ArrayList<Song> favSongs = new ArrayList<>();

        int title = cursor.getColumnIndex(FavouritesDBHandler.COLUMN_TITLE);
        int artistName = cursor.getColumnIndex(FavouritesDBHandler.COLUMN_ARTIST_NAME);
        int path = cursor.getColumnIndex(FavouritesDBHandler.COLUMN_PATH);
        int fav = cursor.getColumnIndex(FavouritesDBHandler.COLUMN_FAV);
        int albumName = cursor.getColumnIndex(FavouritesDBHandler.COLUMN_ALBUM_NAME);
        int duration = cursor.getColumnIndex(FavouritesDBHandler.COLUMN_DURATION);
        int dateAdd = cursor.getColumnIndex(FavouritesDBHandler.COLUMN_DATE_ADD);
        int artistID = cursor.getColumnIndex(FavouritesDBHandler.COLUMN_ARTIST_ID);

        while (cursor.moveToNext()){
            Song song = new Song();
            song.setTitle(cursor.getString(title));
            song.setPath(cursor.getString(path));
            song.setArtistName(cursor.getString(artistName));
            song.setArtistID(cursor.getInt(artistID));
            song.setAlbumName(cursor.getString(albumName));
            song.setDuration(cursor.getInt(duration));
            song.setDateAdd(cursor.getInt(dateAdd));
            song.setFavSong(cursor.getInt(fav) > 0);
            favSongs.add(song);

            System.out.println(song.isFavSong() + " ********************(*(Y*(&YHIJOKP<>");
        }

        //close();
        return favSongs;
    }

    @Override
    public void deleteByPath(String path) {
            open();
            String whereClause = FavouritesDBHandler.COLUMN_PATH + "=?";
            String[] whereArgs = new String[]{path};
            database.delete(FavouritesDBHandler.FAVOURITE_SONG_TABLE, whereClause, whereArgs);
            close();
    }

    public void open() {
        database = dbHandler.getWritableDatabase();
    }

    public void close() {
        dbHandler.close();
    }
}
