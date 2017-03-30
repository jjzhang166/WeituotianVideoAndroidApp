package com.weituotian.video.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * 注册view接口
 * Created by ange on 2017/3/29.
 */

public interface IRegView extends MvpView{

    void showRegFail(Throwable throwable);

    void showRegSuccess();

}
