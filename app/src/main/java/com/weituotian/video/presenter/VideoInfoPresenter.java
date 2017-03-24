package com.weituotian.video.presenter;

import com.weituotian.video.entity.AppMember;
import com.weituotian.video.entity.FrontVideo;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.mvpview.IVideoInfoView;
import com.weituotian.video.presenter.base.BasePresenter;
import com.weituotian.video.presenter.func1.ResultToEntityFunc1;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by ange on 2017/3/24.
 */

public class VideoInfoPresenter extends BasePresenter<IVideoInfoView> {

    public void getVideoInfo(Integer videoId) {
        RetrofitFactory.getVideoService().getVideoInfo(videoId)
                .flatMap(new ResultToEntityFunc1<FrontVideo>())
                .compose(this.<FrontVideo>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FrontVideo>() {
                    @Override
                    public void call(FrontVideo frontVideo) {
                        getView().onLoadVideoInfo(frontVideo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        getView().onLoadVideInfoError(throwable);
                    }
                });
    }

}
