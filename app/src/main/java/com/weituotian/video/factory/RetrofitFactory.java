package com.weituotian.video.factory;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.weituotian.video.http.service.BiliApi;
import com.weituotian.video.http.service.IUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author laohu
 * @site http://ittiger.cn
 */
public class RetrofitFactory {

    private static final int TIME_OUT = 12;//超时时间
    private static final String BILI_BASE_URL = "http://www.bilibili.com/";

    private static volatile BiliApi sBiliApi;
    private static volatile IUserService userService;


    private static RxJavaCallAdapterFactory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    private static GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

    //管理cookies的缓存
    private static SetCookieCache setCookieCache;
    private static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private static ClearableCookieJar cookieJar;

    /**
     * 初始化okhttp的cookjar
     * @param context application context
     */
    public static void initCookieJar(Context context) {
        setCookieCache = new SetCookieCache();
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(context);
        cookieJar = new PersistentCookieJar(setCookieCache, sharedPrefsCookiePersistor);
    }

    private static OkHttpClient.Builder getOkhttpBuilder() {

        return new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .cookieJar(cookieJar);//管理cookie
    }

    private static Retrofit.Builder getRetrofitBuilder() {
        return new Retrofit.Builder()
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory);
    }

    /* 以下获得或者新建服务 */

    public static BiliApi getBiliVideoService() {

        if (sBiliApi == null) {
            synchronized (RetrofitFactory.class) {
                if (sBiliApi == null) {
                    sBiliApi = createBiliService();
                }
            }
        }
        return sBiliApi;
    }

    private static BiliApi createBiliService() {

        OkHttpClient client = getOkhttpBuilder().build();

        Retrofit retrofit = getRetrofitBuilder()
                .baseUrl(BILI_BASE_URL)
                .client(client)
                .build();
        return retrofit.create(BiliApi.class);
    }

    public static IUserService getUserService() {

        if (userService == null) {
            synchronized (RetrofitFactory.class) {
                if (userService == null) {
                    userService = createUserService();
                }
            }
        }
        return userService;
    }

    private static IUserService createUserService() {

        OkHttpClient client = getOkhttpBuilder().build();

        Retrofit retrofit = getRetrofitBuilder()
                .baseUrl("http://192.168.1.107:8080/webx/")
                .client(client)
                .build();
        return retrofit.create(IUserService.class);
    }
}
