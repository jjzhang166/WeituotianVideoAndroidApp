package com.weituotian.video.presenter;

import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.http.LoginContext;
import com.weituotian.video.http.provider.StarProvider;
import com.weituotian.video.mvpview.IVideoDetailView;
import com.weituotian.video.presenter.base.BasePresenter;
import com.weituotian.video.presenter.func1.ResultToEntityFunc1;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by ange on 2017/3/27.
 */

public class VideoDetailPresenter extends BasePresenter<IVideoDetailView> {

    public void checkCollect(Integer videoId) {
        if (LoginContext.isLogin()) {
            RetrofitFactory.getCollectService().checkCollect(videoId)
                    .flatMap(new ResultToEntityFunc1<Boolean>())
                    .compose(this.<Boolean>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean isCheck) {
                            getView().onCheckCollect(isCheck);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                        }
                    });
        }
    }

    private boolean sending = false;

    public void collect(Integer videoId) {
        if (sending) {
            return;
        }
        sending = true;
        RetrofitFactory.getCollectService().collect(videoId)
                .flatMap(new ResultToEntityFunc1<Integer>())
                .compose(this.<Integer>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        getView().onCollectSuccess(integer);
                        sending = false;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        getView().onCollectError(throwable);
                        sending = false;
                    }
                });
    }

    public void cancelCollect(Integer videoId) {
        if (sending) {
            return;
        }
        sending = true;
        RetrofitFactory.getCollectService().cancelCollect(videoId)
                .flatMap(new ResultToEntityFunc1<Integer>())
                .compose(this.<Integer>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        getView().onCancelCollectSuccess(integer);
                        sending = false;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        getView().onCancelCollectError(throwable);
                        sending = false;
                    }
                });
    }

    public void checkStar(Integer target) {
        if (LoginContext.isLogin()) {
            StarProvider.getInstance().checkStar(target)
                    .compose(this.<Boolean>bindToLifecycle())
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            getView().onCheckStar(aBoolean);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                        }
                    });
        }
    }

    public void starMember(Integer target) {
        StarProvider.getInstance().starMember(target)
                .compose(this.<String>bindToLifecycle())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String str) {
                        getView().onStarMemberSuccess();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        getView().onStarMemberError(throwable);
                    }
                });
    }

    public void cancelStar(Integer target) {
        StarProvider.getInstance().cancelStar(target)
                .compose(this.<String>bindToLifecycle())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        getView().onCancelStarMemberSuccess();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        getView().onCancelStarMemberError(throwable);
                    }
                });
    }
}
