package com.weituotian.mycommonvideo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.weituotian.mycommonvideo.view.VideoPlayerView;

import java.util.List;

import vn.tungdx.mediapicker.MediaItem;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;

/**
 * 主要activity版本3
 * Created by ange on 2016/12/27.
 */
public class VideoplayActivity3 extends AppCompatActivity{

    private Button btnScreenShot;
    private Button btnOpenVideo;
    private ImageView imageView;

    private VideoPlayerView mVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_videoplay3);
        init();
    }

    private void init() {
        initView();
        setListener();
    }

    private void initView() {

        this.imageView = (ImageView) findViewById(R.id.imageView);
        this.btnOpenVideo = (Button) findViewById(R.id.btnOpenVideo);
        this.btnScreenShot = (Button) findViewById(R.id.btnScreenShot);
        this.mVideoView = (VideoPlayerView) findViewById(R.id.videoView);

    }

    private static final String LOGTAG = "VideoplayActivity";
    private static final int REQUEST_MEDIA = 100;

    private void setListener() {
        //打开视频按钮点击
        btnOpenVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mVideoView.stop();

                MediaOptions.Builder builder = new MediaOptions.Builder();
                MediaOptions options = builder.selectVideo().build();
                MediaPickerActivity.open(VideoplayActivity3.this, REQUEST_MEDIA, options);
            }
        });

        //截图按钮点击
        btnScreenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(mVideoView.getScreenShot());
            }
        });
    }

    private Uri fileURI;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //选择视频完成
        if (requestCode == REQUEST_MEDIA) {
            if (resultCode == RESULT_OK) {
                List<MediaItem> mediaSelectedList = MediaPickerActivity.getMediaItemSelected(data);

                fileURI = null;

                //初始化资源uri
                for (MediaItem mediaItem : mediaSelectedList) {
                    if (fileURI == null) {
                        fileURI = mediaItem.getUriOrigin();
                    }
                }

                mVideoView.play(fileURI);
            }
        }
    }

}
