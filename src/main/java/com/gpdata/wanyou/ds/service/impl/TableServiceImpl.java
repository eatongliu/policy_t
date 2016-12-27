package com.gpdata.wanyou.ds.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.ds.dao.ResourceDao;
import com.gpdata.wanyou.ds.dao.TableDao;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.ds.entity.DataSourceTable;
import com.gpdata.wanyou.ds.service.TableService;
import com.gpdata.wanyou.ds.util.JDBCUtil;
import com.gpdata.wanyou.utils.ConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 数据表服务层接口的实现
 *
 * @author qyl
 */
@SuppressWarnings("rawtypes")
@Service
public class TableServiceImpl extends BaseService implements TableService {

    @Autowired
    private TableDao tableDao;

    @Autowired
    private ResourceDao resourceDao;


    /**
     * 获取数据表表名列表
     *
     * @param resourceId
     * @param caption
     * @return
     */
    @Override
    public List getDtIdAndNameList(Integer resourceId, String caption) {
        return tableDao.getDtIdAndNames(resourceId, caption);
    }


    /**
     * 通过ID查询数据表
     *
     * @param id
     * @return
     */
    @Override
    public DataSourceTable getDataTableById(Integer id) {
        return tableDao.getDataTableById(id);
    }

    /**
     * 关键字查询数据表
     *
     * @param resourceid
     * @param caption
     * @param limit
     * @param offset
     * @return
     */
    @Override
    public Map<String, Object> getDtSearchList(Integer resourceid, String caption, Integer limit, Integer offset) {
        Map<String, Object> map = tableDao.getDataTableByKeyword(resourceid, caption, limit, offset);
        return map;
    }


    @Override
    public List<String> getDtNameList(Integer resourceId) {
        return tableDao.getDtNameList(resourceId);
    }


    @Override
    public void updateTable(DataSourceTable dataTable) {
        tableDao.updataDataTable(dataTable);
    }


    /**
     * mysql自定义sink调用方法
     *
     * @Author yaz
     * @Date 2016/11/17 16:14
     */
    @Override
    public boolean insertDataToTable(DataSourceResource resource, DataSourceTable table, List<String[]> datas) {
        //根据配置文件连接内部数据源
        String url = ConfigUtil.getConfig("project.jdbc.url");
        String driver = ConfigUtil.getConfig("project.jdbc.driverClassName");
        String username = ConfigUtil.getConfig("project.jdbc.username");
        String password = ConfigUtil.getConfig("project.jdbc.password");
        String timeOut = ConfigUtil.getConfig("dataSourceResource.connectTimeout");
        String encode = resource.getEncode();

        url = url.substring(0, url.lastIndexOf("/"));
        url = url + "/" + resource.getDbName() + "_" + resource.getResourceId()
                + "?useUnicode=true&characterEncoding=" + encode + "&connectTimeout=" + timeOut;
        logger.debug("sink使用链接地址-->" + url);

        logger.debug("用户名{}", username);
        logger.debug("密码{}", password);
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            conn.setAutoCommit(false);
            String[] title = datas.get(0);
            int length = title.length;
            StringBuilder sb = new StringBuilder("insert into " + table.getTableName() + " (");
            StringBuilder values = new StringBuilder();

            for (int i = 0; i < length; i++) {
                sb.append(title[i].trim()).append(",");
                values.append("? ,");
            }
            sb.deleteCharAt(sb.length() - 1);
            values.deleteCharAt(values.length() - 1);
            sb.append(") values (").append(values).append(" )");
            String sql = sb.toString();

            logger.debug("sql语句为-->{} ", sql);

            pstmt = conn.prepareStatement(sql);
            datas.remove(0);
            for (String[] bean : datas) {
                //拼接sql
                for (int i = 1; i <= length; i++) {
                    pstmt.setObject(i, bean[i - 1].trim());
                }
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            return true;
        } catch (ClassNotFoundException e) {
            logger.error("获取连接失败！{}", e);
            return false;
        } catch (SQLException e) {
            logger.error("获取连接失败！{}", e);
            return false;
        } catch (Exception e) {
            logger.error("执行失败！{}", e);
            return false;
        } finally {
            JDBCUtil.closeOther(pstmt, conn);
        }
    }

    @Override
    public List<DataSourceTable> getDataSourceTableList(Integer resourceId) {
        return this.tableDao.getDataSourceTableList(resourceId);
    }
}
