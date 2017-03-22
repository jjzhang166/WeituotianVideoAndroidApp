package com.weituotian.video.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.weituotian.video.R;
import com.weituotian.video.VideoApp;
import com.weituotian.video.activity.UploadActivity;

/**
 * Created by ange on 2017/3/22.
 */

public class DialogUtil {

    public static void confirm(Context context, final String msg) {
        new MaterialDialog.Builder(context).title("消息")
                .content(msg)
                .positiveText("确定")
                .negativeText("取消")
                .show();
    }

}
