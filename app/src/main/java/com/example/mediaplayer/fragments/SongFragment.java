package com.example.mediaplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediaplayer.R;
import com.example.mediaplayer.adapters.recycleview.SongsListAdapter;
import com.example.mediaplayer.data.Song;
import com.example.mediaplayer.interfaces.RecycleViewListener;
import com.example.mediaplayer.interfaces.StorageObserver;
import com.example.mediaplayer.services.MediaService;
import com.example.mediaplayer.utilities.StorageUtils;

//import com.example.mediaplayer.activities.SingleSongPlayerActivity;

/**
 * Created by Daria Popova on 28.10.17.
 */

public class SongFragment extends Fragment implements RecycleViewListener, StorageObserver, View.OnClickListener {

    private RecyclerView songsRecycleView;
    private SongsListAdapter songsListAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.songs_fragment, container, false);
        createRecycleView(view);
        addObserver(this);
        return view;
    }

    private void createRecycleView(View view) {
        songsRecycleView = (RecyclerView) view.findViewById(R.id.songs_recycle_view);

        songsListAdapter = new SongsListAdapter(getContext(), StorageUtils.getSongs(getContext()), this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());

        songsRecycleView.setAdapter(songsListAdapter);
        songsRecycleView.setLayoutManager(manager);

    }

    public static SongFragment newInstance() {
        Bundle args = new Bundle();
        SongFragment fragment = new SongFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void recyclerViewItemClicked(View view, int position) {
        Song song = StorageUtils.getSongs(getContext()).get(position);
        MediaService.MediaServiceCreator.playSong(getActivity().getApplicationContext(), song);
    }

    @Override
    public void update() {
        songsListAdapter.notifyAdapterDataSetChanged();
    }
}
