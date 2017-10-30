package com.example.mediaplayer.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import com.example.mediaplayer.data.Artist;
import com.example.mediaplayer.data.Song;
import com.example.mediaplayer.interfaces.StorageObserver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Daria Popova on 30.09.17.
 */

public final class StorageUtils {

    private static final String SONG_POSITION_KEY = "song_position";
    private static List<Song> songs;
    private static List<Artist> artists;
    private static Song currentSong;
    private static List<StorageObserver> observers;

    public static void addObserver(StorageObserver observer) {
        if (observers == null)
            observers = new ArrayList<>();
        observers.add(observer);
    }

    private static final String CURRENT_SONG_KEY = "current_song";

    public static void saveSongWithPosition(Context context, Song song, int position) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        String json = new Gson().toJson(song);
        editor.putInt(SONG_POSITION_KEY, position);
        editor.putString(CURRENT_SONG_KEY, json);
        editor.apply();
    }

    public static void saveSong(Context context, Song song) {
        saveSongWithPosition(context, song, 0);
    }

    public static int getSongPosition(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int position = sp.getInt(SONG_POSITION_KEY, 0);
        return position;
    }

    public static Song getCurrentSong(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sp.getString(CURRENT_SONG_KEY, null);
        if (json == null) return null;
        Type type = new TypeToken<Song>() {
        }.getType();
        Song song = new Gson().fromJson(json, type);
        return song;
    }

    public static Song getCurrentSong() {
        return currentSong;
    }

    public static List<Song> getSongs(Context context) {
        if (songs == null)
            songs = initSongs(context);
        return songs;
    }

    public static List<Artist> getArtists(Context context) {
        if (artists == null)
            artists = initArtists(context);
        return artists;
    }

    public static void clearData(Context context) {
        songs = null;
        artists = null;
//        currentSong = null;
    }

    public static void updateData(Context context) {
        clearData(context);
        songs = initSongs(context);
        artists = initArtists(context);

        notifyChanges();
    }

    private static void notifyChanges() {
        if (observers != null)
            for (StorageObserver observer : observers) {
                observer.update();
            }
    }

    private static List<Artist> initArtists(Context context) {
        ContentResolver resolver = context.getContentResolver();

        Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Audio.Artists.ARTIST + " ASC";
        Cursor artistCursor = resolver.query(artistUri, null, null, null, sortOrder);

        return getArtistData(artistCursor);
    }

    private static List<Artist> getArtistData(Cursor cursor) {
        List<Artist> artists = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            int artistColumn = cursor.getColumnIndex(
                    MediaStore.Audio.Artists.ARTIST);
            int idColumn = cursor.getColumnIndex(
                    MediaStore.Audio.Artists._ID);
            Artist artist;
            do {
                artist = new Artist.Builder()
                        .setId(cursor.getLong(idColumn))
                        .setName(cursor.getString(artistColumn))
                        .build();
                artists.add(artist);
            } while (cursor.moveToNext());
        }
        closeCursor(cursor);
        return artists;
    }

    private static void closeCursor(Cursor cursor) {
        if (cursor != null)
            cursor.close();
    }

    private static List<Song> initSongs(Context context) {
        ContentResolver resolver = context.getContentResolver();

        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor musicCursor = resolver.query(musicUri, null, selection, null, sortOrder);

        return getSongsData(musicCursor);
    }

    private static List<Song> getSongsData(Cursor cursor) {
        List<Song> songList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            int titleColumn = cursor.getColumnIndex(
                    MediaStore.Audio.Media.TITLE);
            int artistColumn = cursor.getColumnIndex(
                    MediaStore.Audio.Media.ARTIST);
            int albomColumn = cursor.getColumnIndex(
                    MediaStore.Audio.Media.ALBUM);
            int idColumn = cursor.getColumnIndex(
                    MediaStore.Audio.Media._ID);
            int artistIdColumn = cursor.getColumnIndex(
                    MediaStore.Audio.Media.ARTIST_ID);
            int durationColumn = cursor.getColumnIndex(
                    MediaStore.Audio.Media.DURATION);
            int dataColumn = cursor.getColumnIndex(
                    MediaStore.Audio.Media.DATA);
            Song song;
            do {
                song = new Song.Builder()
                        .setId(cursor.getLong(idColumn))
                        .setAlbum(cursor.getString(albomColumn))
                        .setArtist(cursor.getString(artistColumn))
                        .setDuration(cursor.getInt(durationColumn))
                        .setTitle(cursor.getString(titleColumn))
                        .setData(cursor.getString(dataColumn))
                        .setArtistId(cursor.getLong(artistIdColumn))
                        .build();

                songList.add(song);
            } while (cursor.moveToNext());

        }
        closeCursor(cursor);
        return songList;
    }

}
