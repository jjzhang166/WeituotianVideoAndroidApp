package com.weituotian.video.http;

/**
 * @author laohu
 * @site http://ittiger.cn
 */
public enum DataType {

    BILI(5);

    int mValue;

    DataType(int value) {

        mValue = value;
    }

    public int value() {

        return mValue;
    }
}
