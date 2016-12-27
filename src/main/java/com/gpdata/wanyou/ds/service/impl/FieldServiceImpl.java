package com.gpdata.wanyou.ds.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.ds.dao.FieldDao;
import com.gpdata.wanyou.ds.entity.DataSourceField;
import com.gpdata.wanyou.ds.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 数据字段服务层
 *
 * @author qyl
 */
@Service
public class FieldServiceImpl extends BaseService implements FieldService {

    @Autowired
    private FieldDao fieldDao;

    /**
     * 通过ID查询数据字段
     *
     * @param fieldId
     * @return
     */
    @Override
    public DataSourceField getDataFieldById(Integer fieldId) {
        return fieldDao.getDataFieldById(fieldId);
    }

    /**
     * 关键字模糊查询数据字段
     *
     * @param tableId
     * @param caption
     * @param fieldName
     * @param limit
     * @param offset
     * @return
     */
    @Override
    public Map<String, Object> getDataTableColumns(Integer tableId, String caption,
                                                   String fieldName, Integer limit, Integer offset) {
        return fieldDao.getDataTableColumns(tableId, caption,
                fieldName, limit, offset);
    }


}
