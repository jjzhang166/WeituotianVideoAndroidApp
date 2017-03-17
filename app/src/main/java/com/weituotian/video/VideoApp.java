package com.weituotian.video;

import android.app.Application;

import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.http.LoginContext;

/**
 * Created by ange on 2017/3/17.
 */

public class VideoApp extends Application {

    public static VideoApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    private void init() {

        RetrofitFactory.initCookieJar(getApplicationContext());
    }

    public static VideoApp getInstance() {
        return instance;
    }

}
