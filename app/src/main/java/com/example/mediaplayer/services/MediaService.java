package com.example.mediaplayer.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.mediaplayer.interfaces.MediaController;
import com.example.mediaplayer.interfaces.MediaControllerListener;
import com.example.mediaplayer.interfaces.StorageObserver;
import com.example.mediaplayer.utilities.AppUtils;
import com.example.mediaplayer.utilities.StorageUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MediaService extends Service implements ExoPlayer.EventListener,
        StorageObserver, MediaControllerListener {

    public static final String CURRENT_SONG_KEY = "current_song";


    private static final String TAG = MediaService.class.getSimpleName();
    private List<Song> songs;
    private SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;

    private NotificationManager notificationManager;
    private Song currentSong;
    private PlaybackStateCompat.Builder stateBuilder;
    private BroadcastReceiver receiver;
    private List<MediaSource> mediaSourceList;


    @Override
    public void onCreate() {
        super.onCreate();

        addObserver(this);
        registerLocalReceiver();
        initializeMediaSession();
        initializePlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        setData(intent);
        preparePlayer();
        return super.onStartCommand(intent, flags, startId);
    }

    private void setData(Intent intent) {
        songs = StorageUtils.getSongs(this);
        currentSong = intent.getParcelableExtra(CURRENT_SONG_KEY);
        if (currentSong == null)
            currentSong = songs.get(0);
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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "666");
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

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, 0);

        builder.setContentTitle(currentSong.getTitle())
                .setContentText(currentSong.getArtist())
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_music_player)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(skipToPreviousAction)
                .addAction(playPauseAction)
                .addAction(skipToNextAction)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(1, 2));

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(666, builder.build());
    }

    private void initializePlayer() {
        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
//            trackSelector = new DefaultTrackSelector()
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

            exoPlayer.addListener(this);
            sendPlayer();
        }
    }

    private void preparePlayer() {
        exoPlayer.stop();

        ConcatenatingMediaSource mediaSource = createMediaSources();
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
    }

    private ConcatenatingMediaSource createMediaSources() {

        String userAgent = AppUtils.getUserAgent(this, getString(R.string.app_name));
        DataSource.Factory factory = new DefaultDataSourceFactory(
                this, userAgent, new DefaultBandwidthMeter());

        if (mediaSourceList == null) {
            mediaSourceList = new ArrayList<>();
            for (Song song : songs) {
                if (song.getId() != currentSong.getId()) {
                    MediaSource source1 = new ExtractorMediaSource(song.getUri(), factory,
                            new DefaultExtractorsFactory(), null, null);
                    mediaSourceList.add(source1);
                }
            }
        }
        Collections.shuffle(mediaSourceList);
        MediaSource source = new ExtractorMediaSource(currentSong.getUri(), factory,
                new DefaultExtractorsFactory(), null, null);

        mediaSourceList.add(0, source);

        MediaSource[] mediaSources = new MediaSource[mediaSourceList.size()];
        return new ConcatenatingMediaSource(mediaSourceList.toArray(mediaSources));
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

    private void playNewSong(Song song) {
        if (song != null) {
            currentSong = song;
            preparePlayer();
        }
    }

    @Override
    public void update() {
        songs = StorageUtils.getSongs(this);
    }

    @Override
    public void sendPlayer() {
        for (MediaController controller : MediaController.controllers) {
            controller.receivePlayer(exoPlayer);
        }
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
            new Thread(() -> {
                Intent intent = new Intent(context, MediaService.class);
                intent.putExtra(CURRENT_SONG_KEY, song);
                context.startService(intent);
                IS_CREATED = true;
            }).start();
        }

        public static void stopService(Context context) {
            Intent intent = new Intent(context, MediaService.class);
            context.stopService(intent);
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

    public class MediaBrodcastReciever extends MediaButtonReceiver {

        public static final String PLAY_ANOTHER_SONG_ACTION = "com.example.mediaplayer.PLAY_ANOTHER_SONG_ACTION";

        @Override
        public void onReceive(Context context, Intent intent) {
            Song song = intent.getParcelableExtra(CURRENT_SONG_KEY);
            playNewSong(song);
        }
    }

}

