package com.gpdata.wanyou.system.service;

import com.gpdata.wanyou.system.entity.SystemUrl;

import java.util.Map;

/**
 * Created by chengchao on 2016/10/26.
 */
public interface SystemUrlService {

    Map<String, SystemUrl> retrieveAllSystemUrl();

}
