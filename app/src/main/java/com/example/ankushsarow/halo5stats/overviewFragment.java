package com.example.ankushsarow.halo5stats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Overview fragment for the overview tab - This will be the first visual fragment when
 * the stats activity is created - Main overview of stats is displayed
 */
public class OverviewFragment extends Fragment {
    public OverviewFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        System.out.println("HI THERE");
        return view;
    }
}
