package com.weituotian.mycommonvideo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.RelativeLayout;

import com.weituotian.mycommonvideo.R;
import com.weituotian.mycommonvideo.view.listener.PlayerControllerListener;

import java.io.IOException;

/**
 * 自定义的视频播放view
 * Created by ange on 2016/12/22.
 */

public class VideoPlayerView extends RelativeLayout implements TextureView.SurfaceTextureListener, PlayerControllerListener {

    //视频播放介质
    private TextureView mTextureView;

    //播放控制器
    private PlayerControllerView playerControll;

    //外层relativelayout
    private RelativeLayout mContainerView;

    //视频播放器
    private MediaPlayer mediaPlayer;

    private VideoPlayState mPlayState = VideoPlayState.STAND_TO_PREPARE;//默认是静止状态

    private static final String LOGTAG = "VideoPlayerView";

    /**
     * ---------------- 初始化----------------------
     **/
    public VideoPlayerView(Context context) {
        this(context, null);
    }

    public VideoPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.video_player, this);

        mContainerView = (RelativeLayout) this.findViewById(R.id.video_player_view_container);

        playerControll = (PlayerControllerView) this.findViewById(R.id.playerControll);

        setListener();
    }

    private void setListener() {
        //接受播放控制器事件
        playerControll.setmControllerListener(this);
    }

    /**
     * ---------------- 对外方法----------------------
     **/
    private Uri mVideoUrl;

    /**
     * 外部接口 播放资源url
     *
     * @param videoUrl
     */
    public void play(Uri videoUrl) {
        mPlayState = VideoPlayState.STAND_TO_PREPARE;
        createMediaPlayer();
        mVideoUrl = videoUrl;
    }

    /**
     * 外部接口 继续播放视频
     */
    public void play() {
        setPlayState(VideoPlayState.PLAY);
    }

    /**
     * 外部接口 停止播放视频
     */
    public void stop() {
        setPlayState(VideoPlayState.PAUSE);
    }

    /**
     * 外部接口 是否正在播放
     */
    public boolean isPlaying() {
        return mPlayState == VideoPlayState.PLAY;
    }

    /**
     * 外部接口 返回截图
     *
     * @return
     */
    public Bitmap getScreenShot() {
        return mTextureView != null ? mTextureView.getBitmap() : null;
    }


    /**---------------- UI----------------------**/


    /**---------------- 视频状态控制----------------------**/
    /**
     * 设置播放状态
     *
     * @param state
     */
    private void setPlayState(VideoPlayState state) {
        //state为要变成的状态
        switch (state) {
            case STAND_TO_PREPARE:
                break;
            case PLAY:
                if (mPlayState == VideoPlayState.PAUSE || mPlayState == VideoPlayState.PREPARE) {
                    mediaPlayer.start();
                    mPlayState = state;
                    updatePlayProgress();//触发进行进度条更新
                    updateUI(state);//更新界面
                }

                break;
            case PAUSE:
                if (mPlayState == VideoPlayState.PLAY) {
                    mediaPlayer.pause();
                    mPlayState = state;
                    updatePlayProgress();
                    updateUI(state);//更新界面
                }

                break;
            default:
                break;
        }
    }

    /**
     * 更新各个视频状态的UI界面
     *
     * @param state 要变成的状态
     */
    private void updateUI(VideoPlayState state) {
        switch (state) {
            case STAND_TO_PREPARE:
                break;
            case PLAY:

                //设置总长度
                int duration = mediaPlayer.getDuration();
                playerControll.setVideoDuration(duration);

                playerControll.play();//控制条

                break;
            case PAUSE:

                playerControll.pause();//控制条
                playerControll.show();//移除自动隐藏效果

                break;
            default:
                break;
        }
    }

    /**
     * 更新播放进度
     */
    private void updatePlayProgress() {

        try {

            int playTime = mediaPlayer.getCurrentPosition();
            playerControll.setVideoPlayTime(playTime);

//            playerControll.setSecondaryProgress(mSecProgress);
        } catch (Exception e) {
            Log.d(LOGTAG, "update play progress failure", e);
        }

        if (mPlayState == VideoPlayState.PLAY) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    updatePlayProgress();
                }
            }, 1000);
        }
    }

    /**---------------- mediaPlayer g构建----------------------**/
    /**
     * 创建视频播放控制器
     */
    private void createMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        addVideoPlayListener();
        createTextureView();
    }

    /**
     * 为播放器添加监听
     */
    private void addVideoPlayListener() {
        mediaPlayer.setOnPreparedListener(mOnPreparedListener);
    }

    /**
     * mediaplayer准备完成
     */
    MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mPlayState = VideoPlayState.PREPARE;//设置状态为已经载入视频
            play();
        }
    };

    /**
     * 加载视频
     */
    private void loadVideo() {
        try {
            mediaPlayer.setDataSource(getContext(), mVideoUrl);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**---------------- textureview 视频播放介质 ----------------------**/
    /**
     * 创建textureview显示画面
     */
    private void createTextureView() {
        if (mTextureView == null) {
            mTextureView = new TextureView(getContext()); //(TextureView) this.findViewById(R.id.textureView);
            mTextureView.setId(R.id.textureView);

            mTextureView.setSurfaceTextureListener(this);
            mTextureView.setOnClickListener(mTextureViewOnClickListener);
//
            //居中显示
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            mTextureView.setLayoutParams(params);

            mContainerView.addView(mTextureView, 0);//0是放在最底层
        }

    }

    //点击视频播放界面就显示控制条
    private OnClickListener mTextureViewOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            playerControll.toggle();
        }
    };

    private Surface mSurface;

    /**
     * textureview可用的时候
     *
     * @param surfaceTexture
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        mSurface = new Surface(surfaceTexture);
        mediaPlayer.setSurface(mSurface);
        loadVideo();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    /**---------------- 视频控制器事件 ----------------------**/
    /**
     * 播放控制器事件,播放按钮被点击
     */
    @Override
    public void onPlayClick() {
        setPlayState(VideoPlayState.PLAY);
    }

    /**
     * 播放控制器事件,暂停按钮被点击
     */
    @Override
    public void onPauseClick() {
        setPlayState(VideoPlayState.PAUSE);
    }

    @Override
    public void onProgressChanged(int seekTime) {
        mediaPlayer.seekTo(seekTime);
        updatePlayProgress();
    }

    @Override
    public void onFastForward() {
        videoSeek(true);
    }

    @Override
    public void onBackForward() {
        videoSeek(false);
    }

    private static final int ONE_SECOND = 1000;

    /**
     * 视频前进后退
     *
     * @param isForward
     */
    private void videoSeek(boolean isForward) {

        if (mPlayState != VideoPlayState.PLAY &&
                mPlayState != VideoPlayState.PAUSE && mPlayState != VideoPlayState.PREPARE) {//进入播放
            return;
        }
        try {

            playerControll.show();

            int duration = mediaPlayer.getDuration();//总时长

            int step = (int) ( duration * 0.05);//每次前进视频百分比的百分之五

            int current = mediaPlayer.getCurrentPosition();//当前播放时长

            if (isForward) {//前进
                current = current + step >= duration ? duration : current + step;
            } else {
                current = current - step <= 0 ? 0 : current - step;
            }

            mediaPlayer.seekTo(current);
            updatePlayProgress();

        } catch (Exception e) {
            Log.d(LOGTAG, "video forward and backward error", e);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mediaPlayer != null) {
            // Make sure we stop video and release resources when activity is destroyed.
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
