package com.gpdata.wanyou.dq.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 范围验证结果记录
 * @author acer_liuyutong
 */
@Entity
@Table(name="validate_range_record")
public class ValidateRangeRecord {

    /**
     * 记录Id
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer recordId;
    /**
     * 元数据实体Id
     */
    private Integer entityId;
    /**
     * 规则Id
     */
    private Integer ruleId;
    /**
     * 通过验证数量
     */
    private Integer passValidate;
    /**
     * 未通过验证数量
     */
    private Integer npassValidate;
    /**
     * 通过率	passPercent
     */
    private double passPercent;
    /**
     * 验证日期
     */
    private Date validateDate;
	public Integer getRecordId() {
		return recordId;
	}
	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}
	public Integer getEntityId() {
        return entityId;
    }
    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }
    public Integer getRuleId() {
		return ruleId;
	}
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
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
	public double getPassPercent() {
		return passPercent;
	}
	public void setPassPercent(double passPercent) {
		this.passPercent = passPercent;
	}
	public Date getValidateDate() {
		return validateDate;
	}
	public void setValidateDate(Date validateDate) {
		this.validateDate = validateDate;
	}
	public void initRecord(){
	    this.npassValidate = 0;
	    this.passValidate = 0;
	    this.passPercent = 0;
	}
	public void incrPassData() {
	   this.passValidate++;
	   this.passPercent = (double)passValidate/(double)(passValidate + npassValidate);
	}
	public void incrNPassData() {
	   this.npassValidate++;
	   this.passPercent = (double)passValidate/(double)(passValidate + npassValidate);
	}
    @Override
    public String toString() {
        return "ValidateRangeRecord [recordId=" + recordId + ", entityId=" + entityId + ", ruleId=" + ruleId
                + ", passValidate=" + passValidate + ", npassValidate=" + npassValidate + ", passPercent=" + passPercent
                + ", validateDate=" + validateDate + "]";
    }
}