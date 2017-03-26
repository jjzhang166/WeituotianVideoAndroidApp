package com.weituotian.video.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.weituotian.video.R;
import com.weituotian.video.adapter.VideoCommentAdapter;
import com.weituotian.video.adapter.helper.EndlessRecyclerOnScrollListener;
import com.weituotian.video.adapter.helper.HeaderViewRecyclerAdapter;
import com.weituotian.video.entity.CommentVo;
import com.weituotian.video.entity.PageInfo;
import com.weituotian.video.mvpview.ICommentView;
import com.weituotian.video.presenter.CommentPresenter;
import com.weituotian.video.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hcc on 16/8/4 21:18
 * 100332338@qq.com
 * <p/>
 * 视频评论界面
 */
public class VideoCommentFragment extends BaseMvpFragment<ICommentView, CommentPresenter> implements ICommentView {

    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;

    @BindView(R.id.comment_text)
    TextView mCommentText;

    @BindView(R.id.send)
    ImageButton mSend;

    private View loadMoreView;
    private TextView headerView;

    private List<CommentVo> comments = new ArrayList<>();
    private HeaderViewRecyclerAdapter mAdapter;

    //评论数据相关
    private int videoId;
    private int pageNum = 1;
    private int pageSize = 15;


    private final static String EXTRA_VIDEO_ID = "extra_video_id";

    private PageInfo<CommentVo> pageInfo;

    private boolean isRresh = false;

    //发送的评论
    private String content;

    private listener listener;

    @NonNull
    @Override
    public CommentPresenter createPresenter() {
        return new CommentPresenter();
    }

    public static VideoCommentFragment newInstance(int videoId) {
        VideoCommentFragment fragment = new VideoCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_VIDEO_ID, videoId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_video_comment, container, false);
        ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        this.videoId = getArguments().getInt(EXTRA_VIDEO_ID);
        finishCreateView();
    }

    public void finishCreateView() {
        initRecyclerView();
    }

    protected void initRecyclerView() {

        VideoCommentAdapter mRecyclerAdapter = new VideoCommentAdapter(mRecyclerView, comments);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mAdapter = new HeaderViewRecyclerAdapter(mRecyclerAdapter);
        mRecyclerView.setAdapter(mAdapter);

        createHeaderView();
        createLoadMoreView();

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int i) {
                pageNum++;
                loadData();
            }
        });
    }

    private void createHeaderView() {
        headerView = new TextView(getActivity());
        headerView.setText("暂时没有评论");
    }

    private void createLoadMoreView() {
        loadMoreView = LayoutInflater.from(getActivity())
                .inflate(R.layout.item_footer, mRecyclerView, false);
        mAdapter.addFooterView(loadMoreView);
        loadMoreView.setVisibility(View.GONE);
    }


    protected void loadData() {
        presenter.getComments(videoId, pageNum, pageSize);
        loadMoreView.setVisibility(View.VISIBLE);
    }

    protected void refreshData() {
        pageNum = 1;
        comments.clear();
        pageInfo = null;
        loadData();
    }

    protected void finishTask() {
        loadMoreView.setVisibility(View.GONE);

        int start = pageNum * pageSize - pageSize - 1;
        if (start > 0) {
            mAdapter.notifyItemRangeChanged(start, pageSize);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }


    //发送按钮
    @OnClick(R.id.send)
    public void onclick() {
        if (pageInfo != null) {
            content = mCommentText.getText().toString().trim();
            presenter.sendComment(videoId, content);
        }
    }

    /* 以下实现view接口 */

    @Override
    public void onGetComments(PageInfo<CommentVo> pageInfo) {
        this.pageInfo = pageInfo;

        if (pageInfo.getTotal() == 0) {
            mAdapter.addHeaderView(headerView);
            ((FrameLayout) mRecyclerView.getParent()).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100, 0));
        } else {
            mAdapter.removeHeadView();
            ((FrameLayout) mRecyclerView.getParent()).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

            //没有更多了
            if (pageInfo.getList().size() < pageSize) {
                loadMoreView.setVisibility(View.GONE);
                mAdapter.removeFootView();
            }
            comments.addAll(pageInfo.getList());
        }

        finishTask();

        if (listener != null) {
            listener.onLoaded(pageInfo);
        }
    }

    @Override
    protected void onFirstVisable() {
        refreshData();
    }

    @Override
    public void onGetCommentsError(Throwable throwable) {
        UIUtil.showToast(getActivity(), throwable.getMessage());
        loadMoreView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSendCommentSuccess() {
        UIUtil.showToast(getActivity(), "发送成功,刷新评论中");
        refreshData();
//        CommentVo commentVo = new CommentVo();
//        commentVo.setContent(content);
//        commentVo.setFloor(pageInfo.getTotal() + 1);
//        commentVo.set
//        comments.add(0);
    }

    @Override
    public void onSendCommentError(Throwable throwable) {
        UIUtil.showToast(getActivity(), throwable.getMessage());
    }

    /* listener */

    public VideoCommentFragment.listener getListener() {
        return listener;
    }

    public void setListener(VideoCommentFragment.listener listener) {
        this.listener = listener;
    }

    public interface listener {
        void onLoaded(PageInfo<CommentVo> pageInfo);
    }
}

