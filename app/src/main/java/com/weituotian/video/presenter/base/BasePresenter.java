package com.weituotian.video.presenter.base;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.weituotian.video.entity.RetInfo;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ange on 2017/3/17.
 */

public class BasePresenter<V extends MvpView> extends BaseLifecyclePresenter<V> {

    /**
     * 处理observable,让它可以处理连接服务器失败的情况
     * 同时将Observable<Result<RET>>转换为Observable<E>
     * 同时绑定状态周期
     *
     * @param observable 包装了Result<RET>的observable
     * @param <RET>      使用RetInfo<>包装了T实体类
     * @param <E>        　实体类,我们要用的bean
     * @return Observable<E>
     */
    protected <RET extends RetInfo<E>, E> Observable<E> handleObservable(final Observable<Result<RET>> observable) {
        return observable.flatMap(new Func1<Result<RET>, Observable<E>>() {
            @Override
            public Observable<E> call(Result<RET> retInfoResult) {
                if (retInfoResult.isError()) {
                    return Observable.error(new NullPointerException("网络连接错误"));
                }
                RET ret = retInfoResult.response().body();
                if (!ret.isSuccess()) {//检查返回的json的success字段,如果不成功也抛出错误
                    return Observable.error(new NullPointerException(ret.getMsg()));
                }
                return Observable.just(retInfoResult.response().body().getObj());
            }
        }).compose(this.<E>bindToLifecycle());
    }

}