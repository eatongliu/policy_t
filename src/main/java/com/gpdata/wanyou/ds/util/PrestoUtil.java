package com.gpdata.wanyou.ds.util;

import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.utils.ConfigUtil;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 采用presto进行本地文件及hdfs数据的管理
 */
@Component
public class PrestoUtil {

    /**
     * 获取连接
     */
    public static Connection getConnection(String dbname, String username, String password) {
        Connection connection = null;

        String url = ConfigUtil.getConfig("PRESTO.JDBC");
        try {
            Class.forName("com.facebook.presto.jdbc.PrestoDriver");
            connection = DriverManager.getConnection(url, username, password);
            connection.setCatalog(dbname);  
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 将数据保存到数据库中
     */
    @SuppressWarnings("rawtypes")
    public static boolean insertDB(Connection connection, String tableName, List fieldnames, List fieldvalues) {
        Statement statement = null;
        //拼接插入sql
        StringBuilder sql = new StringBuilder("insert into ");
        sql.append(tableName).append("(");
        int length = fieldnames.size();
        for (int i = 0; i < length; i++) {
            if (i != length - 1) {
                sql.append(fieldnames.get(i)).append(",");
            } else {
                sql.append(fieldnames.get(i)).append(")");
            }
        }
        sql.append("values(");
        for (int i = 0; i < length; i++) {
            if (i != length - 1) {
                sql.append("'").append(fieldvalues.get(i)).append("',");
            } else {
                sql.append("'").append(fieldvalues.get(i)).append("')");
            }
        }

        try {
            statement = connection.createStatement();
            statement.execute(sql.toString());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeOther(statement, connection);
        }
        return false;
    }

    /**
     * 清空数据表内容
     */
    public static boolean clear(Connection connection, String tableName) {
        String sql = "delete from " + tableName;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeOther(statement, connection);
        }
        return false;
    }

    /**
     * 查询文本内容
     */
    public static String queryText(Connection connection, String sql, int limit) {
        sql = sql + " limit " + limit;//对sql进行分页处理
        Statement statement = null;
        ResultSet rs = null;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<Map<String, Object>> resultList = new ArrayList<>();
            int count = 0;
            while (rs.next()) {
                count++;
                String columnName = "";
                String value = "";
                Map<String, Object> subMap = new HashMap<>();
                for (int i = 0; i < columnCount; i++) {
                    columnName = metaData.getColumnName(i + 1);
                    value = rs.getString(metaData.getColumnLabel(i + 1));
                    subMap.put(columnName, value);
                }
                resultList.add(subMap);
            }
            resultMap.put("total", count);
            resultMap.put("row", resultList);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeOther(rs, statement, connection);
        }
        return JSONObject.toJSONString(resultMap);
    }

    /**
     * 关闭AutoCloseable
     */
    private static void closeOther(AutoCloseable... other) {

        Stream.of(other).forEach(x -> {
            if (x != null) {
                try {
                    x.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}