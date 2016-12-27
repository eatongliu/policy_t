package com.gpdata.wanyou.dq.service.impl;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.dq.dao.ValidateFormulaDao;
import com.gpdata.wanyou.dq.entity.ValidateFormula;
import com.gpdata.wanyou.dq.service.ValidateFormulaService;
/**
 * @author acer_liuyutong
 */
@Service
public class ValidateFormulaServiceImpl extends BaseService implements ValidateFormulaService{

    @Autowired
    private ValidateFormulaDao validateFormulaDao;
    /**
     * 获取验证公式列表
     */
    @Override
    public Pair<Integer, List<ValidateFormula>> getByConditions(String formulaName, Integer offset, Integer limit) {
		
        Integer total = validateFormulaDao.getTotal(formulaName,limit,offset);
        List<ValidateFormula> rows = validateFormulaDao.getRows(formulaName,limit,offset);
        
        return Pair.of(total,rows);
	}

}
