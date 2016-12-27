package com.gpdata.wanyou.base.service.impl;

import com.gpdata.wanyou.base.dao.SimpleDao;
import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.base.service.SimpleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chengchao on 2016/10/28.
 */
@Service
public class SimpleServiceImpl extends BaseService implements SimpleService {

    @Autowired
    private SimpleDao simpleDao;

    @Override
    public <T> Serializable save(T bean) {
        return simpleDao.save(bean);
    }

    @Override
    public <T> void update(T bean) {
        simpleDao.update(bean);
    }

    @Override
    public void update(Serializable id, Object partValues) {
        simpleDao.update(id, partValues);
    }

    @Override
    public <T> void delete(T bean) {
        simpleDao.delete(bean);
    }

    @Override
    public <T> void delete(Class<T> type, Serializable id) {
        simpleDao.delete(type, id);
    }

    @Override
    public <T> T merge(T bean) {
        return simpleDao.merge(bean);
    }

    @Override
    public <T> void persist(T bean) {
        simpleDao.persist(bean);
    }

    @Override
    public <T> T getById(Class<T> type, Serializable id) {
        return simpleDao.getById(type, id);
    }

    @Override
    public <T> List<T> getAll(Class<T> type, String orderByLiteral) {
        return simpleDao.getAll(type, orderByLiteral);
    }
}
