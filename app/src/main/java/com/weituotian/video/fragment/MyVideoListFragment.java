package com.weituotian.video.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weituotian.video.R;
import com.weituotian.video.VideoListAdapter;
import com.weituotian.video.activity.MemberInfoDetailsActivity;
import com.weituotian.video.activity.VideoDetailsActivity;
import com.weituotian.video.adapter.helper.EndlessRecyclerOnScrollListener;
import com.weituotian.video.entity.VideoListVo;
import com.weituotian.video.mvpview.IMyVideoListView;
import com.weituotian.video.presenter.MyVideoListPresenter;
import com.weituotian.video.widget.recycler.CommonRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ange on 2017/3/28.
 */

public class MyVideoListFragment extends BaseMvpLceFragment<SwipeRefreshLayout, List<VideoListVo>, IMyVideoListView, MyVideoListPresenter> implements IMyVideoListView {

    @BindView(R.id.recyclerView)
    CommonRecyclerView mRecyclerView;

    private View loadMoreView;

    private VideoListAdapter mAdapter;

    private boolean viewCreated = false;
    private boolean firstLoad = false;

    private final static String TAG = "MyVideoListFragment";
    public static final String PARTITION_KEY = "PARTITION";

    public static MyVideoListFragment newInstance(int partitionId) {
        Bundle arguments = new Bundle();
        arguments.putString(PARTITION_KEY, String.valueOf(partitionId));
        MyVideoListFragment myVideoListFragment = new MyVideoListFragment();
        myVideoListFragment.setArguments(arguments);
        return myVideoListFragment;
    }

    @NonNull
    @Override
    public MyVideoListPresenter createPresenter() {
        return new MyVideoListPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_my_video_list, container, false);
        ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);

        // Setup contentView == SwipeRefreshView
        contentView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }
        });

        initRecycleView();
        viewCreated = true;
        checkFirstLoad();
    }

    private void initRecycleView() {
        // Setup recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

//        mRecyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.d_10)));
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int i) {
                presenter.pagePlus();
                loadDataMore();
            }
        });
        /*mRecyclerView.setOnLoadMoreListener(new CommonRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });*/

        //点击一项打开视频
        mRecyclerView.setOnItemClickListener(new CommonRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View itemView) {
                Integer videoId = mAdapter.getItem(position).getId();
                VideoDetailsActivity.launch(getActivity(), videoId);
            }
        });

        //adapter
        mAdapter = new VideoListAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        createLoadMoreView();
    }

    private void createLoadMoreView() {
        loadMoreView = LayoutInflater.from(getActivity()).inflate(R.layout.item_footer, mRecyclerView, false);
        mAdapter.enableFooterView();
        mAdapter.addFooterView(loadMoreView);
        loadMoreView.setVisibility(View.GONE);
    }

    private void initPartition() {
        Bundle args = getArguments();
        if (args != null) {
            int partitionId = Integer.valueOf(args.getString(PARTITION_KEY));
            presenter.setParititionId(partitionId);
        }
    }

    private void checkFirstLoad() {
        if (viewCreated && !firstLoad && getUserVisibleHint()) {//如果当前framgent可见,并且还没有第一次加载数据
            initPartition();
            loadDataMore();
            firstLoad = true;
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        checkFirstLoad();
    }


    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return "加载错误啦";
    }

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
        contentView.setRefreshing(false);
    }

    /**
     * pull to refresh 的时候调用
     *
     * @param data
     */
    @Override
    public void setData(List<VideoListVo> data) {
        mAdapter.reset(data);
        mAdapter.notifyDataSetChanged();
        finishLoad(data.size());
    }

    /**
     * 第一次或者下拉的时候加载
     *
     * @param vos
     */
    @Override
    public void onLoadMoreData(List<VideoListVo> vos) {
        mAdapter.addAll(vos);
        mAdapter.notifyDataSetChanged();
        finishLoad(vos.size());
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadVideos(pullToRefresh);
    }

    public void loadDataMore() {
        loadMoreView.setVisibility(View.VISIBLE);
        loadData(false);
    }
}
