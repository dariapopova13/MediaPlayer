package com.example.mediaplayer.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;

import java.util.concurrent.TimeUnit;

import jp.wasabeef.blurry.Blurry;

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


    public static void blurryBackgroud(Activity activity) {
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(android.R.id.content);
        viewGroup.post(new Runnable() {
            @Override
            public void run() {
                Blurry.with(activity)
                        .radius(10)
                        .sampling(10)
                        .color(Color.argb(66, 255, 255, 12))
                        .async()
                        .animate()
                        .onto(viewGroup);
            }
        });

    }

//    public static
}
