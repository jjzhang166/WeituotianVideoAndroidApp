package com.weituotian.video.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.weituotian.video.R;

import static android.content.DialogInterface.BUTTON_NEGATIVE;

/**
 * Created by ange on 2017/3/22.
 */

public class ProgressUtil {

    public static ProgressDialog progressDialog;

    public static ProgressDialog openDialog(Context context) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setIcon(R.drawable.ic_cloud_upload_black_24dp);
        progressDialog.setTitle("正在处理数据。。。");
        progressDialog.setMessage("请稍后。。");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置进度条对话框//样式（水平，旋转）

        //进度最大值
        progressDialog.setMax(100);

        //点击屏幕不消失
        progressDialog.setCanceledOnTouchOutside(false);

        // 设置ProgressDialog 是否可以按退回按键取消
//        progressDialog.setCancelable(true);

        //显示
        progressDialog.show();
        //必须设置到show之后
        progressDialog.setProgress(0);

        return progressDialog;
    }

}
