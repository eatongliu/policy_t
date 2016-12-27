package com.gpdata.wanyou.dq.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 范围验证规则dto
 * @author ligang
 * 2016年11月14日
 */

public class ValidateRangeRuleDto implements Serializable {

    private static final long serialVersionUID = 1L;
    //范围验证规则 id
    private Integer ruleId;
    //元数据id
    private Integer metadataId;
    //元数据实体id
    private Integer entityId;
    //元数据实体id List
    private List<Integer> entityIdList;
    //字段id List
    private List<Integer> fieldIdList;
    //元数据名称
    private String metadataCaption;
    //元数据实体名称
    private String metadataEntityCaption;
    //验证公式id
    private Integer formulaId;
    //验证公式名称
    private String formulaName;
    //最大值
    private String maxVal;
    //最小值
    private String minVal;
    //数据精度
    private Integer dataPrecision;
    //创建人Id
    private String creatorId;
    //创建人名字
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
    public Integer getMetadataId() {
        return metadataId;
    }
    public void setMetadataId(Integer metadataId) {
        this.metadataId = metadataId;
    }
    public Integer getEntityId() {
        return entityId;
    }
    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }
    public List<Integer> getEntityIdList() {
        return entityIdList;
    }
    public void setEntityIdList(List<Integer> entityIdList) {
        this.entityIdList = entityIdList;
    }
    public List<Integer> getFieldIdList() {
        return fieldIdList;
    }
    public void setFieldIdList(List<Integer> fieldIdList) {
        this.fieldIdList = fieldIdList;
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

    public String getMetadataCaption() {
        return metadataCaption;
    }
    public void setMetadataCaption(String metadataCaption) {
        this.metadataCaption = metadataCaption;
    }
    public String getMetadataEntityCaption() {
        return metadataEntityCaption;
    }
    public void setMetadataEntityCaption(String metadataEntityCaption) {
        this.metadataEntityCaption = metadataEntityCaption;
    }
    public String getFormulaName() {
        return formulaName;
    }
    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }
    public String getCreatorName() {
        return creatorName;
    }
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    public Integer getRuleStatus() {
        return ruleStatus;
    }
    public void setRuleStatus(Integer ruleStatus) {
        this.ruleStatus = ruleStatus;
    }
    
}
