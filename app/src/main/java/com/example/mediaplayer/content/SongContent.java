package com.example.mediaplayer.content;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.mediaplayer.data.Song;
import com.example.mediaplayer.data.builder.SongBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daria Popova on 30.09.17.
 */

public class SongContent {

    public static List<Song> songs;
    private static Uri musicUri;
    private static ContentResolver musicResolver;
    private static Cursor musicCursor;

    private SongContent(Context context) {
        this(context.getContentResolver());
    }

    private SongContent(ContentResolver contentResolver) {
        this.musicResolver = contentResolver;

        musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        musicCursor = musicResolver.query(musicUri, null, selection, null, sortOrder);
    }


    public static void initSongs(Context context) {
        musicResolver = context.getContentResolver();

        musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        musicCursor = musicResolver.query(musicUri, null, selection, null, sortOrder);
        songs = getSongList();
    }

    private static List<Song> getSongList() {
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
//            MediaStore.Audio.Albums.
//            int albumCoverColumn = musicCursor.getColumnCount()
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
}
