package com.example.ankushsarow.halo5stats;

import android.os.AsyncTask;
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
    private String userGT;
    private final String USER_TAG = "user tag";

    public OverviewFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userGT = getArguments().getString(USER_TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        return view;
    }
}
