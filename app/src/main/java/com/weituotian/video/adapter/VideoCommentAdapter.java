package com.weituotian.video.adapter;

import android.annotation.SuppressLint;
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
import com.weituotian.video.entity.CommentVo;
import com.weituotian.video.entity.enums.SexEnum;
import com.weituotian.video.utils.DateUtil;
import com.weituotian.video.widget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hcc on 16/8/4 14:12
 * 100332338@qq.com
 * <p/>
 * 视频评论Adapter
 */
public class VideoCommentAdapter extends AbsRecyclerViewAdapter {

    private List<CommentVo> comments;


    public VideoCommentAdapter(RecyclerView recyclerView, List<CommentVo> comments) {
        super(recyclerView);
        this.comments = comments;
    }


    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        bindContext(parent.getContext());
        return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_video_comment, parent, false));
    }


    @Override
    public int getItemCount() {
        return comments.size();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {

            ViewHolder mHolder = (ViewHolder) holder;
            CommentVo vo = comments.get(position);
            mHolder.mItemUserName.setText(vo.getUserName());

            Glide.with(getContext())
                    .load(vo.getUserAvatar())
                    .centerCrop()
                    .dontAnimate()
                    .placeholder(R.drawable.ico_user_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mHolder.mItemUserAvatar);


            mHolder.mItemUserSex.setImageResource(SexEnum.getDrawable(vo.getUserSex()));

            String time = DateUtil.getDescriptionTimeFromTimestamp(vo.getPostTime().getTime());
            mHolder.mItemCommentTime.setText(time);
            mHolder.mItemCommentContent.setText(vo.getContent());
            mHolder.mItemCommentFloor.setText("#" + vo.getFloor());
        }

        super.onBindViewHolder(holder, position);
    }


    static class ViewHolder extends ClickableViewHolder {
        @BindView(R.id.item_user_avatar)
        CircleImageView mItemUserAvatar;
        @BindView(R.id.item_user_lever)
        ImageView mItemUserLever;
        @BindView(R.id.item_user_name)
        TextView mItemUserName;
        @BindView(R.id.item_user_sex)
        ImageView mItemUserSex;
        @BindView(R.id.item_comment_floor)
        TextView mItemCommentFloor;
        @BindView(R.id.item_comment_time)
        TextView mItemCommentTime;
        @BindView(R.id.item_comment_more)
        ImageView mItemCommentMore;
        @BindView(R.id.item_comment_content)
        TextView mItemCommentContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
