package com.der.themusic.models;

public class Song {

    private String title;
    private String path;
    private String artistName;
    private int artistID;
    private boolean favSong;
    private String albumName;
    private int duration;
    private int dateAdd;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isFavSong() {
        return favSong;
    }

    public void setFavSong(boolean favSong) {
        this.favSong = favSong;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getArtistID() {
        return artistID;
    }

    public void setArtistID(int artistID) {
        this.artistID = artistID;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(int dateAdd) {
        this.dateAdd = dateAdd;
    }
}
