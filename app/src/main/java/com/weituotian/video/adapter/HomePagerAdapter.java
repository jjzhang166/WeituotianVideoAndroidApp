package com.weituotian.video.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.weituotian.video.entity.Partition;
import com.weituotian.video.fragment.BaseFragment;
import com.weituotian.video.fragment.BaseMvpLceFragment;
import com.weituotian.video.fragment.BiliFragment;
import com.weituotian.video.fragment.MyVideoListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017-03-15.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {

    private List<Item> items = new ArrayList<>();

    public void addFragment(Listener listener, String title) {
        items.add(new Item(null, listener, title));
        notifyDataSetChanged();
    }

    public void addFragment(int position, Listener listener, String title) {
        items.add(position, new Item(null, listener, title));
        notifyDataSetChanged();
    }

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {//从0开始算
        Item item = items.get(position);
        Fragment fragment = item.getFragment();
        if (fragment == null) {
            fragment = item.getListener().onLazyCreate();
            item.setFragment(fragment);
        }
        return fragment;
        /*if (fragments[position] == null) {

            Integer partitionId = partitions.get(position).getId();
            fragments[position] = MyVideoListFragment.newInstance(partitionId);

        }
        return fragments[position];*/
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return items.get(position).getTitle();
    }

    public class Item {
        Fragment fragment;
        Listener listener;
        String title;

        public Item(BaseMvpLceFragment fragment, Listener listener, String title) {
            this.fragment = fragment;
            this.listener = listener;
            this.title = title;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        public Listener getListener() {
            return listener;
        }

        public void setListener(Listener listener) {
            this.listener = listener;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public interface Listener {
        Fragment onLazyCreate();
    }
}
