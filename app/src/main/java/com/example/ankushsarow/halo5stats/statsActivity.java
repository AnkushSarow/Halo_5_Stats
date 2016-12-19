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

/**
 * This class will represent the StatsActivity for the application. This Activity will consist
 * of the main toolbar and a tabbed layout with 3 tabs (overview, arena, and warzone). Each
 * tab will display different stats. This will be done by using fragments.
 */
public class StatsActivity extends AppCompatActivity {
    private String userGT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Intent intent = getIntent();
        userGT = intent.getStringExtra("USER_GT");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        //number of tabs (3)
        private final int COUNT = 3;
        private final String[] tabTitles = {"Overview", "Arena", "Warzone"};

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
            switch (position) {
                case 0:
                    return new OverviewFragment();
                case 1:
                    return new ArenaFragment();
                case 2:
                    return new WarzoneFragment();
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
