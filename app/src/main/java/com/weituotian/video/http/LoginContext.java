package com.weituotian.video.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.weituotian.video.VideoApp;
import com.weituotian.video.entity.User;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.http.service.BiliApi;
import com.weituotian.video.utils.SaveObjectUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by ange on 2017/3/16.
 */

public class LoginContext {

    /**
     * 保存当前登录用户
     */
    public static User user;

    public static boolean isLogin() {
        return user != null;
    }

    public static void loadUser(Context context) {
        /*SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString("name", "wujaycode");
        editor.putInt("age", 4);
        editor.commit();//提交修改*/

        SaveObjectUtils saveObjectUtils = new SaveObjectUtils(context, "user");
        user = saveObjectUtils.getObject("user", User.class);
    }

    public static void saveUser(User userx){
        SaveObjectUtils saveObjectUtils = new SaveObjectUtils(VideoApp.getInstance(), "user");
        saveObjectUtils.setObject("user", userx);

        user = userx;
    }
}
