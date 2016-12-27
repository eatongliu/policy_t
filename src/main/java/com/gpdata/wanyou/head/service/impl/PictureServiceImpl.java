package com.gpdata.wanyou.head.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.head.dao.PictureDao;
import com.gpdata.wanyou.head.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author yaz
 * @create 2016-12-16 11:17
 */
@Service
public class PictureServiceImpl extends BaseService implements PictureService {
    @Autowired
    private PictureDao dao;

    @Override
    public Map<String, List> getAll() {
        return dao.getAll();
    }
}
