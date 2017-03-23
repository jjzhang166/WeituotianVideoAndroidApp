package com.weituotian.video.entity.enums;

/**
 * 性别枚举
 * Created by ange on 2017/2/22.
 */
public enum SexEnum {
    MALE("MALE", "男"), FEMALE("FEMALE", "女"), UNKNOW("UNKNOW", "未知");

    private String value;

    private String title;

    public String getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    SexEnum(String value, String title) {
        this.value = value;
        this.title = title;
    }

    @Override
    public String toString() {
        return "SexEnum{" +
                "value='" + value + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

}

