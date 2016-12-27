package com.gpdata.wanyou.dq.service;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.gpdata.wanyou.dq.entity.ValidateFormula;

/**
 * @author acer_liuyutong
 */
public interface ValidateFormulaService {

	/**
	 * 获取验证公式列表
	 * @param formulaName
	 * @param offset
	 * @param limit
	 */
	Pair<Integer, List<ValidateFormula>> getByConditions(String formulaName, Integer offset, Integer limit);

}
