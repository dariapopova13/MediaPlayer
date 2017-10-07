package com.example.mediaplayer.activities;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediaplayer.R;
import com.example.mediaplayer.data.Song;
import com.example.mediaplayer.data.enums.RepeatTypeEnum;
import com.example.mediaplayer.data.enums.ShuffleEnum;
import com.example.mediaplayer.services.MediaService;
import com.example.mediaplayer.utilities.PreferencesUtils;

/**
 * Created by Daria Popova on 07.10.17.
 */

public class SingleSongPlayerActivity extends AppCompatActivity
        implements View.OnClickListener, MediaController.MediaPlayerControl{

    public static final String SONG_EXTRA_NAME = "song";
    private SeekBar playerSeekBar;
    private TextView timePlaying;
    private TextView timeLeft;
    private ImageView playStopImageView;
    private ImageView nextSongImageView;
    private ImageView previousSongImageView;
    private ImageView repeatImageView;
    private ImageView shuffleImageView;
    private Song song;
    private MediaService mediaService;
    private boolean musicBound = false;
    private Toast toast;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    private MediaController mediaController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_song_player);

        song = getIntent().getParcelableExtra(SONG_EXTRA_NAME);
        if (song == null)
            finish();

        mediaController = (MediaController) findViewById(R.id.media_controller);

        mediaController.show();
//        initViews();
    }

    private void cancelToast() {
        if (toast != null && toast.getView().isShown())
            toast.cancel();
    }

//    private void initViews() {
//        playerSeekBar = (SeekBar) findViewById(R.id.single_song_player_seek_bar_controller);
//        timePlaying = (TextView) findViewById(R.id.single_song_player_time_playing);
//        timeLeft = (TextView) findViewById(R.id.single_song_player_time_left);
//
//        playStopImageView = (ImageView) findViewById(R.id.single_song_player_controller_play_stop_action);
//        playStopImageView.setOnClickListener(this);
//
//        nextSongImageView = (ImageView) findViewById(R.id.single_song_player_controller_next_action);
//        nextSongImageView.setOnClickListener(this);
//
//        previousSongImageView = (ImageView) findViewById(R.id.single_song_player_controller_previous_action);
//        previousSongImageView.setOnClickListener(this);
//
//        repeatImageView = (ImageView) findViewById(R.id.single_song_player_controller_repeat_action);
//        repeatImageView.setOnClickListener(this);
//
//        shuffleImageView = (ImageView) findViewById(R.id.single_song_player_controller_shuffle_action);
//        shuffleImageView.setOnClickListener(this);
//
//        applyPreferences();
//    }

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

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int i) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
