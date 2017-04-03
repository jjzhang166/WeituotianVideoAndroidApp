package com.weituotian.video.utils.validation;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;

import javax.annotation.Nonnull;

/**
 * Created by ange on 2017/3/31.
 */

public class CheckItem {

    private TextInputEditText editText;

    private TextInputLayout layout;
    private CheckManager checkManager;

    private Integer minLength;
    private String minLengthStr;

    private Integer maxLength;
    private String maxLengthStr;

    private boolean email = false;
    private String emailStr;

    private TextInputEditText equalTo;
    private String equalToStr;

    private CheckItem(Builder builder) {
        setEditText(builder.editText);
        setLayout(builder.layout);
        setMinLength(builder.minLength);
        setMinLengthStr(builder.minLengthStr);
        setMaxLength(builder.maxLength);
        setMaxLengthStr(builder.maxLengthStr);
        setEmail(builder.email);
        setEmailStr(builder.emailStr);
        equalTo = builder.equalTo;
        equalToStr = builder.equalToStr;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(@Nonnull CheckItem copy) {
        Builder builder = new Builder();
        builder.editText = copy.editText;
        builder.layout = copy.layout;
        builder.minLength = copy.minLength;
        builder.minLengthStr = copy.minLengthStr;
        builder.maxLength = copy.maxLength;
        builder.maxLengthStr = copy.maxLengthStr;
        builder.email = copy.email;
        builder.emailStr = copy.emailStr;
        builder.equalTo = copy.equalTo;
        builder.equalToStr = copy.equalToStr;
        return builder;
    }

    private String getValue(TextInputEditText editText) {
        return CheckHelper.getTextInputText(editText);
    }

    private String getValue() {
        return CheckHelper.getTextInputText(editText);
    }

    public void setErrorString(String msg) {
        if (layout != null) {
            layout.setErrorEnabled(true);
            layout.setError(msg);
        }
    }

    public void check() throws CheckException {
        if (editText == null) {
            throw new CheckException(ErrorType.NO_EDIT_TEXT);
        }

        if (minLength != null) {
            if (getValue().length() < minLength) {//verifyEmail()
                throw new CheckException(ErrorType.MIN_LEHGTH, minLengthStr);
            }
        }

        if (email) {
            if (!CheckHelper.verifyEmail(getValue())) {
                throw new CheckException(ErrorType.EMAIL, emailStr);
            }
        }

        if (maxLength != null) {
            if (getValue().length() > maxLength) {
                throw new CheckException(ErrorType.MAX_LENGTH, maxLengthStr);
            }
        }

        if (equalTo != null) {
            if (!getValue().equals(getValue(equalTo))) {
                throw new CheckException(ErrorType.NOT_EQUAL, maxLengthStr);
            }
        }
    }

    public void appendTo(CheckManager checkManager) {
        checkManager.getCheckItems().add(this);
    }

    /*getter and setter*/

    public TextInputEditText getEditText() {
        return editText;
    }

    public void setEditText(TextInputEditText editText) {
        this.editText = editText;
    }

    public TextInputLayout getLayout() {
        return layout;
    }

    public void setLayout(TextInputLayout layout) {
        this.layout = layout;
    }

    public CheckManager getCheckManager() {
        return checkManager;
    }

    public void setCheckManager(CheckManager checkManager) {
        this.checkManager = checkManager;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public String getMinLengthStr() {
        return minLengthStr;
    }

    public void setMinLengthStr(String minLengthStr) {
        this.minLengthStr = minLengthStr;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getMaxLengthStr() {
        return maxLengthStr;
    }

    public void setMaxLengthStr(String maxLengthStr) {
        this.maxLengthStr = maxLengthStr;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public String getEmailStr() {
        return emailStr;
    }

    public void setEmailStr(String emailStr) {
        this.emailStr = emailStr;
    }

    public static final class Builder {
        private TextInputEditText editText;
        private TextInputLayout layout;
        private Integer minLength;
        private String minLengthStr;
        private Integer maxLength;
        private String maxLengthStr;
        private boolean email;
        private String emailStr;
        private TextInputEditText equalTo;
        private String equalToStr;

        private Builder() {
        }

        @Nonnull
        public Builder editText(@Nonnull TextInputEditText editText) {
            this.editText = editText;
            return this;
        }

        @Nonnull
        public Builder layout(@Nonnull TextInputLayout layout) {
            this.layout = layout;
            return this;
        }

        @Nonnull
        public Builder minLength(@Nonnull Integer minLength) {
            this.minLength = minLength;
            return this;
        }

        @Nonnull
        public Builder minLengthStr(@Nonnull String minLengthStr) {
            this.minLengthStr = minLengthStr;
            return this;
        }

        @Nonnull
        public Builder maxLength(@Nonnull Integer maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        @Nonnull
        public Builder maxLengthStr(@Nonnull String maxLengthStr) {
            this.maxLengthStr = maxLengthStr;
            return this;
        }

        @Nonnull
        public Builder email(boolean email) {
            this.email = email;
            return this;
        }

        @Nonnull
        public Builder emailStr(@Nonnull String emailStr) {
            this.emailStr = emailStr;
            return this;
        }

        @Nonnull
        public Builder equalTo(@Nonnull TextInputEditText equalTo) {
            this.equalTo = equalTo;
            return this;
        }

        @Nonnull
        public Builder equalToStr(@Nonnull String equalToStr) {
            this.equalToStr = equalToStr;
            return this;
        }

        @Nonnull
        public CheckItem build() {
            return new CheckItem(this);
        }
    }



    /* builder */




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
