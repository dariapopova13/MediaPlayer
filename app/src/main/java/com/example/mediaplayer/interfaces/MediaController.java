package com.example.mediaplayer.interfaces;

import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daria Popova on 30.10.17.
 */

public interface MediaController {

    public static List<MediaController> controllers = new ArrayList<>();

    default void addMediaController(MediaController mediaController) {
        controllers.add(mediaController);
    }

    void receivePlayer(SimpleExoPlayer exoPlayer);
}
