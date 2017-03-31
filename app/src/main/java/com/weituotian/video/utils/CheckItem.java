package com.weituotian.video.utils;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ange on 2017/3/31.
 */

public class CheckItem {

    private TextInputEditText editText;
    private TextInputLayout layout;

    private Integer minLength;
    private String minLengthStr = CheckHelper.defaultMinLengthError;
//    private onCheckListener minLengthListener;

    private Integer maxLength;
    private String maxLengthStr = CheckHelper.defaultMinLengthError;

    private boolean email = false;
    private String emailStr = CheckHelper.defaultEmailError;

    private String getValue() {
        return CheckHelper.getTextInputText(editText);
    }

    private void setErrorString(String msg) {
        if (layout != null) {
            layout.setErrorEnabled(true);
            layout.setError(msg);
        }
    }

    public boolean check() {
        if (editText == null) {
            return false;
        }

        if (minLength != null) {
            if (getValue().length() >= minLength) {//verifyEmail()
                layout.setErrorEnabled(false);
            } else {
                setErrorString(minLengthStr);
                return false;
            }
        }

        if (email) {
            if (CheckHelper.verifyEmail(getValue())) {
                layout.setErrorEnabled(false);
            } else {
                setErrorString(emailStr);
                return false;
            }
        }

        if (maxLength != null) {
            if (getValue().length() <= maxLength) {
                layout.setErrorEnabled(false);
            } else {
                setErrorString(emailStr);
                return false;
            }
        }

        return true;
    }

    public void addCheckRule() {

    }

    /*public interface onCheckListener{
        void onSuccess();
        void onFailed();
    }*/

    /*public static class Builder {

        TextInputEditText editText;
        TextInputLayout layout;

        int minLength;
        String strMinLength;

        public Builder edidText(TextInputEditText editText) {
            this.editText = editText;
            return this;
        }

        public Builder Layout(TextInputLayout textInputLayout) {
            this.layout = textInputLayout;
        }

        public Builder minLength(int minLength, String msg) {
            this.minLength = minLength;
            this.strMinLength = msg;
            return this;
        }

    }*/
}
