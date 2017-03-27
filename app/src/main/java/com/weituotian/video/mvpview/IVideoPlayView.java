package com.weituotian.video.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.weituotian.video.entity.FrontVideo;

/**
 * Created by ange on 2017/3/24.
 */

public interface IVideoPlayView extends MvpView{

    void onLoadVideoInfo(FrontVideo frontVideo);

    void onLoadVideInfoError(Throwable throwable);

    void onLoadVideoSrc(String url);
}
