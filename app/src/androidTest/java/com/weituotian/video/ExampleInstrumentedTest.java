package com.weituotian.video;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.weituotian.video.entity.RetInfo;
import com.weituotian.video.entity.User;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.presenter.LoginPresenter;

import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.weituotian.weituotianvideo", appContext.getPackageName());
    }

    @Test
    public void testCookies() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        RetrofitFactory.initCookieJar(appContext);

        String username = "weituotian";
        String password = "1q2w3e4r";

        Observable<Result<RetInfo<User>>> resultObservable = RetrofitFactory.getUserService().doLogin(username, password);
        LoginPresenter basePresenter = new LoginPresenter();
        basePresenter.doLogin(username, password);
    }
}
