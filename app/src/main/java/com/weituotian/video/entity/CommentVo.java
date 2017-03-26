package com.weituotian.video.entity;

import com.weituotian.video.entity.enums.SexEnum;

import java.util.Date;

/**
 * Created by Administrator on 2017-03-25.
 */
public class CommentVo {

    private int id;

    private Integer floor;

    private Date postTime;

    private String userName;

    private String userAvatar;

    private SexEnum userSex;

    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public SexEnum getUserSex() {
        return userSex;
    }

    public void setUserSex(SexEnum userSex) {
        this.userSex = userSex;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommentVo{" +
                "id=" + id +
                ", floor=" + floor +
                ", postTime=" + postTime +
                ", userName='" + userName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", userSex=" + userSex +
                ", content='" + content + '\'' +
                '}';
    }
}
