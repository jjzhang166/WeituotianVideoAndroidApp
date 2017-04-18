package com.weituotian.video.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.weituotian.video.R;
import com.weituotian.video.adapter.VideoListAdapter;
import com.weituotian.video.adapter.helper.EndlessRecyclerOnScrollListener;
import com.weituotian.video.mvpview.ISearchView;
import com.weituotian.video.presenter.SearchPresenter;
import com.weituotian.video.widget.recycler.CommonRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ange on 2017/4/18.
 */

public class SearchActivity extends BaseMvpActivity<ISearchView, SearchPresenter> implements ISearchView {

    public static String EXTRA_QUERY_STRING = "extra_query";
    @BindView(R.id.search_back)
    ImageView mSearchBack;
    @BindView(R.id.search_edit)
    EditText mSearchEdit;
    @BindView(R.id.search_text_clear)
    ImageView mSearchTextClear;
    @BindView(R.id.search_img)
    ImageView mSearchImg;
    @BindView(R.id.search_card_view)
    CardView mSearchCardView;
    @BindView(R.id.recyclerView)
    CommonRecyclerView mRecyclerView;
    @BindView(R.id.contentView)
    SwipeRefreshLayout mContentView;

    private View loadMoreView;
    private TextView headerView;

    private VideoListAdapter mAdapter;

    public static void launch(Activity activity, String str) {
        Intent mIntent = new Intent(activity, SearchActivity.class);
        mIntent.putExtra(EXTRA_QUERY_STRING, str);
        activity.startActivity(mIntent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_search;
    }

    @NonNull
    @Override
    public SearchPresenter createPresenter() {
        return new SearchPresenter();
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        //下拉刷新
        mContentView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        //adapter
        mAdapter = new VideoListAdapter(this);
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
                VideoDetailsActivity.launch(SearchActivity.this, videoId);
            }
        });

        createHeaderView();
        createLoadMoreView();
    }

    private void createHeaderView() {
        headerView = new TextView(this);
        headerView.setText("没有搜索结果哦");
        mAdapter.enableHeaderView();
        mAdapter.addHeaderView(headerView);
        headerView.setVisibility(View.GONE);
    }

    private void createLoadMoreView() {
        loadMoreView = LayoutInflater.from(this).inflate(R.layout.item_footer, mRecyclerView, false);
        mAdapter.enableFooterView();
        mAdapter.addFooterView(loadMoreView);
        loadMoreView.setVisibility(View.GONE);
    }


    /**
     * 加载第一页数据，用于初始化的时候和刷新的时候
     */
    private void loadData() {
//        presenter.getCollects(false);
    }

    /**
     * 加载更多数据
     */
    private void loadMoreData() {
//        presenter.getCollects(true);
        loadMoreView.setVisibility(View.VISIBLE);
    }

    /*以下实现 view 的接口*/



}
