package com.gpdata.wanyou.ds.util.resource;

import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.ds.entity.DataSourceTable;
import com.gpdata.wanyou.ds.util.resource.dto.FieldInfoDto;
import com.gpdata.wanyou.ds.util.resource.dto.TablesInfoDto;

import java.sql.Connection;
import java.util.List;

/**
 * Created by yaz on 2016/11/8.
 */
public interface DataResourceHelper {

    /**
     * 获取数据源中的表信息
     */
    List<TablesInfoDto> getTablesByDataResource(DataSourceResource dataSourceResource, Connection conn);

    /**
     * 获取指定表中的字段信息
     */
    List<FieldInfoDto> getFieldsByTable(DataSourceResource dataSourceResource, DataSourceTable dataSourceTable, Connection conn);

    /**
     * 获取连接
     */
    Connection getConnection(DataSourceResource dataSourceResource);
}
