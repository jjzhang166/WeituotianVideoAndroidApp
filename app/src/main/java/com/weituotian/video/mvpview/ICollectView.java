package com.weituotian.video.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.weituotian.video.entity.PageInfo;
import com.weituotian.video.entity.VideoListVo;

/**
 * 收藏
 * Created by ange on 2017/3/31.
 */

public interface ICollectView extends MvpView{

    void onLoadCollects(PageInfo<VideoListVo> pageInfo);

    void onLoadMoreCollects(PageInfo<VideoListVo> pageInfo);

    void onLoadError(Throwable throwable);
}
