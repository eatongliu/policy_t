package com.gpdata.wanyou.md.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 元数据比对成果
 * @author acer_liuyutong
 */
@Entity
@Table(name="metadata_compare")
public class MetadataCompare implements Serializable{
	
	private static final long serialVersionUID = 1L;

	//比对id
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	//表id
	private Integer tableId;
	
	//字段总数
	private Integer totalField;
	
	//合规字段
	private Integer standField;
	
	//非合规字段

	private Integer nstandField;
	
	//匹配百分比
	private Double standPercent;

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

	public Double getStandPercent() {
		return this.standPercent;
	}

	public void setStandPercent(Double standPercent) {
		this.standPercent = standPercent;
	}

	public void incrStandField(){
		if (standField == totalField) {
			return;
		}
		this.standField ++;
		this.nstandField = totalField - standField;
		this.standPercent = (double)standField/(double)totalField;
	}

	@Override
	public String toString() {
		return "MetadataCompare [id=" + id + ", tableId=" + tableId + ", totalField=" + totalField + ", standField="
				+ standField + ", nstandField=" + nstandField + ", standPercent=" + standPercent + "]";
	}

}