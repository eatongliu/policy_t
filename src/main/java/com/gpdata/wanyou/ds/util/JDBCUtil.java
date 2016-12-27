package com.gpdata.wanyou.ds.util;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * jdbc相关工具方法
 * 抽自原helper类
 *
 * @author yaz
 * @create 2016-11-09 9:18
 */
public class JDBCUtil {
    private static final Logger logger = LoggerFactory.getLogger(JDBCUtil.class);

    /**
     * 工具类统一执行方法
     */
    public static List<Map<String, Object>> createSql(PreparedStatement stat) {
        ResultSet res = null;
        try {
            res = stat.executeQuery();

            List<Map<String, Object>> result = new ArrayList<>();
            while (res.next()) {
                ResultSetMetaData rsmd = res.getMetaData();
                int columnCount = rsmd.getColumnCount();
                Map<String, Object> map = new HashedMap();
                for (int i = 0; i < columnCount; i++) {
                    String name = rsmd.getColumnName(i + 1);
                    Object value = res.getObject(i + 1);
                    map.put(name, value);
                }
                result.add(map);
            }
            return result;
        } catch (SQLException e) {
            logger.error("执行sql结果异常！{}", e);
            return null;
        } finally {
            closeOther(res);
        }
    }

    public static Pair<List<String>, List<Map<String, Object>>> execStatement(Connection conn, String sql) {
        Statement stat = null;
        ResultSet res = null;
        try {
            stat = conn.createStatement();
            res = stat.executeQuery(sql);

            List<String> titleList = new ArrayList<>();
            List<Map<String, Object>> dataList = new ArrayList<>();


            int first = 0;
            int columnCount = 0;
            ResultSetMetaData rsmd = null;
            while (res.next()) {
                if (first == 0) {
                    rsmd = res.getMetaData();
                    columnCount = rsmd.getColumnCount();
                    for (int i = 0; i < columnCount; i++) {
                        String name = rsmd.getColumnName(i + 1);
                        titleList.add(name);
                    }
                    first += 1;
                }

                Map<String, Object> rowMap = new HashMap<>();
                for (int i = 0; i < columnCount; i++) {
                    String name = rsmd.getColumnName(i + 1);
                    Object value = res.getObject(i + 1);
                    rowMap.put(name, value);
                }

                dataList.add(rowMap);
            }

            return Pair.of(titleList, dataList);
        } catch (SQLException e) {
            logger.error("执行sql结果异常！{}", e);
            return null;
        } finally {
            closeOther( res, stat);
        }
    }

    /**
     * 关闭连接
     */
    public static void closeOther(AutoCloseable... other) {
        Stream.of(other).forEach(x -> {
            if (x != null) {
                try {
                    x.close();
                } catch (Exception e) {
                    logger.error("JDBC的连接未关闭！{}", e);
                }
            }
        });
    }
}
