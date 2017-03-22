package com.weituotian.video.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.weituotian.video.R;
import com.weituotian.video.event.AppBarStateChangeEvent;
import com.weituotian.video.mvpview.IUserInfoView;
import com.weituotian.video.presenter.UserInfoPresenter;
import com.weituotian.video.utils.SystemBarHelper;
import com.weituotian.video.widget.CircleImageView;

import butterknife.BindView;

/**
 * Created by hcc on 16/8/4 21:18
 * 100332338@qq.com
 * <p/>
 * 用户个人中心界面
 */
public class UserInfoDetailsActivity extends BaseMvpActivity<IUserInfoView, UserInfoPresenter> implements IUserInfoView {

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

    private String name = "";

    private int userID;

    private String avatar_url;

    private static final String EXTRA_USER_NAME = "extra_user_name",
            EXTRA_USERID = "extra_userid", EXTRA_AVATAR_URL = "extra_avatar_url";


    @Override
    public int getContentViewId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
//            name = intent.getStringExtra(EXTRA_USER_NAME);
            userID = intent.getIntExtra(EXTRA_USERID, -1);
//            avatar_url = intent.getStringExtra(EXTRA_AVATAR_URL);
        }

        /*if (name != null) {
            mUserNameText.setText(name);
        }

        if (avatar_url != null) {
            Glide.with(UserInfoDetailsActivity.this)
                    .load(avatar_url)
                    .centerCrop()
                    .dontAnimate()
                    .placeholder(R.drawable.ico_user_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mAvatarImage);
        }*/

        initToolBar();

        presenter.getUserInfo();
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
                    mCollapsingToolbarLayout.setTitle(name);
                    mLineView.setVisibility(View.GONE);
                } else {
                    mCollapsingToolbarLayout.setTitle("");
                    mLineView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @NonNull
    @Override
    public UserInfoPresenter createPresenter() {
        return new UserInfoPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void finishTask() {
/*
        //设置用户头像
        Glide.with(UserInfoDetailsActivity.this)
                .load(mUserDetailsInfo.getCard().getFace())
                .centerCrop()
                .dontAnimate()
                .placeholder(R.drawable.ico_user_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mAvatarImage);

        //设置粉丝和关注
        mUserNameText.setText(mUserDetailsInfo.getCard().getName());
        mFollowNumText.setText(String.valueOf(mUserDetailsInfo.getCard().getAttention()));
        mFansNumText.setText(NumberUtil.converString(mUserDetailsInfo.getCard().getFans()));

        //设置用户等级
        setUserLevel(Integer.valueOf(mUserDetailsInfo.getCard().getRank()));

        //设置用户性别
        switch (mUserDetailsInfo.getCard().getSex()) {
            case "男":
                mUserSex.setImageResource(R.drawable.ic_user_male);
                break;
            case "女":
                mUserSex.setImageResource(R.drawable.ic_user_female);
                break;
            default:
                mUserSex.setImageResource(R.drawable.ic_user_gay_border);
                break;
        }

        //设置用户签名信息
        if (!TextUtils.isEmpty(mUserDetailsInfo.getCard().getSign())) {
            mDescriptionText.setText(mUserDetailsInfo.getCard().getSign());
        } else {
            mDescriptionText.setText("这个人懒死了,什么都没有写(・－・。)");
        }

        //设置认证用户信息
        if (mUserDetailsInfo.getCard().isApprove()) {
            //认证用户 显示认证资料
            mAuthorVerifiedLayout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mUserDetailsInfo.getCard().getDescription())) {
                mAuthorVerifiedText.setText(mUserDetailsInfo.getCard().getDescription());
            } else {
                mAuthorVerifiedText.setText("这个人懒死了,什么都没有写(・－・。)");
            }
        } else {
            //普通用户
            mAuthorVerifiedLayout.setVisibility(View.GONE);
        }

        //获取用户详情全部数据
        getUserAllData();
*/
    }


    private void getUserAllData() {

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


    public static void launch(Activity activity,int userId) {

        Intent intent = new Intent(activity, UserInfoDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(EXTRA_USER_NAME, name);
        intent.putExtra(EXTRA_USERID, userId);
//        intent.putExtra(EXTRA_AVATAR_URL, avatar_url);
        activity.startActivity(intent);
    }


}
