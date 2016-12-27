package com.gpdata.wanyou.dq.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.dq.dao.ValidateFormulaDao;
import com.gpdata.wanyou.dq.entity.ValidateFormula;
/**
 * @author acer_liuyutong
 */
@Repository
public class ValidateFormulaDaoImpl extends BaseDao implements ValidateFormulaDao{

    /**
     * 获取验证公式列表
     */
    @Override
    public Integer getTotal(String formulaName, Integer limit, Integer offset) {
        String hql = "select count(v) from ValidateFormula v ";
        if (StringUtils.isNotBlank(formulaName)) {
            hql += "where v.formulaName like :formulaName ";
        }
        
        Query query = getCurrentSession().createQuery(hql);
        
        if (StringUtils.isNotBlank(formulaName)) {
        	query.setParameter("formulaName", formulaName);
        }
        
        String total = query.uniqueResult().toString();
        return Integer.valueOf(total,10);
	}

	@Override
	public List<ValidateFormula> getRows(String formulaName, Integer limit, Integer offset) {
		String hql = "from ValidateFormula v ";
        if (StringUtils.isNotBlank(formulaName)) {
            hql += "where v.formulaName like :formulaName ";
        }
        
        Query query = getCurrentSession().createQuery(hql);
        
		if (StringUtils.isNotBlank(formulaName)) {
        	query.setParameter("formulaName", formulaName);
        }
        
		List<ValidateFormula> list = query.setFirstResult(offset).setMaxResults(limit).list();
        
		return list;
	}

}
