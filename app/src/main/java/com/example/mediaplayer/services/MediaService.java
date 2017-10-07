package com.example.mediaplayer.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.MediaController;

import com.example.mediaplayer.data.enums.RepeatTypeEnum;
import com.example.mediaplayer.utilities.DataUtils;
import com.example.mediaplayer.utilities.PreferencesUtils;

import java.io.IOException;
import java.util.Random;

public class MediaService extends Service
        implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaController.MediaPlayerControl {

    private MediaPlayer mediaPlayer;
    private int currentPosition;
    private MusicBinder binder = new MusicBinder();
    private int resumePosition;

    @Override
    public void start() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying())
            mediaPlayer.start();
    }

    @Override
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
        }
    }

    @Override
    public int getDuration() {
        return DataUtils.songs.get(currentPosition).getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    @Override
    public void seekTo(int i) {

    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
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

    private void stop() {
        if (mediaPlayer == null)
            return;
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
    }

    private void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(resumePosition);
            mediaPlayer.start();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentPosition = intent.getIntExtra("position", 0);
        initMediaPlayer();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            stop();
            mediaPlayer.release();
        }
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
//        AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                .setContentType()
//        mediaPlayer.setAudioAttributes();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.reset();

        setMediaPlayerData(DataUtils.songs.get(currentPosition).getData());
        mediaPlayer.prepareAsync();
        start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        setNextSong();
    }

    private void setNextSong() {
        RepeatTypeEnum repeatTypeEnum = PreferencesUtils.getRepeatTypeEnum(this);
        if (repeatTypeEnum.equals(RepeatTypeEnum.REPEAT_ONE)) {
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
            return;
        }
        boolean shuffled = PreferencesUtils.isShuffled(this);
        boolean repeatAll = PreferencesUtils.isRepeatAll(this);
        if (shuffled) {
            Random random = new Random();
            int randomPosition;
            do {
                randomPosition = random.nextInt(DataUtils.songs.size());
            } while (randomPosition == currentPosition);
            currentPosition = randomPosition;
            setMediaPlayerData(DataUtils.songs.get(currentPosition).getData());
        } else {
            int temp = currentPosition++;
            if (temp > DataUtils.songs.size() && repeatAll) {
                currentPosition = 0;
                setMediaPlayerData(DataUtils.songs.get(currentPosition).getData());
            } else if (temp > DataUtils.songs.size() && !repeatAll) {
                stop();
                stopSelf();
            } else {
                setMediaPlayerData(DataUtils.songs.get(temp).getData());
                currentPosition = temp;
            }
        }
    }

    private boolean setMediaPlayerData(String data) {
        try {
            mediaPlayer.setDataSource(data);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override

    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }

    public void playSong(int position) {
        setMediaPlayerData(DataUtils.songs.get(position).getData());
        start();
    }

    public class MusicBinder extends Binder {

        public MediaService getService() {
            return MediaService.this;
        }
    }
}
