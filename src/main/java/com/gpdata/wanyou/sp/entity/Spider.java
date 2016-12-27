package com.gpdata.wanyou.sp.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "spider_baseinfo")
public class Spider {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer spiderid;
    private Integer taskid;
    private String caption;
    private String name;
    private String creator;
    private Date createdate;
    private Date revisedate;
    private Integer depth;
    private Integer threads;
    private Integer topn;
    private String remark;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getSpiderid() {
        return spiderid;
    }

    public void setSpiderid(Integer spiderid) {
        this.spiderid = spiderid;
    }

    public Integer getTaskid() {
        return taskid;
    }

    public void setTaskid(Integer taskid) {
        this.taskid = taskid;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getRevisedate() {
        return revisedate;
    }

    public void setRevisedate(Date revisedate) {
        this.revisedate = revisedate;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public Integer getTopn() {
        return topn;
    }

    public void setTopn(Integer topn) {
        this.topn = topn;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Spider [spiderid=" + spiderid + ", taskid=" + taskid
                + ", caption=" + caption + ", name=" + name + ", creator="
                + creator + ", createdate=" + createdate + ", revisedate="
                + revisedate + ", depth=" + depth + ", threads=" + threads
                + ", topn=" + topn + ", remark=" + remark + "]";
    }


}
