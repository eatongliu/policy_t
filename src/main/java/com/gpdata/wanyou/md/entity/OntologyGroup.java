package com.gpdata.wanyou.md.entity;


import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 本体组
 */
@Entity
@Table(name = "ontology_group")
public class OntologyGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 本体组 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer groupId;

    /**
     * 标题
     */
    private String caption;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建用户的 ID
     */
    private String userId;

    /**
     * 创建人
     */
    @Formula(value="(select a.nickName from sys_user a where a.userId = userId)")
    private String creator;

    /**
     * 修改时间
     */
    private Date reviseDate;

    /**
     * 说明
     */
    private String remark;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getReviseDate() {
        return reviseDate;
    }

    public void setReviseDate(Date reviseDate) {
        this.reviseDate = reviseDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "OntologyGroup [groupId=" + groupId + ", caption=" + caption
                + ", createDate=" + createDate + ", creator=" + creator
                + ", reviseDate=" + reviseDate + ", remark=" + remark + "]";
    }

}
