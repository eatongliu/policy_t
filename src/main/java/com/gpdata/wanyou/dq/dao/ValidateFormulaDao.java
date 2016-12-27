package com.gpdata.wanyou.dq.dao;

import java.util.List;

import com.gpdata.wanyou.dq.entity.ValidateFormula;

/**
 * @author acer_liuyutong
 */
public interface ValidateFormulaDao {
    /**
     * 获取验证公式列表
     */
    Integer getTotal(String formulaName, Integer limit, Integer offset);
    
    List<ValidateFormula> getRows(String formulaName, Integer limit, Integer offset);
    
}
