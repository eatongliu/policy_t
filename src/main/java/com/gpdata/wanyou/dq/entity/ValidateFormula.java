package com.gpdata.wanyou.dq.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

/**
 * 验证公式
 * @author acer_liuyutong
 */
@Entity
@Table(name = "validate_formula")
public class ValidateFormula implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer formulaId;
    
    /**
     * 表达式名称
     */
    private String formulaName;
    
    /**
     * 表达式
     */
    private String formula;
    
    /**
     * 创建人id
     */
    private String creatorId;
    
    /**
     * 创建人名字
     */
    @Formula(value="(select a.nickName from sys_user a where a.userId = creatorId)")
    private String creatorName;
    
    /**
     * 创建日期
     */
    private Date createDate;
    
    /**
     * 修改日期
     */
    private Date reviseDate;
    
    /**
     * 备注
     */
    private String remark;

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

	public String getformula() {
		return formula;
	}

	public void setformula(String formula) {
		this.formula = formula;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
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

    @Override
    public String toString() {
        return "validateFomular [formulaId=" + formulaId + ", formulaName=" + formulaName + ", formula=" + formula
                + ", creatorId=" + creatorId + ", creatorName=" + creatorName + ", createDate=" + createDate
                + ", reviseDate=" + reviseDate + ", remark=" + remark + "]";
    }
    
}