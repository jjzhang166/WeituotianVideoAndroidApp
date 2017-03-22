package com.weituotian.video.presenter.base;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.weituotian.video.entity.RetInfo;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ange on 2017/3/17.
 */

public class BasePresenter<V extends MvpView> extends BaseLifecyclePresenter<V> {

}
