package com.gpdata.wanyou.dq.utils;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chengchao on 2016/11/17.
 */
public class MySQLUtil {


    public static final String NEW_LINE = "\n"; // "\r\n";


    private static String getDateString(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    public static List<String> createExportTableSqls(List<String> tableNames, Connection connection) {

        List<String> result = tableNames.stream()
                .map(tableName -> createExportTableSql(tableName, connection))
                .collect(Collectors.toList());
        return result;
    }

    public static String createExportTableSql(String tableName, Connection connection) {

        String sql = "select * from " + tableName + " limit 0, 0";
        String createTable;
        try (Statement stat = connection.createStatement();
             ResultSet rs = stat.executeQuery(sql)) {
            //得到创建表的sql语句
            createTable = getCreateTableSql(rs, tableName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return createTable + NEW_LINE;
    }


    /**
     *
     * @param rs
     * @param tableName
     * @return
     * @throws SQLException
     */
    public static String getCreateTableSql(ResultSet rs, String tableName)
            throws SQLException {

        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        int count = resultSetMetaData.getColumnCount();

        StringBuilder buff = new StringBuilder();

//        buff.append("DROP TABLE IF EXISTS `")
//                .append(tableName)
//                .append("`;")
//                .append(NEW_LINE);

        buff.append("CREATE TABLE `")
                .append(tableName)
                .append("` (")
                .append(NEW_LINE);

        for (int i = 1; i <= count; i++) {
            int size = resultSetMetaData.getColumnDisplaySize(i);
            //String colClassName = resultSetMetaData.getColumnClassName(i);
            String colTypeName = resultSetMetaData.getColumnTypeName(i);
            String colName = resultSetMetaData.getColumnName(i);
            buff.append("`")
                    .append(colName)
                    .append("` ")
                    .append(colTypeName);

            //在这儿我只做了一些简单的判断
            if (!"double".equalsIgnoreCase(colTypeName)
                    && !"date".equalsIgnoreCase(colTypeName)
                    && !"text".equalsIgnoreCase(colTypeName)) {
                buff.append("(")
                        .append(size)
                        .append(") ");
            }
            buff.append(" DEFAULT NULL," + NEW_LINE);
        }
        buff.deleteCharAt(buff.length() - 2);

        buff.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");

        return buff.toString();
    }

    /**
     * 得到字段的名字，存放到一个数组里
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private static String[] getColumns(ResultSet rs) throws SQLException {
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        int count = resultSetMetaData.getColumnCount();
        String[] result = new String[count];
        for (int i = 1; i <= count; i++) {
            String colName = resultSetMetaData.getColumnName(i);
            result[i - 1] = colName;
        }
        return result;
    }

    /**
     * 拼接所有字段名
     * @param args
     * @return
     */
    private static String getColumnsString(String[] args) {
        StringBuilder  buffer = new StringBuilder(args.length * 30);
        for (int i = 0; i < args.length; i++) {
            buffer.append("`").append(args[i]).append("`,");
        }
        return buffer.deleteCharAt(buffer.length() - 1).toString();
    }


    /*
    public static String getTableDataSql(ResultSet rs, String tableName)
            throws SQLException {
        String[] columns = getColumns(rs);
        StringBuffer columnBuffer = new StringBuffer();
        columnBuffer.append("INSERT INTO `" + tableName + "` ("
                + getColumnsString(columns) + ") VALUES");
        while (rs.next()) {
            columnBuffer.append("(");
            for (int i = 0; i < columns.length; i++) {
                Object obj = rs.getObject(columns[i]);
                String typeName = "";
                if (obj == null) {
                    obj = "";
                }
                if (obj.getClass() != null) {
                    typeName = obj.getClass().getName();
                }//在这儿我只做了一些简单的判断
                if ("java.lang.String".equals(typeName)
                        || "java.sql.Date".equals(typeName)) {
                    columnBuffer.append("'" + obj + "',");
                } else {
                    columnBuffer.append(obj + ",");
                }
            }
            columnBuffer.deleteCharAt(columnBuffer.length() - 1);
            columnBuffer.append("),");
        }
        if (columnBuffer.toString().endsWith("VALUES"))
            return "";
        columnBuffer.deleteCharAt(columnBuffer.length() - 1).append(";");
        return columnBuffer.toString();
    }
    */

}
