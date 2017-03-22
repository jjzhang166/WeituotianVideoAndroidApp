package com.weituotian.video.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.weituotian.mycommonvideo.view.VideoPlayerView;
import com.weituotian.video.R;
import com.weituotian.video.VideoApp;
import com.weituotian.video.entity.Partition;
import com.weituotian.video.entity.RetInfo;
import com.weituotian.video.http.LoginContext;
import com.weituotian.video.mvpview.IUploadView;
import com.weituotian.video.presenter.UploadPresenter;
import com.weituotian.video.utils.DialogUtil;
import com.weituotian.video.utils.FileUtils;
import com.weituotian.video.utils.UIUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import vn.tungdx.mediapicker.MediaItem;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;

/**
 * 上传activity
 * Created by ange on 2017/3/18.
 */

public class UploadActivity extends BaseMvpActivity<IUploadView, UploadPresenter> implements IUploadView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.videoView)
    VideoPlayerView mVideoView;

    //btns
    @BindView(R.id.btn_open_video)
    Button mBtnOpenVideo;
    @BindView(R.id.btn_upload_video)
    Button mBtnUploadVideo;
    @BindView(R.id.btn_screen_shot)
    Button mBtnScreenShot;
    @BindView(R.id.btn_upload_cover)
    Button mBtnUploadCover;

    @BindView(R.id.iv_screenshot)
    ImageView mIvScreenshot;

    @BindView(R.id.flowlayout_partition)
    TagFlowLayout mPartition;

    @BindView(R.id.et_title)
    EditText mEtTitle;
    @BindView(R.id.et_description)
    EditText mEtDescription;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;

    private static final String TAG = "UploadActivity";
    private static final int REQUEST_MEDIA = 100;

    private Uri fileURI;//选择的视频uri
    private List<Partition> partitions;//分区列表

    private String cover;
    private Integer attachmentId;

    @Override
    public int getContentViewId() {
        return R.layout.activity_upload;
    }

    @NonNull
    @Override
    public UploadPresenter createPresenter() {
        return new UploadPresenter();
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        presenter.getPartitions();
        initView();
        setListener();
    }

    private void initView() {
        if (mToolbar != null) {
            mToolbar.setTitle("上传视频");
            setSupportActionBar(mToolbar);
        }
    }

    private void setListener() {
        //打开视频按钮点击
        mBtnOpenVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mVideoView.stop();

                MediaOptions.Builder builder = new MediaOptions.Builder();
                MediaOptions options = builder.selectVideo().build();
                MediaPickerActivity.open(UploadActivity.this, REQUEST_MEDIA, options);
            }
        });

        //截图按钮点击
        mBtnScreenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvScreenshot.setImageBitmap(mVideoView.getScreenShot());
            }
        });

        //上传视频按钮
        mBtnUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileURI != null) {
                    String path = FileUtils.getPath(UploadActivity.this, fileURI);
                    if (path != null) {
                        File file = new File(path);
                        onStartUplaod();
                        presenter.uploadVideo(file);
                    }
                } else {
                    UIUtil.showToast(UploadActivity.this, "请选择视频");
                }
            }
        });

        //上传封面按钮
        mBtnUploadCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) mIvScreenshot.getDrawable()).getBitmap();
                if (bitmap != null) {
                    File file = FileUtils.saveBitmapFile(bitmap);
                    onStartUplaod();
                    presenter.uploadCover(file);
                }
            }
        });

        //提交按钮
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer partitionId;
                String title;
                String descript;

                Set<Integer> selectedList = mPartition.getSelectedList();
                if (selectedList.size() == 0) {
                    DialogUtil.confirm(UploadActivity.this, "请选择分区");
                    return;
                } else {
                    Integer index = selectedList.iterator().next();
                    partitionId = partitions.get(index).getId();
                }

                title = mEtTitle.getText().toString();
                if (title.equals("")) {
                    DialogUtil.confirm(UploadActivity.this, "请填写标题");
                    return;
                }

                descript = mEtDescription.getText().toString();
                if (descript.equals("")) {
                    DialogUtil.confirm(UploadActivity.this, "请填写视频描述");
                    return;
                }

                if (attachmentId == null) {
                    DialogUtil.confirm(UploadActivity.this, "请上传视频");
                    return;
                }

                if (cover == null) {
                    DialogUtil.confirm(UploadActivity.this, "请上传封面");
                    return;
                }

                Map<String, String> optionMap = new HashMap<>();
                optionMap.put("cover", cover);
                optionMap.put("partitionId", partitionId.toString());
                optionMap.put("attachmentId", attachmentId.toString());
                optionMap.put("title", title);
                optionMap.put("descript", descript);

                presenter.submitVideo(optionMap);
            }
        });
    }

    @Override
    public void onLoadPartitions(List<Partition> partitions) {

        this.partitions = partitions;

        //设置分区标签
        mPartition.setAdapter(new TagAdapter<Partition>(partitions) {
            @Override
            public View getView(FlowLayout parent, int position, Partition partition) {
                TextView tv = (TextView) LayoutInflater.from(UploadActivity.this).inflate(R.layout.tag_video_partitions,
                        mPartition, false);
                tv.setText(partition.getName());
                return tv;
            }
        });

        mPartition.setMaxSelectCount(1);

        /*for (Integer integer : mPartition.getSelectedList()) {
            partitions.get(integer).getId();
        }*/
    }

    @Override
    public void onLoadPartitionsError(Throwable e) {
        UIUtil.showToast(this, e.getMessage());
        //2s后关闭
        Observable.timer(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
//                        UploadActivity.this.finish();
                    }
                });
    }


    private MaterialDialog dialog;

    public void onStartUplaod() {
        boolean showMinMax = false;
        dialog = new MaterialDialog.Builder(this)
                .title("上传中")
//                .content("进度")
                .progress(false, 100, showMinMax)
                .negativeText("取消")
//                .autoDismiss(false)
                .iconRes(R.drawable.ic_cloud_upload_black_24dp)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.cancelVideo();
                        UIUtil.showToast(UploadActivity.this, "上传取消");
                    }
                })
                .canceledOnTouchOutside(false)//点击屏幕不消失
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        presenter.cancelVideo();
                        UIUtil.showToast(UploadActivity.this, "上传取消");
                    }
                })
                .show();

        /*ProgressUtil.openDialog(this);
        // 监听cancel事件
        ProgressUtil.progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                presenter.cancelVideo();
                UIUtil.showToast(UploadActivity.this,"上传取消");
            }
        });*/

    }

    @Override
    public void onVideoUploadProgress(Integer percent) {
        dialog.setProgress(percent);
//        ProgressUtil.progressDialog.setProgress(percent);
    }

    @Override
    public void onVideoUploadSuccess(Integer attachmentId) {
        this.attachmentId = attachmentId;
        dialog.dismiss();
        UIUtil.showToast(UploadActivity.this, "上传完成");
//        ProgressUtil.progressDialog.dismiss();
    }

    @Override
    public void onCoverUploadSuccess(String url) {
        cover = url;
        dialog.dismiss();
        UIUtil.showToast(UploadActivity.this, "上传完成");
    }

    @Override
    public void onSubmitVideoFinish(final RetInfo<String> retInfo) {
        new MaterialDialog.Builder(UploadActivity.this).title("消息")
                .content(retInfo.getMsg())
                .positiveText("确定")
                .negativeText("取消")
                .canceledOnTouchOutside(false)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (retInfo.isSuccess()) {
                            //成功就关闭上传activity
                            UploadActivity.this.finish();
                        }
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //选择视频完成
        if (requestCode == REQUEST_MEDIA) {
            if (resultCode == RESULT_OK) {
                List<MediaItem> mediaSelectedList = MediaPickerActivity.getMediaItemSelected(data);

                fileURI = null;

                //初始化资源uri,选择第一个
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
