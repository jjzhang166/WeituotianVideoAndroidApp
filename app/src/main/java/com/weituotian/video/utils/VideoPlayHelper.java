package com.weituotian.video.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.weituotian.mycommonvideo.view.VideoPlayerView;

/**
 * Created by ange on 2017/3/25.
 */

public class VideoPlayHelper {

    private Context mContext;

    private VideoPlayerView mVideoPlayerView;

    public VideoPlayHelper(Context context) {
        this.mContext = context;
        mVideoPlayerView = new VideoPlayerView(context);
    }

    public void play(ViewGroup parent, String videoUrl, int position) {

    }

}
