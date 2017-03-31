package com.weituotian.video.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.weituotian.video.entity.AppMember;
import com.weituotian.video.entity.PageInfo;

/**
 * 关注
 * Created by ange on 2017/3/31.
 */

public interface IStarView extends MvpView{


    void onLoadStars(PageInfo<AppMember> pageInfo);

    void onLoadMoreStars(PageInfo<AppMember> pageInfo);

    void onLoadError(Throwable throwable);

}
