package com.weituotian.video.http.provider;

import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.http.service.IBiliService;
import com.weituotian.video.presenter.func1.ResultToEntityFunc1;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ange on 2017/3/27.
 */

public class StarProvider {

    private static StarProvider starProvider;

    public static StarProvider getInstance() {

        if (starProvider == null) {
            synchronized (StarProvider.class) {
                if (starProvider == null) {
                    starProvider = new StarProvider();
                }
            }
        }
        return starProvider;
    }

    public Observable<String> starMember(Integer target) {
        return RetrofitFactory.getStarService().star(target)
                .flatMap(new ResultToEntityFunc1<String>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> cancelStar(Integer target) {
        return RetrofitFactory.getStarService().cancelStar(target)
                .flatMap(new ResultToEntityFunc1<String>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Boolean> checkStar(Integer target) {
        return RetrofitFactory.getStarService().checkStar(target)
                .flatMap(new ResultToEntityFunc1<Boolean>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
