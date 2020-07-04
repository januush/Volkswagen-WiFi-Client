package com.example.kfgclient.ui.main;


import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kfgclient.R;

public class ControlFragment extends Fragment {
	private static final String ARG_SECTION_NUMBER = "section_number";
	private MonitoringViewModel controlViewModel;
	private Button testButton;
	private Activity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controlViewModel = ViewModelProviders.of(this).get(MonitoringViewModel.class);
		activity = this.getActivity();
	}

	@Override
	public View onCreateView(
			@NonNull LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_control, container, false);
		testButton = (Button) root.findViewById(R.id.testBtn);
		testButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				controlViewModel.callFunction("vWN_Function_Call_Value_01",1,true);
			}
		});
		return root;
	}
}