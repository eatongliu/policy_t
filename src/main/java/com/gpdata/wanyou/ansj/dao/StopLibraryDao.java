package com.gpdata.wanyou.ansj.dao;

import com.gpdata.wanyou.ansj.entity.StopLibrary;

import java.util.List;

/**
 * StopLibrary的Dao接口
 * Created by yaz on 2016/12/12.
 */
public interface StopLibraryDao {

    void saveEntity(StopLibrary stopLibrary);

    void deleteEntity(StopLibrary stopLibrary);

    void updateEntity(StopLibrary stopLibrary);

    List<StopLibrary> getStopLibraryList();

    List<String> getStopWordList();
}
