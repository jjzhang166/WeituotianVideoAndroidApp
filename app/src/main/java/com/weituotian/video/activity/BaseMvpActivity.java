package com.weituotian.video.activity;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.weituotian.video.mvpview.ILoginView;
import com.weituotian.video.presenter.LoginPresenter;

/**
 * Created by ange on 2017/3/16.
 */

public abstract class BaseMvpActivity extends MvpActivity<ILoginView, LoginPresenter> implements ILoginView{

}
