package com.example.mediaplayer.fragments;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by Daria Popova on 28.10.17.
 */

public class AlbumTabFragment extends Fragment {

//    private

    public static AlbumTabFragment newInstance(String title) {
        Bundle args = new Bundle();

        AlbumTabFragment fragment = new AlbumTabFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
