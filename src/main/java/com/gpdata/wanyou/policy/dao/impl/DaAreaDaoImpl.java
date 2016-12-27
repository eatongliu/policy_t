package com.gpdata.wanyou.policy.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.policy.dao.DaAreaDao;
import com.gpdata.wanyou.policy.entity.DaArea;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wenjie
 */
@Repository
public class DaAreaDaoImpl extends BaseDao implements DaAreaDao {

    @Override
    public List<DaArea> queryAcea() {
        String sql = "from DaArea where parentid = 0";

        Query query = getCurrentSession().createQuery(sql);

        //返回list
        List<DaArea> dAreas = query.list();

        return dAreas;
    }

    @Override
    public List<DaArea> querycity(Long codeid) {
        String sql = "from DaArea where parentid = :codeid";
        Query query = getCurrentSession().createQuery(sql);
        if (codeid != null) {
            query.setLong("codeid", codeid);
        }
        //返回市级菜单内容
        List<DaArea> city = query.list();

        return city;
    }


}
