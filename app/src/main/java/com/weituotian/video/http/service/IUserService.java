package com.weituotian.video.http.service;

import com.weituotian.video.entity.AppMember;
import com.weituotian.video.entity.PageInfo;
import com.weituotian.video.entity.RetInfo;
import com.weituotian.video.entity.User;
import com.weituotian.video.entity.VideoListVo;

import java.util.List;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ange on 2017/3/16.
 */

public interface IUserService {

    @POST("api/login")
    Observable<Result<RetInfo<User>>> doLogin(@Query("username") String username, @Query("password") String password);

    @POST("api/logout")
    Observable<Result<RetInfo<String>>> logout();

    @POST("api/touch")
    Observable<Result<RetInfo<User>>> touch();

    @POST("api/member/info/{id}")
    Observable<Result<RetInfo<AppMember>>> getMemberInfo(@Path("id") Integer userId);

    //    @POST("upload/cover/1.json")
    @POST("api/member/videos")
    Observable<Result<RetInfo<PageInfo<VideoListVo>>>> getMemberVideos(@Query("userId") Integer userId, @Query("page") Integer page, @Query("pageSize") Integer pageSize);

    @POST("member/reg/doreg")
    Observable<Result<RetInfo<User>>> doreg(@Query("loginName") String loginName, @Query("name") String name, @Query("email") String email, @Query("password") String password);
}
