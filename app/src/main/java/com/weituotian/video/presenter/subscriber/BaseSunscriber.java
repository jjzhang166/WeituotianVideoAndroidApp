package com.weituotian.video.presenter.subscriber;

import android.content.Context;

import com.weituotian.video.VideoApp;
import com.weituotian.video.utils.CommonUtil;
import com.weituotian.video.utils.UIUtil;

import rx.Subscriber;

/**
 * Created by ange on 2017/3/17.
 */

public abstract class BaseSunscriber<T> extends Subscriber<T> {
    @Override
    public void onStart() {
        super.onStart();

        Context context = VideoApp.getInstance();

        if (!CommonUtil.isNetworkAvailable(context)) {

            UIUtil.showToast(VideoApp.getInstance(),"当前网络不可用，请检查网络情况");
            // **一定要主动调用下面这一句**
            onCompleted();

            return;
        }

        // 显示进度条
//        showLoadingProgress();
    }

    @Override
    public void onCompleted() {
        //关闭等待进度条
//        closeLoadingProgress();

    }

}
