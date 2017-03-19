package com.weituotian.mycommonvideo.view.listener;

/**
 * 视频播放控制栏的事件接口,提供事件回调
 * Created by ange on 2016/12/22.
 */
public interface PlayerControllerListener {

    /**
     * 播放按钮被点击
     */
    void onPlayClick();

    /**
     * 暂停按钮被点击
     */
    void onPauseClick();

    /**
     * 进度条被点击
     */
    void onProgressChanged(int time);

    /**
     * 快进
     */
    void onFastForward();

    /**
     * 快退
     */
    void onBackForward();
}
