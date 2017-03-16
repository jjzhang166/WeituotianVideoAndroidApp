package com.weituotian.video.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.weituotian.video.R;
import com.weituotian.video.widget.LoadingView;

/**
 * @author laohu
 * @site http://ittiger.cn
 */
public abstract class BaseFragment<CV extends View, M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
        extends MvpLceFragment<CV, M, V, P> {

    protected final static String TAG = "BaseFragment";

    protected Context mContext;

    protected View mContentView;

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //防止Fragment多次切换时调用onCreateView重新加载View
        if (null == mContentView) {
            ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_base, container, false);

            view.addView(getContentView(inflater, savedInstanceState));

            mContentView = view;
        }
        else
        {
            /**
             * 缓存的rootView需要判断是否已经被加过parent，
             * 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
             */
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null)
            {
                parent.removeView(mContentView);
            }
        }

        return mContentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");

        super.onViewCreated(view, savedInstanceState);

        initView();

        showLoading(false);
        if(isInitRefreshEnable() && !isDelayRefreshEnable()) {
            Log.d(TAG, "onViewCreated->loadData");
            loadData(false);
        }
    }

    protected abstract void initView();

    /*private void refreshData(final boolean pullToRefresh) {


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
    }*/

    /**
     * Fragment数据视图
     * @param inflater
     * @param savedInstanceState
     * @return
     */
    public abstract View getContentView(LayoutInflater inflater, @Nullable Bundle savedInstanceState);

    public boolean isInitRefreshEnable() {//刚刚创建fragment就加载数据

        return false;
    }

    public boolean isDelayRefreshEnable() {//fragment可见的时候才加载数据
        return true;
    }

    public abstract String getName();

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
