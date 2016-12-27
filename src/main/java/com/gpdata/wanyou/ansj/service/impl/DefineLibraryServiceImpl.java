package com.gpdata.wanyou.ansj.service.impl;

import com.gpdata.wanyou.ansj.dao.DefineLibraryDao;
import com.gpdata.wanyou.ansj.entity.DefineLibrary;
import com.gpdata.wanyou.ansj.service.DefineLibraryService;
import com.gpdata.wanyou.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yaz
 * @create 2016-12-12 15:25
 */

@Service
public class DefineLibraryServiceImpl extends BaseService implements DefineLibraryService {
    @Autowired
    private DefineLibraryDao defineLibraryDao;

    @Override
    public void deleteEntity(DefineLibrary defineLibrary) {
        defineLibraryDao.deleteEntity(defineLibrary);
    }

    @Override
    public List<DefineLibrary> getDefineLibraryList() {
        return defineLibraryDao.getDefineLibraryList();
    }

    @Override
    public void saveEntity(DefineLibrary defineLibrary) {
        defineLibraryDao.saveEntity(defineLibrary);
    }

    @Override
    public void updateEntity(DefineLibrary defineLibrary) {
        defineLibraryDao.updateEntity(defineLibrary);
    }

    @Override
    public List<String> getDefineWordList() {
        return defineLibraryDao.getDefineWordList();
    }
}
