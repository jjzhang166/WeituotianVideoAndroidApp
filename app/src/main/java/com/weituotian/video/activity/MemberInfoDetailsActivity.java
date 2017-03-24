package com.weituotian.video.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.weituotian.video.R;
import com.weituotian.video.adapter.MyVideoAdapter;
import com.weituotian.video.adapter.VideoAdapter;
import com.weituotian.video.adapter.helper.AbsRecyclerViewAdapter;
import com.weituotian.video.adapter.helper.EndlessRecyclerOnScrollListener;
import com.weituotian.video.adapter.helper.HeaderViewRecyclerAdapter;
import com.weituotian.video.entity.AppMember;
import com.weituotian.video.entity.PageInfo;
import com.weituotian.video.entity.VideoListVo;
import com.weituotian.video.entity.enums.SexEnum;
import com.weituotian.video.event.AppBarStateChangeEvent;
import com.weituotian.video.mvpview.IMemberInfoView;
import com.weituotian.video.presenter.MemberInfoPresenter;
import com.weituotian.video.utils.SystemBarHelper;
import com.weituotian.video.utils.UIUtil;
import com.weituotian.video.widget.CircleImageView;
import com.weituotian.video.widget.recycler.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by hcc on 16/8/4 21:18
 * 100332338@qq.com
 * <p/>
 * 用户个人中心界面
 */
public class MemberInfoDetailsActivity extends BaseMvpActivity<IMemberInfoView, MemberInfoPresenter> implements IMemberInfoView {

    @BindView(R.id.user_avatar_view)
    CircleImageView mAvatarImage;

    @BindView(R.id.user_name)
    TextView mUserNameText;

    @BindView(R.id.user_desc)
    TextView mDescriptionText;

    @BindView(R.id.tv_follow_users)
    TextView mFollowNumText;

