package com.weituotian.video.http.service;

import com.weituotian.video.entity.RetInfo;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ange on 2017/3/27.
 */

public interface ICollectService {

    @POST("member/collect/check")
    Observable<Result<RetInfo<Boolean>>> checkCollect(@Query("videoId") Integer videoId);

    @POST("member/collect/add")
    Observable<Result<RetInfo<Integer>>> collect(@Query("videoId") Integer videoId);

    @POST("member/collect/delete")
    Observable<Result<RetInfo<Integer>>> cancelCollect(@Query("videoId") Integer videoId);
}
