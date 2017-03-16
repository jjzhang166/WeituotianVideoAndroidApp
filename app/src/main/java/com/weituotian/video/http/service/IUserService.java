package com.weituotian.video.http.service;

import com.weituotian.video.entity.RetInfo;
import com.weituotian.video.entity.User;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ange on 2017/3/16.
 */

public interface IUserService {

    @POST("api/login")
    Observable<Result<RetInfo<User>>> doLogin(@Query("username") String username, @Query("password") String password);

}
