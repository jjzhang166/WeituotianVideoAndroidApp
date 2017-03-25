package com.weituotian.mycommonvideo.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.weituotian.mycommonvideo.R;
import com.weituotian.mycommonvideo.view.listener.PlayerControllerListener;

/**
 * 视频播放控制栏view
 * Created by ange on 2016/12/21.
 */
public class PlayerControllerView extends LinearLayout {

    //UI
    private SeekBar mSeekBar;
    private TextView mTotalTimeText;
    private TextView mPlayTimeText;
    private ImageButton mPlayBtn;
    private ImageButton mForwardBtn;
    private ImageButton mBackwardBtn;
    private ImageButton mFullScreenBtn;

    //保存视频总长度
    private int mVideoDuration = 0;

    PlayerControllerListener mControllerListener;

    private VideoPlayState mPlayState = VideoPlayState.PAUSE;//默认没有播放

    public PlayerControllerView(Context context) {
        this(context, null);
//        super(context);
    }

    public PlayerControllerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
//        super(context, attrs);
    }

    public PlayerControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化UI
     *
     * @param context
     */
    private void initView(Context context) {

        inflate(context, R.layout.player_controller, this);
//        LayoutParams params = (LayoutParams) this.getLayoutParams();

        this.setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundColor(Color.parseColor("#0a000000"));
        this.setPadding(0, 15, 0, 0);

        this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 76));

//        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        params.height = 76;
//        this.setLayoutParams(params);

        mSeekBar = (SeekBar) this.findViewById(R.id.sb_process);
        mTotalTimeText = (TextView) this.findViewById(R.id.tv_total_time);
        mPlayTimeText = (TextView) this.findViewById(R.id.tv_current_time);
        mPlayBtn = (ImageButton) this.findViewById(R.id.iv_pause);
        mForwardBtn = (ImageButton) this.findViewById(R.id.iv_next);
        mBackwardBtn = (ImageButton) this.findViewById(R.id.iv_pre);
        mFullScreenBtn = (ImageButton) this.findViewById(R.id.iv_fullscreen);
        mFullScreenBtn.setVisibility(View.GONE);//默认全屏按钮不显示

        setListener();
    }

    /*
    `*初始化设置一些view的事件
     */
    private void setListener() {

        //进度条拖动
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {
                    int time = progress * mVideoDuration / 100 * 1000;//转化成毫秒
                    if (mControllerListener != null) {
                        mControllerListener.onProgressChanged(time);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //播放按钮点击
        mPlayBtn.setOnClickListener(mOnBtnClickListener);

        mForwardBtn.setOnClickListener(mOnBtnClickListener);
        mBackwardBtn.setOnClickListener(mOnBtnClickListener);
    }

    private OnClickListener mOnBtnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.iv_next) {
                if (mControllerListener != null) {
                    mControllerListener.onFastForward();
                }

            } else if (i == R.id.iv_pre) {
                if (mControllerListener != null) {
                    mControllerListener.onBackForward();
                }

            } else if (i == R.id.iv_pause) {
                if (mControllerListener != null) {

                    switch (mPlayState) {
                        case PLAY:
                            mControllerListener.onPauseClick();
                            break;
                        case PAUSE:
                            mControllerListener.onPlayClick();
                            break;
                    }
                }
            }
        }
    };

    /**--对外方法--**/

    /**
     * 设置播放状态
     */
    public void play() {
        int resid = R.drawable.ic_pause_circle_outline_light_blue_500_24dp;
        mPlayBtn.setImageResource(resid);

        mPlayState = VideoPlayState.PLAY;
    }

    public void pause() {
        int resid = R.drawable.ic_play_circle_outline_light_blue_500_24dp;
        mPlayBtn.setImageResource(resid);

        mPlayState = VideoPlayState.PAUSE;
    }

    public void showFullScreenBtn(){
        mFullScreenBtn.setVisibility(VISIBLE);
    }

    public void hideFullScreenBtn(){
        mFullScreenBtn.setVisibility(GONE);
    }

    /**
     * UI/设置视频总长度
     *
     * @param videoDuration 时间millionsecond
     */
    public void setVideoDuration(int videoDuration) {
        mVideoDuration = videoDuration / 1000;
        mTotalTimeText.setText(formatVideoTimeLength(mVideoDuration));
    }

    /**
     * UI/设置当前播放时间和进度条
     *
     * @param playTime 时间millionsecond
     */
    public void setVideoPlayTime(int playTime) {
        playTime = playTime / 1000;
        mPlayTimeText.setText(formatVideoTimeLength(playTime));
        int progress = (int) (playTime * 1.0 / mVideoDuration * 100 + 0.5f);
        mSeekBar.setProgress(progress);
    }

    /**
     * 转换视频时长(s)为时分秒的展示格式
     *
     * @param seconds 视频总时长，单位秒
     * @return string
     */
    public static String formatVideoTimeLength(long seconds) {

        String formatLength = "";
        if (seconds == 0) {
            formatLength = "00:00";
        } else if (seconds < 60) {//小于1分钟
            formatLength = "00:" + (seconds < 10 ? "0" + seconds : seconds);
        } else if (seconds < 60 * 60) {//小于1小时
            long sec = seconds % 60;
            long min = seconds / 60;
            formatLength = (min < 10 ? "0" + min : String.valueOf(min)) + ":" +
                    (sec < 10 ? "0" + sec : String.valueOf(sec));
        } else {
            long hour = seconds / 3600;
            long min = seconds % 3600 / 60;
            long sec = seconds % 3600 % 60;
            formatLength = (hour < 10 ? "0" + hour : String.valueOf(hour)) + ":" +
                    (min < 10 ? "0" + min : String.valueOf(min)) + ":" +
                    (sec < 10 ? "0" + sec : String.valueOf(sec));
        }
        return formatLength;
    }

    /**
     * 显示或者隐藏界面
     */
    public void toggle() {
        if (this.getVisibility() == View.VISIBLE) {
            hide();
//            this.setVisibility(View.GONE);
        } else {
            show();
            this.postDelayed(mHideRunnable, 3000);//3s后会再次调用这个toggle,实现自动隐藏
//            this.setVisibility(VISIBLE);
        }
    }

    /**
     * 使用动画隐藏
     */
    public void hide() {

        this.removeCallbacks(mHideRunnable);
        this.clearAnimation();
        if (this.isShown()) {
            Animation animation = AnimationUtils.loadAnimation(getContext(),
                    R.anim.option_leave_from_bottom);
            animation.setAnimationListener(new AnimationImp() {

                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    PlayerControllerView.this.setVisibility(View.GONE);
                }

            });

            this.startAnimation(animation);
        }
    }

    /**
     * 使用动画显示
     */
    public void show() {

        this.clearAnimation();
        if (!this.isShown()) {
            Animation animation = AnimationUtils.loadAnimation(getContext(),
                    R.anim.option_entry_from_bottom);
            animation.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {

                    super.onAnimationEnd(animation);
                    PlayerControllerView.this.setVisibility(View.VISIBLE);

                }
            });
            this.startAnimation(animation);
        }
        this.removeCallbacks(mHideRunnable);
    }

    /**
     * 主要用来实现自动隐藏
     */
    private Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            toggle();
        }
    };

    /**
     * 简单的动画实现
     */
    public class AnimationImp implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

    }

    //getter和setter

    public PlayerControllerListener getmControllerListener() {
        return mControllerListener;
    }

    public void setmControllerListener(PlayerControllerListener mControllerListener) {
        this.mControllerListener = mControllerListener;
    }
}
