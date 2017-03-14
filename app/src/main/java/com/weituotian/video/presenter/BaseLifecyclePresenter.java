package com.weituotian.video.presenter;

import android.support.annotation.CallSuper;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.annotation.Nonnull;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 *
 * Created by ange on 2017/3/15.
 */

public class BaseLifecyclePresenter {



/*    @Override
    @CallSuper
    public void attachView(V view) {
        super.attachView(view);
        lifecycleSubject.onNext(PresenterEvent.ATTACH);
    }

    @Override
    @CallSuper
    public void detachView() {
        super.detachView();
        lifecycleSubject.onNext(PresenterEvent.DETACH);
    }

    @Nonnull
    @Override
    public Observable<Integer> lifecycle() {
        return null;
    }

    @Nonnull
    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(@Nonnull Integer event) {
        return null;
    }

    @Nonnull
    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return null;
    }*/
}
