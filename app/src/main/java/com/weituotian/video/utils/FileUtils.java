package com.weituotian.video.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ange on 2016/3/24.
 */
public class FileUtils {

    public static String getPath(Context context, Uri uri) {
        String path = "";

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    int column_index = cursor.getColumnIndexOrThrow("_data");
                    if (cursor.moveToFirst()) {
                        path = cursor.getString(column_index);
                    }
                    cursor.close();
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }

        return path;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {//图片所在SD卡的路径
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 960, 540);//自定义一个宽和高
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    //Bitmap对象保存味图片文件
    public static File saveBitmapFile(Bitmap bitmap) {
//        File.createTempFile("cover", "jpeg");
//        File file=new File("");//将要保存图片的路径
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString(), "weituotianVideo_cover.jpg");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap = zoomImg(bitmap, 960, 540);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 处理图片到新的宽高
     *
     * @param bm        所要转换的bitmap
     * @param newWidth  新的宽
     * @param newHeight 新的高
     * @return 指定宽高的bitmap
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片   www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }


    //计算图片的缩放值
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;//获取图片的高
        final int width = options.outWidth;//获取图片的框
        int inSampleSize = 4;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;//求出缩放值
    }

    //将Bitmap转换成Base64
    public static String getImgStr(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 80, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    //将Base64转换成bitmap
    public static Bitmap getimg(String str) {
        byte[] bytes;
        bytes = Base64.decode(str, 0);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
