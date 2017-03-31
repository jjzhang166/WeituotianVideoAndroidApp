package com.weituotian.video.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weituotian.video.R;
import com.weituotian.video.adapter.VideoListAdapter;
import com.weituotian.video.activity.MainActivity;
import com.weituotian.video.activity.VideoDetailsActivity;
import com.weituotian.video.adapter.helper.EndlessRecyclerOnScrollListener;
import com.weituotian.video.entity.PageInfo;
import com.weituotian.video.entity.VideoListVo;
import com.weituotian.video.http.LoginContext;
import com.weituotian.video.mvpview.ICollectView;
import com.weituotian.video.presenter.CollectPresenter;
import com.weituotian.video.utils.UIUtil;
import com.weituotian.video.widget.recycler.CommonRecyclerView;

import butterknife.BindView;

/**
 * 收藏
 * Created by ange on 2017/3/31.
 */

public class CollectFragment extends BaseMvpFragment<ICollectView, CollectPresenter> implements ICollectView {

    @BindView(R.id.toolbar_simple)
    Toolbar mToolbar;

    @BindView(R.id.loadingView)
    ProgressBar mLoadingView;

    @BindView(R.id.recyclerView)
    CommonRecyclerView mRecyclerView;

    @BindView(R.id.contentView)
    SwipeRefreshLayout mContentView;


    private View loadMoreView;
    private TextView headerView;

    private VideoListAdapter mAdapter;

    @NonNull
    @Override
    public CollectPresenter createPresenter() {
        return new CollectPresenter();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_collect;
    }

    @Override
    protected void initAllMembersView(View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        finishCreateView();
        Integer uid = LoginContext.user.getId();
        presenter.setUserId(uid);
    }

    private void finishCreateView() {
        //下拉刷新
        mContentView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        initToolBar();
        initRecyclerView();
    }

    public void initToolBar() {
        //toolbar
        if (mToolbar != null) {
            mToolbar.setTitle("我的收藏");
            MainActivity activity = ((MainActivity) getActivity());
            activity.setSupportActionBar(mToolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        //adapter
        mAdapter = new VideoListAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        //加载更多
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int i) {
                loadMoreData();
            }
        });

        //点击一项打开视频
        mRecyclerView.setOnItemClickListener(new CommonRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View itemView) {
                Integer videoId = mAdapter.getItem(position).getId();
                VideoDetailsActivity.launch(getActivity(), videoId);
            }
        });

        createHeaderView();
        createLoadMoreView();
    }

    private void createHeaderView() {
        headerView = new TextView(getActivity());
        headerView.setText("暂时没有评论");
    }

    private void createLoadMoreView() {
        loadMoreView = LayoutInflater.from(getActivity()).inflate(R.layout.item_footer, mRecyclerView, false);
        mAdapter.enableFooterView();
        mAdapter.addFooterView(loadMoreView);
        loadMoreView.setVisibility(View.GONE);
    }

    private void showHeadView() {
        mAdapter.enableHeaderView();
        mAdapter.addHeaderView(headerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onFirstVisable() {
        loadData();
    }

    /**
     * 加载第一页数据，用于初始化的时候和刷新的时候
     */
    private void loadData() {
        presenter.getCollects(false);
        mLoadingView.setVisibility(View.VISIBLE);
    }

    /**
     * 加载更多数据
     */
    private void loadMoreData() {
        presenter.getCollects(true);
        loadMoreView.setVisibility(View.VISIBLE);
    }

    /*以下实现 view 的接口*/

    public void finishLoad(Integer size) {
        if (size < presenter.getSize()) {
            //没有更多了
            TextView textView = (TextView) loadMoreView.findViewById(R.id.footer_text);
            textView.setText("没有更多了");
            loadMoreView.findViewById(R.id.progressBar).setVisibility(View.GONE);
            loadMoreView.setVisibility(View.VISIBLE);
        } else {
            loadMoreView.setVisibility(View.GONE);
        }
        mContentView.setRefreshing(false);
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void onLoadCollects(PageInfo<VideoListVo> pageInfo) {
        if (pageInfo.getSize() <= 0) {
            showHeadView();
        }
        mAdapter.reset(pageInfo.getList());
//        mAdapter.notifyDataSetChanged();
        finishLoad(pageInfo.getList().size());
    }

    @Override
    public void onLoadMoreCollects(PageInfo<VideoListVo> pageInfo) {
        mAdapter.addAll(pageInfo.getList());
        finishLoad(pageInfo.getList().size());
    }

    @Override
    public void onLoadError(Throwable throwable) {
        UIUtil.showToast(getActivity(), throwable.getMessage());
    }
}
