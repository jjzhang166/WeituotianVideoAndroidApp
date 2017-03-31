package com.weituotian.video.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.weituotian.video.R;
import com.weituotian.video.activity.LoginActivity;
import com.weituotian.video.activity.MainActivity;
import com.weituotian.video.activity.MemberInfoDetailsActivity;
import com.weituotian.video.activity.UploadActivity;
import com.weituotian.video.adapter.HomePagerAdapter;
import com.weituotian.video.entity.Partition;
import com.weituotian.video.http.LoginContext;
import com.weituotian.video.mvpview.IHomeView;
import com.weituotian.video.presenter.HomePresenter;
import com.weituotian.video.utils.ColorUtil;
import com.weituotian.video.utils.UIUtil;
import com.weituotian.video.widget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ange on 2017/3/27.
 */

public class HomeFragment extends BaseMvpFragment<IHomeView, HomePresenter> implements IHomeView {

    @BindView(R.id.toolbar_user_avatar)
    CircleImageView mUserAvatar;

    @BindView(R.id.tv_username)
    TextView mTvUsername;

    @BindView(R.id.navigation_layout)
    LinearLayout mNavigationLayout;

    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tl_tabs)
    TabLayout mTabs;

    @BindView(R.id.vp_content)
    ViewPager mViewPager;

    @BindView(R.id.fab_refresh)
    FloatingActionButton mFabRefresh;

    private HomePagerAdapter contentAdapter;

    @NonNull
    @Override
    public HomePresenter createPresenter() {
        return new HomePresenter();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initAllMembersView(View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        initContent();//初始化内容页
        initTab();//初始化选项卡
        initToolBar();//初始化toolbar

        presenter.getPartitions();
    }

    private void initContent() {
        //设置页面缓存数量
        mViewPager.setOffscreenPageLimit(3);

        //设置当前fragment
        mViewPager.setCurrentItem(1);

        //adapter
        contentAdapter = new HomePagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(contentAdapter);

        contentAdapter.addFragment(new HomePagerAdapter.Listener() {
            @Override
            public Fragment onLazyCreate() {
                return BiliFragment.newInstance(4);
            }
        }, "bilibili热门");
    }

    private void initTab() {
        mTabs.setTabMode(TabLayout.MODE_FIXED);
        mTabs.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.colorAccent), ContextCompat.getColor(getActivity(), R.color.white));//颜色
        mTabs.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.white));
        ViewCompat.setElevation(mTabs, 10);
        mTabs.setupWithViewPager(mViewPager);
    }

    public void initToolBar() {
        //toolbar
        if (mToolbar != null) {
            mToolbar.setTitle("");
            MainActivity activity = ((MainActivity) getActivity());
            activity.setSupportActionBar(mToolbar);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);

        //菜单图标着色
        MenuItem menuItem = menu.findItem(R.id.menu_upload);
        MenuItem menuSearch = menu.findItem(R.id.menu_search);
        if (menuItem != null) {
            ColorUtil.tintMenuIcon(getActivity(), menuItem, android.R.color.holo_red_light);
        }
        if (menuSearch != null) {
            ColorUtil.tintMenuIcon(getActivity(), menuSearch, android.R.color.holo_red_light);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_upload:
                if (LoginContext.isLogin()) {
                    startActivity(new Intent(getActivity(), UploadActivity.class));
                } else {
                    LoginActivity.launch(getActivity());
                }
                break;
            case R.id.menu_search:
                MemberInfoDetailsActivity.launch(getActivity(), 1);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * DrawerLayout侧滑菜单开关
     */
    @OnClick({R.id.toolbar_user_avatar, R.id.tv_username})
    public void toggleDrawer() {
        if (LoginContext.isLogin()) {
            //已登录
            ((MainActivity) getActivity()).toggleDrawer();
        } else {
            //未登录
            LoginActivity.launch(getActivity());
        }
    }

    public void setLogin() {
        mTvUsername.setText(LoginContext.user.getName());
        Glide.with(getActivity())
                .load(LoginContext.user.getAvatar())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.video_default_cover)
                .dontAnimate()
                .into(mUserAvatar);
    }

    public void setNoLogin() {
        mTvUsername.setText("未登录");
    }

    /* 以下实现IHomeView的接口 */

    @Override
    public void onLoadPartitions(List<Partition> partitions) {

        for (Partition partition : partitions) {
            final Integer pid = partition.getId();
            contentAdapter.addFragment(new HomePagerAdapter.Listener() {
                @Override
                public BaseMvpLceFragment onLazyCreate() {
                    return MyVideoListFragment.newInstance(pid);
                }
            }, partition.getName());
        }

        //点击floating action button刷新
        mFabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int curPosition = mViewPager.getCurrentItem();
                Fragment fragment = contentAdapter.getItem(curPosition);

                if (fragment instanceof MyVideoListFragment) {
                    ((MyVideoListFragment) fragment).loadData(true);//刷新
                } else if (fragment instanceof BiliFragment) {
                    ((BiliFragment) fragment).loadData(true);
                }

            }
        });
    }

    @Override
    public void onLoadPartitionsError(Throwable e) {
        UIUtil.showToast(getActivity(), e.getMessage());
    }
}
