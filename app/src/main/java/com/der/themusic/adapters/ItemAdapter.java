package com.der.themusic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.der.themusic.R;
import com.der.themusic.models.Song;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Song> implements Filterable {

    private List<Song> songList;
    private Context context;

    public ItemAdapter(@NonNull Context context, List<Song> songList) {
        super(context, 0, songList);
        this.songList = songList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        TextView title, artisName, albumName;

        if (convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.items, parent, false);
        }

        title = view.findViewById(R.id.tv_music_name);
        artisName = view.findViewById(R.id.tv_music_subtitle);
        albumName = view.findViewById(R.id.tv_music_album_name);


        Song song = songList.get(position);

        title.setText(song.getTitle());
        albumName.setText(song.getAlbumName());
        artisName.setText(song.getArtistName());

        return view;
    }
}
