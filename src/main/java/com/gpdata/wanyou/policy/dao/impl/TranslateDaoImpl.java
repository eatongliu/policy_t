package com.gpdata.wanyou.policy.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.policy.dao.TranslateDao;
import com.gpdata.wanyou.policy.entity.PolicyTags;
/**
 * 
 * @author wenjie  2016年12月17日16:52:10
 *
 */
@Repository
public class TranslateDaoImpl extends BaseDao implements TranslateDao{

	/**
	 * 返回每条数据的权重
	 */
	@Override
    public List<PolicyTags> getPolicytags(Long pdId) {
        String sql = "from PolicyTags where pdId = :pdId";

        Query query = getCurrentSession().createQuery(sql);
        if (pdId != null) {
        	query.setLong("pdId", pdId);
		}

        //返回list
        List<PolicyTags> tags = query.list();

        return tags;
    }
}
