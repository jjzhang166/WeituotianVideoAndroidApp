package com.weituotian.video.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weituotian.video.R;
import com.weituotian.video.http.DataType;
import com.weituotian.video.presenter.BiliPresenter;
import com.weituotian.video.presenter.VideoPresenter;

/**
 * Created by ange on 2017/3/14.
 */

public class BiliFragment extends VideoFragment {

    //代表本fragment的分区id
    private int partitionId;
    private static final String PARTITION_KEY="PARTITION";

    @Override
    public int getName() {
        return R.id.bili_video;
    }

    public static BiliFragment newInstance(int partitionId) {
        Bundle arguments = new Bundle();
        arguments.putString(PARTITION_KEY, String.valueOf(partitionId));
        BiliFragment biliFragment = new BiliFragment();
        biliFragment.setArguments(arguments);
        return biliFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            partitionId = Integer.valueOf(args.getString(PARTITION_KEY));
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public VideoPresenter createPresenter() {
        return new BiliPresenter();
    }

    public int getPartitionId() {
        return partitionId;
    }
}
