package com.weituotian.video.utils.validation;

import android.support.design.widget.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.POST;

/**
 * Created by ange on 2017/3/31.
 */

public class CheckManager {
    private OnCheckListener onCheckListener;

    private List<CheckItem> checkItems = new ArrayList<>();

    private Map<ErrorType, String> errorMsgMap = new HashMap<>();

    /*public final static String defaultEmailError = "邮箱错误";
    public final static String defaultNoEditTextError = "没有指定edittext";
    public final static String defaultMinLengthError = "最小长度错误";
    public final static String defaultMaxLengthError = "最大长度错误";
    public final static String defaultEqualToStr = "两次输入不一致";*/

    public CheckManager() {
        errorMsgMap.put(ErrorType.EMAIL, "邮箱错误");
        errorMsgMap.put(ErrorType.MIN_LEHGTH, "最小长度错误");
        errorMsgMap.put(ErrorType.MAX_LENGTH, "最大长度错误");
        errorMsgMap.put(ErrorType.NO_EDIT_TEXT, "没有指定edittext");
        errorMsgMap.put(ErrorType.NOT_EQUAL, "两次输入不一致");
    }

    public String getDefaultMsg(ErrorType errorType) {
        return errorMsgMap.get(errorType);
    }

    public boolean checkAll() {
        for (CheckItem checkItem : checkItems) {
            try {
                checkItem.check();

                if (onCheckListener != null) {
                    onCheckListener.onSuccess(checkItem);
                } else {
                    TextInputLayout layout = checkItem.getLayout();
                    if (layout != null) {
                        checkItem.getLayout().setErrorEnabled(false);
                    } else {
                        throw new RuntimeException("validation plugin: not specified layout");
                    }
                }

            } catch (CheckException e) {

                if (onCheckListener != null) {
                    onCheckListener.onFailed(checkItem, e.getErrorType(), e.getMsg());
                } else {
                    if (e.getMsg() != null) {
                        checkItem.setErrorString(e.getMsg());
                    } else {
                        checkItem.setErrorString(getDefaultMsg(e.getErrorType()));
                    }
                }

                return false;
            }
        }
        return true;
    }

    public List<CheckItem> getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(List<CheckItem> checkItems) {
        this.checkItems = checkItems;
    }

    public OnCheckListener getOnCheckListener() {
        return onCheckListener;
    }

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public Map<ErrorType, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<ErrorType, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }

    public interface OnCheckListener {
        void onSuccess(CheckItem checkItem);

        void onFailed(CheckItem checkItem, ErrorType errorType, String msg);
    }
}
