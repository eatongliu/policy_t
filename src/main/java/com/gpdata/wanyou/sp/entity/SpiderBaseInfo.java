package com.gpdata.wanyou.sp.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by guoxy on 2016/10/29.
 */
@Entity
@Table(name = "spider_baseinfo")
public class SpiderBaseInfo {
    private static final long serialVersionUID = 1L;

    @Id
    private String spiderId;
    private String taskName;
    private String creator;
    private String name;
    private Integer taskId;
    private Integer threads;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createDate;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date reviseDate;
    private Integer depth;
    private String remark;
    private String caption;
    private Integer topn;
    private Integer disporder;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSpiderId() {
        return spiderId;
    }

    public void setSpiderId(String spiderId) {
        this.spiderId = spiderId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getReviseDate() {
        return reviseDate;
    }

    public void setReviseDate(Date reviseDate) {
        this.reviseDate = reviseDate;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Integer getTopn() {
        return topn;
    }

    public void setTopn(Integer topn) {
        this.topn = topn;
    }

    public Integer getDisporder() {
        return disporder;
    }

    public void setDisporder(Integer disporder) {
        this.disporder = disporder;
    }
}
