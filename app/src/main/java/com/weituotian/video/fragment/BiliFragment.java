package com.weituotian.video.fragment;

import com.weituotian.video.R;
import com.weituotian.video.http.DataType;
import com.weituotian.video.presenter.BiliPresenter;
import com.weituotian.video.presenter.VideoPresenter;

/**
 * Created by ange on 2017/3/14.
 */

public class BiliFragment extends VideoFragment {
    @Override
    public int getName() {
        return R.id.bili_video;
    }

    @Override
    public VideoPresenter createPresenter() {
        return new BiliPresenter();
    }
}
