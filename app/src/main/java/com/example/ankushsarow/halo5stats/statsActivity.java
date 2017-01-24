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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Intent intent = getIntent();
        userGT = intent.getStringExtra(USER_TAG);
        userSR = intent.getStringExtra(USER_SR);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(1);


        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    //Handle the refresh icon being pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                int tabPosition = tabLayout.getSelectedTabPosition();
                viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
                viewPager.setCurrentItem(tabPosition);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        private final int COUNT = 2;
        private final String[] tabTitles = {"Arena", "Warzone"};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        //Retrieve the fragment based on the position passed in (0 - arena, 1 -
        //warzone
        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString(USER_TAG, userGT);
            bundle.putString(USER_SR, userSR);

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
