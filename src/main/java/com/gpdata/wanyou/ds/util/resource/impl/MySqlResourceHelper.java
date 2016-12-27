package com.gpdata.wanyou.ds.util.resource.impl;

import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.ds.entity.DataSourceTable;
import com.gpdata.wanyou.ds.util.JDBCUtil;
import com.gpdata.wanyou.ds.util.resource.DataResourceHelper;
import com.gpdata.wanyou.ds.util.resource.dto.FieldInfoDto;
import com.gpdata.wanyou.ds.util.resource.dto.TablesInfoDto;
import com.gpdata.wanyou.utils.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * mysql数据库专用工具类
 *
 * @author yaz
 * @create 2016-11-08 14:37
 */
@Component
public class MySqlResourceHelper implements DataResourceHelper {
    private static final Logger logger = LoggerFactory.getLogger(MySqlResourceHelper.class);

    @Override
    public List<TablesInfoDto> getTablesByDataResource(DataSourceResource dataSourceResource, Connection conn) {
        PreparedStatement stat = null;
        ResultSet res = null;
        String sql = "select TABLE_NAME, DATA_LENGTH from information_schema.tables where table_schema = ? ";
        ArrayList<TablesInfoDto> result = new ArrayList<>();
        try {
            stat = conn.prepareCall(sql);
            stat.setString(1, dataSourceResource.getDbName());
            res = stat.executeQuery();
            while (res.next()) {
                String name = res.getObject("TABLE_NAME") == null ? "" : res.getObject("TABLE_NAME").toString();
                Long size = 0L;
                Object sizeTemp = res.getObject("DATA_LENGTH");
                if (null != sizeTemp && !"".equals(sizeTemp.toString())) {
                    size = Long.parseLong(sizeTemp.toString());
                }
                TablesInfoDto dto = new TablesInfoDto(name, size);
                result.add(dto);
            }
            return result;
        } catch (SQLException e) {
            logger.error("sql语句预编译异常！{}", e);
            throw new RuntimeException();
        } catch (Exception e) {
            logger.error("封装数据格式失败！{}", e);
            throw new RuntimeException();
        } finally {
            JDBCUtil.closeOther(res, stat);
        }
    }

    @Override
    public List<FieldInfoDto> getFieldsByTable(DataSourceResource dataSourceResource, DataSourceTable dataSourceTable, Connection conn) {
        PreparedStatement stat = null;
        ResultSet res = null;
        ArrayList<FieldInfoDto> result = new ArrayList<>();
        String sql = "select COLUMN_NAME ,COLUMN_COMMENT, COLUMN_TYPE, IS_NULLABLE ," +
                "CHARACTER_MAXIMUM_LENGTH , NUMERIC_PRECISION from information_schema.`COLUMNS` c " +
                "where c.table_schema = ?  and c.table_name = ? ";
        try {
            stat = conn.prepareCall(sql);
            stat.setString(1, dataSourceResource.getDbName());
            stat.setString(2, dataSourceTable.getTableName());
            res = stat.executeQuery();
            while (res.next()) {
                FieldInfoDto dto = new FieldInfoDto();
                dto.setName(res.getObject("COLUMN_NAME") == null ? "" : res.getObject("COLUMN_NAME").toString());
                dto.setComment(res.getObject("COLUMN_COMMENT") == null ? "" : res.getObject("COLUMN_COMMENT").toString());
                dto.setType(res.getObject("COLUMN_TYPE") == null ? "" : res.getObject("COLUMN_TYPE").toString());

                String length = res.getObject("CHARACTER_MAXIMUM_LENGTH") == null ? "" : res.getObject("CHARACTER_MAXIMUM_LENGTH").toString();
                if (!"".equals(length)) {
                    dto.setLength(Long.parseLong(length));
                }

                //结果为NO和YES，对应数据库中：0：不允许为空；1：允许为空
                if (res.getObject("IS_NULLABLE") != null) {
                    dto.setIsNull(res.getObject("IS_NULLABLE").toString().equalsIgnoreCase("NO") ? 0 : 1);
                }

                //精度，如果为varchar等，则此项为空
                String precision = res.getObject("NUMERIC_PRECISION") == null ? "" : res.getObject("NUMERIC_PRECISION").toString();
                if (!"".equals(precision)) {
                    dto.setPrecision(Integer.parseInt(precision));
                }

                result.add(dto);
            }
            return result;
        } catch (SQLException e) {
            logger.error("sql语句预编译异常！{}", e);
            throw new RuntimeException();
        } catch (Exception e) {
            logger.error("封装数据格式失败！{}", e);
            throw new RuntimeException();
        } finally {
            JDBCUtil.closeOther(res, stat);
        }
    }

    @Override
    public Connection getConnection(DataSourceResource dataSourceResource) {
        // 获取信息
        String url;
        String driver = "com.mysql.jdbc.Driver";
        String username = dataSourceResource.getUserName();
        String password = dataSourceResource.getPassWord();
        //获取超时时间配置
        String timeOut = ConfigUtil.getConfig("dataSourceResource.connectTimeout");
        String connString = dataSourceResource.getConnString();

        if (null != connString && !"".equals(connString)) {
            url = connString;
        } else {
            String hostname = dataSourceResource.getHost();
            Integer port = dataSourceResource.getPort();
            String dbName = dataSourceResource.getDbName();
            String encode = dataSourceResource.getEncode();
            url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=" + encode + "&connectTimeout=" + timeOut;
        }
        logger.debug("测试url地址是否可连-->" + url);
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            return conn;
        } catch (ClassNotFoundException e) {
            logger.error("获取连接失败！{}", e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            logger.error("获取连接失败！{}", e);
            throw new RuntimeException(e);
        }
    }
}
