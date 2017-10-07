package com.example.mediaplayer.data.builder;

import android.os.Parcel;

import com.example.mediaplayer.data.Song;


public class SongBuilder {

    private long id;
    private String title;
    private String album;
    private String artist;
    private int duration;
    private String data;
    private String albumCover;
    private Parcel in;

    public SongBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public SongBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public SongBuilder setAlbum(String album) {
        this.album = album;
        return this;
    }

    public SongBuilder setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public SongBuilder setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public SongBuilder setData(String data) {
        this.data = data;
        return this;
    }

    public SongBuilder setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
        return this;
    }

    public SongBuilder setIn(Parcel in) {
        this.in = in;
        return this;
    }

    public Song createSong() {
        return new Song(id, title, album, artist, duration, data, albumCover);
    }
}