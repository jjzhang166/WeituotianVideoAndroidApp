package com.weituotian.video.fragment;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.weituotian.video.R;
import com.weituotian.video.utils.UIUtil;
import com.weituotian.video.widget.LoadingView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author laohu
 * @site http://ittiger.cn
 */
public abstract class BaseFragment<CV extends View, M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
        extends MvpLceFragment<CV, M, V, P> implements LifecycleProvider<FragmentEvent> {

    protected final static String TAG = "BaseFragment";

    protected Context mContext;

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_base, container, false);

        view.addView(getContentView(inflater, savedInstanceState));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        showLoading(false);
        if(isInitRefreshEnable() && isDelayRefreshEnable() == false) {
            Log.d(TAG, "onViewCreated->loadData");
            loadData(false);
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isInitRefreshEnable() == false && isDelayRefreshEnable()) {
            refreshData(false);
        }
    }

    private void refreshData(final boolean pullToRefresh) {

        if(presenter != null) {
            Log.d(TAG, "refreshData->loadData");

            loadData(pullToRefresh);
        } else {

            Observable.timer(50, TimeUnit.MILLISECONDS)//50ms后执行
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            Log.d(TAG, "Observable subscribe call->loadData");
                            refreshData(pullToRefresh);
                        }
                    });
        }
    }

    /**
     * Fragment数据视图
     * @param inflater
     * @param savedInstanceState
     * @return
     */
    public abstract View getContentView(LayoutInflater inflater, @Nullable Bundle savedInstanceState);

    public boolean isInitRefreshEnable() {

        return true;
    }

    public boolean isDelayRefreshEnable() {

        return false;
    }

    public abstract int getName();

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return getActivity().getString(R.string.load_failed);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {

        super.showLoading(pullToRefresh);
        if(!pullToRefresh) {
            ((LoadingView)loadingView).start();
        }
    }

    @Override
    public void showContent() {

        super.showContent();
        ((LoadingView)loadingView).stop();
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {

        super.showError(e, pullToRefresh);
        if(!pullToRefresh) {
            ((LoadingView)loadingView).stop();
        }
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        presenter = null;
    }
}
