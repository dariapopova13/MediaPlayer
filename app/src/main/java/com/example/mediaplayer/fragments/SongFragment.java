package com.example.mediaplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mediaplayer.R;
import com.example.mediaplayer.adapters.recycleview.SongsListAdapter;
import com.example.mediaplayer.data.Song;
import com.example.mediaplayer.interfaces.MediaController;
import com.example.mediaplayer.interfaces.RecycleViewListener;
import com.example.mediaplayer.interfaces.StorageObserver;
import com.example.mediaplayer.services.MediaService;
import com.example.mediaplayer.utilities.StorageUtils;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

//import com.example.mediaplayer.activities.SingleSongPlayerActivity;

/**
 * Created by Daria Popova on 28.10.17.
 */

public class SongFragment extends Fragment implements RecycleViewListener,
        StorageObserver, View.OnClickListener, MediaController {

    private RecyclerView songsRecycleView;
    private SongsListAdapter songsListAdapter;
    private SimpleExoPlayerView simpleExoPlayerView;
    private Button stopServiceButton;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void receivePlayer(SimpleExoPlayer exoPlayer) {
        simpleExoPlayerView.setPlayer(exoPlayer);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.stop_service:{
                MediaService.MediaServiceCreator.stopService(getActivity().getBaseContext());
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        addMediaController(this);
        View view = inflater.inflate(R.layout.songs_fragment, container, false);
        initViews(view);
        addObserver(this);
        return view;
    }

    private void initViews(View view) {
        songsRecycleView = (RecyclerView) view.findViewById(R.id.songs_recycle_view);

        songsListAdapter = new SongsListAdapter(getContext(), StorageUtils.getSongs(getContext()), this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());

        songsRecycleView.setAdapter(songsListAdapter);
        songsRecycleView.setLayoutManager(manager);

        simpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.simple_exo_player_view);
        stopServiceButton = (Button) view.findViewById(R.id.stop_service);
        stopServiceButton.setOnClickListener(this);
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
        MediaService.MediaServiceCreator.playSong(getActivity().getBaseContext(), song);
    }

    @Override
    public void update() {
        songsListAdapter.notifyAdapterDataSetChanged();
    }
}
