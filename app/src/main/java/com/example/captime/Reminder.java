package com.example.captime;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class Reminder extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private List<String> songList;
    private String selectedSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        ListView listView = findViewById(R.id.listView);
        songList = getSongsFromRawDirectory();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songList);
        listView.setAdapter(adapter);

        Switch switchMusic = findViewById(R.id.switchMusic);
        switchMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mediaPlayer != null) {
                    if (isChecked) {
                        mediaPlayer.start();
                    } else {
                        mediaPlayer.pause();
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSong = songList.get(position);
                int songResource = getResources().getIdentifier(selectedSong, "raw", getPackageName());

                // Release the current MediaPlayer if it exists
                stopPlaying();

                // Create and start the new MediaPlayer
                mediaPlayer = MediaPlayer.create(Reminder.this, songResource);
                mediaPlayer.setLooping(true); // Set looping to true

                // Check if the switch button is already turned on
                Switch switchMusic = findViewById(R.id.switchMusic);
                if (switchMusic.isChecked()) {
                    startPlaying();

                }
            }
        });
    }

    private void startPlaying() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void stopPlaying() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private List<String> getSongsFromRawDirectory() {
        List<String> songs = new ArrayList<>();
        songs.add("ldr");
        songs.add("rain");
        //songs.add("sound");
        return songs;
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlaying();
    }
}