    @BindView(R.id.tv_fans)
    TextView mFansNumText;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.user_lv)
    ImageView mUserLv;

    @BindView(R.id.user_sex)
    ImageView mUserSex;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.line)
    View mLineView;

    @BindView(R.id.rv_videolist)
    RecyclerView mRecyclerView;


    //传过来的userid
    private int userID;

    //访问服务器获得的详细信息
    private AppMember appMember;

    private static final String EXTRA_USER_NAME = "extra_user_name",
            EXTRA_USERID = "extra_userid", EXTRA_AVATAR_URL = "extra_avatar_url";


    private List<VideoListVo> vos = new ArrayList<>();
    private MyVideoAdapter mVideoAdapter;
    private HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    //当前页数,从1开始
    private int pageNum = 1;
    private int pageSize = 10;

    private boolean mIsRefreshing = false;

    private View loadMoreView;
    private View noMoreView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        Intent intent = getIntent();

        userID = intent.getIntExtra(EXTRA_USERID, -1);
        presenter.getMemberInfo(userID);

        initToolBar();
        initRecycleView();

    }

    public void initToolBar() {

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        //设置StatusBar透明
        SystemBarHelper.immersiveStatusBar(this);
        SystemBarHelper.setHeightAndPadding(this, mToolbar);

        //设置AppBar展开折叠状态监听
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeEvent() {

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int verticalOffset) {

                if (state == State.EXPANDED) {
                    //展开状态
                    mCollapsingToolbarLayout.setTitle("");
                    mLineView.setVisibility(View.VISIBLE);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    if (appMember != null) {
                        mCollapsingToolbarLayout.setTitle(appMember.getName());
                    }
                    mLineView.setVisibility(View.GONE);
                } else {
                    mCollapsingToolbarLayout.setTitle("");
                    mLineView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initRecycleView() {

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //adapter
        mVideoAdapter = new MyVideoAdapter(mRecyclerView, vos);
        mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mVideoAdapter);
        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);

        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                pageNum++;
                loadVideos();
            }
        };
        mRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);

        //创建加载更多view
        createLoadMoreView();

        //点击一项打开视频
        mVideoAdapter.setOnItemClickListener(new AbsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, AbsRecyclerViewAdapter.ClickableViewHolder holder) {
                VideoDetailsActivity.launch(MemberInfoDetailsActivity.this, vos.get(position).getId());
            }
        });

        //正在刷新的话就不允许touch
        /*mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mIsRefreshing;
            }
        });*/
    }

    private void createLoadMoreView() {
        loadMoreView = LayoutInflater.from(MemberInfoDetailsActivity.this).inflate(R.layout.item_footer, mRecyclerView, false);
        mHeaderViewRecyclerAdapter.addFooterView(loadMoreView);
        loadMoreView.setVisibility(View.GONE);
        noMoreView = LayoutInflater.from(MemberInfoDetailsActivity.this).inflate(R.layout.item_nomore, mRecyclerView, false);
        mHeaderViewRecyclerAdapter.addFooterView(noMoreView);
        noMoreView.setVisibility(View.GONE);
    }

    private void loadVideos() {
        presenter.getMemberVideos(userID, pageNum, pageSize);
        loadMoreView.setVisibility(View.VISIBLE);
    }

    @NonNull
    @Override
    public MemberInfoPresenter createPresenter() {
        return new MemberInfoPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void finishTask() {

//        mIsRefreshing = false;
        loadMoreView.setVisibility(View.GONE);

        int start = pageNum * pageSize - pageSize - 1;
        if (start > 0) {
            mVideoAdapter.notifyItemRangeChanged(start, pageSize);
        } else {
            mVideoAdapter.notifyDataSetChanged();
        }
    }

    private void setUserLevel(int rank) {

        /*if (rank == 0) {
            mUserLv.setImageResource(R.drawable.ic_lv0);
        } else if (rank == 1) {
            mUserLv.setImageResource(R.drawable.ic_lv1);
        } else if (rank == 200) {
            mUserLv.setImageResource(R.drawable.ic_lv2);
        } else if (rank == 1500) {
            mUserLv.setImageResource(R.drawable.ic_lv3);
        } else if (rank == 3000) {
            mUserLv.setImageResource(R.drawable.ic_lv4);
        } else if (rank == 7000) {
            mUserLv.setImageResource(R.drawable.ic_lv5);
        } else if (rank == 10000) {
            mUserLv.setImageResource(R.drawable.ic_lv6);
        }*/
    }

    public static void launch(Activity activity, int userId) {

        Intent intent = new Intent(activity, MemberInfoDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(EXTRA_USER_NAME, name);
        intent.putExtra(EXTRA_USERID, userId);
//        intent.putExtra(EXTRA_AVATAR_URL, avatar_url);
        activity.startActivity(intent);
    }

    /*以下实现view的接口*/

    @Override
    public void onLoadMemberInfoSuccess(AppMember appMember) {
        this.appMember = appMember;

        //设置用户头像
        Glide.with(MemberInfoDetailsActivity.this)
                .load(appMember.getAvatar())
                .centerCrop()
                .dontAnimate()
                .placeholder(R.drawable.ico_user_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mAvatarImage);

        //设置粉丝和关注
        mUserNameText.setText(appMember.getName());
        mFollowNumText.setText(String.valueOf(appMember.getFollows()));
        mFansNumText.setText(String.valueOf(appMember.getFans()));

        //设置用户性别
        if (appMember.getSex() == SexEnum.MALE) {
            mUserSex.setImageResource(R.drawable.ic_user_male);
        } else if (appMember.getSex() == SexEnum.FEMALE) {
            mUserSex.setImageResource(R.drawable.ic_user_female);
        } else if (appMember.getSex() == SexEnum.UNKNOW) {
            mUserSex.setImageResource(R.drawable.ic_user_gay_border);
        }

        //设置用户签名信息
        if (!TextUtils.isEmpty(appMember.getDescript())) {
            mDescriptionText.setText(appMember.getDescript());
        } else {
            mDescriptionText.setText("这个人懒死了,什么都没有写(・－・。)");
        }

        loadVideos();
    }

    @Override
    public void onLoadMemberInfoError(Throwable e) {
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
    public void onLoadVideos(PageInfo<VideoListVo> pageInfo) {
        if (pageInfo.getList().size() > 0) {
            vos.addAll(pageInfo.getList());
            finishTask();
        } else {
            onNoMoreVideo();
        }
    }

    public void onNoMoreVideo() {
//        UIUtil.showToast(this, "没有更多视频了");
        noMoreView.setVisibility(View.VISIBLE);
        loadMoreView.setVisibility(View.GONE);
    }
}
