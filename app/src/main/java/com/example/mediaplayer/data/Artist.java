package com.example.mediaplayer.data;

import java.util.List;

/**
 * Created by Daria Popova on 28.10.17.
 */

public class Artist {

    private long id;
    private String name;
    private List<Song> songs;

    private Artist(long id, String name, List<Song> songs) {
        this.id = id;
        this.name = name;
        this.songs = songs;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public static class Builder {

        private long id;
        private String name;
        private List<Song> songs;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSongs(List<Song> songs) {
            this.songs = songs;
            return this;
        }

        public Artist build() {
            return new Artist(id, name, songs);
        }
    }
}
