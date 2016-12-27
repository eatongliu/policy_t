package com.gpdata.wanyou.ds.service.impl;

import com.gpdata.wanyou.base.cache.RedisCache;
import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.ds.constant.DataSourceConstant;
import com.gpdata.wanyou.ds.dao.FieldDao;
import com.gpdata.wanyou.ds.dao.ResourceDao;
import com.gpdata.wanyou.ds.dao.TableDao;
import com.gpdata.wanyou.ds.entity.DataSourceField;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.ds.entity.DataSourceTable;
import com.gpdata.wanyou.ds.enums.DataResourceStatus;
import com.gpdata.wanyou.ds.service.ResourceService;
import com.gpdata.wanyou.ds.util.HDFSUtil;
import com.gpdata.wanyou.ds.util.JDBCUtil;
import com.gpdata.wanyou.ds.util.resource.dto.FieldInfoDto;
import com.gpdata.wanyou.ds.util.resource.dto.TablesInfoDto;
import com.gpdata.wanyou.ds.util.resource.factory.DataResourceFactory;
import com.gpdata.wanyou.utils.ConfigUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConstants;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * 数据源相关的service
 *
 * @author yaz
 */
@Service
public class ResourceServiceImpl extends BaseService implements ResourceService {
    //redis中存储的key
    private final String UPDATEDS = "dataResource-isUpdate:";

    @Autowired
    private ResourceDao resourceDao;
    @Autowired
    private TableDao tableDao;
    @Autowired
    private FieldDao fieldDao;
    @Autowired
    private RedisCache redis;

    private DataResourceFactory factory = DataResourceFactory.getFactory();

    /**
     * 新增数据源
     */
    @Override
    public void addDataSourceResource(DataSourceResource dataSourceResource) {
        if (dataSourceResource.getResourceType().equalsIgnoreCase("HDFS")) {
            this.setDsOfHdfsSize(dataSourceResource);
        }

        resourceDao.addDataSource(dataSourceResource);

        if (dataSourceResource.getResourceType().equalsIgnoreCase("DBMS")) {
            Connection conn = null;
            try {
                conn = factory.getHelper(DataSourceConstant.MYSQL).getConnection(dataSourceResource);
                Map<String, String> result = addDataSourceTable(dataSourceResource, conn);
                dataSourceResource.setSize(Long.parseLong(result.get("size")));
                dataSourceResource.setTablesCount(Integer.parseInt(result.get("count")));
                resourceDao.updateDataSource(dataSourceResource);
            } finally {
                JDBCUtil.closeOther(conn);
            }
        }
    }

    /**
     * 根据数据源新增表结构
     * 新增数据源时调用
     */
    @Override
    public Map<String, String> addDataSourceTable(DataSourceResource dataSourceResource, Connection conn) {
        Long totalCount = 0L; //统计数据量大小
        List<TablesInfoDto> queryReslut = factory.getHelper(DataSourceConstant.MYSQL).getTablesByDataResource(dataSourceResource, conn);
        for (TablesInfoDto bean : queryReslut) {
            //处理table
            DataSourceTable table = this.tablesFilter(bean, dataSourceResource);
            tableDao.addDataTable(table);
            //处理field
            Map<String, String> fieldResult = this.addDataSourceField(dataSourceResource, table, conn);
            //更新
            table.setCloumnsCount(Integer.parseInt(fieldResult.get("count")));
            tableDao.updataDataTable(table);
            totalCount += table.getSize();
        }
        HashMap<String, String> result = new HashMap<>();
        result.put("count", String.valueOf(queryReslut.size()));
        result.put("size", totalCount.toString());
        return result;
    }

    /**
     * 根据数据、数据表增加字段信息
     * 新增数据源时调用
     */
    @Override
    public Map<String, String> addDataSourceField(DataSourceResource dataSourceResource, DataSourceTable datasourceTable, Connection conn) {
        List<FieldInfoDto> queryResult = factory.getHelper(DataSourceConstant.MYSQL).getFieldsByTable(dataSourceResource, datasourceTable, conn);
        for (FieldInfoDto bean : queryResult) {
            DataSourceField field = this.fieldsFilter(bean, datasourceTable);
            fieldDao.addDataField(field);
        }
        HashMap<String, String> result = new HashMap<>();
        result.put("count", String.valueOf(queryResult.size()));
        return result;
    }

