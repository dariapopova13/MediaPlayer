//package com.example.mediaplayer.activities;
//
//import android.Manifest;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.Toast;
//
//import com.example.mediaplayer.R;
//import com.example.mediaplayer.adapters.recycleview.SongsListAdapter;
//import com.example.mediaplayer.interfaces.RecycleViewListener;
//import com.example.mediaplayer.utilities.PermissionUtils;
//import com.example.mediaplayer.utilities.StorageUtils;
//
//public class MainActivity extends AppCompatActivity implements RecycleViewListener {
//
//    private static final int MY_PERMISSION_REQUEST_READ_EXTERNAL = 666;
//    private RecyclerView songsRecycleView;
//    private SongsListAdapter songsListAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
//
//        if (getPermission()) {
//            StorageUtils.storeSongData(this);
//            initViews();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case PermissionUtils.MULTIPLE_PERMISSION_REQUEST: {
//                if (PermissionUtils.verifyPermissions(grantResults)) {
//                    StorageUtils.storeSongData(this);
//                    initViews();
//                } else {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                        showPermissionDialog(getString(R.string.permission_message), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                switch (i) {
//                                    case DialogInterface.BUTTON_POSITIVE: {
//                                        PermissionUtils.getPermission(MainActivity.this);
//                                        break;
//                                    }
//                                    case DialogInterface.BUTTON_NEGATIVE: {
//                                        Toast.makeText(MainActivity.this,
//                                                getString(R.string.permission_denied_message), Toast.LENGTH_LONG).show();
//                                        break;
//                                    }
//                                }
//                            }
//                        });
//                    }
//                    break;
//                }
//            }
//        }
//    }
//
//    private void showPermissionDialog(String message, DialogInterface.OnClickListener listener) {
//        new AlertDialog.Builder(this)
//                .setMessage(message)
//                .setPositiveButton("OK", listener)
//                .setNegativeButton("Cancel", listener)
//                .create()
//                .show();
//    }
//
//    private boolean getPermission() {
//        return PermissionUtils.getPermission(this);
//    }
//
//
//    @Override
//    public void recyclerViewItemClicked(View view, int position) {
//        Intent intent = new Intent(this, SingleSongPlayerActivity.class);
//        intent.putExtra(SingleSongPlayerActivity.SONG_POSITION_EXTRA_NAME, position);
//        startActivity(intent);
//    }
//
//}
