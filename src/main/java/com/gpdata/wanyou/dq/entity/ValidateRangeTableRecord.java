package com.gpdata.wanyou.dq.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ligang
 * 2016年11月24日
 */
@Entity
@Table(name="validate_range_table_record")
public class ValidateRangeTableRecord {
    /**
     * 记录Id
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer vrtrId;
    /**
     * 数据源Id
     */
    private String datasourceId;
    /**
     * 表Id
     */
    private String tableId;
    /**
     * 验证日期
     */
    private Date validateDate;
    /**
     * 通过验证数量
     */
    private Integer passValidate;
    /**
     * 未通过验证数量
     */
    private Integer npassValidate;
    /**
     * 通过率  passPercent
     */
    private String passPercent;
    public Integer getVrtrId() {
        return vrtrId;
    }
    public void setVrtrId(Integer vrtrId) {
        this.vrtrId = vrtrId;
    }
    public String getDatasourceId() {
        return datasourceId;
    }
    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }
    public String getTableId() {
        return tableId;
    }
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
    public Date getValidateDate() {
        return validateDate;
    }
    public void setValidateDate(Date validateDate) {
        this.validateDate = validateDate;
    }
    public Integer getPassValidate() {
        return passValidate;
    }
    public void setPassValidate(Integer passValidate) {
        this.passValidate = passValidate;
    }
    public Integer getNpassValidate() {
        return npassValidate;
    }
    public void setNpassValidate(Integer npassValidate) {
        this.npassValidate = npassValidate;
    }
    public String getPassPercent() {
        return passPercent;
    }
    public void setPassPercent(String passPercent) {
        this.passPercent = passPercent;
    }
}
