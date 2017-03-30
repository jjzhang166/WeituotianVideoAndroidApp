package com.weituotian.video.http;

import android.content.Context;
import android.util.Log;

import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.weituotian.video.VideoApp;
import com.weituotian.video.entity.RetInfo;
import com.weituotian.video.entity.User;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.presenter.func1.ResultToEntityFunc1;
import com.weituotian.video.presenter.func1.ResultToRetInfoFun1;
import com.weituotian.video.utils.SaveObjectUtils;
import com.weituotian.video.utils.UIUtil;

import java.util.List;

import okhttp3.Cookie;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ange on 2017/3/16.
 */

public class LoginContext {

    //保存sessionID和remember me的cookie的key,
    public final static String SESSION_ID_NAME = "shiro_cookie";
    public final static String REMEMBER_ME_NAME = "shiro_remeberme";

    //sharePreference的用户key
    private static final String SP_USER_KEY = "user";

    /**
     * 保存当前登录用户
     */
    public static User user;

    //管理cookies的缓存
    public static SetCookieCache setCookieCache;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;

    public static void init(Context context) {
        setCookieCache = new SetCookieCache();
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(context);
    }

    public static String getSessionId() {
        for (Cookie next : sharedPrefsCookiePersistor.loadAll()) {
            if (next.name().equals(SESSION_ID_NAME)) {
                return next.value();
            }
        }
        return null;
    }

    public static boolean isLogin() {
        return user != null;
    }

    public static void loadUser(final Context context) {
        /*SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString("name", "wujaycode");
        editor.putInt("age", 4);
        editor.commit();//提交修改*/

        SaveObjectUtils saveObjectUtils = new SaveObjectUtils(context, SP_USER_KEY);
        user = saveObjectUtils.getObject("user", User.class);
        if (user != null) {
            //如果用户上次登录过的
            //
            // 使用touch方法更新用户信息
        }
    }

    public static boolean hasLoginBefore(Context context){
        SaveObjectUtils saveObjectUtils = new SaveObjectUtils(context, SP_USER_KEY);
        user = saveObjectUtils.getObject("user", User.class);
        return user != null;
    }

    public static void saveUser(User userx) {
        SaveObjectUtils saveObjectUtils = new SaveObjectUtils(VideoApp.getInstance(), SP_USER_KEY);
        saveObjectUtils.setObject("user", userx);

        user = userx;
    }

    public static void logout() {
        user = null;
        //清除保存的user
        SaveObjectUtils saveObjectUtils = new SaveObjectUtils(VideoApp.getInstance(), SP_USER_KEY);
        saveObjectUtils.setObject("user", null);
        //清空cookies
        /*setCookieCache.clear();
        sharedPrefsCookiePersistor.clear();*/
    }
}
