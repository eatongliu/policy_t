package com.gpdata.wanyou.dq.utils;

import com.gpdata.wanyou.ds.entity.DataSourceField;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.ds.entity.DataSourceTable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RunnableFuture;
import java.util.stream.Collectors;

/**
 * Created by chengchao on 2016/11/17.
 */
public class ForeignDs2InnerDsCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForeignDs2InnerDsCreator.class);

    private static final String NEW_LINE = "\n";

    public static boolean createInnerDbFromDataSource(DataSourceResource foreign
                                                   , List<DataSourceTable> dataSourceTableList
                                                   , Map<Integer, List<DataSourceField>> dataSourceFieldMap
                                                   , DataSourceResource inner) {

        if ("MySqL".equalsIgnoreCase(foreign.getDataType())) {
            String driver = "com.mysql.jdbc.Driver";
            try {
                Class.forName(driver);
                mySql2MySql(foreign, dataSourceTableList, dataSourceFieldMap, inner);
                return true;
            } catch (ClassNotFoundException e) {
                LOGGER.error("Exception : ", e);
            }

        } else {
            throw new UnsupportedOperationException("非 MySQL 数据方法尚未实现!");
        }

        return false;
    }

    private static void mySql2MySql(DataSourceResource foreig
                                    , List<DataSourceTable> dataSourceTableList
                                    , Map<Integer, List<DataSourceField>> dataSourceFieldMap
                                    , DataSourceResource inner) {

        LOGGER.debug("执行 MySQL 数据库结构复制 ...");
        List<String> tableNames = dataSourceTableList
                .stream()
                .map(DataSourceTable::getTableName)
                .collect(Collectors.toList());

        String dbName = foreig.getDbName();
        String hostname = foreig.getHost();
        Integer port = foreig.getPort();
        String encode = foreig.getEncode();
        String username = foreig.getUserName();
        String password = foreig.getPassWord();

        String url = createMySQLUrl(hostname, port, dbName, encode);

        List<String> createTableSqls = null;

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            //createTableSqls = MySQLUtil.createExportTableSqls(tableNames, connection);
            createTableSqls = getCreateTablesSQL(dataSourceTableList, dataSourceFieldMap);
        } catch (SQLException sqlex) {
            LOGGER.error("SQLException ", sqlex);
            throw new RuntimeException(sqlex);
        }

        if (createTableSqls == null || createTableSqls.isEmpty()) {
            LOGGER.warn("建表语句未能获得, 方法返回 ...");
            return;
        }

        boolean createDatabaseStatus = execCreateDatabaseSQL(inner);
        if (!createDatabaseStatus) {
            LOGGER.warn("建库失败, 方法返回 ...");
            return;
        }

        execCreateTableSQL(inner, createTableSqls);

    }

    private static List<String> getCreateTablesSQL(List<DataSourceTable> dataSourceTableList
                                                    , Map<Integer, List<DataSourceField>> dataSourceFieldMap) {

        List<String> result = new ArrayList<>();

        dataSourceTableList.stream()
                .forEach(dataSourceTable -> {
                    List<DataSourceField> dataSourceFieldList = dataSourceFieldMap.get(dataSourceTable.getTableId());
                    StringBuilder buff = new StringBuilder(dataSourceFieldList.size() * 30);
                    buff.append("CREATE TABLE `")
                            .append(dataSourceTable.getTableName())
                            .append("` (")
                            .append(NEW_LINE);

                    dataSourceFieldList.stream().forEach(dataSourceField -> {
                        String colName = dataSourceField.getFieldName();
                        String colTypeName = dataSourceField.getType();
                        buff.append("    `")
                                .append(colName)
                                .append("` ")
                                .append(colTypeName);
                        if (dataSourceField.getAllowNull() == 1) {
                            buff.append(" NULL");
                        } else {
                            buff.append(" NOT NULL");
                        }
                        buff.append(",").append(NEW_LINE);

                    });
                    buff.deleteCharAt(buff.length() - 2);
                    buff.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
                    result.add(buff.toString());
                });
        return result;
    }

    private static boolean execCreateDatabaseSQL(DataSourceResource inner) {

        String dbName = inner.getDbName();
        String hostname = inner.getHost();
        Integer port = inner.getPort();
        String encode = inner.getEncode();
        String username = inner.getUserName();
        String password = inner.getPassWord();


        String url = createMySQLUrl(hostname, port, "mysql", encode);


        String sql  = "CREATE DATABASE  `"+ dbName + "` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;";

        LOGGER.debug("执行 MySQL 数据库创建 ...");
        LOGGER.debug("sql : {}", sql);

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement() ) {

            statement.execute(sql);
            return true;
        } catch (SQLException sqlex) {
            LOGGER.error("SQLException ", sqlex);
            throw new RuntimeException(sqlex);
        }


    }

    private static void execCreateTableSQL(DataSourceResource inner, List<String> createTableSqls) {

        String dbName = inner.getDbName();
        String hostname = inner.getHost();
        Integer port = inner.getPort();
        String encode = inner.getEncode();
        String username = inner.getUserName();
        String password = inner.getPassWord();

        String url = createMySQLUrl(hostname, port, dbName, encode);


        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            for (String sql : createTableSqls) {
                try (Statement statement = connection.createStatement()) {
                    LOGGER.debug("执行 MySQL 数据表创建 ...");
                    LOGGER.debug("sql : {}", sql);
                    statement.execute(sql);
                } catch (SQLException sqlex) {
                    LOGGER.error("SQLException ", sqlex);
                    throw new RuntimeException(sqlex);
                }
            }

        } catch (SQLException sqlex) {
            LOGGER.error("SQLException ", sqlex);
            throw new RuntimeException(sqlex);
        }

    }

    private static String createMySQLUrl(String hostname, int port, String dbName, String encode) {
        return new StringBuilder()
                .append("jdbc:mysql://")
                .append(hostname)
                .append(":")
                .append(port)
                .append("/")
                .append(dbName)
                .append("?useUnicode=true&characterEncoding=")
                .append(StringUtils.isBlank(encode) ? "UTF-8" : encode)
                .toString();
    }
}
