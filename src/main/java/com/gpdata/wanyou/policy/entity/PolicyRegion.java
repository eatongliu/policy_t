package com.gpdata.wanyou.policy.entity;
/**
 * 颁布机构
 * @author  wenjie
 */
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "policy_region")
public class PolicyRegion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long RegionId;
	
	/**
	 * 政策文件地区名称
	 */
	private String regionName;
	
	/**
	 * 父类ID
	 */
	private Integer pid;

	public Long getRegionId() {
		return RegionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public Integer getPid() {
		return pid;
	}

	public void setRegionId(Long regionId) {
		RegionId = regionId;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PolicyRegion [RegionId=");
		builder.append(RegionId);
		builder.append(", regionName=");
		builder.append(regionName);
		builder.append(", pid=");
		builder.append(pid);
		builder.append("]");
		return builder.toString();
	}
	
	
}
