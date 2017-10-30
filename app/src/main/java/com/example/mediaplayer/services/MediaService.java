package com.example.mediaplayer.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mediaplayer.R;
import com.example.mediaplayer.activities.StartActivity;
import com.example.mediaplayer.data.Song;
import com.example.mediaplayer.interfaces.StorageObserver;
import com.example.mediaplayer.utilities.AppUtils;
import com.example.mediaplayer.utilities.StorageUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.List;


public class MediaService extends Service implements ExoPlayer.EventListener, StorageObserver {

    public static final String CURRENT_SONG_KEY = "current_song";

    private static final String TAG = MediaService.class.getSimpleName();
    private MusicBinder binder = new MusicBinder();
    private List<Song> songs;
    private ExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;
    private SimpleExoPlayerView exoPlayerView;
    private NotificationManager notificationManager;
    private List<Song> orderSongs;
    private Song currentSong;
    private MediaSource mediaSource;
    private PlaybackStateCompat.Builder stateBuilder;
    private BroadcastReceiver receiver;


    @Override
    public void onCreate() {
        super.onCreate();

        initializeMediaSession();
        initializePlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        addObserver(this);
        registerLocalReceiver();
        songs = StorageUtils.getSongs(this);
        currentSong = intent.getParcelableExtra(CURRENT_SONG_KEY);
        if (currentSong == null)
            currentSong = songs.get(0);
        preparePlayer();
        return super.onStartCommand(intent, flags, startId);
    }

    private void registerLocalReceiver() {
        receiver = new MediaBrodcastReciever();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                new IntentFilter(MediaBrodcastReciever.PLAY_ANOTHER_SONG_ACTION));
    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(this, TAG);

        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_STOP |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);

        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MediaServiceCallback());
        mediaSession.setActive(true);
    }

    private void showNotification(PlaybackStateCompat state) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        int icon;

        String playPause;
        if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            icon = R.drawable.exo_controls_pause;
            playPause = getString(R.string.pause);
        } else {
            icon = R.drawable.exo_controls_play;
            playPause = getString(R.string.play);
        }

        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, playPause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                        this, PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action skipToNextAction = new NotificationCompat.Action(
                R.drawable.exo_controls_next, getString(R.string.skip_to_next),
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                        this, PlaybackStateCompat.ACTION_SKIP_TO_NEXT));

        NotificationCompat.Action skipToPreviousAction = new NotificationCompat.Action(
                R.drawable.exo_controls_previous, getString(R.string.skip_to_previous),
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                        this, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        Intent intent = new Intent(this, StartActivity.class);
//        intent.putExtra("", position);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, 0);

        builder.setContentTitle(currentSong.getTitle())
                .setContentText(currentSong.getArtist())
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_music_player)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(playPauseAction)
                .addAction(skipToNextAction)
                .addAction(skipToPreviousAction)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0, 1));

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    private void initializePlayer() {
        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

            exoPlayer.addListener(this);
        }
    }

    private void preparePlayer() {
        exoPlayer.stop();

        String userAgent = AppUtils.getUserAgent(this, getString(R.string.app_name));
        DataSource.Factory factory = new DefaultDataSourceFactory(
                this, userAgent, new DefaultBandwidthMeter());
        mediaSource = new ExtractorMediaSource(currentSong.getUri(),
                factory, new DefaultExtractorsFactory(), null, null);

        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    exoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    exoPlayer.getCurrentPosition(), 1f);
        }
        mediaSession.setPlaybackState(stateBuilder.build());
        showNotification(stateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onDestroy() {
        releasePlayer();
        mediaSession.setActive(false);
        super.onDestroy();
        LocalBroadcastManager.getInstance(this.getApplicationContext()).unregisterReceiver(receiver);
    }

    private void releasePlayer() {
        notificationManager.cancelAll();
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private void playSong(Song song) {
        currentSong = song;
        preparePlayer();
    }

    @Override
    public void update() {
        songs = StorageUtils.getSongs(this);
    }


    public static class MediaServiceCreator {

        static boolean IS_CREATED = false;

        public static void playSong(Context context, Song song) {
            if (IS_CREATED) {
                changeSong(context, song);
            } else {
                createService(context, song);
            }
        }

        private static void changeSong(Context context, Song song) {
            Intent intent = new Intent(context, MediaBrodcastReciever.class);
            intent.setAction(MediaBrodcastReciever.PLAY_ANOTHER_SONG_ACTION);
            intent.putExtra(CURRENT_SONG_KEY, song);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

        private static void createService(Context context, Song song) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(context, MediaService.class);
                    intent.putExtra(CURRENT_SONG_KEY, song);
                    context.startService(intent);
                    IS_CREATED = true;
                }
            }).start();
        }
    }

    public class MusicBinder extends Binder {

        public MediaService getService() {
            return MediaService.this;
        }
    }

    private class MediaServiceCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
        }

        // TODO: 29.10.17  skip to next
        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
        }

        // TODO: 29.10.17 skip to previous
        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
        }

        // TODO: 29.10.17 stop
        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
        }
    }

    public class MediaBrodcastReciever extends BroadcastReceiver {

        public static final String PLAY_ANOTHER_SONG_ACTION = "com.example.mediaplayer.PLAY_ANOTHER_SONG_ACTION";

        @Override
        public void onReceive(Context context, Intent intent) {
            Song song = intent.getParcelableExtra(CURRENT_SONG_KEY);
            if (song != null) {
                playSong(song);
            }
        }
    }
}

