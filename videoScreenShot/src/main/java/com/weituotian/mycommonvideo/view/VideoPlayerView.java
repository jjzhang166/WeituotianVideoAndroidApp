package com.weituotian.mycommonvideo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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

    private Context mContext;

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
        mContext = context;
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
     * ---------------- 对外方法---------------------
     */

    private Uri mVideoUrl;

    /**
     * 外部接口 播放资源路径
     *
     * @param path
     */
    public void play(String path) {
        Uri uri = Uri.parse(path);
        play(uri);
    }

    /**
     * 外部接口 播放资源url
     *
     * @param videoUrl
     */
    public void play(Uri videoUrl) {
        mVideoUrl = videoUrl;
        mPlayState = VideoPlayState.STAND_TO_PREPARE;
        createMediaPlayer();
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
     * 更新播放进度,每过去1s执行
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
//        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(mContext, mVideoUrl);
        addVideoPlayListener();
        createTextureView();
    }

    /**
     * 为播放器添加监听
     */
    private void addVideoPlayListener() {
        mediaPlayer.setOnPreparedListener(mOnPreparedListener);

        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                updateTextureViewSizeCenterCrop(width, height);
            }
        });
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
//        mediaPlayer.prepareAsync();
        /*try {
            mediaPlayer.setDataSource(getContext(), mVideoUrl);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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

    //重新计算video的显示位置，裁剪后全屏显示
    private void updateTextureViewSizeCenterCrop(int width, int height) {

        int tvWidth = mTextureView.getWidth();
        int tvHeight = mTextureView.getHeight();

        float sx = (float) tvWidth / (float) width;
        float sy = (float) tvHeight / (float) height;
        Matrix matrix = new Matrix();
        float maxScale = Math.max(sx, sy);

        //第1步:把视频区移动到View区,使两者中心点重合.
        matrix.preTranslate((tvWidth - width) / 2, (tvHeight - height) / 2);

        //第2步:因为默认视频是fitXY的形式显示的,所以首先要缩放还原回来.
        matrix.preScale(width / (float) tvWidth, height / (float) tvHeight);

        //第3步,等比例放大或缩小,直到视频区的一边超过View一边, 另一边与View的另一边相等. 因为超过的部分超出了View的范围,所以是不会显示的,相当于裁剪了.
        matrix.postScale(maxScale, maxScale, tvWidth / 2, tvHeight / 2);//后两个参数坐标是以整个View的坐标系以参考的
        mTextureView.setTransform(matrix);
        mTextureView.postInvalidate();
    }

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


    /**---------------- 以下实现playController的事件 ----------------------**/
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
        if (mPlayState != VideoPlayState.STAND_TO_PREPARE) {
            mediaPlayer.seekTo(seekTime);
            updatePlayProgress();
        }
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

            int step = (int) (duration * 0.05);//每次前进视频百分比的百分之五

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
