package com.gpdata.wanyou.policy.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.policy.dao.PolicyRegionDao;
import com.gpdata.wanyou.policy.entity.PolicyRegion;
/**
 * 
 * @author wenjie
 *
 */
@Repository
public class PolicyRegionDaoImpl extends BaseDao implements PolicyRegionDao {

	/**
	 * 获取主题分类一级菜单
	 */
	@Override
	public List<PolicyRegion> queryClass() {
		String sql ="from PolicyRegion where pid = 0";
		
		 Query query = getCurrentSession().createQuery(sql);
		 
		 //返回list
		 List<PolicyRegion> PolicyRegionlist = query.list();
		 
		return PolicyRegionlist;
	}
	
	@Override
	public List<PolicyRegion> queryClass2(Long Id) {
		String sql ="from PolicyRegion where Pid = :Id";
		
		 Query query = getCurrentSession().createQuery(sql);
		 if (Id != null) {
	            query.setLong("Id", Id );
	        }
		 //返回list
		 List<PolicyRegion> PolicyRegionlist2 = query.list();
		 
		return PolicyRegionlist2;
		
		
	}
}
