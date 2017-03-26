package com.weituotian.video.presenter;

import com.weituotian.video.entity.CommentVo;
import com.weituotian.video.entity.PageInfo;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.mvpview.ICommentView;
import com.weituotian.video.presenter.base.BasePresenter;
import com.weituotian.video.presenter.func1.ResultToEntityFunc1;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by ange on 2017/3/26.
 */

public class CommentPresenter extends BasePresenter<ICommentView> {

    public void getComments(Integer videoId, Integer page, Integer size) {
        RetrofitFactory.getVideoService().getComments(videoId, page, size)
                .flatMap(new ResultToEntityFunc1<PageInfo<CommentVo>>())
                .compose(this.<PageInfo<CommentVo>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PageInfo<CommentVo>>() {
                    @Override
                    public void call(PageInfo<CommentVo> commentVoPageInfo) {
                        getView().onGetComments(commentVoPageInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        getView().onGetCommentsError(throwable);
                    }
                });

    }

    //是否正在发送评论
    private boolean sending = false;

    public void sendComment(Integer videoId, String content) {
        sending = true;
        RetrofitFactory.getVideoService().addComment(videoId, content)
                .flatMap(new ResultToEntityFunc1<String>())
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        getView().onSendCommentSuccess();
                        sending = false;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        getView().onSendCommentError(throwable);
                        sending = false;
                    }
                });
    }
}
