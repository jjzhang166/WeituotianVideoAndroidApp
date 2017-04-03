package com.weituotian.video.presenter;

import android.util.Log;

import com.weituotian.video.VideoApp;
import com.weituotian.video.entity.DaoSession;
import com.weituotian.video.entity.FrontVideo;
import com.weituotian.video.entity.HistoryVideo;
import com.weituotian.video.entity.HistoryVideoDao;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.mvpview.IVideoPlayView;
import com.weituotian.video.presenter.base.BasePresenter;
import com.weituotian.video.presenter.func1.ResultToEntityFunc1;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.rx.RxDao;

import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ange on 2017/3/24.
 */

public class VideoPlayPresenter extends BasePresenter<IVideoPlayView> {

    private HistoryVideoDao historyVideoDao;
    QueryBuilder<HistoryVideo> query;


    public VideoPlayPresenter() {
        DaoSession daoSession = VideoApp.getInstance().getDaoSession();
        historyVideoDao = daoSession.getHistoryVideoDao();
        query = historyVideoDao.queryBuilder().orderAsc(HistoryVideoDao.Properties.ViewTime).limit(deleteSize);
    }

    public void getVideoInfo(Integer videoId) {
        RetrofitFactory.getVideoService().getVideoInfo(videoId)
                .flatMap(new ResultToEntityFunc1<FrontVideo>())
                .compose(this.<FrontVideo>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FrontVideo>() {
                    @Override
                    public void call(FrontVideo frontVideo) {
                        getView().onLoadVideoInfo(frontVideo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        getView().onLoadVideInfoError(throwable);
                    }
                });
    }

    public void getVideoSrc(Integer videoId) {
        RetrofitFactory.getVideoService().getVideoSrc(videoId)
                .flatMap(new ResultToEntityFunc1<String>())
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String url) {
                        getView().onLoadVideoSrc(url);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        getView().onLoadVideInfoError(throwable);
                    }
                });
    }

    private Integer limitSize = 60;
    private Integer deleteSize = 10;

    /*增加播放历史*/
    public void addHistory(FrontVideo frontVideo) {

        /*final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());*/
        final HistoryVideo historyVideo = new HistoryVideo();
        historyVideo.setId((long) frontVideo.getId());
        historyVideo.setTitle(frontVideo.getTitle());
        historyVideo.setClick(frontVideo.getClick());
        historyVideo.setCollect(frontVideo.getCollect());
        historyVideo.setCover(frontVideo.getCover());
        historyVideo.setCreateTime(frontVideo.getCreateTime());
        historyVideo.setDescript(frontVideo.getDescript());
        historyVideo.setMemberName(frontVideo.getMemberName());
        historyVideo.setPartitionName(frontVideo.getPartitionName());
        historyVideo.setTotalTime(frontVideo.getTotalTime());
        historyVideo.setPlay(frontVideo.getPlay());
        //观看时间
        historyVideo.setViewTime(new Date());

        final RxDao<HistoryVideo, Long> historyVideoDaoRx = historyVideoDao.rx();

        historyVideoDaoRx.insertOrReplace(historyVideo)
                .flatMap(new Func1<HistoryVideo, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(HistoryVideo historyVideo) {
                        return historyVideoDaoRx.count();
                    }
                })
                .flatMap(new Func1<Long, Observable<List<HistoryVideo>>>() {
                    @Override
                    public Observable<List<HistoryVideo>> call(Long aLong) {
                        if (aLong > limitSize) {
                            return query.rx().list();
                        }
                        return Observable.never();
                    }
                })
                .flatMap(new Func1<List<HistoryVideo>, Observable<Void>>() {
                    @Override
                    public Observable<Void> call(List<HistoryVideo> historyVideos) {
                        Log.i("video play presenter", "删除videos:" + historyVideos.toString());
                        return historyVideoDaoRx.deleteInTx(historyVideos);
                    }
                })
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

        /*Observable.concat(historyVideoDaoRx.insert(historyVideo), historyVideoDaoRx.count())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (o instanceof Long) {
                            if (((Long) o) > 60) {
                                query.rx().list().observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Action1<List<HistoryVideo>>() {
                                            @Override
                                            public void call(List<HistoryVideo> historyVideos) {
                                                historyVideoDaoRx.deleteInTx(historyVideos).subscribe(new Action1<Void>() {
                                                    @Override
                                                    public void call(Void aVoid) {

                                                    }
                                                });
                                            }
                                        });
                            }
                        }
                    }
                });*/


        /*historyVideoDao.insert(historyVideo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HistoryVideo>() {
                    @Override
                    public void call(HistoryVideo historyVideo1) {
                        //do something handle historyVideo1

                        Observable<Long> count = historyVideoDao.count();
                        count.observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Long>() {
                                    @Override
                                    public void call(Long aLong) {
                                        // do something to handle along
                                    }
                                });

                    }
                });*/

                /*.concatMap(new Func1<HistoryVideo, Observable<?>>() {
                    @Override
                    public Observable<?> call(HistoryVideo historyVideo) {
                        return historyVideoDao.count();
                    }
                });*/

    }
}
