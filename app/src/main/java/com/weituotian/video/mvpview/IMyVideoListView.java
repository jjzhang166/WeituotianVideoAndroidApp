package com.weituotian.video.mvpview;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.weituotian.video.entity.VideoListVo;

import java.util.List;

/**
 * Created by ange on 2017/3/28.
 */

public interface IMyVideoListView extends MvpLceView<List<VideoListVo>> {

    void onLoadMoreData(List<VideoListVo> vos);
}
