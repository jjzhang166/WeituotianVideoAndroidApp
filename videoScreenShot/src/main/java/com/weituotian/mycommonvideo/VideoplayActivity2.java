package com.weituotian.mycommonvideo;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.weituotian.mycommonvideo.view.PlayerControllerView;

import java.io.IOException;
import java.util.List;

import vn.tungdx.mediapicker.MediaItem;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;

public class VideoplayActivity2 extends AppCompatActivity implements TextureView.SurfaceTextureListener {


    private Button btnScreenShot;
    private Button btnOpenVideo;
    private ImageView imageView;

    private MediaPlayer mediaPlayer;
    private PlayerControllerView playerControll;

    private TextureView textureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_videoplay2);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            // Make sure we stop video and release resources when activity is destroyed.
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void init() {
        initView();
        setListener();
    }

    private void initView() {
        this.imageView = (ImageView) findViewById(R.id.imageView);
        this.btnOpenVideo = (Button) findViewById(R.id.btnOpenVideo);
        this.btnScreenShot = (Button) findViewById(R.id.btnScreenShot);
        this.playerControll = (PlayerControllerView) findViewById(R.id.playerControll);

        textureView = (TextureView) findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(false);
        // Play video when the media source is ready for playback.
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
    }

    private static final String LOGTAG = "VideoplayActivity";
    private static final int REQUEST_MEDIA = 100;

    private void setListener() {
        //打开视频按钮点击
        btnOpenVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                MediaOptions.Builder builder = new MediaOptions.Builder();
                MediaOptions options = builder.selectVideo().build();
                MediaPickerActivity.open(VideoplayActivity2.this, REQUEST_MEDIA, options);
            }
        });

        //截图按钮点击
        btnScreenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(textureView.getBitmap());
            }
        });

        textureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerControll.toggle();
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

                //初始化资源uri
                for (MediaItem mediaItem : mediaSelectedList) {
//                    Log.d(LOGTAG, mediaItem.getPathOrigin(VideoplayActivity.this));
                    if (fileURI == null) {
                        fileURI = mediaItem.getUriOrigin();
//                        fileName = mediaItem.getPathOrigin(VideoplayActivity.this);//获得选择的视频路径
                    }
//                    break;
                }

                /*
                try {
                    if (fileURI != null) {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(this, fileURI);
                        // don't forget to call MediaPlayer.prepareAsync() method when you use constructor for
                        // creating MediaPlayer
                        mediaPlayer.prepare();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */

            }
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Surface surface = new Surface(surfaceTexture);

        try {
            mediaPlayer.reset();

            if (fileURI != null) {
                mediaPlayer.setSurface(surface);
                mediaPlayer.setDataSource(VideoplayActivity2.this, fileURI);
                // don't forget to call MediaPlayer.prepareAsync() method when you use constructor for
                // creating MediaPlayer
                mediaPlayer.prepareAsync();
            }

        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            e.printStackTrace();
//            Log.d(LOGTAG, e.getMessage());
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (null != mediaPlayer) {
            mediaPlayer.reset();
        }

        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
