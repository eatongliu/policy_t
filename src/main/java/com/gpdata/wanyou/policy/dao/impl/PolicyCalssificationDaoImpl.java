package com.gpdata.wanyou.policy.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.gpdata.wanyou.base.dao.BaseDao;

import com.gpdata.wanyou.policy.dao.PolicyClassificationDao;
import com.gpdata.wanyou.policy.entity.PolicyClassification;
/**
 * 
 * 主题分类
 * @author wenjie
 *
 */
@Repository
public class PolicyCalssificationDaoImpl  extends BaseDao implements PolicyClassificationDao{

	/**
	 * 获取主题分类一级菜单
	 */
	@Override
	public List<PolicyClassification> queryClass() {
		String sql ="from PolicyClassification where type = 1";
		
		 Query query = getCurrentSession().createQuery(sql);
		 
		 //返回list
		 List<PolicyClassification> Classificationlist = query.list();
		 
		return Classificationlist;
	}
	
	@Override
	public List<PolicyClassification> queryClass2(Long Id) {
		String sql ="from PolicyClassification where Pid = :Id";
		
		 Query query = getCurrentSession().createQuery(sql);
		 if (Id != null) {
	            query.setLong("Id", Id );
	        }
		 //返回list
		 List<PolicyClassification> Classificationlist2 = query.list();
		 
		return Classificationlist2;
		
		
	}
}
