package com.weituotian.video.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * 视频列表Vo
 * Created by ange on 2017/3/8.
 */
@Entity
public class HistoryVideo {

    @Id
    private Long id;

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

    //收藏量
    private Integer collect;

    private String memberName;

    private String partitionName;

    private Date createTime;

    private Integer totalTime;

    //观看此视频的时间
    private Date viewTime;



    @Generated(hash = 2063164825)
    public HistoryVideo(Long id, String cover, String title, String descript,
            Integer click, Integer play, Integer collect, String memberName,
            String partitionName, Date createTime, Integer totalTime,
            Date viewTime) {
        this.id = id;
        this.cover = cover;
        this.title = title;
        this.descript = descript;
        this.click = click;
        this.play = play;
        this.collect = collect;
        this.memberName = memberName;
        this.partitionName = partitionName;
        this.createTime = createTime;
        this.totalTime = totalTime;
        this.viewTime = viewTime;
    }

    @Generated(hash = 717473908)
    public HistoryVideo() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getPartitionName() {
        return partitionName;
    }

    public void setPartitionName(String partitionName) {
        this.partitionName = partitionName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getCollect() {
        return collect;
    }

    public void setCollect(Integer collect) {
        this.collect = collect;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public Date getViewTime() {
        return viewTime;
    }

    public void setViewTime(Date viewTime) {
        this.viewTime = viewTime;
    }

    @Override
    public String toString() {
        return "HistoryVideo{" +
                "click=" + click +
                ", id=" + id +
                ", cover='" + cover + '\'' +
                ", title='" + title + '\'' +
                ", descript='" + descript + '\'' +
                ", play=" + play +
                ", collect=" + collect +
                ", memberName='" + memberName + '\'' +
                ", partitionName='" + partitionName + '\'' +
                ", createTime=" + createTime +
                ", totalTime=" + totalTime +
                ", viewTime=" + viewTime +
                '}';
    }
}
