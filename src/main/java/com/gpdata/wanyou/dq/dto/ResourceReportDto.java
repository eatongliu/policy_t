package com.gpdata.wanyou.dq.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author liuyutong on 2016年11月24日
 */
public class ResourceReportDto implements Serializable{
    private static final long serialVersionUID = 1L;
    //通过验证的数据量
    private BigDecimal passValidate;
    //未通过验证的数据量
    private BigDecimal npassValidate;
    //通过验证百分比
    private BigDecimal passPercent;
    
    public BigDecimal getPassValidate() {
        return passValidate;
    }
    public void setPassValidate(BigDecimal passValidate) {
        this.passValidate = passValidate;
    }
    public BigDecimal getNpassValidate() {
        return npassValidate;
    }
    public void setNpassValidate(BigDecimal npassValidate) {
        this.npassValidate = npassValidate;
    }
    public BigDecimal getPassPercent() {
        return passPercent;
    }
    public void setPassPercent(BigDecimal passPercent) {
        this.passPercent = passPercent;
    }
    @Override
    public String toString() {
        return "ResourceReportDto [passValidate=" + passValidate + ", npassValidate=" + npassValidate + ", passPercent="
                + passPercent + "]";
    }
}
