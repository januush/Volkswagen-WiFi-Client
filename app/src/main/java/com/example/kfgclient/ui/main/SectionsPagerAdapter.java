package com.example.kfgclient.ui.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kfgclient.R;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
	@StringRes
	private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
	private final Context mContext;

	public SectionsPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position){
			case 0:
				MonitoringFragment monitoringFragment = new MonitoringFragment();
				return monitoringFragment;
			case 1:
				ControlFragment info1 = new ControlFragment();
				return info1;

		}
		return null;
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		return mContext.getResources().getString(TAB_TITLES[position]);
	}

	@Override
	public int getCount() {
		// Show 2 total pages.
		return 2;
	}
}