package com.gpdata.wanyou.md.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class MetadataCompareDto implements Serializable{

    private static final long serialVersionUID = 1L;

	//比对id
    private Integer id;
		
    //表id
    private Integer tableId;
		
    //表名
    private String tableCaption;
		
    //字段总数
    private Integer totalField;
		
    //合规字段
    private Integer standField;
		
    //非合规字段
    private Integer nstandField;
		
    //匹配百分比
    private BigDecimal standPercent;

    public Integer getId() {
        return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public String getTableCaption() {
		return tableCaption;
	}

	public void setTableCaption(String tableCaption) {
		this.tableCaption = tableCaption;
	}

	public Integer getTotalField() {
		return totalField;
	}

	public void setTotalField(Integer totalField) {
		this.totalField = totalField;
	}

	public Integer getStandField() {
		return standField;
	}

	public void setStandField(Integer standField) {
		this.standField = standField;
	}

	public Integer getNstandField() {
		return totalField - standField;
	}

	public void setNstandField(Integer nstandField) {
		this.nstandField = nstandField;
	}

	public BigDecimal getStandPercent() {
		return this.standPercent;
	}

    public void setStandPercent(BigDecimal standPercent) {
        this.standPercent = standPercent;
	}

    @Override
    public String toString() {
	    return "MetadataCompareDto [id=" + id + ", tableId=" + tableId + ", tableCaption=" + tableCaption + ", totalField="
	    	    + totalField + ", standField=" + standField + ", nstandField=" + nstandField + ", standPercent="
	    	    + standPercent + "]";
    }
}
