package com.example.ankushsarow.halo5stats;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * This class will represent the StatsActivity for the application. This Activity will consist
 * of the main toolbar and a tabbed layout with 3 tabs (overview, arena, and warzone). Each
 * tab will display different stats. This will be done by using fragments.
 */
public class StatsActivity extends AppCompatActivity {
    private String userGT;
    private String userSR;
    //Send key for the bundle's message to the fragments
    private final String USER_TAG = "user tag";
    private final String USER_SR = "user sr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Intent intent = getIntent();
        userGT = intent.getStringExtra(USER_TAG);
        userSR = "SR: " + intent.getStringExtra(USER_SR);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TextView gamertag = (TextView) findViewById(R.id.gtText);
        TextView sRank = (TextView) findViewById(R.id.srank);
        gamertag.setText(userGT);
        sRank.setText(userSR);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //getSupportActionBar().setTitle(userGT);
        }

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        //number of tabs (3)
        private final int COUNT = 2;
        private final String[] tabTitles = {"Arena", "Warzone"};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        //Retrieve the fragment based on the position passed in (0 - overview, 1 - arena, 2 -
        //warzone
        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString(USER_TAG, userGT);

            switch (position) {
                case 0:
                    ArenaFragment arenaFragment = new ArenaFragment();
                    arenaFragment.setArguments(bundle);
                    return arenaFragment;
                case 1:
                    WarzoneFragment warzoneFragment = new WarzoneFragment();
                    warzoneFragment.setArguments(bundle);
                    return warzoneFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return COUNT;
        }
    }
}
