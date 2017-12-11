package com.demo.adapter.model.tab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return TabSubFragment.newInstance(position);
        } else if (position == 1) {
            return TabSubFragment.newInstance(position);
        } else {
            return TabSubFragment.newInstance(position);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return position == 0 ? "TAB1" : (position == 1 ? "TAB2" : "TAB3");
    }
}
