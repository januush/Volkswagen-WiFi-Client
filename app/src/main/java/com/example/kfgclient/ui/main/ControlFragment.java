package com.example.kfgclient.ui.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import com.example.kfgclient.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ControlFragment extends Fragment {
    //TODO tutaj muszę mieć ExlapListAdapder, listę

    private static final String ARG_SECTION_NUMBER = "section_number";

    private MonitoringViewModel pageViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(MonitoringViewModel.class);
//        String index = 1;
//        if (getArguments() != null) {
//            index = getArguments().getInt(ARG_SECTION_NUMBER);
//        }
//        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_control, container, false);

        // final TextView textView = root.findViewById(R.id.section_label);

//        pageViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                //   textView.setText(s);
//            }
//        });

        return root;
    }
}