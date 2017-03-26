package com.weituotian.video.fragment;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by ange on 2017/3/26.
 */

public abstract class BaseMvpFragment<V extends MvpView,P extends MvpPresenter<V>> extends MvpFragment<V,P>{


    //标志位,是否第一次加载
    private boolean firstLoad = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && firstLoad) {
            onFirstVisable();//只在第一次加载的时候执行
            firstLoad = false;
        }
    }

    /**
     * 子类可以覆盖该方法,表示第一次可见的时候
     */
    protected void onFirstVisable(){

    }

}
