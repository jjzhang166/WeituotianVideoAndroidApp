package com.weituotian.video.presenter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.weituotian.video.mvpview.IVideoView;

import javax.annotation.Nonnull;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * 仅仅实现presenter的状态周期循环
 * Created by ange on 2017/3/15.
 */

public class BaseLifecyclePresenter<V extends MvpView> extends MvpBasePresenter<V>  implements LifecycleProvider<Integer> {

    private final BehaviorSubject<Integer> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    public final Observable<Integer> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull Integer event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecyclePresenter.bindPresenter(lifecycleSubject);
    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
        lifecycleSubject.onNext(PresenterEvent.ATTACH);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        lifecycleSubject.onNext(PresenterEvent.DETACH);
    }

}
