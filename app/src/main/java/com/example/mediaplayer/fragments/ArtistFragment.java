package com.example.mediaplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediaplayer.R;
import com.example.mediaplayer.adapters.recycleview.ArtistListAdapter;
import com.example.mediaplayer.data.Artist;
import com.example.mediaplayer.interfaces.RecycleViewListener;
import com.example.mediaplayer.interfaces.StorageObserver;
import com.example.mediaplayer.utilities.StorageUtils;

/**
 * Created by Daria Popova on 29.10.17.
 */

public class ArtistFragment extends Fragment implements StorageObserver, RecycleViewListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ArtistListAdapter adapter;

    public static ArtistFragment newInstance() {
        Bundle args = new Bundle();
        ArtistFragment fragment = new ArtistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artist_fragment, container, false);
        initViews(view);
        addObserver(this);
        return view;
    }

    private void initViews(View view) {
        fab = (FloatingActionButton) view.findViewById(R.id.artist_fragment_fab);
        recyclerView = (RecyclerView) view.findViewById(R.id.artist_fragment_recycle_view);

        adapter = new ArtistListAdapter(StorageUtils.getArtists(getContext()), getContext(), this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void update() {
        adapter.notifyAdapterDataSetChanged();
    }

    @Override
    public void recyclerViewItemClicked(View view, int position) {
        Artist artist = StorageUtils.getArtists(getContext()).get(position);
//        Intent intent = new Intent()
    }
}
