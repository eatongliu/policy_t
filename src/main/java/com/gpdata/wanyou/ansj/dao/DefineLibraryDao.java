package com.gpdata.wanyou.ansj.dao;

import com.gpdata.wanyou.ansj.entity.DefineLibrary;

import java.util.List;

/**
 * DefineLibrary的Dao接口
 * Created by yaz on 2016/12/12.
 */
public interface DefineLibraryDao {

    void saveEntity(DefineLibrary defineLibrary);

    void deleteEntity(DefineLibrary defineLibrary);

    void updateEntity(DefineLibrary defineLibrary);

    List<DefineLibrary> getDefineLibraryList();

    List<String> getDefineWordList();
}
