package com.weituotian.video.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.weituotian.video.R;
import com.weituotian.video.entity.FrontVideo;
import com.weituotian.video.entity.VideoTag;
import com.weituotian.video.utils.NumberUtil;
import com.weituotian.video.widget.CircleImageView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hcc on 16/8/4 21:18
 * 100332338@qq.com
 * <p/>
 * 视频简介界面
 */
public class VideoIntroductionFragment extends Fragment {

    @BindView(R.id.tv_title)
    TextView mTitleText;

    @BindView(R.id.tv_play_time)
    TextView mPlayTimeText;//播放次数

    @BindView(R.id.tv_description)
    TextView mDescText;

    @BindView(R.id.share_num)
    TextView mShareNum;

    @BindView(R.id.fav_num)
    TextView mFavNum;

    @BindView(R.id.tags_layout)
    TagFlowLayout mTagFlowLayout;

    @BindView(R.id.user_avatar)
    CircleImageView mUserAvatar;
    @BindView(R.id.user_name)
    TextView mUserName;

    private final static String EXTRA_FRONT_VIDEO = "extra_front_video";

    private FrontVideo frontVideo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_video_introduction, container, false);
        ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        this.frontVideo = getArguments().getParcelable(EXTRA_FRONT_VIDEO);
        finishCreateView();
    }

    public static VideoIntroductionFragment newInstance(FrontVideo frontVideo) {

        VideoIntroductionFragment fragment = new VideoIntroductionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_FRONT_VIDEO, frontVideo);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void finishCreateView() {

        //设置视频标题
        mTitleText.setText(frontVideo.getTitle());
        //设置视频描述
        mDescText.setText(frontVideo.getDescript());
        //设置视频播放数量
        mPlayTimeText.setText(NumberUtil.converString(frontVideo.getPlay()));

        //设置Up主信息
        Glide.with(VideoIntroductionFragment.this)
                .load(frontVideo.getMemberCover())
                .centerCrop()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ico_user_default)
                .into(mUserAvatar);
        mUserName.setText(frontVideo.getMemberName());

        //设置分享 收藏 投币数量
        mFavNum.setText(NumberUtil.converString(frontVideo.getCollect()));

        //设置视频tags
        List<VideoTag> tags = frontVideo.getTags();
        mTagFlowLayout.setAdapter(new TagAdapter<VideoTag>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, VideoTag videoTag) {
                TextView mTags = (TextView) LayoutInflater.from(getActivity())
                        .inflate(R.layout.tag_video_partitions, parent, false);
                mTags.setText(videoTag.getName());
                return mTags;
            }
        });

        //设置视频相关
        setVideoRelated();
    }

    private void setVideoRelated() {

        /*List<VideoDetailsInfo.DataBean.RelatesBean> relates = mVideoDetailsInfo.getRelates();
        if (relates == null) {
            mVideoRelatedLayout.setVisibility(View.GONE);
            return;
        }
        VideoRelatedAdapter mVideoRelatedAdapter = new VideoRelatedAdapter(mRecyclerView, relates);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        mRecyclerView.setAdapter(mVideoRelatedAdapter);
        mVideoRelatedAdapter.setOnItemClickListener(
                (position, holder) -> VideoDetailsActivity.launch(getActivity(),
                        relates.get(position).getAid(), relates.get(position).getPic()));*/
    }

    @OnClick(R.id.btn_share)
    void share() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");

        intent.putExtra(Intent.EXTRA_TEXT, "来自「韦驮天视频」的分享:" + frontVideo.getDescript());
        startActivity(Intent.createChooser(intent, frontVideo.getTitle()));

    }
}
