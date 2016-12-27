package com.gpdata.wanyou.ds.dao;

import com.gpdata.wanyou.ds.entity.DataSourceField;

import java.util.List;
import java.util.Map;

public interface FieldDao {

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

    /**
     * 新增字段
     *
     * @param dataSourceField
     */
    void addDataField(DataSourceField dataSourceField);

    /**
     * 更新字段
     * 主要是更新状态
     */
    void updateDataField(DataSourceField dataSourceField);

    /**
     * 根据表id 获取字段列表
     */
    List<DataSourceField> getDataFieldByTableId(Integer tableId);
}
