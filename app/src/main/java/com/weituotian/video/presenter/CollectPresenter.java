package com.weituotian.video.presenter;

import com.weituotian.video.entity.PageInfo;
import com.weituotian.video.entity.VideoListVo;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.mvpview.ICollectView;
import com.weituotian.video.presenter.base.BasePresenter;
import com.weituotian.video.presenter.func1.ResultToEntityFunc1;

import rx.functions.Action1;

/**
 * 收藏
 * Created by ange on 2017/3/31.
 */

public class CollectPresenter extends BasePresenter<ICollectView> {

    private Integer userId;

    private Integer page = 1;

    private Integer size = 10;

    public void getCollects(final boolean isLoadMore) {
        if (isLoadMore) {
            page++;
        } else {
            page = 1;
        }
        RetrofitFactory.getUserService().getCollects(userId, page, size)
                .flatMap(new ResultToEntityFunc1<PageInfo<VideoListVo>>())
                .compose(this.<PageInfo<VideoListVo>>bindToLifecycle())
                .compose(this.<PageInfo<VideoListVo>>applySchedulers())
                .subscribe(new Action1<PageInfo<VideoListVo>>() {
                    @Override
                    public void call(PageInfo<VideoListVo> pageInfo) {
                        if (isLoadMore) {
                            getView().onLoadMoreCollects(pageInfo);
                        } else {
                            getView().onLoadCollects(pageInfo);
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
