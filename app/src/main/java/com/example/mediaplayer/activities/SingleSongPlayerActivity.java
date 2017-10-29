package com.example.mediaplayer.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediaplayer.R;
import com.example.mediaplayer.data.Song;
import com.example.mediaplayer.data.enums.RepeatTypeEnum;
import com.example.mediaplayer.data.enums.ShuffleEnum;
import com.example.mediaplayer.services.MediaService;
import com.example.mediaplayer.utilities.PreferencesUtils;
import com.example.mediaplayer.utilities.StorageUtils;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.List;

/**
 * Created by Daria Popova on 07.10.17.
 */

public class SingleSongPlayerActivity extends FragmentActivity
        implements View.OnClickListener {


    public static final String SONG_POSITION_KEY = "position";
    public static final String BROADCAST_PLAY_NEW_AUDIO = "com.example.mediaplayer.PLAY_NEW_AUDIO_ACTION";
    private static final String SERVICE_STATUS_BOUND_ARG = "serviceStatus";

    private SimpleExoPlayerView exoPlayerView;
    private ImageView repeatImageView;
    private ImageView shuffleImageView;
    private Song song;
    private int position;
    private MediaService mediaService;
    private boolean serviceBound = false;
    private Toast toast;
    private TextView songArtist;
    private TextView songTitle;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MediaService.MusicBinder binder = (MediaService.MusicBinder) iBinder;
            mediaService = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            serviceBound = false;
        }
    };


    private void startMediaService() {
        if (!serviceBound) {

            Intent intent = new Intent(SingleSongPlayerActivity.this, MediaService.class);
            intent.putExtra(SONG_POSITION_KEY, position);
            startService(intent);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            Intent broadCastIntent = new Intent(BROADCAST_PLAY_NEW_AUDIO);
            sendBroadcast(broadCastIntent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(SERVICE_STATUS_BOUND_ARG, serviceBound);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean(SERVICE_STATUS_BOUND_ARG);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_song_player_activity);

        position = getIntent().getIntExtra(SONG_POSITION_KEY, 0);
        List<Song> songs = StorageUtils.getSongs(this);
        if (songs == null) {
            finish();
        }
        song = songs.get(position);
//        initViews();
//        applyPreferences();
//        setSongInfo();
        startMediaService();
    }

    private void setSongInfo() {
        songTitle.setText(song.getTitle());
        songArtist.setText(song.getArtist());
    }

    private void cancelToast() {
        if (toast != null && toast.getView().isShown())
            toast.cancel();
    }

    private void initViews() {

//        exoPlayerView = (SimpleExoPlayerView) findViewById(R.id.playerView);

//        repeatImageView = (ImageView) findViewById(R.id.single_song_player_controller_repeat_action);
//        repeatImageView.setOnClickListener(this);
//
//        shuffleImageView = (ImageView) findViewById(R.id.single_song_player_controller_shuffle_action);
//        shuffleImageView.setOnClickListener(this);
//        songTitle = (TextView) findViewById(R.id.single_song_player_info_song_title);
//        songArtist = (TextView) findViewById(R.id.single_song_player_info_song_artist);
    }

    private void applyPreferences() {
        setRepeat();
        setShuffed();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.single_song_player_controller_shuffle_action: {
                setShuffledClicked();
                break;
            }
            case R.id.single_song_player_controller_repeat_action: {
                setRepeatClicked();
                break;
            }
        }
    }

    private void showToast(int messageId) {
        cancelToast();
        toast = Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void setRepeat() {
        RepeatTypeEnum repeatTypeEnum = PreferencesUtils.getRepeatTypeEnum(this);
        repeatImageView.setImageDrawable(ContextCompat.getDrawable(this, repeatTypeEnum.getIconId()));
        repeatImageView.setColorFilter(ContextCompat.getColor(this, repeatTypeEnum.getColorId()));
    }

    private void setShuffed() {
        ShuffleEnum shuffleEnum = PreferencesUtils.getShuffleEnum(this);
        shuffleImageView.setColorFilter(ContextCompat.getColor(this, shuffleEnum.getColorId()));
    }

    private void setRepeatClicked() {
        RepeatTypeEnum nextRepeatTypeEnum = RepeatTypeEnum.getNext(PreferencesUtils.getRepeatTypeEnum(this));
        PreferencesUtils.setIsRepeated(this, nextRepeatTypeEnum);
        setRepeat();
        showToast(nextRepeatTypeEnum.getMessageId());
    }

    private void setShuffledClicked() {
        ShuffleEnum isShuffled = ShuffleEnum.getOpposite(PreferencesUtils.getShuffleEnum(this));
        PreferencesUtils.setShuffle(this, isShuffled);
        setShuffed();
        showToast(isShuffled.getMessageId());
    }

}
