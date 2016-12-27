package com.gpdata.wanyou.dq.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 验证记录详情
 * @author acer_liuyutong
 */
@Entity
@Table(name="validate_record_details")
public class ValidateRecordDetails {

    /**
     * 详情Id
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer detailsId;
    /**
     * 记录Id
     */
    private Integer recordId;
    /**
     * 规则Id
     */
    private Integer ruleId;
    /**
     * 未通过验证数据
     */
    private String npassData;
    /**
     * 错误原因
     */
    private String reason;
    
    public Integer getDetailsId() {
        return detailsId;
    }
    public void setDetailsId(Integer detailsId) {
        this.detailsId = detailsId;
    }
    public Integer getRecordId() {
        return recordId;
    }
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }
    public Integer getRuleId() {
        return ruleId;
    }
    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }
    public String getNpassData() {
        return npassData;
    }
    public void setNpassData(String npassData) {
        this.npassData = npassData;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    @Override
    public String toString() {
        return "ValidateRecordDetails [detailsId=" + detailsId + ", recordId=" + recordId + ", ruleId=" + ruleId
                + ", npassData=" + npassData + ", reason=" + reason + "]";
    }
    
}