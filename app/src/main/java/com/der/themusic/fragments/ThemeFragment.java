package com.der.themusic.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.der.themusic.MainActivity;
import com.der.themusic.R;

public class ThemeFragment extends Fragment implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    ImageButton white_theme, black_theme, orange_theme, violet_theme,
    pink_theme, rosewood_theme, bumblebee_theme, eggplant_theme;
    int selectedTheme;

    OnThemeClickListener onThemeClickListener;

    public ThemeFragment(){

    }

    @Override
    public void onAttach(@NonNull Context context) {

        onThemeClickListener = (OnThemeClickListener) getActivity();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        selectedTheme = sharedPreferences.getInt("selectedTheme", 0);
        setDynamicTheme(selectedTheme);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_theme, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        white_theme = view.findViewById(R.id.whiteTheme);
        black_theme = view.findViewById(R.id.blackTheme);
        orange_theme = view.findViewById(R.id.orangeTheme);
        violet_theme = view.findViewById(R.id.violetTheme);

        pink_theme = view.findViewById(R.id.pinkTheme);
        rosewood_theme = view.findViewById(R.id.rosewoodTheme);
        bumblebee_theme = view.findViewById(R.id.bumblebeeTheme);
        eggplant_theme = view.findViewById(R.id.eggplantTheme);

        white_theme.setOnClickListener(this);
        black_theme.setOnClickListener(this);
        orange_theme.setOnClickListener(this);
        violet_theme.setOnClickListener(this);

        pink_theme.setOnClickListener(this);
        rosewood_theme.setOnClickListener(this);
        bumblebee_theme.setOnClickListener(this);
        eggplant_theme.setOnClickListener(this);

        super.onViewCreated(view, savedInstanceState);
    }

    private void setDynamicTheme(int selectedTheme) {
        switch (selectedTheme){
            case 1:
                getActivity().setTheme(R.style.WhiteTheme);
                onThemeClickListener.getTheme(1);
                break;
            case 2:
                getActivity().setTheme(R.style.BlackTheme);
                onThemeClickListener.getTheme(2);
                break;
            case 3:
                getActivity().setTheme(R.style.OrangeTheme);
                onThemeClickListener.getTheme(3);
                break;
            case 4:
                getActivity().setTheme(R.style.VioletTheme);
                onThemeClickListener.getTheme(4);
                break;
            case 5:
                getActivity().setTheme(R.style.PinkTheme);
                onThemeClickListener.getTheme(5);
                break;
            case 6:
                getActivity().setTheme(R.style.RosewoodTheme);
                onThemeClickListener.getTheme(6);
                break;
            case 7:
                getActivity().setTheme(R.style.BumblebeeTheme);
                onThemeClickListener.getTheme(7);
                break;
            case 8:
                getActivity().setTheme(R.style.EggplantTheme);
                onThemeClickListener.getTheme(8);
                break;
        }
    }


    @Override
    public void onClick(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (view.getId()){
            case R.id.whiteTheme:
                editor.putInt("selectedTheme",  1);
                break;
            case R.id.blackTheme:
                editor.putInt("selectedTheme",  2);
                break;
            case R.id.orangeTheme:
                editor.putInt("selectedTheme",  3);
                break;
            case R.id.violetTheme:
                editor.putInt("selectedTheme",  4);
                break;
            case R.id.pinkTheme:
                editor.putInt("selectedTheme",  5);
                break;
            case R.id.rosewoodTheme:
                editor.putInt("selectedTheme",  6);
                break;
            case R.id.bumblebeeTheme:
                editor.putInt("selectedTheme",  7);
                break;
            case R.id.eggplantTheme:
                editor.putInt("selectedTheme",  8);
                break;
        }

        editor.apply();
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();
    }

    public interface OnThemeClickListener{
        void getTheme(int theme);
    }


}