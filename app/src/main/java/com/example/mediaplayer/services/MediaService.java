package com.example.mediaplayer.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.support.annotation.Nullable;

import com.example.mediaplayer.data.Song;
import com.google.android.exoplayer2.ExoPlayer;

import java.util.List;

public class MediaService extends IntentService {

    public static final String POSITION_ARG_NAME = "position";
    private MusicBinder binder = new MusicBinder();
    private List<Song> songs;
    private ExoPlayer exoPlayer;

    public MediaService(String name) {
        super(name);
    }

    

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    public class MusicBinder extends Binder {

        public MediaService getService() {
            return MediaService.this;
        }
    }
}
