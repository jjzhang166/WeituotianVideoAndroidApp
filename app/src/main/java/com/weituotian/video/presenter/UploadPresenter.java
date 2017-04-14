package com.weituotian.video.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weituotian.video.entity.Partition;
import com.weituotian.video.entity.RetInfo;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.http.LoginContext;
import com.weituotian.video.http.okhttp.DefaultProgressListener;
import com.weituotian.video.http.okhttp.ProgressListener;
import com.weituotian.video.http.okhttp.UploadFileRequestBody;
import com.weituotian.video.mvpview.IUploadView;
import com.weituotian.video.presenter.base.BasePresenter;
import com.weituotian.video.presenter.func1.ResultToEntityFunc1;
import com.weituotian.video.presenter.func1.ResultToRetInfoFun1;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by ange on 2017/3/20.
 */

public class UploadPresenter extends BasePresenter<IUploadView> {

    /**
     * 获取分区
     */
    public void getPartitions() {
        RetrofitFactory.getVideoService().getPartitions()
                .flatMap(new ResultToEntityFunc1<List<Partition>>())
                .compose(this.<List<Partition>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Partition>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getView().onLoadPartitionsError(e);
                    }

                    @Override
                    public void onNext(List<Partition> partitions) {
                        getView().onLoadPartitions(partitions);
                    }
                });

    }

    private RequestCall videoCall;
    private RequestCall coverCall;

    public void uploadVideo(File file) {
       /*Map<String, String> optionMap = new HashMap<>();
        optionMap.put("Platformtype", "Android");
        optionMap.put("loginname", LoginContext.user.getLoginname());

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(file, mProgressListener);
        requestBodyMap.put("file\"; filename=\"" + file.getName(), fileRequestBody);

        RetrofitFactory.getVideoService().uploadVideo(optionMap, requestBodyMap)
                .flatMap(new ResultToEntityFunc1<Integer>())
                .compose(this.<Integer>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.i("uploadVideo", "oncomplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i("uploadVideo", integer.toString());

                    }
                });*/

        videoCall = OkHttpUtils.post()//
                .url(RetrofitFactory.BASE_SERVER_URL + "member/video/ajaxvideo")
                .addFile("file", file.getName(), file)//
//                .addParams("username", LoginContext.user.getName())
                .addHeader("Content-Type", "multipart/form-data")
                .build();

        videoCall.execute(new StringCallback() {
            private Integer percent = 0;

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.w("", "Exception:" + e.getMessage());

            }

            @Override
            public void inProgress(float progress, long total, int id) {
                int cur = (int) (progress * 100);
                if (cur > percent) {//进度改变达到百分之一就
                    percent = cur;
                    Log.i("UploadPresenter", "progress:" + percent + "%");
                    if (isViewAttached()) {
                        getView().onVideoUploadProgress(percent);
                    }
                }
            }

            @Override
            public void onResponse(String response, int id) {
//                Log.i("UploadPresenter", "response:" + response);
                RetInfo<Integer> retInfo = new Gson().fromJson(response, new TypeToken<RetInfo<Integer>>() {
                }.getType());
                Integer attachmentID = retInfo.getObj();
//                Log.i("UploadPresenter", "attachmentID:" + attachmentID);
                if (isViewAttached()) {
                    getView().onVideoUploadSuccess(attachmentID);
                }
            }
        });

    }

    public void cancelVideo() {
        if (videoCall != null) {
            videoCall.cancel();
        }
    }

    public void uploadCover(File file) {
        coverCall = OkHttpUtils.post()//
                .url(RetrofitFactory.BASE_SERVER_URL + "member/video/ajaxcover")
                .addFile("file", file.getName(), file)//
//                .addParams("username", LoginContext.user.getName())
                .addHeader("Content-Type", "multipart/form-data")
                .build();

        coverCall.execute(new StringCallback() {
            private Integer percent = 0;

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.w("", "Exception:" + e.getMessage());

            }

            @Override
            public void inProgress(float progress, long total, int id) {
                int cur = (int) (progress * 100);
                if (cur > percent) {//进度改变达到百分之一就
                    percent = cur;
                    Log.i("UploadPresenter", "progress:" + percent + "%");
                    if (isViewAttached()) {
                        getView().onVideoUploadProgress(percent);
                    }
                }
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("UploadPresenter", "response:" + response);
                RetInfo<Map<String, String>> retInfo = new Gson().fromJson(response, new TypeToken<RetInfo<Map<String, String>>>() {
                }.getType());
                String coverurl = retInfo.getObj().get("filename");
                if (isViewAttached()) {
                    getView().onCoverUploadSuccess(coverurl);
                }
            }
        });
    }

    public void submitVideo(Map<String, String> optionMap) {
        RetrofitFactory.getVideoService().submitVideo(optionMap)
                .flatMap(new ResultToRetInfoFun1<String>())
                .compose(this.<RetInfo<String>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RetInfo<String>>() {
                    @Override
                    public void call(RetInfo<String> retInfo) {
                        getView().onSubmitVideoFinish(retInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private ProgressListener mProgressListener = new ProgressListener() {
        @Override
        public void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish) {

//            Log.i("uploadVideo", "----the current " + hasWrittenLen + "----" + totalLen + "-----" + (hasWrittenLen * 100 / totalLen));

//            System.out.println("----the current " + hasWrittenLen + "----" + totalLen + "-----" + (hasWrittenLen * 100 / totalLen));

            int percent = (int) (hasWrittenLen * 100 / totalLen);
            if (percent > 100) percent = 100;
            if (percent < 0) percent = 0;

            /*Observable.just(percent)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Integer>() {
                        @Override
                        public void call(Integer percent) {
                            getView().onVideoUploadProgress(percent);
                        }
                    });*/
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 1:
                    if (msg.what > 0) {
//                        uploadImageView.updatePercent(msg.what);
                    }
                    break;
            }
        }
    };
}
