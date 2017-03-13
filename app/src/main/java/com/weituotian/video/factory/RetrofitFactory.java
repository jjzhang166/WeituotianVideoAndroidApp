package com.weituotian.video.factory;

import com.weituotian.video.http.service.BiliApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author laohu
 * @site http://ittiger.cn
 */
public class RetrofitFactory {

    private static final int TIME_OUT = 12;//超时时间
    private static final String BILI_BASE_URL = "http://www.bilibili.com/";

    private static volatile BiliApi sBiliApi;

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

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BILI_BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(BiliApi.class);
    }

}
