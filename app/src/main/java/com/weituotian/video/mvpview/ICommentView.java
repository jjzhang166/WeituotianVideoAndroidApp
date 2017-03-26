package com.weituotian.video.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.weituotian.video.entity.CommentVo;
import com.weituotian.video.entity.PageInfo;

/**
 * Created by ange on 2017/3/26.
 */

public interface ICommentView extends MvpView {

    void onGetComments(PageInfo<CommentVo> pageInfo);

    void onGetCommentsError(Throwable throwable);

    void onSendCommentSuccess();

    void onSendCommentError(Throwable throwable);
}
