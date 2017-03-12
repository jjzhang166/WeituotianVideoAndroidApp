package com.weituotian.video.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.LinearLayout;

import com.weituotian.video.R;
import com.weituotian.video.fragment.TabListFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private DrawerLayout mRoot;
    private ActionBarDrawerToggle drawerToggle;
    private LinearLayout mMenu;

    private TabLayout mTabs;
    private List<String> tabIndicators;

    private ViewPager mViewPager;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawer();
        initContent();
        initTab();
    }

    private void initDrawer() {
        this.mRoot = (DrawerLayout) findViewById(R.id.dl_main);
        drawerToggle = new ActionBarDrawerToggle(this, mRoot, R.string.app_name, R.string.app_name);
        mRoot.addDrawerListener(drawerToggle);

        this.mMenu = (LinearLayout) findViewById(R.id.ll_menu);
        DrawerLayout.LayoutParams layoutParams = null;
        if (mMenu != null) {
            layoutParams = (DrawerLayout.LayoutParams) mMenu.getLayoutParams();
            layoutParams.width = getScreenSize()[0]/4*3;
        }

    }

    private void initContent() {

        this.mTabs = (TabLayout) findViewById(R.id.tl_tabs);
        this.mViewPager = (ViewPager) findViewById(R.id.vp_content);

        tabIndicators = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tabIndicators.add("Tab " + i);
        }
        tabFragments = new ArrayList<>();
        for (String s : tabIndicators) {
            tabFragments.add(TabListFragment.newInstance(s));
        }
        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(contentAdapter);
    }

    private void initTab(){
        mTabs.setTabMode(TabLayout.MODE_FIXED);
        mTabs.setTabTextColors(ContextCompat.getColor(this, R.color.colorAccent), ContextCompat.getColor(this, R.color.white));//颜色
        mTabs.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
        ViewCompat.setElevation(mTabs, 10);
        mTabs.setupWithViewPager(mViewPager);
    }

    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabIndicators.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators.get(position);
        }

    }
}
