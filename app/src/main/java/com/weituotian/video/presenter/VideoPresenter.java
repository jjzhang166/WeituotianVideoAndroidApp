package com.weituotian.video.presenter;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.weituotian.video.entity.BiliDingVideo;
import com.weituotian.video.mvpview.VideoMvpView;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author laohu
 * @site http://ittiger.cn
 */
public abstract class VideoPresenter extends MvpBasePresenter<VideoMvpView>
        implements TypePresenter {

    private int mCurPage = 1;

    public void refreshData(boolean pullToRefresh) {

        mCurPage = 1;
        request(false, pullToRefresh);
    }

    public void loadMoreData() {

        request(true, false);
    }

    @Override
    public void detachView(boolean retainInstance) {

        super.detachView(retainInstance);
        mCurPage = 1;
    }

    public abstract Observable<String> getHttpCallObservable(int curPage);

    void request(final boolean loadMore, final boolean pullToRefresh) {

        getHttpCallObservable(mCurPage)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<BiliDingVideo.VideoBean>>() {

                    @Override
                    public List<BiliDingVideo.VideoBean> call(String result) {

                        Gson gson = new Gson();
                        BiliDingVideo dingVideo = gson.fromJson(result, BiliDingVideo.class);
                        return dingVideo.getList();

                    }
                })
                .flatMap(new Func1<List<BiliDingVideo.VideoBean>, Observable<List<BiliDingVideo.VideoBean>>>() {
                    @Override
                    public Observable<List<BiliDingVideo.VideoBean>> call(List<BiliDingVideo.VideoBean> videos) {

                        if (videos == null || videos.size() == 0) {
                            return Observable.error(new NullPointerException("not load video data"));
                        }
                        return Observable.just(videos);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
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
                                getView().setData(videos);
                            } else {
                                getView().setLoadMoreData(videos);
                            }
                        }
                    }
                });
    }
}
