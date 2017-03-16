package com.weituotian.video.mvpview;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.weituotian.video.entity.BiliDingVideo;

import java.util.List;

/**
 * @author laohu
 * @site http://ittiger.cn
 */
public interface IVideoView extends MvpLceView<List<BiliDingVideo.VideoBean>> {

    void showLoadMoreErrorView();

    void showLoadMoreView();

    void setLoadMoreData(List<BiliDingVideo.VideoBean> videos);

    int getPartitionId();
}
