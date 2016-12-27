package com.gpdata.wanyou.policy.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 新闻
 * 
 * @author wenjie
 *
 */
@Entity
@Table(name = "policy_classification")
public class PolicyClassification implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    /*
     * 政策主题ID
     */
    private Long PolicyclassificationId;

    /*
     * 政策分类名称
     */
    private String PolicyclassificationName;
    
    /*
     * 父类ID
     */
    private Integer PId ;
    
    /*
     * 排序
     */
    private Integer order = Integer.valueOf(1);
    
    /*
     * 菜单级别 {1：一级菜单  ; 2：二级菜单}
     */
    private Integer type;
    

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getPolicyclassificationId() {
		return PolicyclassificationId;
	}

	public String getPolicyclassificationName() {
		return PolicyclassificationName;
	}

	public Integer getPId() {
		return PId;
	}

	public Integer getOrder() {
		return order;
	}

	public Integer getType() {
		return type;
	}

	
	public void setPolicyclassificationId(Long policyclassificationId) {
		PolicyclassificationId = policyclassificationId;
	}

	public void setPolicyclassificationName(String policyclassificationName) {
		PolicyclassificationName = policyclassificationName;
	}

	public void setPId(Integer pId) {
		PId = pId;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "policyClassification [PolicyclassificationId=" + PolicyclassificationId + ", PolicyclassificationName="
				+ PolicyclassificationName + ", PId=" + PId + ", order=" + order + ", type=" + type + "]";
	}
    
	public String toString1() {
		return " {PolicyclassificationId:" + PolicyclassificationId + ", PolicyclassificationName:"
				+ PolicyclassificationName + ", PId:" + PId + ", order:" + order + ", type:" + type + "}";
	}
    
    

}
