package com.gpdata.wanyou.ansj.service;

import com.gpdata.wanyou.ansj.entity.StopLibrary;

import java.util.List;

public interface StopLibraryService {

    void saveEntity(StopLibrary stopLibrary);

    void deleteEntity(StopLibrary stopLibrary);

    void updateEntity(StopLibrary stopLibrary);

    List<StopLibrary> getStopLibraryList();

    List<String> getStopWordList();
}
