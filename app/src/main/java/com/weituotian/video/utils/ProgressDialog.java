package com.weituotian.video.utils;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by ange on 2017/3/22.
 */

public class ProgressDialog {

    public static class Builder {

        private Context mContext;
        private String title="";
        private String message = "";
        private Integer iconId=0;
        private int progressStyle = android.app.ProgressDialog.STYLE_HORIZONTAL;

        public Builder(@NonNull Context context) {
            mContext = context;
        }

        public Builder title(String title){
            this.title = title;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder message(Integer iconId) {
            this.iconId = iconId;
            return this;
        }

        public Builder progressStyle(Integer progressStyle) {
            this.progressStyle = progressStyle;
            return this;
        }

        public ProgressDialog build(){
            return new ProgressDialog(this);
        }

    }

    private ProgressDialog(Builder b) {

    }

}
