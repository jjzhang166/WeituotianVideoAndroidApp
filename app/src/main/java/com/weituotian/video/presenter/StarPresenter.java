package com.weituotian.video.presenter;

import com.weituotian.video.entity.AppMember;
import com.weituotian.video.entity.PageInfo;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.mvpview.IStarView;
import com.weituotian.video.presenter.base.BasePresenter;
import com.weituotian.video.presenter.func1.ResultToEntityFunc1;

import rx.functions.Action1;

/**
 * 关注
 * Created by ange on 2017/3/31.
 */

public class StarPresenter extends BasePresenter<IStarView> {
    private Integer userId;

    private Integer page = 1;

    private Integer size = 10;

    public void getStars(final boolean isLoadMore) {
        if (isLoadMore) {
            page++;
        } else {
            page = 1;
        }
        RetrofitFactory.getUserService().getstars(userId, page, size)
                .flatMap(new ResultToEntityFunc1<PageInfo<AppMember>>())
                .compose(this.<PageInfo<AppMember>>bindToLifecycle())
                .compose(this.<PageInfo<AppMember>>applySchedulers())
                .subscribe(new Action1<PageInfo<AppMember>>() {
                    @Override
                    public void call(PageInfo<AppMember> pageInfo) {
                        if (isLoadMore) {
                            getView().onLoadMoreStars(pageInfo);
                        } else {
                            getView().onLoadStars(pageInfo);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        getView().onLoadError(throwable);
                    }
                });
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
