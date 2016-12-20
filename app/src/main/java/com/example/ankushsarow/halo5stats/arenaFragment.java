package com.example.ankushsarow.halo5stats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Arena fragment for the arena tab - This fragment will display the user's Arena stats
 */
public class ArenaFragment extends Fragment {
    private String userGT;
    private final String USER_TAG = "user tag";

    public ArenaFragment() {

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
        View view = inflater.inflate(R.layout.fragment_arena, container, false);
        return view;
    }
}
