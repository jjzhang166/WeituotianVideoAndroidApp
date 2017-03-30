package com.weituotian.video.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.weituotian.video.GlobalConstant;
import com.weituotian.video.R;
import com.weituotian.video.fragment.HomeFragment;
import com.weituotian.video.http.LoginContext;
import com.weituotian.video.mvpview.IMainView;
import com.weituotian.video.presenter.MainPresenter;
import com.weituotian.video.utils.UIUtil;

import butterknife.BindView;

import static com.weituotian.video.GlobalConstant.REQUEST_CODE_LOGIN;
import static com.weituotian.video.GlobalConstant.REQUEST_CODE_REG;

public class MainActivity extends BaseMvpActivity<IMainView, MainPresenter> implements IMainView {

    @BindView(R.id.dl_main)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nv_main)
    NavigationView mNavigationView;

    private static final String TAG = "MainActivity";

    private long exitTime;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        Log.i(TAG, "initAllMembersView初始化所有子view");

        initDrawer();
        initNavigation();//测试化侧边菜单
        initLogin();
        //初始化Fragment
        initFragments();
    }

    private void initDrawer() {

        //设置侧边栏的宽度
        DrawerLayout.LayoutParams layoutParams = null;
        if (mNavigationView != null) {
            layoutParams = (DrawerLayout.LayoutParams) mNavigationView.getLayoutParams();
            layoutParams.width = getScreenSize()[0] / 4 * 3;
        }

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset > 0.5) {
                    if (!LoginContext.isLogin()) {
                        mDrawerLayout.closeDrawers();
                    }
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void initNavigation() {

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_upload:

                        break;
                    case R.id.nav_home:
                        if (checkLogin()) {
                            MemberInfoDetailsActivity.launch(MainActivity.this, LoginContext.user.getId());
                        }
                        break;
                    case R.id.nav_admin:
                        if (checkLogin()) {
                            //打开我的后台
                            Intent in = new Intent(MainActivity.this, BrowserActivity.class);
                            in.putExtra("type", BrowserActivity.TYPE_SYSTEM);
                            startActivityForResult(in, GlobalConstant.REQUEST_CODE_BROSWER);
                        }

                        break;
                    case R.id.nav_logout:
                        if (checkLogin()) {
                            presenter.logout();
                        }

                        break;
                    default:
                        break;
                }

                item.setCheckable(true);//设置选项可选
                item.setChecked(true);//设置选型被选中
//                mDrawerLayout.closeDrawers();//关闭侧边菜单栏

                //true to display the item as the selected item
                return true;
            }
        });
    }

    private void initLogin() {
        if (LoginContext.hasLoginBefore(this)) {
            presenter.touch();
        }
    }

    private HomeFragment mHomePageFragment;

    /**
     * 初始化Fragments
     */
    private void initFragments() {

        mHomePageFragment = new HomeFragment();

        // 添加显示第一个fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, mHomePageFragment)
                .show(mHomePageFragment).commit();
    }

    private boolean checkLogin() {
        if (!LoginContext.isLogin()) {
            LoginActivity.launch(this);
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_REG:
                break;
            case REQUEST_CODE_LOGIN://登录
                if (LoginContext.isLogin()) {
                    //已登录
                    setLogin();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 监听back键处理DrawerLayout和SearchView
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(mDrawerLayout.getChildAt(1))) {
                mDrawerLayout.closeDrawers();
            } else {
                exitApp();
            }
        }

        return true;
    }

    public void toggleDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public void setActionBarDrawerToggle(Toolbar toolbar) {
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Do whatever you want here
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    /**
     * 双击退出App
     */
    private void exitApp() {

        if (System.currentTimeMillis() - exitTime > 2000) {
            UIUtil.showToast(this, "再按一次退出");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }


    /* 以下实现了IMainView的接口 */

    public void setLogin() {
        UIUtil.showToast(this, "欢迎回来哦");

        mHomePageFragment.setLogin();

        //侧边栏
        //navigation view的设置
        View headerView = mNavigationView.getHeaderView(0);

        //用户名
        TextView mUserName = (TextView) headerView.findViewById(R.id.user_name);
        mUserName.setText(LoginContext.user.getName());
        //头像
        ImageView mUserAvatar2 = (ImageView) headerView.findViewById(R.id.user_pic);
        Glide.with(MainActivity.this)
                .load(LoginContext.user.getAvatar())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.video_default_cover)
                .dontAnimate()
                .into(mUserAvatar2);

        mUserAvatar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberInfoDetailsActivity.launch(MainActivity.this, LoginContext.user.getId());
            }
        });
    }

    public void setNoLogin(String msg) {
        UIUtil.showToast(this, msg);
        mHomePageFragment.setNoLogin();
    }

    @Override
    public void onLoginError(Throwable e) {
        UIUtil.showToast(this, e.getMessage());
        setLogin();//使用缓存的user
    }

    @Override
    public void onLogoutSuccess() {
        LoginContext.logout();
        setNoLogin("退出成功");
    }

    @Override
    public void onLogoutError(Throwable e) {
        UIUtil.showToast(this, e.getMessage());
    }

    /**
     * floating button,点击刷新viewpager的fragment的数据
     */
    /*@OnClick(R.id.fab_refresh)
    public void refreshFragmentData() {

    }*/


}
