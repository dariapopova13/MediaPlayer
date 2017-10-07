package com.example.mediaplayer.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daria Popova on 07.10.17.
 */

public final class PermissionUtils {

    public static final int MULTIPLE_PERMISSION_REQUEST = 0;

    public static boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    public static boolean getPermission(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int readExternalPermission = ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            List<String> requiredPermisions = new ArrayList<>();
            if (readExternalPermission != PackageManager.PERMISSION_GRANTED) {
                requiredPermisions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (!requiredPermisions.isEmpty()) {
                ActivityCompat.requestPermissions(activity, requiredPermisions.toArray(
                        new String[requiredPermisions.size()]), MULTIPLE_PERMISSION_REQUEST);
                return false;
            } else return true;
        }
        return false;
    }





}
