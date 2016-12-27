package com.gpdata.wanyou.dq.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author acer_liuyutong
 */
public class RecordDetailsDto implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 详情Id
     */
    private Integer detailsId;
    /**
     * 未通过验证数据
     */
    private String npassData;
    /**
     * 错误原因
     */
    private String reason;
    /**
     * 验证日期
     */
    private Date validateDate;
    /**
     * 最大值
     */
    private Double maxVal;
    /**
     * 最小值
     */
    private Double minVal;
    /**
     * 数据精度
     */
    private Integer dataPrecision;
    /**
     * 验证公式Id
     */
    private Integer formulaId;
    /**
     * 验证公式
     */
    private String formulaName;
    /**
     * 元数据caption
     */
    private String metadataCaption;
    /**
     * 元数据实体caption
     */
    private String metadataEntityCaption;
    public Integer getDetailsId() {
        return detailsId;
    }
    public void setDetailsId(Integer detailsId) {
        this.detailsId = detailsId;
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
    public Date getValidateDate() {
        return validateDate;
    }
    public void setValidateDate(Date validateDate) {
        this.validateDate = validateDate;
    }
    public Double getMaxVal() {
        return maxVal;
    }
    public void setMaxVal(Double maxVal) {
        this.maxVal = maxVal;
    }
    public Double getMinVal() {
        return minVal;
    }
    public void setMinVal(Double minVal) {
        this.minVal = minVal;
    }
    public Integer getDataPrecision() {
        return dataPrecision;
    }
    public void setDataPrecision(Integer dataPrecision) {
        this.dataPrecision = dataPrecision;
    }
    public Integer getFormulaId() {
        return formulaId;
    }
    public void setFormulaId(Integer formulaId) {
        this.formulaId = formulaId;
    }
    public String getFormulaName() {
        return formulaName;
    }
    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
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
    @Override
    public String toString() {
        return "RecordDetailsDto [detailsId=" + detailsId + ", npassData=" + npassData + ", reason=" + reason
                + ", validateDate=" + validateDate + ", maxVal=" + maxVal + ", minVal=" + minVal + ", dataPrecision="
                + dataPrecision + ", formulaId=" + formulaId + ", formulaName=" + formulaName + ", metadataCaption="
                + metadataCaption + ", metadataEntityCaption=" + metadataEntityCaption + "]";
    }
}