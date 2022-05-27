package com.der.themusic.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.der.themusic.R;
import com.der.themusic.adapters.ItemAdapter;
import com.der.themusic.models.Song;

import java.util.ArrayList;

public class AllSongFragment extends ListFragment {


    private ArrayList<Song> songList;
    private ArrayList<Song> newList;
    private CreateDataParse createDataParse;
    private ListView listView;

    public static Fragment getInstance(int position){
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        AllSongFragment allSongFragment = new AllSongFragment();
        allSongFragment.setArguments(bundle);
        return allSongFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        createDataParse = (CreateDataParse) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView = view.findViewById(R.id.play_list);
        setContent();
    }

    private void setContent(){
        boolean searchedList = false;
        songList = new ArrayList<>();
        newList = new ArrayList<>();
        getSongs();
        ItemAdapter adapter = new ItemAdapter(getContext(), songList);
        if (!createDataParse.queryText().equals("")){
            adapter = onQueryTextChange();
            adapter.notifyDataSetChanged();
            searchedList = true;
        }else {
            searchedList = false;
        }

        createDataParse.getLength(songList.size());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getContext(), "is favourites? "+songList.get(position).isFavSong(), Toast.LENGTH_SHORT).show();
                createDataParse.onDataPass(songList.get(position).getTitle(), songList.get(position).getPath());
                createDataParse.fullSongList(songList, position);
            }
        });

    }

    public ItemAdapter onQueryTextChange() {

        String text = createDataParse.queryText();
        for (Song songs : songList) {
            String title = songs.getTitle().toLowerCase();
            if (title.contains(text)) {
                newList.add(songs);
            }
        }
        return new ItemAdapter(getContext(), newList);

    }

    private void getSongs(){

        songList = new ArrayList<>();
        int pos = 1;
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, MediaStore.Audio.Media.DATE_ADDED+" DESC");

        while (cursor.moveToNext()){
            int title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int path = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int albumName = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int duration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int artistID = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID);
            int artistName = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int dateAdd = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED);

            Song song = new Song();
            song.setTitle(cursor.getString(title));
            song.setPath(cursor.getString(path));
            song.setAlbumName(cursor.getString(albumName));
            song.setDuration(cursor.getInt(duration));
            song.setArtistID(cursor.getInt(artistID));
            song.setArtistName(cursor.getString(artistName));
            song.setDateAdd(cursor.getInt(dateAdd));
            song.setFavSong(false);

            songList.add(song);

        }
    }

    public interface CreateDataParse {

         void onDataPass(String name, String path);

         void fullSongList(ArrayList<Song> songList, int position);

         String queryText();

         void currentSong(Song song);

         void getLength(int length);
    }


}
