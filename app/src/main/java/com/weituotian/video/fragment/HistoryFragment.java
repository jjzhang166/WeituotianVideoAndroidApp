package com.weituotian.video.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weituotian.video.R;
import com.weituotian.video.VideoApp;
import com.weituotian.video.activity.MainActivity;
import com.weituotian.video.activity.MemberInfoDetailsActivity;
import com.weituotian.video.activity.VideoDetailsActivity;
import com.weituotian.video.adapter.HistoryAdapter;
import com.weituotian.video.adapter.helper.EndlessRecyclerOnScrollListener;
import com.weituotian.video.entity.DaoSession;
import com.weituotian.video.entity.HistoryVideo;
import com.weituotian.video.entity.HistoryVideoDao;
import com.weituotian.video.widget.recycler.CommonRecyclerView;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.rx.RxDao;
import org.greenrobot.greendao.rx.RxQuery;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by ange on 2017/4/2.
 */

public class HistoryFragment extends Fragment {

    @BindView(R.id.toolbar_simple)
    Toolbar mToolbar;

    @BindView(R.id.loadingView)
    ProgressBar mLoadingView;

    @BindView(R.id.recyclerView)
    CommonRecyclerView mRecyclerView;

    @BindView(R.id.contentView)
    SwipeRefreshLayout mContentView;

    private View loadMoreView;

    DaoSession daoSession;

    private HistoryVideoDao historyVideoDao;

    private int page = 1;
    private int pageSize = 10;

    private HistoryAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initAllMembersView();

        // get the DAO
        daoSession = ((VideoApp) getActivity().getApplication()).getDaoSession();
        historyVideoDao = daoSession.getHistoryVideoDao();

        // query all notes, sorted a-z by their text
        //rx用法

        /*historiesQuery = historyVideoDao.queryBuilder()
                .limit(pageSize)
                .orderDesc(HistoryVideoDao.Properties.ViewTime).build();*/

        loadData();
    }

    private void initAllMembersView() {
        setHasOptionsMenu(true);
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
            mToolbar.setTitle("历史记录");
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
        mAdapter = new HistoryAdapter(getActivity());
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
                Long videoId = mAdapter.getItem(position).getId();
                VideoDetailsActivity.launch(getActivity(), videoId.intValue());
            }
        });

        createLoadMoreView();
    }

    private void createLoadMoreView() {
        loadMoreView = LayoutInflater.from(getActivity()).inflate(R.layout.item_footer, mRecyclerView, false);
        mAdapter.enableFooterView();
        mAdapter.addFooterView(loadMoreView);
        loadMoreView.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getVideos(final boolean loadmore) {
        if (loadmore) {
            page++;
        } else {
            page = 1;
        }

        RxQuery<HistoryVideo> rxQuery = historyVideoDao.queryBuilder()
                .offset((page - 1) * pageSize)
                .limit(pageSize)
                .orderDesc(HistoryVideoDao.Properties.ViewTime)
                .rx();

        rxQuery.list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<HistoryVideo>>() {
                    @Override
                    public void call(List<HistoryVideo> notes) {
                        if (loadmore) {
                            onLoadMore(notes);

                        } else {
                            onLoad(notes);
                        }
                    }
                });
    }

    /**
     * 加载第一页数据，用于初始化的时候和刷新的时候
     */
    private void loadData() {
        getVideos(false);
        loadMoreView.setVisibility(View.VISIBLE);
    }

    /**
     * 加载更多数据
     */
    private void loadMoreData() {
        getVideos(true);
        loadMoreView.setVisibility(View.VISIBLE);
    }

    /* 加载完成 */

    public void finishLoad(Integer size) {
        if (size < pageSize) {
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

    public void onLoad(List<HistoryVideo> videos) {
        mAdapter.reset(videos);
        finishLoad(videos.size());
    }

    public void onLoadMore(List<HistoryVideo> notes) {
        mAdapter.addAll(notes);
        finishLoad(notes.size());
    }
}
