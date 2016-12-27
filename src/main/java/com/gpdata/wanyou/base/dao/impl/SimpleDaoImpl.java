package com.gpdata.wanyou.base.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.base.dao.SimpleDao;
import com.gpdata.wanyou.utils.SimpleBeanPropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chengchao on 2016/10/26.
 */
@Repository
public class SimpleDaoImpl extends BaseDao implements SimpleDao {


    @Override
    public <T> Serializable save(T bean) {
        return this.save(bean, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Serializable save(T bean, boolean flushImmediate) {
        Session session = this.getCurrentSession();
        Serializable result =  session.save(bean);
        if (flushImmediate) {
            session.flush();
        }
        return result;
    }

    /**
     * 更新一个实体对象, 实际是调用 Hibernate 的 update 方法
     *
     * @param bean
     * @param <T>
     */
    @Override
    public <T> void update(T bean) {

        getCurrentSession().update(bean);
    }

    /**
     * 使用一个不完整的对象, 更新其中非空的值.
     *
     * @param id
     * @param partValues
     */
    @Override
    public void update(Serializable id, Object partValues) {


        if (partValues == null) {
            logger.warn("参数 partValues 为空, 操作被忽略, 没有数据被改变!");
            return;
        }

        Session session = this.getCurrentSession();

        Object existsData = session.get(partValues.getClass(), id);

        if (existsData == null) {
            logger.warn("被更新的对象不存在, 操作被忽略, 没有数据被改变!");
            return;
        }

        SimpleBeanPropertiesUtil.copyNotNullProperties(partValues, existsData);

        session.update(existsData);

    }

    @Override
    public <T> void delete(T bean) {

        getCurrentSession().delete(bean);
    }

    @Override
    public <T> void delete(Class<T> type, Serializable id) {

        Session session = this.getCurrentSession();
        Object target = session.get(type, id);
        if (target == null) {
            logger.warn("被删除的对象不存在, 操作被忽略, 没有数据被改变!");
            return;
        }

        session.delete(target);

    }

    /**
     * @param bean
     * @param <T>
     * @return
     */
    @Override
    public <T> T merge(T bean) {

        return (T) getCurrentSession().merge(bean);
    }

    @Override
    public <T> void persist(T bean) {

        getCurrentSession().persist(bean);
    }

    @Override
    public <T> T getById(Class<T> type, Serializable id) {

        return (T) getCurrentSession().get(type, id);
    }


    @Override
    public <T> List<T> getAll(Class<T> type, String orderByLiteral) {
        Session session = this.getCurrentSession();
        String hql = " from " + type.getName();

        if (StringUtils.isNotBlank(orderByLiteral)) {
            hql += " order by  " + orderByLiteral;
        }

        List<T> result = session.createQuery(hql).list();

        return result;

    }

}
