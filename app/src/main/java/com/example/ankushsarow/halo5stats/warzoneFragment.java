package com.example.ankushsarow.halo5stats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Warzone fragment for the warzone tab - This fragment will display the user's warzone stats
 */
public class WarzoneFragment extends Fragment {
    public WarzoneFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_warzone, container, false);
        System.out.println("HI THERE #3");
        return view;
    }
}

