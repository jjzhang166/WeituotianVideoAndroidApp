package com.weituotian.video.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.weituotian.video.R;
import com.weituotian.video.adapter.BiliDingVideoAdapter;
import com.weituotian.video.entity.BiliDingVideo;
import com.weituotian.video.utils.OkHttpClientManager;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by yifeng on 16/8/3.
 */
public class TabListFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    protected String URL;

    private static final String EXTRA_CONTENT = "content";

    private RecyclerView mRecyclerView;

    private BiliDingVideoAdapter mBiliDingVideoAdapter;

    public static TabListFragment newInstance(String content) {
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_CONTENT, content);
        TabListFragment tabContentFragment = new TabListFragment();
        tabContentFragment.setArguments(arguments);
        return tabContentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_tab_list, container, false);

        //初始化RecyclerView及其LayoutManager
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.rv_videolist);
        StaggeredGridLayoutManager mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);

        //设置箭头的颜色
        mRecyclerView.setHasFixedSize(false);

        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view,
                                       RecyclerView parent, RecyclerView.State state) {

                if (parent.getChildLayoutPosition(view) == 0) {
                    return;
                }
                int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
                outRect.left = spacingInPixels;
                outRect.right = spacingInPixels;
                outRect.bottom = spacingInPixels;
                outRect.top = spacingInPixels;
            }
        });

        mRecyclerView.setVisibility(View.INVISIBLE);
        mBiliDingVideoAdapter = new BiliDingVideoAdapter(getContext(), new ArrayList<BiliDingVideo.VideoBean>());
        mRecyclerView.setAdapter(mBiliDingVideoAdapter);

        return contentView;
    }


    protected void getDataWithOkHttp() {

//        String url = GlobalConstant.TX_URL + "json/JsonServlet";
        String url = "http://www.bilibili.com/index/ding/1.json";
        OkHttpClientManager.getInstance().getAsync(url, new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String responseStr = response.body().string();
                notifyDataMap(responseStr);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: failed");
            }
        });
    }

    private void notifyDataMap(String response) {
        Observable.just(response)
                .observeOn(Schedulers.io())
                .map(new Func1<String, Void>() {
                    @Override
                    public Void call(String s) {
                        decodeResult(s);
                        Log.i(TAG, "call: MAP");
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        notifyDataRefresh();
                    }
                });

    }

    private void notifyDataRefresh() {
        mBiliDingVideoAdapter.notifyDataSetChanged();
    }

    protected void decodeResult(String result) {
        Gson gson = new Gson();
        BiliDingVideo dingVideo = gson.fromJson(result, BiliDingVideo.class);
        mBiliDingVideoAdapter.setData(dingVideo.getList());
    }

}
