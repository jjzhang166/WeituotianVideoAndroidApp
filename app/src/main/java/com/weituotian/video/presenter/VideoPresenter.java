package com.weituotian.video.presenter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.weituotian.video.entity.BiliDingVideo;
import com.weituotian.video.mvpview.IVideoView;

import java.util.List;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * @author laohu
 * @site http://ittiger.cn
 */
public abstract class VideoPresenter extends BasePresenter<IVideoView>
        implements TypePresenter {

    private int mCurPage = 1;

    /**
     * 刷新第一页数据
     *
     * @param pullToRefresh 是否上拉刷新
     */
    public void refreshData(boolean pullToRefresh) {

        mCurPage = 1;
        request(false, pullToRefresh);
    }

    /**
     * 加载更多数据
     */
    public void loadMoreData() {
        request(true, false);
    }

    @Override
    public void attachView(IVideoView view) {
        super.attachView(view);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        mCurPage = 1;
    }

    public abstract Observable<Result<BiliDingVideo>> getHttpCallObservable(int curPage);

    void request(final boolean loadMore, final boolean pullToRefresh) {

        getHttpCallObservable(mCurPage)
                .flatMap(new Func1<Result<BiliDingVideo>, Observable<BiliDingVideo>>() {
                    @Override
                    public Observable<BiliDingVideo> call(Result<BiliDingVideo> result) {
                        if (result.isError()) {
                            return Observable.error(new NullPointerException("网络连接错误"));
                        }
                        return Observable.just(result.response().body());
                    }
                })
                .flatMap(new Func1<BiliDingVideo, Observable<List<BiliDingVideo.VideoBean>>>() {

                    @Override
                    public Observable<List<BiliDingVideo.VideoBean>> call(BiliDingVideo dingVideo) {
                        List<BiliDingVideo.VideoBean> videos = dingVideo.getList();

                        if (dingVideo.getCode() != 0) {
                            return Observable.error(new NullPointerException("not load video data"));
                        }
                        /*if (videos == null || videos.size() == 0) {
                            return Observable.error(new NullPointerException("not load video data"));
                        }*/
                        return Observable.just(dingVideo.getList().subList(0, 5));//提取5个，测试用
                    }
                })
                .subscribeOn(Schedulers.io())//以上运行在io进程
                .observeOn(AndroidSchedulers.mainThread())//以下运行在main ui线程
                .compose(this.<List<BiliDingVideo.VideoBean>>bindToLifecycle())
                .subscribe(new Subscriber<List<BiliDingVideo.VideoBean>>() {
                    @Override
                    public void onCompleted() {

                        getView().showContent();
                        mCurPage++;
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (!loadMore) {
                            getView().showError(e, pullToRefresh);
                        } else {
                            getView().showLoadMoreErrorView();
                        }
                    }

                    @Override
                    public void onNext(List<BiliDingVideo.VideoBean> videos) {

                        if (isViewAttached()) {
                            if (!loadMore) {
                                getView().setData(videos);//上拉刷新
                            } else {
                                getView().setLoadMoreData(videos);//继续加载数据
                            }
                        }
                    }
                });
    }
}
