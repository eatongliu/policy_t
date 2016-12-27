package com.gpdata.wanyou.md.entity;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by chengchao on 2016/10/25.
 */
@Entity
@Table(name = "stand_source_map")
public class StandSourceMap implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DATA_STATUS_LOCK = "L";

    /**
     * 数据标准数据源映射
     */
    @Id
    private String ssmId;

    /**
     * 数据标准 id
     */
    private Integer standId;

    /**
     * 数据标准名称
     */
    @Formula(value = "( select ds.standName from data_standard ds where ds.standId = standId )")
    private String standName;

    /**
     * 数据源 id
     */
    private Integer sourceId;

    /**
     * 数据源名称
     */
    @Formula(value = "( select ds.caption from datasource_resource ds where ds.resourceId = sourceId )")
    private String sourceName;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 用户 id
     */
    private String userId;

    /**
     * 创建人
     */
    @Formula(value="(select a.nickName from sys_user a where a.userId = userId)")
    private String creator;

    /**
     * 状态
     */
    private String dataStatus;

    public String getSsmId() {
        return ssmId;
    }

    public void setSsmId(String ssmId) {
        this.ssmId = ssmId;
    }

    public Integer getStandId() {
        return standId;
    }

    public void setStandId(Integer standId) {
        this.standId = standId;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreator() {
        return creator;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getStandName() {
        return standName;
    }

    public void setStandName(String standName) {
        this.standName = standName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @Override
    public String toString() {
        return "StandSourceMap [ssmId=" + ssmId + ", standId=" + standId + ", standName=" + standName + ", sourceId="
                + sourceId + ", sourceName=" + sourceName + ", createDate=" + createDate + ", creator=" + creator
                + ", dataStatus=" + dataStatus + "]";
    }

}