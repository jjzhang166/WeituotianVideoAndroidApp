package com.weituotian.video.http.okhttp;

/**
 * Created by ange on 2017/3/21.
 */

public interface ProgressListener {
    void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish);
}
