package com.weituotian.video.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.weituotian.video.R;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.http.LoginContext;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Cookie;

/**
 * 显示页面的activity
 * Created by ange on 2017/3/18.
 */

public class BrowserActivity extends AppCompatActivity {

    private final String DEFAULT_URL = "";
    private final String TAG = "BrowserActivity";

    public static final int TYPE_SYSTEM = 2;
    public static final int TYPE_CUSTOM = 3;

    public static final String BASE_SYSTEM_URL = RetrofitFactory.BASE_SERVER_URL + "system/index";

    @BindView(R.id.wv_broswer)
    WebView mBroswer;
    @BindView(R.id.toolbar_simple)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        ButterKnife.bind(this);

        initToolBar();
        initBroswer();
    }

    public void initToolBar() {
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initBroswer() {

        Intent intent = getIntent();
        String url = "";
        int type = intent.getIntExtra("type", TYPE_SYSTEM);

        switch (type) {
            case TYPE_SYSTEM:
                url = BASE_SYSTEM_URL;
                break;
            case TYPE_CUSTOM:
                url = intent.getStringExtra("url");
                break;
            default:
                url = BASE_SYSTEM_URL;
        }

        // enable javascript
        mBroswer.getSettings().setJavaScriptEnabled(true);
        mBroswer.getSettings().setLoadsImagesAutomatically(true);
        mBroswer.getSettings().setBuiltInZoomControls(true);
        mBroswer.setWebViewClient(new WebViewClient());

        //设置cookies保存登录状态
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie();//移除

        for (Cookie cookie : LoginContext.setCookieCache) {
            cookieManager.setCookie(RetrofitFactory.BASE_SERVER_URL, cookie.name() + "=" + cookie.value());
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }

        mBroswer.loadUrl(url);
    }

    /**
     * 处理返回键为返回上一页
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "keyCode=" + keyCode);
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mBroswer.canGoBack()) {
            mBroswer.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.webbroswer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_forward:
                break;
            case R.id.menu_refresh:
                mBroswer.reload();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
