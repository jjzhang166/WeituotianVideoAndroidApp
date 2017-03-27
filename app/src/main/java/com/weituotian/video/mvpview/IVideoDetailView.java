package com.weituotian.video.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by ange on 2017/3/27.
 */

public interface IVideoDetailView extends MvpView {

    void onCheckCollect(boolean isCheck);

    void onCollectSuccess(Integer count);

    void onCollectError(Throwable throwable);

    void onCancelCollectSuccess(Integer count);

    void onCancelCollectError(Throwable throwable);

    void onCheckStar(boolean isCheck);

    void onStarMemberSuccess();

    void onStarMemberError(Throwable throwable);

    void onCancelStarMemberSuccess();

    void onCancelStarMemberError(Throwable throwable);

}
