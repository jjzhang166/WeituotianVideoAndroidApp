package com.weituotian.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weituotian.video.R;
import com.weituotian.video.entity.BiliDingVideo;
import com.weituotian.video.widget.recycler.HeaderAndFooterAdapter;
import com.weituotian.video.widget.recycler.ViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author: laohu on 2016/8/24
 * @site: http://ittiger.cn
 */
public class VideoAdapter extends HeaderAndFooterAdapter<BiliDingVideo.VideoBean> {

    private Context mContext;

    public VideoAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public VideoAdapter(Context context, List<BiliDingVideo.VideoBean> list) {

        super(list);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_simple_list_1, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(ViewHolder holder, int position, BiliDingVideo.VideoBean item) {

        VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
        Glide.with(mContext).load(item.getPic())
                .placeholder(R.drawable.cover_place_holder)
                .error(R.drawable.cover_place_holder)
                .into(videoViewHolder.civCover);
        videoViewHolder.tvTime.setText(item.getCreate());
        videoViewHolder.tvTitle.setText(item.getTitle());
        videoViewHolder.setPosition(position);
//        videoViewHolder.itemView.setTag(R.id.tag_video_list_item, VideoPlayState.STOP);
    }

    class VideoViewHolder extends ViewHolder {
        @BindView(R.id.civ_cover)
        ImageView civCover;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_time)
        TextView tvTime;

        int mPosition;

        public VideoViewHolder(View itemView) {

            super(itemView);
            //以宽高比16:9的比例设置播放器的尺寸
            /*int width = DisplayManager.screenWidthPixel(mContext);
            int height = (int) (width * 1.0f / 16 * 9 + 0.5f);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            params.height = height;
            params.width = width;
            itemView.setLayoutParams(params);*/
            ButterKnife.bind(this, itemView);
        }


        public void setPosition(int position) {
            mPosition = position;
        }
    }
}
