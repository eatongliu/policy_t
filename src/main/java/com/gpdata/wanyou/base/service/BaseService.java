package com.gpdata.wanyou.base.service;

import com.gpdata.wanyou.base.dao.SimpleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by chengchao on 2016/10/25.
 */
public class BaseService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected SimpleDao simpleDao;
}
