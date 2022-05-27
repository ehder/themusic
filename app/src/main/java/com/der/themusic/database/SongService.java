package com.der.themusic.database;

import com.der.themusic.models.Song;

import java.util.ArrayList;

public interface SongService {

    void insert(Song song);

    ArrayList<Song> getAll();

    void deleteByPath(String path);

}
