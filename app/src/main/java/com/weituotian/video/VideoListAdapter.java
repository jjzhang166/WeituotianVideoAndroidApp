package com.weituotian.video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.weituotian.video.adapter.BiliVideoAdapter;
import com.weituotian.video.adapter.MyVideoAdapter;
import com.weituotian.video.adapter.helper.AbsRecyclerViewAdapter;
import com.weituotian.video.entity.BiliDingVideo;
import com.weituotian.video.entity.VideoListVo;
import com.weituotian.video.widget.recycler.HeaderAndFooterAdapter;
import com.weituotian.video.widget.recycler.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ange on 2017/3/28.
 */

public class VideoListAdapter extends HeaderAndFooterAdapter<VideoListVo> {

    private Context mContext;

    public VideoListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_strip, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(ViewHolder holder, int position, VideoListVo vo) {
        if (holder instanceof VideoListAdapter.ItemViewHolder) {
            VideoListAdapter.ItemViewHolder itemViewHolder = (VideoListAdapter.ItemViewHolder) holder;
            Glide.with(mContext)
                    .load(vo.getCover())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.video_default_cover)
                    .dontAnimate()
                    .into(itemViewHolder.mItemImg);

            itemViewHolder.mItemTitle.setText(vo.getTitle());
            itemViewHolder.mItemPlay.setText(String.valueOf(vo.getPlay()));
            itemViewHolder.mItemUserName.setText(vo.getMemberName());
        }
    }

    public class ItemViewHolder extends ViewHolder {
        @BindView(R.id.item_img)
        ImageView mItemImg;
        @BindView(R.id.item_title)
        TextView mItemTitle;
        @BindView(R.id.item_user_name)
        TextView mItemUserName;
        @BindView(R.id.item_play)
        TextView mItemPlay;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
