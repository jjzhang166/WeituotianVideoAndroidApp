package com.weituotian.video.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.weituotian.video.R;
import com.weituotian.video.adapter.MainPagerAdapter;
import com.weituotian.video.widget.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar_user_avatar)
    CircleImageView mUserAvatar;

    @BindView(R.id.navigation_layout)
    LinearLayout mNavigationLayout;

    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tl_tabs)
    TabLayout mTabs;

    @BindView(R.id.vp_content)
    ViewPager mViewPager;

    @BindView(R.id.fab_refresh)
    FloatingActionButton mFloatButton;

    @BindView(R.id.ll_menu)
    LinearLayout mMenu;

    @BindView(R.id.dl_main)
    DrawerLayout mRoot;


    private MainPagerAdapter contentAdapter;

    /*private DrawerLayout mRoot;
    private ActionBarDrawerToggle drawerToggle;
    private LinearLayout mMenu;

    private TabLayout mTabs;//TabLayout


    private ViewPager mViewPager;//ViewPager
    private MainPagerAdapter contentAdapter;

    private FloatingActionButton mFloatbutton;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initDrawer();
        initContent();
        initTab();//初始化选项卡
    }

    private void initDrawer() {
//        this.mRoot = (DrawerLayout) findViewById(R.id.dl_main);
//        drawerToggle = new ActionBarDrawerToggle(this, mRoot, R.string.app_name, R.string.app_name);
//        mRoot.addDrawerListener(drawerToggle);
//        this.mMenu = (LinearLayout) findViewById(R.id.ll_menu);

        //设置侧边栏的宽度
        DrawerLayout.LayoutParams layoutParams = null;
        if (mMenu != null) {
            layoutParams = (DrawerLayout.LayoutParams) mMenu.getLayoutParams();
            layoutParams.width = getScreenSize()[0] / 4 * 3;
        }

    }

    private void initContent() {

//        this.mTabs = (TabLayout) findViewById(R.id.tl_tabs);
//        this.mViewPager = (ViewPager) findViewById(R.id.vp_content);


        //adapter
        contentAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(contentAdapter);

        //设置页面缓存数量
        mViewPager.setOffscreenPageLimit(3);

        //设置当前fragment
        mViewPager.setCurrentItem(1);

        //floating button,点击刷新viewpager的fragment的数据
//        mFloatButton = (FloatingActionButton) findViewById(R.id.fab_refresh);
        mFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initTab() {
        mTabs.setTabMode(TabLayout.MODE_FIXED);
        mTabs.setTabTextColors(ContextCompat.getColor(this, R.color.colorAccent), ContextCompat.getColor(this, R.color.white));//颜色
        mTabs.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
        ViewCompat.setElevation(mTabs, 10);
        mTabs.setupWithViewPager(mViewPager);
    }

    /**
     * DrawerLayout侧滑菜单开关
     */
    @OnClick(R.id.toolbar_user_avatar)
    public void toggleDrawer() {
        if (mRoot.isDrawerOpen(GravityCompat.START)) {
            mRoot.closeDrawer(GravityCompat.START);
        } else {
            mRoot.openDrawer(GravityCompat.START);
        }
    }

    /**
     * floating button,点击刷新viewpager的fragment的数据
     */
    @OnClick(R.id.toolbar_user_avatar)
    public void refreshFragmentData(){
        int curPosition = mViewPager.getCurrentItem();
        contentAdapter.refreshData(curPosition);
    }

}
