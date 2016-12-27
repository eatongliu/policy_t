package com.gpdata.wanyou.ansj.service.impl;

import com.gpdata.wanyou.ansj.dao.StopLibraryDao;
import com.gpdata.wanyou.ansj.entity.StopLibrary;
import com.gpdata.wanyou.ansj.service.StopLibraryService;
import com.gpdata.wanyou.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yaz
 * @create 2016-12-12 15:30
 */
@Service
public class StopLibraryServiceImpl extends BaseService implements StopLibraryService {

    @Autowired
    private StopLibraryDao stopLibraryDao;

    @Override
    public void deleteEntity(StopLibrary stopLibrary) {
        stopLibraryDao.deleteEntity(stopLibrary);
    }

    @Override
    public List<StopLibrary> getStopLibraryList() {
        return stopLibraryDao.getStopLibraryList();
    }

    @Override
    public void saveEntity(StopLibrary stopLibrary) {
        stopLibraryDao.saveEntity(stopLibrary);
    }

    @Override
    public void updateEntity(StopLibrary stopLibrary) {
        stopLibraryDao.updateEntity(stopLibrary);
    }

    @Override
    public List<String> getStopWordList() {
        return stopLibraryDao.getStopWordList();
    }
}
