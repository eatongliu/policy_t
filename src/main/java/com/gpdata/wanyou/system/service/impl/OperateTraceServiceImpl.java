package com.gpdata.wanyou.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.system.entity.OperateTrace;
import com.gpdata.wanyou.system.service.OperateTraceService;
import org.springframework.stereotype.Service;

/**
 * Created by chengchao on 2016/10/26.
 */
@Service
public class OperateTraceServiceImpl extends BaseService implements OperateTraceService {


    @Override
    public void saveOperateTrace(OperateTrace operateTrace) {

        //String json = JSONObject.toJSONString(operateTrace);

        logger.debug("用户操作 : {}", operateTrace);

    }
}
