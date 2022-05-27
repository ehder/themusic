package com.der.themusic.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.der.themusic.R;
import com.der.themusic.adapters.ItemAdapter;
import com.der.themusic.database.FavouritesOperation;
import com.der.themusic.models.Song;

import java.util.ArrayList;

public class FavouriteFragment extends ListFragment {

    private FavouritesOperation favoritesOperations;

    public ArrayList<Song> songsList;
    public ArrayList<Song> newList;

    private CreateDataParse createDataParsed;

    private ListView listView;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        FavouriteFragment tabFragment = new FavouriteFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        favoritesOperations = new FavouritesOperation(context);
        createDataParsed = (CreateDataParse) context;
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
        songsList = new ArrayList<>();
        songsList = favoritesOperations.getAll();
        ItemAdapter adapter = new ItemAdapter(getContext(), songsList);

        if (!createDataParsed.queryText().equals("")) {
            adapter = onQueryTextChange();
            adapter.notifyDataSetChanged();
            //searchedList = true;
        } else {
            //searchedList = false;
        }

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "is favourites? "+songsList.get(position).isFavSong(), Toast.LENGTH_SHORT).show();
                createDataParsed.onDataPass(songsList.get(position).getTitle(), songsList.get(position).getPath());
                createDataParsed.fullSongList(songsList, position);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteOption(position);
                return true;
            }
        });


    }

    private void deleteOption(int position) {
        if (position != createDataParsed.getPosition()) {
            showDialog(songsList.get(position).getPath(), position);
        } else {
            Toast.makeText(getContext(), "You Can't delete the Current Song", Toast.LENGTH_SHORT).show();
        }

    }

    private void showDialog(final String index, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete")
                .setMessage("Are you sure you want to delete")
                .setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        favoritesOperations.deleteByPath(index);
                        createDataParsed.fullSongList(songsList, position);
                        setContent();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public ItemAdapter onQueryTextChange() {
        String text = createDataParsed.queryText();
        for (Song songs : songsList) {
            String title = songs.getTitle().toLowerCase();
            if (title.contains(text)) {
                newList.add(songs);
            }
        }
        return new ItemAdapter(getContext(), newList);

    }

    public interface CreateDataParse {

        void onDataPass(String name, String path);

        void fullSongList(ArrayList<Song> songList, int position);

        String queryText();

        int getPosition();

    }


}
