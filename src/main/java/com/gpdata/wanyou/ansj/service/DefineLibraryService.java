package com.gpdata.wanyou.ansj.service;

import com.gpdata.wanyou.ansj.entity.DefineLibrary;

import java.util.List;

public interface DefineLibraryService {

    void saveEntity(DefineLibrary defineLibrary);

    void deleteEntity(DefineLibrary defineLibrary);

    void updateEntity(DefineLibrary defineLibrary);

    List<DefineLibrary> getDefineLibraryList();

    List<String> getDefineWordList();
}
