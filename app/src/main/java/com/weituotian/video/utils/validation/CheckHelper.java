package com.weituotian.video.utils.validation;

import android.support.design.widget.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ange on 2017/3/31.
 */

public class CheckHelper {

    public static String getTextInputText(TextInputEditText textInputEditText) {
        return textInputEditText.getText().toString();
    }

    public static boolean verifyEmail(String email) {
        Pattern pattern = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
