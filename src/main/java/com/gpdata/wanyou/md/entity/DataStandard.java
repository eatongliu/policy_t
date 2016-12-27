package com.gpdata.wanyou.md.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 数据标准
 *
 * @author acer_liuyutong
 */
@Entity
@Table(name = "data_standard")
public class DataStandard implements Serializable {

    private static final long serialVersionUID = 1L;

    //数据标准id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer standId;

    //数据标准编号
    private String standSn;

    //数据标准名称
    private String standName;

    //发布部门
    private String pubDept;

    //发布日期
    private Date pubDate;

    //实施日期
    private Date effectDate;

    //主管部门
    private String adminDept;

    //适用范围
    private String applyScope;

    //状态
    private String standStatus;

    //备注
    private String remark;

    public Integer getStandId() {
        return standId;
    }

    public void setStandId(Integer standId) {
        this.standId = standId;
    }

    public String getStandSn() {
        return standSn;
    }

    public void setStandSn(String standSn) {
        this.standSn = standSn;
    }

    public String getStandName() {
        return standName;
    }

    public void setStandName(String standName) {
        this.standName = standName;
    }

    public String getPubDept() {
        return pubDept;
    }

    public void setPubDept(String pubDept) {
        this.pubDept = pubDept;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public Date getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }

    public String getAdminDept() {
        return adminDept;
    }

    public void setAdminDept(String adminDept) {
        this.adminDept = adminDept;
    }

    public String getApplyScope() {
        return applyScope;
    }

    public void setApplyScope(String applyScope) {
        this.applyScope = applyScope;
    }

    public String getStandStatus() {
        return standStatus;
    }

    public void setStandStatus(String standStatus) {
        this.standStatus = standStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "DataStandard [standId=" + standId + ", standSn=" + standSn + ", standName=" + standName + ", pubDept="
                + pubDept + ", pubDate=" + pubDate + ", effectDate=" + effectDate + ", adminDept=" + adminDept
                + ", applyScope=" + applyScope + ", standStatus=" + standStatus + ", remark=" + remark + "]";
    }

}