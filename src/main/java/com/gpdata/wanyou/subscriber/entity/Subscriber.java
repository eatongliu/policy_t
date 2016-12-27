package com.gpdata.wanyou.subscriber.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户订阅
 * Created by guoxy on 2016/11/30.
 */
@Entity
@Table(name = "subscriber")
public class Subscriber {
    private static final long serialVersionUID = 1L;
    /**
     * 订阅号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long sid;
    /**
     * 订阅日期
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createDate;
    /**
     * 修改日期
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date reviseDate;
    /**
     * 关键字
     */
    private String keyWords;
    /**
     * 用户备注
     */
    private String uRemark;
    /**
     * 用户原文
     */
    private String uContext;
    /**
     * 订阅状态
     */
    private String status;
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 管理备注
     */
    private String mRemark;

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
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

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getuRemark() {
        return uRemark;
    }

    public void setuRemark(String uRemark) {
        this.uRemark = uRemark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getmRemark() {
        return mRemark;
    }

    public void setmRemark(String mRemark) {
        this.mRemark = mRemark;
    }

    public String getuContext() {
        return uContext;
    }

    public void setuContext(String uContext) {
        this.uContext = uContext;
    }
}
