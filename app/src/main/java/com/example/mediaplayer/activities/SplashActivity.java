package com.example.mediaplayer.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.mediaplayer.R;
import com.example.mediaplayer.utilities.PermissionUtils;

/**
 * Created by Daria Popova on 29.10.17.
 */

public class SplashActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    @Override
    public void onClick(DialogInterface dialog, int which) {
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (!PermissionUtils.checkPermission(this, PermissionUtils.READ_EXTERNAL_STORAGE_PERMISSION)) {
                PermissionUtils.getPermission(this);
            } else {
                startActivity();
            }
        } else {
            startActivity();
        }
    }

    private void startActivity() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.READ_EXTERNAL_STORAGE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity();
                } else {
                    showPermissionDeniedDialog();
                }
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showPermissionDeniedDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.permission_denied_message)
                .setTitle(R.string.permission_denied_title)
                .setPositiveButton(R.string.permission_denied_button, this)
                .create();

        dialog.show();
    }


}
