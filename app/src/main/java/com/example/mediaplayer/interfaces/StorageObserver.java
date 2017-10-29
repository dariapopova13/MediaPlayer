package com.example.mediaplayer.interfaces;

import com.example.mediaplayer.utilities.StorageUtils;

/**
 * Created by Daria Popova on 29.10.17.
 */

public interface StorageObserver {

    void update();

    static void addObserver(StorageObserver observer) {
        StorageUtils.addObserver(observer);
    }


    public interface SongUpdate{
        void notifyAdapterDataSetChanged();
    }
}
