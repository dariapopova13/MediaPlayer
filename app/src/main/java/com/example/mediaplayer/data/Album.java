package com.example.mediaplayer.data;

/**
 * Created by Daria Popova on 28.10.17.
 */

public class Album {

    private long id;
    private String name;
    private long artistId;
    private String cover;

    private Album(long id, String name, long artistId, String cover) {
        this.id = id;
        this.name = name;
        this.artistId = artistId;
        this.cover = cover;
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

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public static class Builder {

        private long id;
        private String name;
        private long artistId;
        private String cover;

        public Builder setCover(String cover) {
            this.cover = cover;
            return this;
        }

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setArtistId(long artistId) {
            this.artistId = artistId;
            return this;
        }

        public Album build() {
            return new Album(id, name, artistId,cover);
        }
    }
}
