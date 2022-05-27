package com.der.themusic;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import com.der.themusic.adapters.ViewPageAdapter;
import com.der.themusic.database.FavouritesOperation;
import com.der.themusic.fragments.AllSongFragment;
import com.der.themusic.fragments.FavouriteFragment;
import com.der.themusic.fragments.ThemeFragment;
import com.der.themusic.models.Song;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        ThemeFragment.OnThemeClickListener,
        AllSongFragment.CreateDataParse,
        FavouriteFragment.CreateDataParse,
        View.OnClickListener {

    private final int MY_PERMISSION_REQUEST = 100;

    private NavigationView navView;
    private DrawerLayout mDrawerLy;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager2 viewPager2;

    private ImageView ivPlay, ivNext, ivPrevious, ivRepeat, ivLoop;
    private TextView tvCurrent, tvTotalTime;
    private SeekBar seekBar;

    private SharedPreferences sharedPreferences;
    private int theme;

    private boolean checkFlag = false,favFlag = true, repeatFlag = false, playContinueFlag = false;

    private List<Song> songList;
    private int currentPosition;
    String searchText = "";

    private MediaPlayer mediaPlayer;
    Handler handler;
    Runnable runnable;

    RelativeLayout smallMediaPlayerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int selectedTheme = sharedPreferences.getInt("selectedTheme", 0);
        int currentSongPosition = sharedPreferences.getInt("currSongPosition", 0);
        setDynamicTheme(selectedTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        grantedPermission();

    }

    private void setDynamicTheme(int selectedTheme) {
        switch (selectedTheme){
            case 1:
                MainActivity.this.setTheme(R.style.WhiteTheme);
                break;
            case 2:
                MainActivity.this.setTheme(R.style.BlackTheme);
                break;
            case 3:
                MainActivity.this.setTheme(R.style.OrangeTheme);
                break;
            case 4:
                MainActivity.this.setTheme(R.style.VioletTheme);
                break;
            case 5:
                MainActivity.this.setTheme(R.style.PinkTheme);
                break;
            case 6:
                MainActivity.this.setTheme(R.style.RosewoodTheme);
                break;
            case 7:
                MainActivity.this.setTheme(R.style.BumblebeeTheme);
                break;
            case 8:
                MainActivity.this.setTheme(R.style.EggplantTheme);
                break;
        }
    }

    private void init(){

        //set action bar and tool bar
        setToolBarAndActionBar();

        navView = findViewById(R.id.left_nav_view);
        mDrawerLy = findViewById(R.id.drawer_layout);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                clickOnNavItem(item);
                return false;
            }
        });

        ivPlay = findViewById(R.id.play);
        ivNext = findViewById(R.id.next);
        ivPrevious = findViewById(R.id.previous);
        ivRepeat = findViewById(R.id.repeat);
        ivLoop = findViewById(R.id.loop);

        tvCurrent = findViewById(R.id.tv_current_time);
        tvTotalTime = findViewById(R.id.tv_total_time);
        seekBar = findViewById(R.id.seekbar_controller);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager2);

        ivPlay.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivPrevious.setOnClickListener(this);
        ivRepeat.setOnClickListener(this);
        ivLoop.setOnClickListener(this);

        smallMediaPlayerLayout = findViewById(R.id.media_player);
        smallMediaPlayerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "layout was click", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
            }
        });

        mediaPlayer = new MediaPlayer();
        handler = new Handler();

    }

    private void grantedPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Snackbar snackbar = Snackbar.make(mDrawerLy, "Provide the Storage Permission", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        } else {
            setPagerLayout();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                        setPagerLayout();
                    } else {
                        Snackbar snackbar = Snackbar.make(mDrawerLy, "Provide the Storage Permission", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        finish();
                    }
                }
        }
    }

    private void setPagerLayout(){

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(viewPageAdapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0){
                    tab.setText("All");
                    //Toast.makeText(MainActivity.this, "tabLayout 1 was click", Toast.LENGTH_SHORT).show();
                }else if(position == 1){
                    //Toast.makeText(MainActivity.this, "tabLayout 2 was click", Toast.LENGTH_SHORT).show();
                    tab.setText("Favourites");
                }

            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Toast.makeText(MainActivity.this, tab.getPosition() == 0 ? "all song" : "fav song", Toast.LENGTH_SHORT).show();
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void setToolBarAndActionBar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        /*action bar ရဲ့ home ကို set (ပြန်ခေါ်ရင် android.R.id.home)*/
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

    }

    //tool bar အတွင်း က item တွေကို ဒီမှာ ရေးသည် 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //drawer menu ထဲက item တွေကို ဒီမှာ setting လုပ်
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLy.openDrawer(Gravity.LEFT);
                return true;
            case R.id.favourite_btn:
                if (checkFlag)
                if (mediaPlayer != null){
                    if (favFlag){
                        Toast.makeText(this, songList.get(currentPosition).getTitle()+"id Added to Favorites", Toast.LENGTH_SHORT).show();
                        item.setIcon(R.drawable.ic_fill_fav);

                        Song favSong = new Song();
                        favSong.setTitle(songList.get(currentPosition).getTitle());
                        favSong.setArtistName(songList.get(currentPosition).getArtistName());
                        favSong.setPath(songList.get(currentPosition).getPath());
                        favSong.setArtistID(songList.get(currentPosition).getArtistID());
                        favSong.setAlbumName(songList.get(currentPosition).getAlbumName());
                        favSong.setDuration(songList.get(currentPosition).getDuration());
                        favSong.setDateAdd(songList.get(currentPosition).getDateAdd());
                        favSong.setFavSong(true);

                        FavouritesOperation favoritesOperations = new FavouritesOperation(this);
                        favoritesOperations.insert(favSong);
                        setPagerLayout();
                        favFlag = false;
                    }else {
                        item.setIcon(R.drawable.ic_not_fill_fav);
                        favFlag = true;
                    }
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private boolean clickOnNavItem(MenuItem item){
        item.setCheckable(true);
        mDrawerLy.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.theme:
                theme();
                break;
            case R.id.rate:
                rateApp();
                break;
            case R.id.about:
                about();
                break;
        }
        return true;
    }

    private void theme() {
        getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, new ThemeFragment())
                .addToBackStack(null)
                .commit();
    }

    private void attachMusic(String name, String path){
        ivPlay.setImageResource(R.drawable.play);
        //menu.getItem(0).setIcon(R.drawable.favorite_icon);
        checkFlag = true;
        favFlag = true;

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            setControls();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ivPlay.setImageResource(R.drawable.play);
                if (playContinueFlag) {
                    //Toast.makeText(MainActivity.this, "next song...", Toast.LENGTH_SHORT).show();
                    if (currentPosition + 1 < songList.size()) {
                        attachMusic(songList.get(currentPosition + 1).getTitle(), songList.get(currentPosition + 1).getPath());
                        currentPosition += 1;
                    } else {
                        //Toast.makeText(MainActivity.this, "PlayList Ended", Toast.LENGTH_SHORT).show();
                        currentPosition = 0;
                        attachMusic(songList.get(currentPosition).getTitle(), songList.get(currentPosition).getPath());
                    }
                }
            }
        });

    }

    private void setControls(){
        seekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.start();
        //TODO I Don't get it. HOW THIS IS WORK.
        playCycle();

        if (mediaPlayer.isPlaying()) {
            ivPlay.setImageResource(R.drawable.pause);
            tvTotalTime.setText(getTimeFormatted(mediaPlayer.getDuration()));
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    tvCurrent.setText(getTimeFormatted(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    //TODO I Don't get it. HOW THIS IS WORK.
    private void playCycle() {
        try {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            tvCurrent.setText(getTimeFormatted(mediaPlayer.getCurrentPosition()));

            if (mediaPlayer.isPlaying()) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        playCycle();
                    }
                };
                handler.postDelayed(runnable, 100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTimeFormatted(long milliSeconds){

        String finalTimerString = "";
        String secondsString;

        //Converting total duration into time
        int hours = (int) (milliSeconds / 3600000);
        int minutes = (int) (milliSeconds % 3600000) / 60000;
        int seconds = (int) ((milliSeconds % 3600000) % 60000 / 1000);

        // Adding hours if any
        if (hours > 0)
            finalTimerString = hours + ":";

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10)
            secondsString = "0" + seconds;
        else
            secondsString = "" + seconds;

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // Return timer String;
        return finalTimerString;
    }


    @Override
    public void getTheme(int theme) {
        this.theme = theme;
    }

    @Override
    public void onDataPass(String name, String path) {
        attachMusic(name, path);
    }

    @Override
    public void fullSongList(ArrayList<Song> songList, int position) {
        this.songList = songList;
        this.currentPosition = position;
    }

    @Override
    public String queryText() {
       return searchText.toLowerCase();
    }

    @Override
    public int getPosition() {
        //Toast.makeText(MainActivity.this, "curr pos: "+currentPosition, Toast.LENGTH_SHORT).show();
        return currentPosition;
    }

    @Override
    public void currentSong(Song song) {

    }

    @Override
    public void getLength(int length) {

    }

    private void rateApp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please rate this app")
                .setMessage(R.string.rate_text)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "thank for using app", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void about(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("about")
                .setMessage(R.string.about_text)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "thank for using app", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play:
                if (checkFlag){
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        ivPlay.setImageResource(R.drawable.play);
                    }else if (!mediaPlayer.isPlaying()){
                        mediaPlayer.start();
                        ivPlay.setImageResource(R.drawable.pause);
                    }
                }else {
                    Toast.makeText(this, "Please select the Song to play.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.previous:
                //No Repeat
                if (currentPosition -1 > -1){
                    attachMusic(songList.get(currentPosition - 1).getTitle(), songList.get(currentPosition - 1).getPath());
                    currentPosition = currentPosition - 1;
                }else {
                    attachMusic(songList.get(currentPosition).getTitle(), songList.get(currentPosition).getPath());
                }
                break;
            case R.id.next:
                //No Repeat
                if (currentPosition + 1 < songList.size()){
                    attachMusic(songList.get(currentPosition + 1).getTitle(), songList.get(currentPosition + 1).getPath());
                    currentPosition = currentPosition + 1;
                }else {
                    attachMusic(songList.get(currentPosition).getTitle(), songList.get(currentPosition).getPath());
                }
                break;

            case R.id.loop:
                if (!playContinueFlag) {
                    playContinueFlag = true;
                    //Toast.makeText(this, "Loop Added", Toast.LENGTH_SHORT).show();
                    ivLoop.setImageResource(R.drawable.ic_loop_red);
                } else {
                    playContinueFlag = false;
                    //Toast.makeText(this, "Loop Removed", Toast.LENGTH_SHORT).show();
                    ivLoop.setImageResource(R.drawable.ic_loop_black);
                }
                break;
            case R.id.repeat:

                if (repeatFlag) {
                    //Toast.makeText(this, "Replaying Removed..", Toast.LENGTH_SHORT).show();
                    ivRepeat.setImageResource(R.drawable.ic_repeat_black);
                    mediaPlayer.setLooping(false);
                    repeatFlag = false;
                } else {
                    //Toast.makeText(this, "Replaying Added..", Toast.LENGTH_SHORT).show();
                    ivRepeat.setImageResource(R.drawable.ic_repeat_red);
                    mediaPlayer.setLooping(true);
                    repeatFlag = true;
                }
                break;
        }
    }
}