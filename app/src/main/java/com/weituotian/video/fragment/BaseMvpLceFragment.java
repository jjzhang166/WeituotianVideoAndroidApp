package com.weituotian.video.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import static com.weituotian.video.fragment.MyVideoListFragment.PARTITION_KEY;

/**
 * Created by ange on 2017/3/28.
 */

public abstract class BaseMvpLceFragment<CV extends View, M, V extends MvpLceView<M>, P extends MvpPresenter<V>> extends MvpLceFragment<CV, M, V, P> {

/*    //标志位,是否第一次加载
    private boolean firstLoad = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && firstLoad) {
            onFirstVisable();//只在第一次加载的时候执行
            firstLoad = false;
        }
    }

    *//**
     * 子类可以覆盖该方法,表示第一次可见的时候
     *//*
    protected void onFirstVisable() {

    }*/

}
