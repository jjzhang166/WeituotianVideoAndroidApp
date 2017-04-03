package com.weituotian.video.utils.validation;

/**
 * Created by ange on 2017/4/1.
 */

public class CheckException extends Exception {

    private ErrorType errorType;

    private String msg;

    public CheckException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public CheckException(ErrorType errorType, String str) {
        this.errorType = errorType;
        this.msg = str;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
