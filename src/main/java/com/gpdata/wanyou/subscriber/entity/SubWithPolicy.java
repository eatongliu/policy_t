package com.gpdata.wanyou.subscriber.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 订阅关联政策表
 * Created by guoxy on 2016/12/14.
 */
@Entity
@Table(name = "sub_with_policy")
@IdClass(value = PidSidPk.class)
public class SubWithPolicy {
    /**
     * 政策id 主键
     */
    @Id
    @Column(name = "pid", nullable = false)
    private Long pid;
    /**
     * 订阅id
     */
    @Id
    @Column(name = "sid", nullable = false)
    private Long sid;
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 政策标题
     */
    private String title;
    /**
     * 政策字号
     */
    private String typeId;
    /**
     * 关键词
     */
    private String keyWord;
    /**
     * 生成时间
     */
    private Date createDate;
    /**
     * 推送时间
     */
    private Date sendDate;
    /**
     * 用户删除
     * 1：正常
     * -1：删除
     */
    private int isDelete;
    /**
     * 文章类型
     */
    private String fileType;
    /**
     * 用户备注
     */
    @Column(columnDefinition = "text")
    private String remark;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
