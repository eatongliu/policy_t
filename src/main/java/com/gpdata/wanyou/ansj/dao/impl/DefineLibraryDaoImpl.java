package com.gpdata.wanyou.ansj.dao.impl;

import com.gpdata.wanyou.ansj.dao.DefineLibraryDao;
import com.gpdata.wanyou.ansj.entity.DefineLibrary;
import com.gpdata.wanyou.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DefineLibraryDao的实现
 *
 * @author yaz
 * @create 2016-12-12 13:48
 */
@Repository
public class DefineLibraryDaoImpl extends BaseDao implements DefineLibraryDao {

    @Override
    public void deleteEntity(DefineLibrary defineLibrary) {
        getCurrentSession().delete(defineLibrary);
    }

    @Override
    public List<DefineLibrary> getDefineLibraryList() {
        return getCurrentSession().createQuery("from DefineLibrary d order by d.createDate desc").list();
    }

    @Override
    public void saveEntity(DefineLibrary defineLibrary) {
        getCurrentSession().save(defineLibrary);
    }

    @Override
    public void updateEntity(DefineLibrary defineLibrary) {
        getCurrentSession().update(defineLibrary);
    }

    @Override
    public List<String> getDefineWordList() {
        return getCurrentSession().createQuery("select d.word from DefineLibrary d order by d.createDate desc").list();
    }
}
