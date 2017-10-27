package com.example.mediaplayer.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import com.example.mediaplayer.data.Song;
import com.example.mediaplayer.data.builder.SongBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Daria Popova on 30.09.17.
 */

public final class StorageUtils {

    private static final String STORAGE = "com.example.mediaplayer.STORAGE";


    public static void storeData(Context context) {
        List<Song> songs = initSongs(context);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(songs);
        editor.putString(STORAGE, json);
        editor.apply();
    }

    private static List<Song> initSongs(Context context) {
        ContentResolver musicResolver = context.getContentResolver();

        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor musicCursor = musicResolver.query(musicUri, null, selection, null, sortOrder);

        return getSongsData(musicCursor);
    }

    private static List<Song> getSongsData(Cursor musicCursor) {
        List<Song> songList = new ArrayList<>();

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex(
                    MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex(
                    MediaStore.Audio.Media.ARTIST);
            int albomColumn = musicCursor.getColumnIndex(
                    MediaStore.Audio.Media.ALBUM);
            int idColumn = musicCursor.getColumnIndex(
                    MediaStore.Audio.Media._ID);
            int durationColumn = musicCursor.getColumnIndex(
                    MediaStore.Audio.Media.DURATION);
            int dataColumn = musicCursor.getColumnIndex(
                    MediaStore.Audio.Media.DATA);
            do {
                Song song = new SongBuilder()
                        .setId(musicCursor.getLong(idColumn))
                        .setAlbum(musicCursor.getString(albomColumn))
                        .setArtist(musicCursor.getString(artistColumn))
                        .setDuration(musicCursor.getInt(durationColumn))
                        .setTitle(musicCursor.getString(titleColumn))
                        .setData(musicCursor.getString(dataColumn))
                        .createSong();

                songList.add(song);
            } while (musicCursor.moveToNext());

        }
        return songList;
    }

    public static void clearData(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.remove(STORAGE);
        editor.apply();
    }

    public static void updateData(Context context) {
        clearData(context);
        storeData(context);
    }

    public static List<Song> getSongsData(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sp.getString(STORAGE, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Song>>() {
            }.getType();
            return gson.fromJson(json, type);
        }
        return null;
    }


    
}
