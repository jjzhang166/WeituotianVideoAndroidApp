package com.weituotian.video.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.weituotian.video.R;
import com.weituotian.video.adapter.VideoAdapter;
import com.weituotian.video.entity.BiliDingVideo;
import com.weituotian.video.mvpview.IVideoView;
import com.weituotian.video.presenter.VideoPresenter;
import com.weituotian.video.utils.UIUtil;
import com.weituotian.video.widget.recycler.CommonRecyclerView;
import com.weituotian.video.widget.recycler.SpacesItemDecoration;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * @author laohu
 * @site http://ittiger.cn
 */
public abstract class VideoFragment extends
        BaseFragment<SwipeRefreshLayout, List<BiliDingVideo.VideoBean>, IVideoView, VideoPresenter>
        implements IVideoView, CommonRecyclerView.LoadMoreListener {

    @BindView(R.id.contentView)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.rv_videolist)
    CommonRecyclerView mRecyclerView;

    private View mFooterView;//显示加载中的view
    private VideoAdapter mVideoAdapter;
    private boolean mIsFirstLoad = true;

    @Override
    public View getContentView(LayoutInflater inflater, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab_list, null);
        ButterKnife.bind(this, view);

        //上拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh->loadData");
                UIUtil.showToast(mContext, "开始加载数据");
                loadData(true);
            }
        });

        initRecycleView();

        viewCreated = true;
        return view;
    }

    private void initRecycleView() {
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.d_10)));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setOnLoadMoreListener(this);

        //adapter
        mVideoAdapter = new VideoAdapter(mContext);
        mVideoAdapter.enableFooterView();
        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.item_footer, mRecyclerView, false);
        mVideoAdapter.addFooterView(mFooterView);
        mRecyclerView.setAdapter(mVideoAdapter);


        //暂时无用
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //mRecyclerView.getFirstVisiblePosition()
                //mRecyclerView.getLastVisiblePosition()

                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /**
     * 当fragment可见和不可见的时候会遇到
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        /*if(isVisibleToUser && !isInitRefreshEnable() && isDelayRefreshEnable()) {
            refreshData(false);
        }*/
        if (getUserVisibleHint() && firstLoad) {
            lazyLoadData(true);//只在第一次加载的时候执行
            firstLoad = false;
        }
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        showLoading(pullToRefresh);
        presenter.refreshData(pullToRefresh);
        if (mIsFirstLoad) {
            mIsFirstLoad = false;
        }
    }

    //标志位,是否第一次加载
    private boolean firstLoad = true;
    //标志位,是否view都创建好了
    private boolean viewCreated = false;

    public void lazyLoadData(final boolean pullToRefresh) {
        if (viewCreated) {
            loadData(pullToRefresh);
        }else{
            Observable.timer(50, TimeUnit.MILLISECONDS)//50ms后执行
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            Log.d(TAG, "Observable subscribe call->loadData");
                            lazyLoadData(pullToRefresh);
                        }
                    });
        }
    }

    @Override
    public void showLoadMoreErrorView() {

        if (mFooterView.getVisibility() == View.VISIBLE) {
            mFooterView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoadMoreView() {

        if (mFooterView.getVisibility() == View.GONE) {
            mFooterView.setVisibility(View.VISIBLE);
        }
//        UIUtil.showToast(mContext, mContext.getString(R.string.loading));
    }

    @Override
    public void setData(List<BiliDingVideo.VideoBean> data) {
        mVideoAdapter.reset(data);
//            mVideoAdapter.addAll(data, 0);//从第0个开始增加

    }

    @Override
    public void setLoadMoreData(List<BiliDingVideo.VideoBean> videos) {

        mVideoAdapter.addAll(videos);//从最后面开始增加
    }

    /**
     * swipeRecycleView 下拉刷新的时候
     */
    @Override
    public void onLoadMore() {
        showLoadMoreView();
        Log.d(TAG, "onLoadMore->loadData");
        presenter.loadMoreData();
    }

    @Override
    public void showLoading(boolean pullToRefresh) {

        super.showLoading(pullToRefresh);
        if (pullToRefresh) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void showContent() {

        super.showContent();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {

        super.showError(e, pullToRefresh);
        if (pullToRefresh) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        mVideoAdapter = null;
        mIsFirstLoad = true;
    }
}
