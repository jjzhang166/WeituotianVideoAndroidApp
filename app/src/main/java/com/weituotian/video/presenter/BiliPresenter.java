package com.weituotian.video.presenter;

import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.http.DataType;

import rx.Observable;

/**
 * Created by ange on 2017/3/14.
 */

public class BiliPresenter extends VideoPresenter {
    private static final int PAGE_SIZE = 20;

    @Override
    public Observable<String> getHttpCallObservable(int curPage) {
        int offset = (curPage - 1) * PAGE_SIZE;
        return RetrofitFactory.getBiliVideoService().getVideos(PAGE_SIZE, offset);
    }

    @Override
    public DataType getType() {
        return DataType.BILI;
    }
}
