package com.weituotian.video.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.weituotian.video.entity.Partition;

import java.util.List;

/**
 * Created by ange on 2017/3/28.
 */

public interface IHomeView extends MvpView {

    /**
     * 成功加载分区数据
     */
    void onLoadPartitions(List<Partition> partitions);

    /**
     * 加载分区数据失败
     */
    void onLoadPartitionsError(Throwable e);

}
