package com.weituotian.video.presenter.func1;

import com.weituotian.video.entity.RetInfo;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ange on 2017/3/19.
 */

/**
 * 第二版
 * 将Result<RetInfo<E>>处理为Observable<E>
 * @param <E> 实体类
 */
public class ResultToEntityFunc1<E> implements Func1<Result<RetInfo<E>>, Observable<E>>{

    /**
     * @param result 使用RetInfo<>包装了E实体类
     * @return Observable<E>
     */
    @Override
    public Observable<E> call(Result<RetInfo<E>> result) {
        if (result.isError()) {
            return Observable.error(result.error());
        }
        RetInfo<E> ret = result.response().body();
        if (!ret.isSuccess()) {//检查返回的json的success字段,如果不成功也抛出错误
            return Observable.error(new NullPointerException(ret.getMsg()));
        }
        return Observable.just(result.response().body().getObj());
    }

}

/**
 * 第一版
 * 将Result<RetInfo<E>>处理为Observable<E>
 * @param <RET> 继承RetInfo类
 * @param <E> 实体类
 */
/*
public class ResultToEntityFunc1<RET extends RetInfo<E>,E> implements Func1<Result<RET>, Observable<E>>{

    */
/**
     * @param result 使用RetInfo<>包装了E实体类
     * @return Observable<E>
     *//*

    @Override
    public Observable<E> call(Result<RET> result) {
        if (result.isError()) {
            return Observable.error(result.error());
        }
        RET ret = result.response().body();
        if (!ret.isSuccess()) {//检查返回的json的success字段,如果不成功也抛出错误
            return Observable.error(new NullPointerException(ret.getMsg()));
        }
        return Observable.just(result.response().body().getObj());
    }
}*/
