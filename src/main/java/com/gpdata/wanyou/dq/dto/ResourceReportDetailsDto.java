package com.gpdata.wanyou.dq.dto;

import java.io.Serializable;
/**
 * 
 * @author liuyutong on 2016年11月24日
 */
public class ResourceReportDetailsDto implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * 详情Id
     */
    private Integer detailsId;
    /**
     * 数据源名称
     */
    private String resourceCaption;
    /**
     * 表名称
     */
    private String tableCaption;
    /**
     * 字段名称
     */
    private String fieldCaption;
    /**
     * 元数据名称
     */
    private String metadataCaption;
    /**
     * 元数据实体名称
     */
    private String metadataEntityCaption;
    /**
     * 未通过的数据
     */
    private String npassData;
    /**
     * 未通过原因
     */
    private String reason;
    public Integer getDetailsId() {
        return detailsId;
    }
    public void setDetailsId(Integer detailsId) {
        this.detailsId = detailsId;
    }
    public String getResourceCaption() {
        return resourceCaption;
    }
    public void setResourceCaption(String resourceCaption) {
        this.resourceCaption = resourceCaption;
    }
    public String getTableCaption() {
        return tableCaption;
    }
    public void setTableCaption(String tableCaption) {
        this.tableCaption = tableCaption;
    }
    public String getFieldCaption() {
        return fieldCaption;
    }
    public void setFieldCaption(String fieldCaption) {
        this.fieldCaption = fieldCaption;
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
        return "ResourceReportDto [detailsId=" + detailsId + ", resourceCaption=" + resourceCaption + ", tableCaption="
                + tableCaption + ", fieldCaption=" + fieldCaption + ", metadataCaption=" + metadataCaption
                + ", metadataEntityCaption=" + metadataEntityCaption + ", npassData=" + npassData + ", reason=" + reason
                + "]";
    }
}
