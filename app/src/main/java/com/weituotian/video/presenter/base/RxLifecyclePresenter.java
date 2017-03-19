package com.weituotian.video.presenter.base;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.OutsideLifecycleException;
import com.trello.rxlifecycle.RxLifecycle;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ange on 2017/3/15.
 */

public class RxLifecyclePresenter {
    private static final Func1<Integer, Integer> PRESENTER_LIFECYCLE =
            new Func1<Integer, Integer>() {
                @Override
                public Integer call(Integer lastEvent) {
                    switch (lastEvent) {
                        case PresenterEvent.ATTACH:
                            return PresenterEvent.DETACH;
                        case PresenterEvent.DETACH:
                            throw new OutsideLifecycleException(
                                    "Cannot bind to presenter lifecycle when outside of it");
                        default:
                            throw new UnsupportedOperationException(
                                    "Binding to " + lastEvent + " not yet implemented");
                    }
                }
            };


    public static <T> LifecycleTransformer<T> bindPresenter(Observable<Integer> lifecycle) {
        return RxLifecycle.bind(lifecycle, PRESENTER_LIFECYCLE);
    }
}
