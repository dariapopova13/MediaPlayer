package com.example.mediaplayer.adapters.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mediaplayer.R;
import com.example.mediaplayer.data.Song;
import com.example.mediaplayer.interfaces.RecycleViewListener;
import com.example.mediaplayer.utilities.AppUtils;

import java.util.List;

/**
 * Created by Daria Popova on 07.10.17.
 */

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.SongsViewHolder> {

    private Context mContext;
    private List<Song> songs;
    private RecycleViewListener recycleViewListener;

    public SongsListAdapter(Context mContext, List<Song> songs, RecycleViewListener recycleViewListener) {
        this.mContext = mContext;
        this.songs = songs;
        this.recycleViewListener = recycleViewListener;
    }

    @Override
    public SongsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.songs_fragment_recycle_view_instance, parent, false);

        return new SongsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SongsViewHolder holder, int position) {
        Song song = songs.get(position);

        holder.songTitle.setText(song.getTitle());
        holder.songArtist.setText(song.getArtist());
        holder.songDuration.setText(AppUtils.getSongDuration(song.getDuration()));
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupMenu(holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs == null ? 0 : songs.size();
    }


    private void createPopupMenu(SongsViewHolder holder) {
//        PopupMenu popupMenu = new PopupMenu(mContext, holder.menu);
//        popupMenu.inflate(R.menu.item_song_menu);
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                return false;
//            }
//        });
//        popupMenu.show();
    }

    public class SongsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView songDuration;
        public TextView songTitle;
        public TextView songArtist;
        public ImageView songCover;
        public TextView menu;

        public SongsViewHolder(View view) {
            super(view);

            songDuration = (TextView) view.findViewById(R.id.song_duration);
            songTitle = (TextView) view.findViewById(R.id.song_title);
            songArtist = (TextView) view.findViewById(R.id.song_artist);
            songCover = (ImageView) view.findViewById(R.id.song_cover);
            menu = (TextView) view.findViewById(R.id.item_song_menu);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recycleViewListener.recyclerViewItemClicked(view, this.getAdapterPosition());
        }
    }
}
