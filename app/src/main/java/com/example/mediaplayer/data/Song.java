package com.example.mediaplayer.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mediaplayer.data.builder.SongBuilder;


/**
 * Created by Daria Popova on 29.09.17.
 */

public class Song implements Parcelable {


    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel source) {
            return new SongBuilder().setIn(source).createSong();
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
    private long id;
    private String title;
    private String album;
    private String artist;
    private int duration;
    private String data;
    private String albumCover;

    public Song(long id, String title, String album, String artist, int duration, String data, String albumCover) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.data = data;
        this.albumCover = albumCover;
    }

    public Song() {
    }

    protected Song(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.album = in.readString();
        this.artist = in.readString();
        this.duration = in.readInt();
        this.data = in.readString();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", album='" + album + '\'' +
                ", artist='" + artist + '\'' +
                ", duration=" + duration +
                ", data='" + data + '\'' +
                '}';
    }

    public String getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.album);
        dest.writeString(this.artist);
        dest.writeInt(this.duration);
        dest.writeString(this.data);
    }
}
