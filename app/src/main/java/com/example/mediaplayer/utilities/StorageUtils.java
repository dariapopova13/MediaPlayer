package com.example.mediaplayer.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.mediaplayer.data.Artist;
import com.example.mediaplayer.data.Song;
import com.example.mediaplayer.interfaces.StorageObserver;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Daria Popova on 30.09.17.
 */

public final class StorageUtils {

    private static List<Song> songs;
    private static List<Artist> artists;
    private static Song currentSong;
    private static List<StorageObserver> observers;

    public static void addObserver(StorageObserver observer) {
        if (observers == null)
            observers = new ArrayList<>();
        observers.add(observer);
    }

    public static void getArtistSongs(Artist artist) {
        if (songs == null) return;
        for (Song song : songs) {
            if (song.getArtistId() == artist.getId())
                artist.addSong(song);
        }
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

//    public static List<Song> getSongsData(Context context) {
//
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//        String json = sp.getString(SONG_STORAGE, null);
//        if (json != null) {
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<Song>>() {
//            }.getType();
//            return gson.fromJson(json, type);
//        }
//        return null;
//    }
//
//    public static void storeArtistData(Context context) {
//        List<Artist> artists = initArtists(context);
//        storeData(context, artists, ARTIST_STORAGE);
//    }
//
//    private static void storeSongData(Context context) {
//        List<Song> songs = initSongs(context);
//        storeData(context, songs, SONG_STORAGE);
//    }
//
//    private static void storeData(Context context, List songs, String name) {
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = sp.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(songs);
//        editor.putString(name, json);
//        editor.apply();
//    }

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
