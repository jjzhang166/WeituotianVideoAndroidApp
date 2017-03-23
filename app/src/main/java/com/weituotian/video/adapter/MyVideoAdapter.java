package com.weituotian.video.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.weituotian.video.R;
import com.weituotian.video.adapter.helper.AbsRecyclerViewAdapter;
import com.weituotian.video.entity.VideoListVo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyVideoAdapter extends AbsRecyclerViewAdapter {

    private List<VideoListVo> vos;


    public MyVideoAdapter(RecyclerView recyclerView, List<VideoListVo> vos) {

        super(recyclerView);
        this.vos = vos;
    }


    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        bindContext(parent.getContext());
        return new ItemViewHolder(LayoutInflater.from(getContext())
                .inflate(R.layout.item_video_strip, parent, false));
    }

    @Override
    public int getItemCount() {
        return vos.size();
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            VideoListVo vo = vos.get(position);

            Glide.with(getContext())
                    .load(vo.getCover())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.video_default_cover)
                    .dontAnimate()
                    .into(itemViewHolder.mItemImg);

            itemViewHolder.mItemTitle.setText(vo.getTitle());
            itemViewHolder.mItemPlay.setText(vo.getPlay());
            itemViewHolder.mItemUserName.setText(vo.getMemberName());
        }
        super.onBindViewHolder(holder, position);
    }

    private class ItemViewHolder extends ClickableViewHolder {
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
