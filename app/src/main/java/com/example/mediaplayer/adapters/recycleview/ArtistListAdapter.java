package com.example.mediaplayer.adapters.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mediaplayer.R;
import com.example.mediaplayer.data.Artist;
import com.example.mediaplayer.interfaces.RecycleViewListener;
import com.example.mediaplayer.utilities.StorageUtils;

import java.util.List;

/**
 * Created by Daria Popova on 29.10.17.
 */

public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.ArtistViewHolder> {

    private List<Artist> artists;
    private Context mContext;
    private RecycleViewListener recycleViewListener;

    public ArtistListAdapter(List<Artist> artists, Context mContext, RecycleViewListener recycleViewListener) {
        this.artists = artists;
        this.mContext = mContext;
        this.recycleViewListener = recycleViewListener;
    }

    @Override

    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_fragment_recycle_view_instance, parent, false);

        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {
        Artist artist = artists.get(position);

        holder.artistName.setText(artist.getName());
    }

    @Override
    public int getItemCount() {
        return artists == null ? 0 : artists.size();
    }

    public void notifyAdapterDataSetChanged() {
        artists = StorageUtils.getArtists(mContext);
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView artistName;

        public ArtistViewHolder(View view) {
            super(view);

            artistName = (TextView) view.findViewById(R.id.artist_recycle_view_instance_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recycleViewListener.recyclerViewItemClicked(view, getAdapterPosition());
        }
    }
}
