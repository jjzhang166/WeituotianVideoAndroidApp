package com.weituotian.video.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weituotian.video.R;
import com.weituotian.video.adapter.MainPagerAdapter;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.http.LoginContext;
import com.weituotian.video.mvpview.IMainView;
import com.weituotian.video.presenter.MainPresenter;
import com.weituotian.video.presenter.func1.ResultToEntityFunc1;
import com.weituotian.video.utils.UIUtil;
import com.weituotian.video.widget.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseMvpActivity<IMainView, MainPresenter> implements IMainView {

    private final int REQUEST_CODE_LOGIN = 1;
    private final int REQUEST_CODE_BROSWER = 1;

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

    @BindView(R.id.dl_main)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.tv_username)
    TextView tvUsername;

    @BindView(R.id.nv_main)
    NavigationView mNavigationView;


    private MainPagerAdapter contentAdapter;

    private static final String TAG = "MainActivity";

    private long exitTime;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        Log.i(TAG, "initAllMembersView初始化所有子view");

        initDrawer();
        initContent();//初始化内容页
        initTab();//初始化选项卡
        initToolBar();//初始化toolbar
        initNavigation();//测试化侧边菜单
        initLogin();

        //butter knife绑定flotingactionbutton的onclick无效,这里手动注册
        mFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPosition = mViewPager.getCurrentItem();
                contentAdapter.refreshData(curPosition);
            }
        });
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    private void initDrawer() {
//        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main);
//        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
//        mDrawerLayout.addDrawerListener(drawerToggle);
//        this.mMenu = (LinearLayout) findViewById(R.id.ll_menu);

        //设置侧边栏的宽度
        DrawerLayout.LayoutParams layoutParams = null;
        if (mNavigationView != null) {
            layoutParams = (DrawerLayout.LayoutParams) mNavigationView.getLayoutParams();
            layoutParams.width = getScreenSize()[0] / 4 * 3;
        }

    }

    public void initToolBar() {
        //toolbar
        if (mToolbar != null) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
        }
    }

    private void initTab() {
        mTabs.setTabMode(TabLayout.MODE_FIXED);
        mTabs.setTabTextColors(ContextCompat.getColor(this, R.color.colorAccent), ContextCompat.getColor(this, R.color.white));//颜色
        mTabs.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
        ViewCompat.setElevation(mTabs, 10);
        mTabs.setupWithViewPager(mViewPager);
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

    private void initNavigation() {

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_upload:

                        break;
                    case R.id.nav_home:
                        MemberInfoDetailsActivity.launch(MainActivity.this, LoginContext.user.getId());
                    case R.id.nav_admin:
                        //打开我的后台
                        Intent in = new Intent(MainActivity.this, BrowserActivity.class);
                        in.putExtra("type", BrowserActivity.TYPE_SYSTEM);
                        startActivityForResult(in, REQUEST_CODE_BROSWER);
                        break;
                    case R.id.nav_logout:
                        RetrofitFactory.getUserService().logout()
                                .flatMap(new ResultToEntityFunc1<String>())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<String>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        UIUtil.showToast(MainActivity.this, e.getMessage());
                                    }

                                    @Override
                                    public void onNext(String str) {
                                        UIUtil.showToast(MainActivity.this, str);
                                        LoginContext.logout();
                                    }
                                });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
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
     * DrawerLayout侧滑菜单开关
     */
    @OnClick({R.id.toolbar_user_avatar, R.id.tv_username})
    public void toggleDrawer() {
        if (LoginContext.isLogin()) {
            //已登录
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        } else {
            //未登录
            Intent in = new Intent(MainActivity.this, LoginActivity.class);
            in.putExtra("a", "a");
            startActivityForResult(in, REQUEST_CODE_LOGIN);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_upload:
                startActivity(new Intent(MainActivity.this, UploadActivity.class));
                break;
            case R.id.menu_search:
                MemberInfoDetailsActivity.launch(this,1);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
        tvUsername.setText(LoginContext.user.getName());

        //navigation view的设置
        View headerView = mNavigationView.getHeaderView(0);
        TextView mUserName = (TextView) headerView.findViewById(R.id.user_name);
        ImageView mUserAvatar = (ImageView) headerView.findViewById(R.id.user_pic);
        mUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberInfoDetailsActivity.launch(MainActivity.this, LoginContext.user.getId());
            }
        });
    }

    public void setNoLogin(String msg) {
        UIUtil.showToast(this, msg);
        tvUsername.setText("未登录");
    }

    @Override
    public void onLoginError(Throwable e) {
        UIUtil.showToast(this, e.getMessage());
    }

    /**
     * floating button,点击刷新viewpager的fragment的数据
     */
    /*@OnClick(R.id.fab_refresh)
    public void refreshFragmentData() {

    }*/


}
