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
import com.weituotian.video.mvpview.VideoMvpView;
import com.weituotian.video.presenter.VideoPresenter;
import com.weituotian.video.utils.UIUtil;
import com.weituotian.video.widget.recycler.CommonRecyclerView;
import com.weituotian.video.widget.recycler.SpacesItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author laohu
 * @site http://ittiger.cn
 */
public abstract class VideoFragment extends
        BaseFragment<SwipeRefreshLayout, List<BiliDingVideo.VideoBean>, VideoMvpView, VideoPresenter>
        implements VideoMvpView, CommonRecyclerView.LoadMoreListener {

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

        mRecyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.d_10)));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setOnLoadMoreListener(this);

        //上拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh->loadData");
                UIUtil.showToast(mContext, "开始加载数据");
                loadData(true);
            }
        });

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
        return view;
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        showLoading(pullToRefresh);
        presenter.refreshData(pullToRefresh);
        if (mIsFirstLoad) {
            mIsFirstLoad = false;
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

        if (mVideoAdapter == null) {
            mVideoAdapter = new VideoAdapter(mContext, data);
            mVideoAdapter.enableFooterView();
            mFooterView = LayoutInflater.from(mContext).inflate(R.layout.item_footer, mRecyclerView, false);
            mVideoAdapter.addFooterView(mFooterView);
            mRecyclerView.setAdapter(mVideoAdapter);
        } else {
            mVideoAdapter.reset(data);
            mVideoAdapter.addAll(data, 0);//从第0个开始增加
        }
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
