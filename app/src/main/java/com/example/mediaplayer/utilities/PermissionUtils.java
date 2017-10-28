package com.example.mediaplayer.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Daria Popova on 07.10.17.
 */

public final class PermissionUtils {

    public static final int READ_EXTERNAL_STORAGE_PERMISSION = 1;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(Activity activity, int permissionCode) {
        String permission = null;
        switch (permissionCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION: {
                permission = Manifest.permission.READ_EXTERNAL_STORAGE;
            }
        }

        return permission != null &&
                ContextCompat.checkSelfPermission(activity, permission)
                        == PackageManager.PERMISSION_GRANTED;

    }


    public static void getPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION);
        }
    }
}
