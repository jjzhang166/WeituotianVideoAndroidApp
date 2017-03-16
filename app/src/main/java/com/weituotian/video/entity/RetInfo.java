package com.weituotian.video.entity;

/**
 * Created by ange on 2017/3/17.
 */

public class RetInfo<T> {

    private boolean success;
    private String msg;
    private T obj;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}
