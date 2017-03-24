package com.weituotian.video.entity;


import android.os.Parcel;
import android.os.Parcelable;

import com.weituotian.video.entity.enums.VideoState;

import java.util.Date;
import java.util.List;

/**
 * Created by ange on 2017/3/24.
 */
public class FrontVideo implements Parcelable {

    private int id;

    //封面
    private String cover;

    //视频标题
    private String title;

    //视频简介
    private String descript;

    //点击量
    private Integer click;

    //播放量
    private Integer play;

    //总时间
    private Integer totalTime;

    //收藏量
    private Integer collect;

    private Date createTime;

    //上传者
    private String memberName;
    private String memberCover;

    private VideoState videoState;

    private String partitionName;

    private String token;

    private List<VideoTag> tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemberCover() {
        return memberCover;
    }

    public void setMemberCover(String memberCover) {
        this.memberCover = memberCover;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Integer getClick() {
        return click;
    }

    public void setClick(Integer click) {
        this.click = click;
    }

    public Integer getPlay() {
        return play;
    }

    public void setPlay(Integer play) {
        this.play = play;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public Integer getCollect() {
        return collect;
    }

    public void setCollect(Integer collect) {
        this.collect = collect;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public VideoState getVideoState() {
        return videoState;
    }

    public void setVideoState(VideoState videoState) {
        this.videoState = videoState;
    }

    public String getPartitionName() {
        return partitionName;
    }

    public void setPartitionName(String partitionName) {
        this.partitionName = partitionName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<VideoTag> getTags() {
        return tags;
    }

    public void setTags(List<VideoTag> tags) {
        this.tags = tags;
    }

    public FrontVideo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.cover);
        dest.writeString(this.title);
        dest.writeString(this.descript);
        dest.writeValue(this.click);
        dest.writeValue(this.play);
        dest.writeValue(this.totalTime);
        dest.writeValue(this.collect);
        dest.writeLong(this.createTime != null ? this.createTime.getTime() : -1);
        dest.writeString(this.memberName);
        dest.writeString(this.memberCover);
        dest.writeInt(this.videoState == null ? -1 : this.videoState.ordinal());
        dest.writeString(this.partitionName);
        dest.writeString(this.token);
        dest.writeTypedList(this.tags);
    }

    protected FrontVideo(Parcel in) {
        this.id = in.readInt();
        this.cover = in.readString();
        this.title = in.readString();
        this.descript = in.readString();
        this.click = (Integer) in.readValue(Integer.class.getClassLoader());
        this.play = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalTime = (Integer) in.readValue(Integer.class.getClassLoader());
        this.collect = (Integer) in.readValue(Integer.class.getClassLoader());
        long tmpCreateTime = in.readLong();
        this.createTime = tmpCreateTime == -1 ? null : new Date(tmpCreateTime);
        this.memberName = in.readString();
        this.memberCover = in.readString();
        int tmpVideoState = in.readInt();
        this.videoState = tmpVideoState == -1 ? null : VideoState.values()[tmpVideoState];
        this.partitionName = in.readString();
        this.token = in.readString();
        this.tags = in.createTypedArrayList(VideoTag.CREATOR);
    }

    public static final Creator<FrontVideo> CREATOR = new Creator<FrontVideo>() {
        @Override
        public FrontVideo createFromParcel(Parcel source) {
            return new FrontVideo(source);
        }

        @Override
        public FrontVideo[] newArray(int size) {
            return new FrontVideo[size];
        }
    };
}
