package com.gpdata.wanyou.ds.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.ds.dao.ResourceDao;
import com.gpdata.wanyou.ds.dao.TableDao;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.ds.entity.DataSourceTable;
import com.gpdata.wanyou.ds.entity.NewsDto;
import com.gpdata.wanyou.ds.service.ContentService;
import com.gpdata.wanyou.ds.util.PrestoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ContentServiceImpl extends BaseService implements ContentService {

    @Autowired
    private TableDao TableDao;
    @Autowired
    private ResourceDao ResourceDao;

    /**
     * presto插入文本
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean insertDB(NewsDto newsDto) {
        DataSourceTable table = TableDao.getDataTableById(Integer.parseInt(newsDto.getTableid()));
        String tablename = table.getTableName();
        int resourceId = table.getResourceId();
        DataSourceResource dataSource = ResourceDao.getDataSourceById(resourceId);
        if (dataSource == null) {
            throw new RuntimeException("数据源不存在！");
        }
        Connection connection = PrestoUtil.getConnection(dataSource.getDbName(), dataSource.getUserName(), dataSource.getPassWord());

        try {
            List<Map<String, Object>> row = newsDto.getRow();
            List<String> fieldnames = new ArrayList<String>();
            List<String> fieldvalues = new ArrayList<String>();
            for (Map<String, Object> map : row) {
                for (String key : map.keySet()) {
                    fieldnames.add(key);
                    fieldvalues.add(map.get(key).toString());
                }
                PrestoUtil.insertDB(connection, tablename, fieldnames, fieldvalues);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * presto清空表内容
     */
    @Override
    public boolean clear(NewsDto newsDto) {

        DataSourceTable table = TableDao.getDataTableById(Integer.parseInt(newsDto.getTableid()));
        String tablename = table.getTableName();
        int resourceId = table.getResourceId();

        DataSourceResource dataSource = ResourceDao.getDataSourceById(resourceId);
        if (dataSource == null) {
            throw new RuntimeException("数据源不存在！");
        }
        Connection connection = PrestoUtil.getConnection(dataSource.getDbName(), dataSource.getUserName(), dataSource.getPassWord());

        return PrestoUtil.clear(connection, tablename);
    }

}