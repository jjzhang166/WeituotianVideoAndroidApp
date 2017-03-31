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
import com.weituotian.video.entity.AppMember;
import com.weituotian.video.entity.enums.SexEnum;
import com.weituotian.video.widget.CircleImageView;
import com.weituotian.video.widget.recycler.HeaderAndFooterAdapter;
import com.weituotian.video.widget.recycler.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ange on 2017/3/31.
 */

public class AppMemberAdapter extends HeaderAndFooterAdapter<AppMember> {

    private Context mContext;

    public AppMemberAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_appmember, parent, false);
        return new AppMemberAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(ViewHolder holder, int position, AppMember member) {
        if (holder instanceof AppMemberAdapter.ItemViewHolder) {
            AppMemberAdapter.ItemViewHolder itemViewHolder = (AppMemberAdapter.ItemViewHolder) holder;
            Glide.with(mContext)
                    .load(member.getAvatar())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.video_default_cover)
                    .dontAnimate()
                    .into(itemViewHolder.mUserAvatar);

            itemViewHolder.mUserName.setText(member.getName());
            itemViewHolder.mUserSex.setImageResource(SexEnum.getDrawable(member.getSex()));
        }
    }


    static class ItemViewHolder extends ViewHolder {
        @BindView(R.id.user_avatar)
        CircleImageView mUserAvatar;

        @BindView(R.id.user_name)
        TextView mUserName;

        @BindView(R.id.user_sex)
        ImageView mUserSex;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
