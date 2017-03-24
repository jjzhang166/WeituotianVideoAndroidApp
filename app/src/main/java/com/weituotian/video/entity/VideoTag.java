package com.weituotian.video.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * 视频标签
 * Created by Administrator on 2017-03-10.
 */
public class VideoTag implements Parcelable {

    private Integer id;

    private String name;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeLong(this.createTime != null ? this.createTime.getTime() : -1);
    }

    public VideoTag() {
    }

    protected VideoTag(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        long tmpCreateTime = in.readLong();
        this.createTime = tmpCreateTime == -1 ? null : new Date(tmpCreateTime);
    }

    public static final Parcelable.Creator<VideoTag> CREATOR = new Parcelable.Creator<VideoTag>() {
        @Override
        public VideoTag createFromParcel(Parcel source) {
            return new VideoTag(source);
        }

        @Override
        public VideoTag[] newArray(int size) {
            return new VideoTag[size];
        }
    };
}
