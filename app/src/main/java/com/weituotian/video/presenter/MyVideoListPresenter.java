package com.weituotian.video.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.weituotian.video.entity.PageInfo;
import com.weituotian.video.entity.VideoListVo;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.mvpview.IMyVideoListView;
import com.weituotian.video.presenter.base.BasePresenter;
import com.weituotian.video.presenter.func1.ResultToEntityFunc1;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by ange on 2017/3/28.
 */

public class MyVideoListPresenter extends BasePresenter<IMyVideoListView> {

    private Integer parititionId;
    private Integer page = 1;
    private Integer size = 10;

    public void loadVideos(final boolean pullToRefresh) {
        if (pullToRefresh) {
            page = 1;//下拉刷新的情况,page重置为1,其它情况按当前page获取
        }

        getView().showLoading(pullToRefresh);

        RetrofitFactory.getVideoService().getParititionVideos(parititionId, page, size)
                .flatMap(new ResultToEntityFunc1<PageInfo<VideoListVo>>())
                .compose(this.<PageInfo<VideoListVo>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PageInfo<VideoListVo>>() {
                    @Override
                    public void call(PageInfo<VideoListVo> pageInfo) {
                        if (pullToRefresh) {//上拉刷新
                            getView().setData(pageInfo.getList());
                        }else{
                            //下拉加载
                            getView().onLoadMoreData(pageInfo.getList());
                        }
                        getView().showContent();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        getView().showError(throwable, pullToRefresh);
                    }
                });
    }

    /**
     * 页码加1
     */
    public void pagePlus(){
        page++;
    }

    public Integer getParititionId() {
        return parititionId;
    }

    public void setParititionId(Integer parititionId) {
        this.parititionId = parititionId;
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
