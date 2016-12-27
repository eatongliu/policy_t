package com.gpdata.wanyou.admin.service.impl;

import com.gpdata.wanyou.admin.dao.ProblemRecordDao;
import com.gpdata.wanyou.admin.service.ProblemRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by ligang on 2016/12/10.
 */
@Service
public class ProblemRecordServiceImpl implements ProblemRecordService{

    @Resource
    private ProblemRecordDao problemRecordDao;
    @Override
    public Map<String, Object> getProblemRecordList(String input, Integer limit, Integer offset) {
        return problemRecordDao.getProblemRecordList(input,limit,offset);
    }
}
