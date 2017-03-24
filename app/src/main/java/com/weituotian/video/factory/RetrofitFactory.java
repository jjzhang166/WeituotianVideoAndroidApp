package com.weituotian.video.factory;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.weituotian.video.VideoApp;
import com.weituotian.video.http.service.IBiliService;
import com.weituotian.video.http.service.IUserService;
import com.weituotian.video.http.service.IVideoService;
import com.weituotian.video.utils.CommonUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
    public static final String BASE_SERVER_URL = "http://192.168.1.107:8080/webx/";

    private static volatile IBiliService biliService;
    private static volatile IUserService userService;
    private static volatile IVideoService videoService;

    private static RxJavaCallAdapterFactory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    private static Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private static GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(mGson);


    public static ClearableCookieJar cookieJar;

    /**
     * 初始化okhttp的cookjar
     */
    public static void initCookieJar(SetCookieCache setCookieCache, SharedPrefsCookiePersistor sharedPrefsCookiePersistor) {
        cookieJar = new PersistentCookieJar(setCookieCache, sharedPrefsCookiePersistor);
    }

    /**
     * 云端响应头拦截器，用来配置缓存策略
     */
    public Interceptor getNetWorkInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                if (CommonUtil.isNetworkAvailable(VideoApp.getInstance())) {
                    int maxAge = 1 * 60;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    // 无网络时，设置超时为1周
                    int maxStale = 60 * 60 * 24 * 7;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
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

    public static IBiliService getBiliVideoService() {

        if (biliService == null) {
            synchronized (RetrofitFactory.class) {
                if (biliService == null) {
                    biliService = createBiliService();
                }
            }
        }
        return biliService;
    }

    private static IBiliService createBiliService() {

        OkHttpClient client = getOkhttpBuilder().build();

        Retrofit retrofit = getRetrofitBuilder()
                .baseUrl(BILI_BASE_URL)
                .client(client)
                .build();
        return retrofit.create(IBiliService.class);
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

    private static IVideoService createVideoService() {

        OkHttpClient client = getOkhttpBuilder().build();

        Retrofit retrofit = getRetrofitBuilder()
                .baseUrl("http://192.168.1.107:8080/webx/")
                .client(client)
                .build();
        return retrofit.create(IVideoService.class);
    }

    public static IVideoService getVideoService() {

        if (videoService == null) {
            synchronized (RetrofitFactory.class) {
                if (videoService == null) {
                    videoService = createVideoService();
                }
            }
        }
        return videoService;
    }


}
