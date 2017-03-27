package com.weituotian.video.presenter;

import com.weituotian.video.entity.Partition;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.mvpview.IHomeView;
import com.weituotian.video.presenter.base.BasePresenter;
import com.weituotian.video.presenter.func1.ResultToEntityFunc1;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by ange on 2017/3/28.
 */

public class HomePresenter extends BasePresenter<IHomeView> {

    /**
     * 获取分区
     */
    public void getPartitions() {
        RetrofitFactory.getVideoService().getPartitions()
                .flatMap(new ResultToEntityFunc1<List<Partition>>())
                .compose(this.<List<Partition>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Partition>>() {
                    @Override
                    public void call(List<Partition> partitions) {
                        getView().onLoadPartitions(partitions);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        getView().onLoadPartitionsError(throwable);

                    }
                });
    }
}
