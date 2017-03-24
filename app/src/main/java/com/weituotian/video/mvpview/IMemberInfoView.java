package com.weituotian.video.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.weituotian.video.entity.AppMember;
import com.weituotian.video.entity.PageInfo;
import com.weituotian.video.entity.VideoListVo;

/**
 * Created by ange on 2017/3/22.
 */

public interface IMemberInfoView extends MvpView{

    void onLoadMemberInfoSuccess(AppMember appMember);

    void onLoadMemberInfoError(Throwable e);

    void onLoadVideos(PageInfo<VideoListVo> pageInfo);
}
