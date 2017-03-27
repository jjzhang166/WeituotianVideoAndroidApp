package com.weituotian.video.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.weituotian.video.R;
import com.weituotian.video.entity.FrontVideo;
import com.weituotian.video.entity.VideoTag;
import com.weituotian.video.http.LoginContext;
import com.weituotian.video.mvpview.IVideoDetailView;
import com.weituotian.video.presenter.VideoDetailPresenter;
import com.weituotian.video.utils.NumberUtil;
import com.weituotian.video.utils.UIUtil;
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
public class VideoIntroductionFragment extends BaseMvpFragment<IVideoDetailView, VideoDetailPresenter> implements IVideoDetailView {

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

    @BindView(R.id.collect_image)
    ImageButton mCollectImage;

    @BindView(R.id.collect_text)
    TextView mCollectText;

    @BindView(R.id.star)
    TextView mStar;

    private final static String EXTRA_FRONT_VIDEO = "extra_front_video";

    private FrontVideo frontVideo;

    public static VideoIntroductionFragment newInstance(FrontVideo frontVideo) {

        VideoIntroductionFragment fragment = new VideoIntroductionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_FRONT_VIDEO, frontVideo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public VideoDetailPresenter createPresenter() {
        return new VideoDetailPresenter();
    }

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

        if (LoginContext.isLogin()) {
            //如果是自己的话隐藏关注按钮
            if (frontVideo.getMemberId().equals(LoginContext.user.getId())) {
                mStar.setVisibility(View.GONE);
            }
        }

        finishCreateView();
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

        presenter.checkCollect(frontVideo.getId());
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

    //点击分享
    @OnClick(R.id.btn_share)
    void share() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");

        intent.putExtra(Intent.EXTRA_TEXT, "来自「韦驮天视频」的分享:" + frontVideo.getDescript());
        startActivity(Intent.createChooser(intent, frontVideo.getTitle()));

    }

    //点击收藏
    @OnClick(R.id.btn_collect)
    void collect() {
        Integer videoId = frontVideo.getId();
        if (mCollectText.getText().toString().equals(getResources().getString(R.string.to_ollect))) {
            presenter.collect(videoId);
        } else {
            presenter.cancelCollect(videoId);
        }
    }

    //点击关注
    @OnClick(R.id.star)
    void star() {
        //要关注的人id
        Integer memberId = frontVideo.getMemberId();
        if (mStar.getText().toString().equals(getResources().getString(R.string.star))) {
            presenter.starMember(memberId);
        } else {
            presenter.cancelStar(memberId);
        }
    }

    /**/
    /*-- 以下实现videodetailview的接口 --*/
    /**/

    //设置按钮为去收藏
    private void setBtnCollect() {
        mCollectText.setText(getResources().getString(R.string.to_ollect));

        //设置图片着色
        Drawable drawableUp = DrawableCompat.wrap(mCollectImage.getDrawable());
        DrawableCompat.setTint(drawableUp, ContextCompat.getColor(getActivity(), R.color.colorAccent));
    }

    //设置按钮为取消收藏
    private void setBtnCancelCollect() {
        mCollectText.setText(getResources().getString(R.string.cancel_ollect));

        Drawable drawableUp = DrawableCompat.wrap(mCollectImage.getDrawable());
        DrawableCompat.setTint(drawableUp, ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
    }

    @Override
    public void onCheckCollect(boolean isCheck) {
        if (isCheck) {
            setBtnCancelCollect();
        } else {
            setBtnCollect();
        }
    }

    @Override
    public void onCollectSuccess(Integer count) {
        mFavNum.setText(String.valueOf(count));
        setBtnCancelCollect();
    }

    @Override
    public void onCollectError(Throwable throwable) {
        UIUtil.showToast(getActivity(), throwable.getMessage());
    }

    @Override
    public void onCancelCollectSuccess(Integer count) {
        mFavNum.setText(String.valueOf(count));
        setBtnCollect();
    }

    @Override
    public void onCancelCollectError(Throwable throwable) {
        UIUtil.showToast(getActivity(), throwable.getMessage());
    }


    private void setStar() {
        mStar.setText(getResources().getString(R.string.star));
    }

    private void setCancelStar() {
        mStar.setText(getResources().getString(R.string.cancel_star));
    }

    @Override
    public void onCheckStar(boolean isCheck) {
        if (isCheck) {
            setCancelStar();
        } else {
            setStar();
        }
    }

    @Override
    public void onStarMemberSuccess() {
        setCancelStar();
    }

    @Override
    public void onStarMemberError(Throwable throwable) {
        UIUtil.showToast(getActivity(), throwable.getMessage());
    }

    @Override
    public void onCancelStarMemberSuccess() {
        setStar();
    }

    @Override
    public void onCancelStarMemberError(Throwable throwable) {
        UIUtil.showToast(getActivity(), throwable.getMessage());
    }


}
