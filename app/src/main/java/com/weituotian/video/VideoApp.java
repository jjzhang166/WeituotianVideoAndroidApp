package com.weituotian.video;

import android.app.Application;

import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.http.LoginContext;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.OkHttpClient;

/**
 * Created by ange on 2017/3/17.
 */

public class VideoApp extends Application {

    public static VideoApp instance;

    //标识打开app后有无和服务器联系,touch,更新用户信息
    public static boolean touched = false;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    private void init() {
        LoginContext.init(getApplicationContext());
        RetrofitFactory.initCookieJar(LoginContext.setCookieCache, LoginContext.sharedPrefsCookiePersistor);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(RetrofitFactory.cookieJar)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public static VideoApp getInstance() {
        return instance;
    }

}
