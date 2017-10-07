package com.example.mediaplayer.utilities;

import java.util.concurrent.TimeUnit;

/**
 * Created by Daria Popova on 30.09.17.
 */

public final class AppUtils {

//    public static List<Song> songs;

    public static String getSongDuration(int duration) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        return String.valueOf(minutes + ":" + (seconds > 10 ? seconds : "0" + seconds));
    }


//    public static void loadSongs(Context context) {
//        loadSongs(context.getContentResolver());
//    }

//    public static void loadSongs(ContentResolver contentResolver) {
//        DataUtils songContent = new DataUtils(contentResolver);
//        songs = songContent.getSongList();
//    }
}
