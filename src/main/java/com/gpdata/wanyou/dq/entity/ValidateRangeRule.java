package com.gpdata.wanyou.dq.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

/**
 * 范围验证规则实体
 * @author ligang
 * 2016年11月14日
 */

@Entity
@Table(name = "validate_range_rule")
public class ValidateRangeRule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //范围验证规则 id
    private Integer ruleId;
    //元数据实体id
    private Integer entityId;
    //验证公式id
    private Integer formulaId;
    //验证公式名称
    @Formula(value="(select a.formulaName from validate_formula a where a.formulaId = formulaId)")
    private String formulaName;
    //最大值
    String  maxVal;
    //最小值
    String  minVal;
    //数据精度
    private Integer dataPrecision;
    //创建人Id
    private String creatorId;
    //创建人名字
    @Formula(value="(select a.nickName from sys_user a where a.userId = creatorId)")
    private String creatorName;
    //创建日期
    private Date createDate;
    //修改日期
    private Date reviseDate;
    //备注
    private String remark;
    //规则状态
    private Integer ruleStatus;
    
    public Integer getRuleId() {
        return ruleId;
    }
    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }
    public Integer getEntityId() {
        return entityId;
    }
    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }
    public Integer getFormulaId() {
        return formulaId;
    }
    public void setFormulaId(Integer formulaId) {
        this.formulaId = formulaId;
    }
    public Integer getDataPrecision() {
        return dataPrecision;
    }
    public void setDataPrecision(Integer dataPrecision) {
        this.dataPrecision = dataPrecision;
    }
    public String getMaxVal() {
        return maxVal;
    }
    public void setMaxVal(String maxVal) {
        this.maxVal = maxVal;
    }
    public String getMinVal() {
        return minVal;
    }
    public void setMinVal(String minVal) {
        this.minVal = minVal;
    }
    public String getCreatorId() {
        return creatorId;
    }
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
    public String getCreatorName() {
        return creatorName;
    }
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getFormulaName() {
        return formulaName;
    }
    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }
    public Integer getRuleStatus() {
        return ruleStatus;
    }
    public void setRuleStatus(Integer ruleStatus) {
        this.ruleStatus = ruleStatus;
    }
    
    
}
