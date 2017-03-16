package com.weituotian.video.factory;

import android.util.Log;

import com.weituotian.video.entity.RetInfo;
import com.weituotian.video.entity.User;
import com.weituotian.video.mvpview.ILoginView;
import com.weituotian.video.presenter.BasePresenter;
import com.weituotian.video.presenter.LoginPresenter;

import org.junit.Before;
import org.junit.Test;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import static org.junit.Assert.*;

/**
 * Created by ange on 2017/3/16.
 */
public class RetrofitFactoryTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testLogin() {
        String username = "weituotian";
        String password = "1q2w3e4r";

        Observable<Result<RetInfo<User>>> resultObservable = RetrofitFactory.getUserService().doLogin(username, password);
        LoginPresenter basePresenter = new LoginPresenter();
        basePresenter.doLogin(username, password);

    }
}