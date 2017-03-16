package com.weituotian.video.factory;

import com.weituotian.video.http.service.BiliApi;
import com.weituotian.video.http.service.IUserService;

import java.util.concurrent.TimeUnit;

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

    private static RxJavaCallAdapterFactory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    private static GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

    private static OkHttpClient.Builder getOkhttpBuilder() {
        return new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY));
    }

    private static Retrofit.Builder getRetrofitBuilder() {
        return new Retrofit.Builder()
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory);
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
                .baseUrl("http://localhost:8080/webx/")
                .client(client)
                .build();
        return retrofit.create(IUserService.class);
    }
}
