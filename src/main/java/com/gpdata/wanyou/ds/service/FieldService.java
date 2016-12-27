package com.gpdata.wanyou.ds.service;

import com.gpdata.wanyou.ds.entity.DataSourceField;

import java.util.List;
import java.util.Map;

public interface FieldService {

    /**
     * 通过ID查询数据字段
     *
     * @param fieldId
     * @return
     */
    DataSourceField getDataFieldById(Integer fieldId);


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
    Map<String, Object> getDataTableColumns(Integer tableId, String caption,
                                            String fieldName, Integer limit, Integer offset);

}
