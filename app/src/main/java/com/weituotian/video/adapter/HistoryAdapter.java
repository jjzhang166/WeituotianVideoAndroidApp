package com.weituotian.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.weituotian.video.R;
import com.weituotian.video.entity.HistoryVideo;
import com.weituotian.video.entity.enums.SexEnum;
import com.weituotian.video.utils.DateUtil;
import com.weituotian.video.widget.recycler.HeaderAndFooterAdapter;
import com.weituotian.video.widget.recycler.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ange on 2017/4/2.
 */

public class HistoryAdapter extends HeaderAndFooterAdapter<HistoryVideo> {

    private Context mContext;

    public HistoryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_history, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(ViewHolder holder, int position, HistoryVideo item) {
        if (holder instanceof HistoryAdapter.ItemViewHolder) {
            HistoryAdapter.ItemViewHolder itemViewHolder = (HistoryAdapter.ItemViewHolder) holder;

            Glide.with(mContext)
                    .load(item.getCover())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.video_default_cover)
                    .dontAnimate()
                    .into(itemViewHolder.mCover);

            itemViewHolder.mTitle.setText(item.getTitle());
            itemViewHolder.mDescription.setText(item.getDescript());
            itemViewHolder.mViewTime.setText(DateUtil.getDescriptionTimeFromTimestamp(item.getViewTime().getTime()));//转换成描述性时间

        }
    }

    static class ItemViewHolder extends ViewHolder {
        @BindView(R.id.cover)
        ImageView mCover;
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.description)
        TextView mDescription;
        @BindView(R.id.view_time)
        TextView mViewTime;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
