package com.weituotian.video.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.tablayout.SlidingTabLayout;

import com.weituotian.mycommonvideo.view.VideoPlayerView;
import com.weituotian.video.R;
import com.weituotian.video.entity.FrontVideo;
import com.weituotian.video.event.AppBarStateChangeEvent;
import com.weituotian.video.fragment.VideoIntroductionFragment;
import com.weituotian.video.mvpview.IVideoInfoView;
import com.weituotian.video.presenter.VideoInfoPresenter;
import com.weituotian.video.utils.DisplayUtil;
import com.weituotian.video.utils.SystemBarHelper;
import com.weituotian.video.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by hcc on 16/8/4 21:18
 * 100332338@qq.com
 * <p/>
 * 视频详情界面
 */
public class VideoDetailsActivity extends BaseMvpActivity<IVideoInfoView, VideoInfoPresenter> implements IVideoInfoView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.video_preview)
    ImageView mVideoPreview;

    @BindView(R.id.tab_layout)
    SlidingTabLayout mSlidingTabLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.fab)
    FloatingActionButton mFAB;

    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.tv_player)
    TextView mTvPlayer;

    @BindView(R.id.tv_av)
    TextView mAvText;

    @BindView(R.id.fl_player_container)
    FrameLayout mFlPlayerContainer;

    //    @BindView(R.id.videoplayer)
    JCVideoPlayerStandard jcVideoPlayerStandard;
