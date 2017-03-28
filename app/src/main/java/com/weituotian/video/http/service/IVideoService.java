package com.weituotian.video.http.service;

import com.weituotian.video.entity.CommentVo;
import com.weituotian.video.entity.FrontVideo;
import com.weituotian.video.entity.PageInfo;
import com.weituotian.video.entity.Partition;
import com.weituotian.video.entity.RetInfo;
import com.weituotian.video.entity.User;
import com.weituotian.video.entity.VideoListVo;

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
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by ange on 2017/3/20.
 */

public interface IVideoService {

    @Headers("Cache-Control: public, max-age=3600")
    @GET("video/av/partitions")
    Observable<Result<RetInfo<List<Partition>>>> getPartitions();

    @POST("video/av/info")
    Observable<Result<RetInfo<FrontVideo>>> getVideoInfo(@Query("videoId") Integer videoId);

    @POST("video/av/getSrc")
    Observable<Result<RetInfo<String>>> getVideoSrc(@Query("videoId") Integer videoId);

    @POST("video/av/comments")
    Observable<Result<RetInfo<PageInfo<CommentVo>>>> getComments(@Query("videoId") Integer videoId, @Query("page") Integer page, @Query("size") Integer size);

    /**
     * 获得视频列表
     * @param partitionId 分区id
     * @param page 当前页码
     * @param size 页面大小
     * @return
     */
    @POST("video/av/partition_videos")
    Observable<Result<RetInfo<PageInfo<VideoListVo>>>> getParititionVideos(@Query("partitionId") Integer partitionId, @Query("page") Integer page, @Query("size") Integer size);


    @POST("member/video/ajaxvideo")
    @Multipart
    Observable<Result<RetInfo<Integer>>> uploadVideo(@QueryMap Map<String, String> options,
                                                     @PartMap Map<String, RequestBody> externalFileParameters);

    @POST("member/video/doadd")
    Observable<Result<RetInfo<String>>> submitVideo(@QueryMap Map<String, String> options);


    @POST("member/my/addcomment")
    Observable<Result<RetInfo<String>>> addComment(@Query("videoId") Integer videoId, @Query("content") String content);
}
