package com.weituotian.video.http.service;

import com.weituotian.video.entity.Partition;
import com.weituotian.video.entity.RetInfo;
import com.weituotian.video.entity.User;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by ange on 2017/3/20.
 */

public interface IVideoService {

    @Headers("Cache-Control: public, max-age=3600")
    @GET("video/av/partitions")
    Observable<Result<RetInfo<List<Partition>>>> getPartitions();

    @POST("member/video/ajaxvideo")
    @Multipart
    Observable<Result<RetInfo<Integer>>> uploadVideo(@QueryMap Map<String, String> options,
                                            @PartMap Map<String, RequestBody> externalFileParameters) ;

}