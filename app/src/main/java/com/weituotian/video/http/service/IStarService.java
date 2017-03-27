package com.weituotian.video.http.service;

import com.weituotian.video.entity.RetInfo;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 关注服务
 * Created by ange on 2017/3/27.
 */

public interface IStarService {

    @POST("member/follow/check")
    Observable<Result<RetInfo<Boolean>>> checkStar(@Query("target") Integer target);

    @POST("member/follow/add")
    Observable<Result<RetInfo<String>>> star(@Query("target") Integer target);

    @POST("member/follow/delete")
    Observable<Result<RetInfo<String>>> cancelStar(@Query("target") Integer target);
}
