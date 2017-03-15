package com.weituotian.video.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.weituotian.video.fragment.BiliFragment;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017-03-15.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    private String[] TITLES;
    private Fragment[] fragments;

    /*private List<String> tabIndicators;
    private Set<Fragment> tabFragments;*/

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        TITLES = new String[]{"json1", "json2", "json3"};
        fragments = new Fragment[TITLES.length];
    }

    @Override
    public Fragment getItem(int position) {//从0开始算
        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] = BiliFragment.newInstance(1);
                    break;
                case 1:
                    fragments[position] = BiliFragment.newInstance(2);
                    break;
                case 2:
                    fragments[position] = BiliFragment.newInstance(4);
                    break;
                default:
                    break;
            }
        }
        return fragments[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

}
