package com.weituotian.video.presenter;

import com.weituotian.video.entity.AppMember;
import com.weituotian.video.entity.PageInfo;
import com.weituotian.video.entity.VideoListVo;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.mvpview.IMemberInfoView;
import com.weituotian.video.presenter.base.BasePresenter;
import com.weituotian.video.presenter.func1.ResultToEntityFunc1;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by ange on 2017/3/22.
 */

public class MemberInfoPresenter extends BasePresenter<IMemberInfoView> {

    public void getMemberInfo(Integer userId) {
        RetrofitFactory.getUserService().getMemberInfo(userId)
                .flatMap(new ResultToEntityFunc1<AppMember>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AppMember>() {
                    @Override
                    public void call(AppMember appMember) {
                        getView().onLoadMemberInfoSuccess(appMember);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        getView().onLoadMemberInfoError(throwable);
                    }
                });
    }

    public void getMemberVideos(Integer userId, Integer page, Integer pageSize) {
        RetrofitFactory.getUserService().getMemberVideos(userId, page, pageSize)
                .flatMap(new ResultToEntityFunc1<PageInfo<VideoListVo>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PageInfo<VideoListVo>>() {
                    @Override
                    public void call(PageInfo<VideoListVo> pageInfo) {
                        getView().onLoadVideos(pageInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }
}