//@BindView(R.id.detail_player)
//    StandardGSYVideoPlayer gsyVideoPlayer;

    public final static String EXTRA_AV = "extra_av";

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    private int videoId;
    private String mVideoUrl;
    private FrontVideo frontVideo;


    //视频播放view,动态初始化
    private VideoPlayerView mVideoPlayerView;

    //floating action button 的动画
    private AppBarLayout.OnOffsetChangedListener onOffsetChangedListener1 = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            setViewsTranslation(verticalOffset);
        }
    };

    public static void launch(Activity activity, int videoId) {

        Intent intent = new Intent(activity, VideoDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_AV, videoId);
        activity.startActivity(intent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_video_details;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        videoId = intent.getIntExtra(EXTRA_AV, -1);
        presenter.getVideoInfo(videoId);

        mFAB.setClickable(false);
        /*mFAB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_20)));
        mFAB.setTranslationY(-getResources().getDimension(R.dimen.floating_action_button_size_half));*/

        //打开视频播放activity
        /*mFAB.setOnClickListener(v -> VideoPlayerActivity.launch(VideoDetailsActivity.this,
                mVideoDetailsInfo.getPages().get(0).getCid(), mVideoDetailsInfo.getTitle()));*/


        mAppBarLayout.addOnOffsetChangedListener(onOffsetChangedListener1);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeEvent() {

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int verticalOffset) {

                if (state == State.EXPANDED) {
                    //展开状态
                    mTvPlayer.setVisibility(View.GONE);
                    mAvText.setVisibility(View.VISIBLE);
                    mToolbar.setContentInsetsRelative(DisplayUtil.dp2px(VideoDetailsActivity.this, 15), 0);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    mTvPlayer.setVisibility(View.VISIBLE);
                    mAvText.setVisibility(View.GONE);
                    mToolbar.setContentInsetsRelative(DisplayUtil.dp2px(VideoDetailsActivity.this, 150), 0);
                } else {
                    mTvPlayer.setVisibility(View.GONE);
                    mAvText.setVisibility(View.VISIBLE);
                    mToolbar.setContentInsetsRelative(DisplayUtil.dp2px(VideoDetailsActivity.this, 15), 0);
                }
            }
        });

        initToolBar();
    }

    @NonNull
    @Override
    public VideoInfoPresenter createPresenter() {
        return new VideoInfoPresenter();
    }

    @SuppressLint("SetTextI18n")
    public void initToolBar() {

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        //设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        //设置收缩后Toolbar上字体的颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        //设置StatusBar透明
        SystemBarHelper.immersiveStatusBar(this);
        SystemBarHelper.setHeightAndPadding(this, mToolbar);

        mAvText.setText("av" + videoId);
    }

    private void showFAB() {

        mFAB.animate().scaleX(1f).scaleY(1f)
                .setInterpolator(new OvershootInterpolator())
                .start();

        mFAB.setClickable(true);
    }

    private void hideFAB() {

        mFAB.animate().scaleX(0f).scaleY(0f)
                .setInterpolator(new AccelerateInterpolator())
                .start();

        mFAB.setClickable(false);
    }

    private void setViewsTranslation(int target) {

        mFAB.setTranslationY(target);
        if (target == 0) {
            showFAB();
        } else if (target < 0) {
            hideFAB();
        }
    }

    private void measureTabLayoutTextWidth(int position) {
        String title = titles.get(position);
        TextView titleView = mSlidingTabLayout.getTitleView(position);
        TextPaint paint = titleView.getPaint();
        float textWidth = paint.measureText(title);
        mSlidingTabLayout.setIndicatorWidth(textWidth / 3);
    }

    /*-- activity事件 --*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    /*  实现view的接口 */

    @Override
    public void onLoadVideoInfo(FrontVideo frontVideo) {
        this.frontVideo = frontVideo;

        mAvText.setText(frontVideo.getTitle());


        titles.add("简介");
//        titles.add("评论");

        VideoIntroductionFragment mVideoIntroductionFragment = VideoIntroductionFragment.newInstance(frontVideo);
        fragments.add(mVideoIntroductionFragment);


        VideoDetailsPagerAdapter mAdapter = new VideoDetailsPagerAdapter(getSupportFragmentManager(), fragments, titles);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(titles.size());
        mSlidingTabLayout.setViewPager(mViewPager);
        measureTabLayoutTextWidth(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {

                measureTabLayoutTextWidth(position);
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mFAB.setClickable(true);
        /*mFAB.setBackgroundTintList(
                ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));*/
        mCollapsingToolbarLayout.setTitle("");

        //加载视频封面
        Glide.with(VideoDetailsActivity.this)
                .load(frontVideo.getCover())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.video_default_cover)
                .dontAnimate()
                .into(mVideoPreview);//mVideoPreview
//        gsyVideoPlayer.setThumbImageView(mVideoPreview);

        //可以点击播放视频
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoUrl == null) {
                    //去获得播放视频源
                    presenter.getVideoSrc(videoId);
                }
            }
        });
    }

    @Override
    public void onLoadVideInfoError(Throwable e) {
        UIUtil.showToast(this, e.getMessage());
        //2s后关闭
        Observable.timer(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
//                        UploadActivity.this.finish();
                    }
                });
    }

    @Override
    public void onLoadVideoSrc(String url) {
        mVideoUrl = url;

        //取消mfab的显示和动画
        mAppBarLayout.removeOnOffsetChangedListener(onOffsetChangedListener1);
        hideFAB();

        //初始化视频播放
        /*mVideoPlayerView = new VideoPlayerView(VideoDetailsActivity.this);
        mFlPlayerContainer.addView(mVideoPlayerView);
        mVideoPreview.setVisibility(View.GONE);
        Observable.just(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mVideoPlayerView.play(s);
                    }
                });*/
        mVideoPreview.setVisibility(View.GONE);

        jcVideoPlayerStandard = new JCVideoPlayerStandard(VideoDetailsActivity.this);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mFlPlayerContainer.addView(jcVideoPlayerStandard, lp);

        jcVideoPlayerStandard.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, frontVideo.getTitle());

        //开始播放
        jcVideoPlayerStandard.startVideo();

        /*gsyVideoPlayer = new StandardGSYVideoPlayer(VideoDetailsActivity.this);
        mFlPlayerContainer.addView(gsyVideoPlayer);

        gsyVideoPlayer.setUp(url, true, null, frontVideo.getTitle());

        //非全屏下，不显示title
        gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);

//非全屏下不显示返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);

//打开非全屏下触摸效果
        gsyVideoPlayer.setIsTouchWiget(true);

//立即播放
        gsyVideoPlayer.startPlayLogic();

        //设置旋转
        final OrientationUtils orientationUtils = new OrientationUtils(this, gsyVideoPlayer);

        //开启自动旋转
        gsyVideoPlayer.setRotateViewAuto(false);

        //全屏首先横屏
        gsyVideoPlayer.setLockLand(true);

        //是否需要全屏动画效果
        gsyVideoPlayer.setShowFullAnimation(false);

        //设置全屏按键功能
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gsyVideoPlayer.startWindowFullscreen(VideoDetailsActivity.this, false, false);
//                orientationUtils.resolveByClick();
            }
        });*/
    }

    /* 内置view pager 的adapter*/

    public static class VideoDetailsPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments;

        private List<String> titles;


        VideoDetailsPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {

            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }


        @Override
        public Fragment getItem(int position) {

            return fragments.get(position);
        }


        @Override
        public int getCount() {

            return fragments.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {

            return titles.get(position);
        }
    }
}
