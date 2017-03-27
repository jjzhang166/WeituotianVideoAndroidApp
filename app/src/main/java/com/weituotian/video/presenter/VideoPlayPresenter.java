package com.weituotian.video.presenter;

import com.weituotian.video.entity.FrontVideo;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.mvpview.IVideoPlayView;
import com.weituotian.video.presenter.base.BasePresenter;
import com.weituotian.video.presenter.func1.ResultToEntityFunc1;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by ange on 2017/3/24.
 */

public class VideoPlayPresenter extends BasePresenter<IVideoPlayView> {

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

    public void getVideoSrc(Integer videoId) {
        RetrofitFactory.getVideoService().getVideoSrc(videoId)
                .flatMap(new ResultToEntityFunc1<String>())
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String url) {
                        getView().onLoadVideoSrc(url);
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
