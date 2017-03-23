package com.weituotian.video.entity.enums;

/**
 * 视频当前状态
 * Created by ange on 2017/3/5.
 */
public enum VideoState {
    Uncommitted("Uncommitted", "未提交"),
    Auditing("Auditing", "审核中"),
    Audited("Audited", "审核通过"),
    AuditFailure("AuditFailure", "审核不通过"),
    ReEdit("ReEdit", "重新修改");

    private String value;

    private String title;

    public String getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    VideoState(String value, String title) {
        this.value = value;
        this.title = title;
    }

    @Override
    public String toString() {
        return "VideoState{" +
                "value='" + value + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
