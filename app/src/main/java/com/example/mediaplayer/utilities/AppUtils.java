package com.example.mediaplayer.utilities;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;

import java.util.concurrent.TimeUnit;

/**
 * Created by Daria Popova on 30.09.17.
 */

public final class AppUtils {

    public static String getSongDuration(int duration) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        return String.valueOf(minutes + ":" + (seconds > 10 ? seconds : "0" + seconds));
    }

    public static String getUserAgent(Context context, String applicationName) {
        String versionName;
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "?";
        }
        return applicationName + "/" + versionName + " (Linux;Android " + Build.VERSION.RELEASE
                + ") " + "ExoPlayerLib/" + ExoPlayerLibraryInfo.VERSION;
    }

}
