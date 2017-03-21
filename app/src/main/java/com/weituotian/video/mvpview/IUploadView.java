package com.weituotian.video.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.weituotian.video.entity.Partition;

import java.util.List;

/**
 * 上传
 * Created by ange on 2017/3/20.
 */

public interface IUploadView extends MvpView {

    /**
     * 成功加载分区数据
     * @return
     */
     void onLoadPartitions(List<Partition> partitions);

    /**
     * 加载分区数据失败
     * @return
     */
    void onLoadPartitionsError(Throwable e);

    void onVideoUploadStart();

    void onVideoUploadProgress(Integer percent);

    void onVideoUploadSuccess(Integer attachmentId);
}
