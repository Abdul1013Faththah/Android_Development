package com.example.music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView ;
    TextView noSongsView;
    ArrayList<AudioModel> songList = new ArrayList<>();
    ImageView playingNowView , libraryView , searchView;
    EditText searchBar;
    Button searchBtn;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        noSongsView = findViewById(R.id.no_songs_text);
        playingNowView = findViewById(R.id.playingNow);
        libraryView = findViewById(R.id.library);
        searchView = findViewById(R.id.search);
        searchBar= findViewById(R.id.search_Bar);
        searchBtn = findViewById(R.id.Btn_search);

        if(checkPermission() == false){
            requstPermission();
            return;
        }

        String[] projection = {

                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC +"!=0";

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null);
        while(cursor.moveToNext()){
            AudioModel songData = new AudioModel(cursor.getString(1), cursor.getString(0), cursor.getString(2) );
            if(new File(songData.getPath()).exists())
                songList.add(songData);
        }



        libraryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playingNowView.setColorFilter(Color.parseColor("#000000"));
                searchView.setColorFilter(Color.parseColor("#000000"));
                libraryView.setColorFilter(Color.parseColor("#FF0000"));
                searchBar.setVisibility(View.GONE);
                searchBtn.setVisibility(View.GONE);
                if (songList.size() == 0) {
                    noSongsView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(new musicAdapter(songList, getApplicationContext()));
                    noSongsView.setVisibility(View.GONE);
                }
            }
        });

        playingNowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                libraryView.setColorFilter(Color.parseColor("#000000"));
                searchView.setColorFilter(Color.parseColor("#000000"));
                playingNowView.setColorFilter(Color.parseColor("#FF0000"));
                searchBar.setVisibility(View.GONE);
                searchBtn.setVisibility(View.GONE);
                if (songList.size() == 0) {
                    noSongsView.setVisibility(View.VISIBLE);
                }
                else{
                    if(mediaPlayer.isPlaying() == false){
                        MyMediaPlayer.currentIndex = 0;
                        Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
                        startActivity(intent);
                    }
                    Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
                    intent.putExtra("LIST", songList);
                    startActivity(intent);
                }
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                libraryView.setColorFilter(Color.parseColor("#000000"));
                searchView.setColorFilter(Color.parseColor("#FF0000"));
                playingNowView.setColorFilter(Color.parseColor("#000000"));
                noSongsView.setVisibility(View.GONE);
                searchBar.setVisibility(View.VISIBLE);
                searchBtn.setVisibility(View.VISIBLE);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchBar.getText().toString();
                searchSongs(query);
            }
        });




    }
    boolean checkPermission(){
        int results = ContextCompat.checkSelfPermission(MainActivity.this,android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if(results == PackageManager.PERMISSION_GRANTED){
            return true ;
        }else{
            return false ;
        }

    }

    void requstPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this,"Read permission is required",Toast.LENGTH_SHORT).show();
        }else
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerView != null){
            recyclerView.setAdapter(new musicAdapter(songList,getApplicationContext()));
        }
    }

    private void searchSongs(String query) {
        ArrayList<AudioModel> searchResults = new ArrayList<>();

        for (AudioModel song : songList) {
            if (song.getTittle().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(song);
            }
        }

        if (searchResults.isEmpty()) {
            noSongsView.setVisibility(View.VISIBLE);
            noSongsView.setText("Song Not Found");
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setAdapter(new musicAdapter(searchResults, getApplicationContext()));
            noSongsView.setVisibility(View.GONE);
        }
    }
}