package com.ericwyn.kindlesharehelper.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作为主Activity的ViewPager适配器
 * Created by ericwyn on 17-5-5.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList=new ArrayList<>();
    private ArrayList<String> titles=new ArrayList<>();
    private int fragmentNum=0;
    public MyPagerAdapter(FragmentManager fm,Fragment... fragments){
        super(fm);
        for (Fragment fragment:fragments){
            fragmentList.add(fragment);
            titles.add("Fragnment"+(fragmentNum++));
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }
    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
