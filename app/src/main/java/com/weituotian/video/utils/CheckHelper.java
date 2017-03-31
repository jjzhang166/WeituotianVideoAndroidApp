package com.weituotian.video.utils;

import android.support.design.widget.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ange on 2017/3/31.
 */

public class CheckHelper {

    public final static String defaultEmailError = "邮箱错误";
    public final static String defaultMinLengthError = "最小长度错误";
    public final static String defaultMaxLengthError = "最大长度错误";

    public static String getTextInputText(TextInputEditText textInputEditText) {
        return textInputEditText.getText().toString();
    }

    public static boolean verifyEmail(String email) {
        Pattern pattern = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
