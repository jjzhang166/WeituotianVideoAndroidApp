package com.weituotian.video.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.weituotian.video.R;

import butterknife.ButterKnife;

/**
 * Created by ange on 2017/3/26.
 */

public abstract class BaseMvpFragment<V extends MvpView, P extends MvpPresenter<V>> extends MvpFragment<V, P> {


    //标志位,是否第一次加载数据
    private boolean firstLoad = false;
    //标志位，是否view创建完毕
    private boolean viewCreated = false;

    public abstract @LayoutRes int getContentViewId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(getContentViewId(), container, false);
        ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCreated = true;
        initAllMembersView(view, savedInstanceState);
        checkFirstLoad();
    }

    protected abstract void initAllMembersView(View view, @Nullable Bundle savedInstanceState);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        checkFirstLoad();
        /*if (getUserVisibleHint() && firstLoad) {
            onFirstVisable();//只在第一次加载的时候执行
            firstLoad = false;
        }*/
    }

    /**
     * 检查
     */
    private void checkFirstLoad() {
        if (viewCreated && !firstLoad && getUserVisibleHint()) {//如果当前framgent可见,并且还没有第一次加载数据
            firstLoad = true;
            onFirstVisable();
        }
    }

    /**
     * 子类可以覆盖该方法,表示第一次可见的时候
     */
    protected void onFirstVisable() {

    }

}
