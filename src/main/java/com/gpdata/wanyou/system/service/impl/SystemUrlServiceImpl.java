package com.gpdata.wanyou.system.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.system.entity.SystemUrl;
import com.gpdata.wanyou.system.service.SystemUrlService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by chengchao on 2016/10/26.
 */
@Service
public class SystemUrlServiceImpl extends BaseService implements SystemUrlService {


    @Override
    public Map<String, SystemUrl> retrieveAllSystemUrl() {
        return null;
    }
}
