package com.example.mediaplayer.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.mediaplayer.R;
import com.example.mediaplayer.adapters.fragment.TabPagerAdapter;
import com.example.mediaplayer.fragments.ArtistFragment;
import com.example.mediaplayer.fragments.SongFragment;
import com.example.mediaplayer.utilities.StorageUtils;

public class StartActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initViews();
        setViewPagerContent();
    }


    private void setViewPagerContent() {
        StorageUtils.updateData(this);
        tabLayout.setupWithViewPager(viewPager);

        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(SongFragment.newInstance(), getString(R.string.song_tab_title));
        adapter.addFragment(ArtistFragment.newInstance(), getString(R.string.artist_tab_title));

        viewPager.setAdapter(adapter);
    }

    private void initViews() {
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.tab_view_pager);
    }

}
