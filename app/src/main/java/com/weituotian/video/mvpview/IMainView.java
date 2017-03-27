package com.weituotian.video.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by ange on 2017/3/19.
 */

public interface IMainView extends MvpView {

    void setLogin();

    void setNoLogin(String msg);

    void onLoginError(Throwable e);

    void onLogoutSuccess();

    void onLogoutError(Throwable e);
}
