package com.gpdata.wanyou.ansj.dao.impl;

import com.gpdata.wanyou.ansj.dao.StopLibraryDao;
import com.gpdata.wanyou.ansj.entity.StopLibrary;
import com.gpdata.wanyou.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * StopLibraryDao的实现
 *
 * @author yaz
 * @create 2016-12-12 13:47
 */
@Repository
public class StopLibraryDaoImpl extends BaseDao implements StopLibraryDao {

    @Override
    public void deleteEntity(StopLibrary stopLibrary) {
        getCurrentSession().delete(stopLibrary);
    }

    @Override
    public List<StopLibrary> getStopLibraryList() {
        return getCurrentSession().createQuery("from StopLibrary s order by s.createDate desc").list();
    }

    @Override
    public void saveEntity(StopLibrary stopLibrary) {
        getCurrentSession().save(stopLibrary);
    }

    @Override
    public void updateEntity(StopLibrary stopLibrary) {
        getCurrentSession().update(stopLibrary);
    }

    @Override
    public List<String> getStopWordList() {
        return getCurrentSession().createQuery("select s.word from StopLibrary s order by s.createDate desc").list();
    }
}
