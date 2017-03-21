package com.weituotian.video.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

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
}
