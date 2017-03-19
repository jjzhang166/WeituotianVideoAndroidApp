package com.weituotian.video.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.weituotian.video.mvpview.ILoginView;
import com.weituotian.video.presenter.LoginPresenter;

import butterknife.ButterKnife;

/**
 * Created by ange on 2017/3/16.
 */

public abstract class BaseMvpActivity<V extends MvpView,P extends MvpPresenter<V>> extends MvpActivity<V, P>{

    public abstract int getContentViewId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        initAllMembersView(savedInstanceState);
    }

    protected abstract void initAllMembersView(Bundle savedInstanceState);

    protected int[] getScreenSize() {
        int screenSize[] = new int[2];
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenSize[0] = displayMetrics.widthPixels;
        screenSize[1] = displayMetrics.heightPixels;
        return screenSize;
    }

}
