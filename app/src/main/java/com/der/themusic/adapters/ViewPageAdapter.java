package com.der.themusic.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.der.themusic.fragments.AllSongFragment;
import com.der.themusic.fragments.FavouriteFragment;

public class ViewPageAdapter extends FragmentStateAdapter {

    private static String title[] = {"All SONGS","CURRENT PLAYLIST", "FAVORITES"};

    public ViewPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return AllSongFragment.getInstance(position);
            default:
                return FavouriteFragment.getInstance(position);
        }
    }

    //TODO : -> What the fuck is that? <-  WHY??????
    @Override
    public int getItemCount() {
        return title.length;
    }
}