    @Override
    public DataSourceResource getDataSourceById(int resourceId) {
        return resourceDao.getDataSourceById(resourceId);
    }

    @Override
    public BeanResult getDataSourceList(Map<String, String> params) {
        List<DataSourceResource> list = resourceDao.getDataSourceList(params);
        int total = resourceDao.getTotalDataSource(params);
        return BeanResult.success(total, list);
    }

    @Override
    public List<DataSourceResource> getAllDataSourceWithoutLimit() {
        return resourceDao.getAllDataSourceWithoutLimit();
    }

    @Override
    public BeanResult getAllDataSourceIdAndNameList(String resourceType) {
        List<Map<String, Object>> result = resourceDao.getAllDataSourceIdAndNameList(resourceType);
        return BeanResult.success(result);
    }

    /**
     * 测试DBMS的MySql型数据源连通性
     */
    @Override
    public BeanResult testDataSourceConn(DataSourceResource dataSourceResource) {
        // 目前只读MYSQL
        if (!"MYSQL".equalsIgnoreCase(dataSourceResource.getDataType())) {
            return BeanResult.error("请选择MySql类型数据库！");
        }

        Connection conn = null;
        try {
            conn = factory.getHelper(DataSourceConstant.MYSQL).getConnection(dataSourceResource);
            if (null != conn) {
                return BeanResult.success("测试成功！");
            }
            return BeanResult.error("连接失败");
        } catch (Exception e) {
            logger.error("连接测试异常！{}", e);
            return BeanResult.error("连接失败！");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                    logger.debug("测试数据源连接的Connection已成功关闭！");
                }
            } catch (SQLException e) {
                logger.error("测试数据源连接的Connection关闭失败！{}", e);
            }
        }
    }

    /**
     * 测试链接方法；在自动更新数据源时调用，也可以在添加时调用；
     * 目的：避免多次创建连接
     * 注意：本方法不释放链接，由调用方法释放
     */
    @Override
    public Connection testDataSourceConnWithoutClose(DataSourceResource dataSourceResource) {
        // 目前只读MYSQL
        if (!"MYSQL".equalsIgnoreCase(dataSourceResource.getDataType())) {
            return null;
        }
        Connection conn = null;
        try {
            conn = factory.getHelper(DataSourceConstant.MYSQL).getConnection(dataSourceResource);
            return conn;
        } catch (Exception e) {
            logger.error("连接测试异常！{}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新不可达的数据源，
     * 将所有状态改为统一的不可达
     */
    @Override
    public void updateDataSourceResourceWithoutConn(DataSourceResource dataSourceResource) {
        if (dataSourceResource.getResourceType().equalsIgnoreCase("DBMS")) {
            List<DataSourceTable> tables = tableDao.getTableListByDsId(dataSourceResource.getResourceId());
            for (DataSourceTable table : tables) {
                List<DataSourceField> fields = fieldDao.getDataFieldByTableId(table.getTableId());
                for (DataSourceField field : fields) {
                    field.setStatus(DataResourceStatus.ERROR.getStateInfo());
                    fieldDao.updateDataField(field);
                }
                table.setReviseDate(new Date());
                table.setStatus(DataResourceStatus.ERROR.getStateInfo());
                tableDao.updataDataTable(table);
            }
        }
        dataSourceResource.setReviseDate(new Date());
        dataSourceResource.setStatus(DataResourceStatus.ERROR.getStateInfo());
        resourceDao.updateDataSource(dataSourceResource);
    }

    /**
     * 更新可用的数据源
     */
    @Override
    public void updateDataSourceResource(DataSourceResource dataSourceResource) {
        if (dataSourceResource.getResourceType().equalsIgnoreCase("DBMS")) {
            Connection conn = null;
            try {
                conn = factory.getHelper(DataSourceConstant.MYSQL).getConnection(dataSourceResource);
                Map<String, String> result = updateDataSourceTable(dataSourceResource, conn);
                dataSourceResource.setSize(Long.parseLong(result.get("size")));
                dataSourceResource.setTablesCount(Integer.parseInt(result.get("count")));
            } finally {
                JDBCUtil.closeOther(conn);
            }
        }

        if (dataSourceResource.getResourceType().equalsIgnoreCase("HDFS")) {
            this.setDsOfHdfsSize(dataSourceResource);
        }

        resourceDao.updateDataSource(dataSourceResource);
    }

    /**
     * 更新所有数据源（包括可用和不可用）
     */
    @Override
    public void updateDataSourceResource(DataSourceResource dataSourceResource, Connection conn) {
        //数据库型
        if (dataSourceResource.getResourceType().equalsIgnoreCase("DBMS")) {
            if (null == conn) {
                dataSourceResource.setStatus(DataResourceStatus.ERROR.getStateInfo());
                this.updateDataSourceResourceWithoutConn(dataSourceResource);
            } else {
                dataSourceResource.setStatus(DataResourceStatus.SUCCESS.getStateInfo());
                Map<String, String> result = updateDataSourceTable(dataSourceResource, conn);
                dataSourceResource.setSize(Long.parseLong(result.get("size")));
                dataSourceResource.setTablesCount(Integer.parseInt(result.get("count")));
            }
        }
        dataSourceResource.setReviseDate(new Date());
        resourceDao.updateDataSource(dataSourceResource);
    }

    /**
     * 更新表
     * 更新数据源时调用
     */
    @Override
    public Map<String, String> updateDataSourceTable(DataSourceResource dataSourceResource, Connection conn) {
        Long totalCount = 0L; //统计数据量大小
        List<TablesInfoDto> queryReslut = factory.getHelper(DataSourceConstant.MYSQL).getTablesByDataResource(dataSourceResource, conn);
        List<DataSourceTable> sqlList = new ArrayList<>();
        for (TablesInfoDto bean : queryReslut) {
            DataSourceTable temp = this.tablesFilter(bean, dataSourceResource);
            sqlList.add(temp);
            totalCount += temp.getSize();
        }
        List<DataSourceTable> dbList = tableDao.getTableListByDsId(dataSourceResource.getResourceId());
        Map<String, List> differenceSet = this.getDifferenceSet(sqlList, dbList);
        //新增的表
        if (null != differenceSet.get("addList") && !differenceSet.get("addList").isEmpty()) {
            for (Object obj : differenceSet.get("addList")) {
                DataSourceTable bean = (DataSourceTable) obj;
                bean.setStatus(DataResourceStatus.SUCCESS.getStateInfo());
                tableDao.addDataTable(bean);
                Map<String, String> res = this.updateDataSourceField(dataSourceResource, bean, conn);
                bean.setCloumnsCount(Integer.parseInt(res.get("count")));
                tableDao.updataDataTable(bean);
            }
        }
        //删除的表
        if (null != differenceSet.get("updateList") && !differenceSet.get("updateList").isEmpty()) {
            for (Object obj : differenceSet.get("updateList")) {
                DataSourceTable bean = (DataSourceTable) obj;
                List<DataSourceField> fields = fieldDao.getDataFieldByTableId(bean.getTableId());
                for (DataSourceField field : fields) {
                    field.setStatus(DataResourceStatus.ERROR.getStateInfo());
                    fieldDao.updateDataField(field);
                }
                bean.setStatus(DataResourceStatus.DELETE.getStateInfo());
                bean.setReviseDate(new Date());
                tableDao.updataDataTable(bean);
            }
        }
        //未变化的表
        if (null != differenceSet.get("oldList") && !differenceSet.get("oldList").isEmpty()) {
            for (Object obj : differenceSet.get("oldList")) {
                DataSourceTable bean = (DataSourceTable) obj;
                Map<String, String> res = this.updateDataSourceField(dataSourceResource, bean, conn);
                bean.setCloumnsCount(Integer.parseInt(res.get("count")));
                bean.setReviseDate(new Date());
                bean.setStatus(DataResourceStatus.SUCCESS.getStateInfo());
                tableDao.updataDataTable(bean);
            }
        }
        HashMap<String, String> result = new HashMap<>();
        result.put("count", String.valueOf(queryReslut.size()));
        result.put("size", totalCount.toString());
        return result;
    }


    /**
     * 更新字段
     * 更新数据源时调用
     */
    @Override
    public Map<String, String> updateDataSourceField(DataSourceResource dataSourceResource, DataSourceTable datasourceTable, Connection conn) {
        HashMap<String, String> result = new HashMap<>();
        List<FieldInfoDto> temp = factory.getHelper(DataSourceConstant.MYSQL).getFieldsByTable(dataSourceResource, datasourceTable, conn);
        List<DataSourceField> sqlList = new ArrayList<>();
        for (FieldInfoDto bean : temp) {
            sqlList.add(this.fieldsFilter(bean, datasourceTable));
        }

        List<DataSourceField> dbList = fieldDao.getDataFieldByTableId(datasourceTable.getTableId());
        Map<String, List> differenceSet = this.getDifferenceSet(sqlList, dbList);
        //新增
        if (null != differenceSet.get("addList") && !differenceSet.get("addList").isEmpty()) {
            for (Object obj : differenceSet.get("addList")) {
                DataSourceField bean = (DataSourceField) obj;
                bean.setStatus(DataResourceStatus.SUCCESS.getStateInfo());
                fieldDao.addDataField(bean);
            }
        }
        //删除
        if (null != differenceSet.get("updateList") && !differenceSet.get("updateList").isEmpty()) {
            for (Object obj : differenceSet.get("updateList")) {
                DataSourceField bean = (DataSourceField) obj;
                bean.setStatus(DataResourceStatus.DELETE.getStateInfo());
                fieldDao.updateDataField(bean);
            }
        }
        //正常
        if (null != differenceSet.get("oldList") && !differenceSet.get("oldList").isEmpty()) {
            for (Object obj : differenceSet.get("oldList")) {
                DataSourceField bean = (DataSourceField) obj;
                bean.setStatus(DataResourceStatus.SUCCESS.getStateInfo());
                fieldDao.updateDataField(bean);
            }
        }

        result.put("count", String.valueOf(temp.size()));
        return result;
    }

    /**
     * 通过sql查询某数据源的信息
     */
    @Override
    public BeanResult getDataFromDataSourceBySql(DataSourceResource dataSourceResource, String sql) {
        // 目前只读MYSQL
        if (!"MYSQL".equalsIgnoreCase(dataSourceResource.getDataType())) {
            return BeanResult.error("请选择MySql类型数据库！");
        }
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = factory.getHelper(DataSourceConstant.MYSQL).getConnection(dataSourceResource);
            stat = conn.prepareStatement(sql);
            List<Map<String, Object>> result = JDBCUtil.createSql(stat);
            return BeanResult.success(result);
        } catch (SQLException e) {
            return BeanResult.error("数据源连接失败或查询语句有误，请检查！");
        } catch (Exception e) {
            return BeanResult.error("数据源连接失败或查询语句有误，请检查！");
        } finally {
            JDBCUtil.closeOther(stat, conn);
        }

    }

    @Override
    public BeanResult getDataFromDataSourceBySql2(DataSourceResource dataSourceResource, String sql) {

        if (StringUtils.isBlank(sql)) {
            return BeanResult.error("sql 语句不可执行 ");
        }
        // 目前只读MYSQL
        if (!"MYSQL".equalsIgnoreCase(dataSourceResource.getDataType())) {
            return BeanResult.error("请选择MySql类型数据库！");
        }

        BeanResult result;
        logger.debug(" sql : {}", sql);

        try (Connection conn = factory.getHelper(DataSourceConstant.MYSQL).getConnection(dataSourceResource)) {
            Map<String, List<?>> map = new HashMap<>();
            Pair<List<String>, List<Map<String, Object>>> pair = JDBCUtil.execStatement(conn, sql);
            if (pair != null) {
                map.put("title", pair.getLeft());
                map.put("rows", pair.getRight());
            }
            result = BeanResult.success(map);
        } catch (Exception e) {
            result = BeanResult.error("数据源连接失败或查询语句有误，请检查！");
        }
        return result;
    }

    /**
     * 根据用户自己写的sql查询数据，定时任务所用
     * 数据和标题分开处理
     */
    @Override
    public Map<String, Object> getDataFromTableBySql(DataSourceResource dataSourceResource, String sql) {
        // 目前只读MYSQL
        if (!"MYSQL".equalsIgnoreCase(dataSourceResource.getDataType())) {
            return null;
        }
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            HashMap<String, Object> result = new HashMap<>();
            List<String[]> datas = new LinkedList<>();
            conn = factory.getHelper(DataSourceConstant.MYSQL).getConnection(dataSourceResource);
            stat = conn.prepareStatement(sql);
            res = stat.executeQuery();
            ResultSetMetaData rsmd = res.getMetaData();
            int columnCount = rsmd.getColumnCount();
            String[] columns = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columns[i] = rsmd.getColumnName(i + 1);
            }
            String[] data;
            while (res.next()) {
                data = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    data[i] = res.getObject(i + 1) == null ? "" : res.getObject(i + 1).toString();
                }
                datas.add(data);
            }
            result.put("title", columns);
            result.put("data", datas);
            return result;
        } catch (Exception e) {
            logger.error("执行出错！请检查{}", e);
            return null;
        } finally {
            JDBCUtil.closeOther(res, stat, conn);
        }
    }

    @Override
    public boolean isUpdateDs(final String step) {
        boolean flag = true;
        if (step.equals("start")) {
            String res = redis.get(UPDATEDS) == null ? "" : redis.get(UPDATEDS).toString();
            switch (res) {
                case "yes":
                    redis.set(UPDATEDS, "no", 60 * 60);
                    break;
                case "no":
                    redis.set(UPDATEDS, "no", 60 * 60);
                    flag = false;
                    break;
                case "":
                    redis.set(UPDATEDS, "no", 60 * 60);
                    flag = false;
                    break;
                default:
                    redis.set(UPDATEDS, "yes", 60 * 60);
            }
        } else if (step.equals("finish")) {
            redis.set(UPDATEDS, "yes", 60 * 60);
        }

        return flag;
    }


    /**
     * 将sql语句中查询出的参数赋值到DataSourceTable对象中
     * 一条记录封装一个对象，避免多次循环
     */
    private DataSourceTable tablesFilter(TablesInfoDto dto, DataSourceResource dataSourceResource) {
        DataSourceTable table = new DataSourceTable();
        try {
            table.setResourceId(dataSourceResource.getResourceId());
            table.setTableName(dto.getTableName());
            table.setCaption(dto.getTableName());
            table.setSize(dto.getTableSize());
            table.setCreator(dataSourceResource.getCreator());
            table.setCreateDate(new Date());
            table.setReviseDate(new Date());
            table.setStatus(DataResourceStatus.NORMAL.getStateInfo());
            table.setIsAutoUpdate(DataSourceConstant.NO);
            return table;
        } catch (Exception e) {
            logger.error("封装表实体失败！{}", e);
            throw new RuntimeException();
        }
    }

    /**
     * 将sql语句中查询出的参数赋值到DataSourceField对象中
     * 一条记录封装一个对象，避免多次循环
     */
    private DataSourceField fieldsFilter(FieldInfoDto dto, DataSourceTable table) {
        DataSourceField dataSourceField = new DataSourceField();
        try {
            dataSourceField.setTableId(table.getTableId());
            dataSourceField.setFieldName(dto.getName());
            dataSourceField.setCaption(dto.getComment());
            dataSourceField.setType(dto.getType());
            dataSourceField.setLength(dto.getLength());
            dataSourceField.setAllowNull(dto.getIsNull());
            dataSourceField.setDecimalCount(dto.getPrecision());
            dataSourceField.setStatus(DataResourceStatus.NORMAL.getStateInfo());
            return dataSourceField;
        } catch (Exception e) {
            logger.error("封装字段实体失败！{}", e);
            throw new RuntimeException();
        }
    }

    /**
     * 获取需要更新和新增的集合；
     * 参数一：通过数据源连接查出的新集合
     * 参数二：数据库读取的旧集合
     */
    private Map<String, List> getDifferenceSet(List sql, List db) {
        HashMap<String, List> result = new HashMap<>();
        List addList = new ArrayList<>();
        List oldList = new ArrayList<>();
        for (Object bean : sql) {
            if (db.contains(bean)) {
                oldList.add(db.get(db.indexOf(bean)));
                db.remove(bean);
            } else {
                addList.add(bean);
            }
        }
        //新增：
        result.put("addList", addList);
        //删除：
        result.put("updateList", db);
        //无变化：
        result.put("oldList", oldList);
        return result;
    }

    /**
     * 设置Hdfs类型的数据源大小
     */
    private void setDsOfHdfsSize(DataSourceResource dataSourceResource) {
        Long size = 0L;
        try {
            size = HDFSUtil.getFolderSize(dataSourceResource.getHdfsPath(), "/");
        } catch (Exception e) {
            logger.error("获取HDFS数据量大小失败！{}", e);
        }
        dataSourceResource.setSize(size);
    }
}
