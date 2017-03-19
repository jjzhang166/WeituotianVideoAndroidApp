package com.weituotian.video.presenter.func1;

import com.weituotian.video.entity.RetInfo;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ange on 2017/3/19.
 */

public class ResultToRetInfoFun1<E> implements Func1<Result<RetInfo<E>>, Observable<RetInfo<E>>> {

    @Override
    public Observable<RetInfo<E>> call(Result<RetInfo<E>> result) {
        if (result.isError()) {
            return Observable.error(result.error());
        }
        RetInfo<E> ret = result.response().body();
        return Observable.just(ret);
    }

}
