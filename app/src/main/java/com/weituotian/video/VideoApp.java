package com.weituotian.video;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.weituotian.video.entity.DaoMaster;
import com.weituotian.video.entity.DaoSession;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.http.LoginContext;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.greendao.database.Database;

import java.io.File;

import okhttp3.OkHttpClient;

/**
 * Created by ange on 2017/3/17.
 */

public class VideoApp extends Application {

    public static VideoApp instance;

    //标识打开app后有无和服务器联系,touch,更新用户信息
    public static boolean touched = false;

    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final boolean ENCRYPTED = false;//是否加密

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
        initGreenDao();
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

    private void initGreenDao() {
        File path = new File(Environment.getExternalStorageDirectory(), "weituotian/video-db");
        path.getParentFile().mkdirs();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, path.getAbsolutePath(), null);
        Database db = helper.getWritableDb();

        daoSession = new DaoMaster(db).newSession();

        /*DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();*/
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public static VideoApp getInstance() {
        return instance;
    }

}
