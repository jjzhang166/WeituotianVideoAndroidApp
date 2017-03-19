package com.weituotian.mycommonvideo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import java.io.File;
import java.util.List;

import vn.tungdx.mediapicker.MediaItem;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;

public class VideoplayActivity extends AppCompatActivity {


    private VideoView videoView;
    private Button btnScreenShot;
    private Button btnOpenVideo;
    private ImageView imageView;
    MediaController mediaController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_videoplay);
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
        this.videoView = (VideoView) findViewById(R.id.videoView);

        //VideoView与MediaController进行关联
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);

        rev = new MediaMetadataRetriever();
    }

    private static final String LOGTAG = "VideoplayActivity";
    private static final int REQUEST_MEDIA = 100;
    MediaMetadataRetriever rev;

    private void setListener() {
        //打开视频按钮点击
        btnOpenVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaOptions.Builder builder = new MediaOptions.Builder();
                MediaOptions options = builder.selectVideo().build();
                MediaPickerActivity.open(VideoplayActivity.this, REQUEST_MEDIA, options);
            }
        });
        //截图按钮点击
        btnScreenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rev.setDataSource(VideoplayActivity.this, fileURI); //这里第一个参数需要Context，传this指针

                Bitmap bitmap = rev.getFrameAtTime(videoView.getCurrentPosition() * 1000,
                        MediaMetadataRetriever.OPTION_CLOSEST);

                imageView.setImageBitmap(bitmap);
            }
        });
    }

    private Uri fileURI;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA) {
            if (resultCode == RESULT_OK) {
                List<MediaItem> mediaSelectedList = MediaPickerActivity.getMediaItemSelected(data);

                fileURI = null;

                for (MediaItem mediaItem : mediaSelectedList) {
//                    Log.d(LOGTAG, mediaItem.getPathOrigin(VideoplayActivity.this));
                    if (fileURI == null) {
                        fileURI = mediaItem.getUriOrigin();
//                        fileName = mediaItem.getPathOrigin(VideoplayActivity.this);//获得选择的视频路径
                    }
//                    break;
                }

                videoView.setVideoURI(fileURI);
//                videoView.setVideoPath(fileName);

                //让VideoView获取焦点
//                    videoView.requestFocus();
                videoView.start();//开始播放

//                mediaController.show(0);

            }
        }
    }
}
