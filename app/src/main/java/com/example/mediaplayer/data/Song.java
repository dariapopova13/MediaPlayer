package com.example.mediaplayer.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Daria Popova on 29.09.17.
 */

public class Song implements Parcelable {

    private long id;
    private String title;
    private String album;
    private String artist;
    private int duration;
    private String data;
    private String albumCover;
    private long artistId;

    public Uri getUri(){
        return Uri.parse(data);
    }

    private Song(long id, String title, String album, String artist,
                 int duration, String data, String albumCover, long artistId) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.data = data;
        this.albumCover = albumCover;
        this.artistId = artistId;

    }

    public Song() {
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
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


    public static class Builder {

        private long id;
        private String title;
        private String album;
        private String artist;
        private int duration;
        private String data;
        private String albumCover;
        private Parcel in;
        private long artistId;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setAlbum(String album) {
            this.album = album;
            return this;
        }

        public Builder setArtist(String artist) {
            this.artist = artist;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder setData(String data) {
            this.data = data;
            return this;
        }

        public Builder setAlbumCover(String albumCover) {
            this.albumCover = albumCover;
            return this;
        }

        public Builder setIn(Parcel in) {
            this.in = in;
            return this;
        }

        public Song build() {
            return new Song(id, title, album, artist, duration, data, albumCover, artistId);
        }

        public Builder setArtistId(long artistId) {
            this.artistId = artistId;
            return this;
        }
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
        dest.writeString(this.albumCover);
        dest.writeLong(this.artistId);
    }

    protected Song(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.album = in.readString();
        this.artist = in.readString();
        this.duration = in.readInt();
        this.data = in.readString();
        this.albumCover = in.readString();
        this.artistId = in.readLong();
    }

    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel source) {
            return new Song(source);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
