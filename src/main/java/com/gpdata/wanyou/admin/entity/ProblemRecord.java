package com.gpdata.wanyou.admin.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ligang on 2016/12/10.
 */
@Entity
@Table(name = "problem_record")
public class ProblemRecord {

    /**
     * 问题编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer prId;
    /**
     * 主标题
     */
    private String mainTitle;
    /**
     * 小标题
     */
    private String subtitle;
    /**
     * 摘要
     */
    private String remark;
    /**
     * 内容
     */
    @Column(columnDefinition = "text")
    private String content;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 创建人Id
     */
    private String creatorId;
    /**
     * 修改日期
     */
    private Date reviseDate;
    /**
     * 显示状态
     */
    private String dispStatus;

    public Integer getPrId() {
        return prId;
    }

    public void setPrId(Integer prId) {
        this.prId = prId;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Date getReviseDate() {
        return reviseDate;
    }

    public void setReviseDate(Date reviseDate) {
        this.reviseDate = reviseDate;
    }

    public String getDispStatus() {
        return dispStatus;
    }

    public void setDispStatus(String dispStatus) {
        this.dispStatus = dispStatus;
    }
}
