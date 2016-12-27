package com.gpdata.wanyou.policy.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 省市区实体类  da_area
 * @author wenjie
 * 2016年12月13日10:33:16
 */
@Entity
@Table(name = "da_area")
public class DaArea implements Serializable{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Long codeid;
	
	/**
	 * 父类ID
	 */
	private Long parentid;
	
	/**
	 * 地区名称
	 */
	
	private String cityName;

	public Long getCodeid() {
		return codeid;
	}

	public Long getParentid() {
		return parentid;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCodeid(Long codeid) {
		this.codeid = codeid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DaArea [codeid=");
		builder.append(codeid);
		builder.append(", parentid=");
		builder.append(parentid);
		builder.append(", cityName=");
		builder.append(cityName);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
