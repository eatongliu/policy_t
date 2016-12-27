package com.gpdata.wanyou.ds.util;

import com.gpdata.wanyou.ds.entity.DataSourceField;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.ds.entity.DataSourceTable;
import com.gpdata.wanyou.ds.util.resource.impl.MySqlResourceHelper;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import java.sql.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * 数据库帮助类
 * 
 * @author qyl
 *
 */
@Component
public class DataBaseMetaDataHelper {

    @Autowired
    private DataSource dataSource;
   

    /**
     * SQL查询数据源
     *
     * @param dataSourceResource
     * @param sql
     * @param offset
     * @param limit
     * @return
     */
    @SuppressWarnings({ "unchecked"})
    public Pair<Integer, List<Map<String, Object>>> sqlSearchDataSources(DataSourceResource dataSourceResource
            , String sql
            , Integer offset, Integer limit) {
        String username = dataSourceResource.getUserName();
        String password = dataSourceResource.getPassWord();
        String hostname = dataSourceResource.getHost();
        Integer port = dataSourceResource.getPort();
        String dbName = dataSourceResource.getDbName();
        String encode = dataSourceResource.getEncode();
        String dataType = dataSourceResource.getDataType(); // MYSQL, ...
        String url =dataSourceResource.getConnString();
        List<Map<String, Object>> result = new ArrayList<>();
        Integer count = 0;
        Integer start, stop;
        start = offset;
        stop = start + limit;
        Connection conn = null;
        PreparedStatement countStat = null;
        ResultSet countRes = null;
        PreparedStatement listStat = null;
        ResultSet listRes = null;
        if ("MYSQL".equalsIgnoreCase(dataType)) {
            String driver = "com.mysql.jdbc.Driver";
            if(url==null)
            	url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=" + encode;
            try {
                Class.forName(driver);
                String countSql = "select count(*) from ( "+sql+" ) as _Oo_oO_";
                System.out.println(countSql);
                String listSql = sql + " limit ? ,?";
                try {
                    conn = DriverManager.getConnection(url, username, password);

                    countStat = conn.prepareStatement(countSql);
                    countRes = countStat.executeQuery();
                    if (countRes.next()) {
                        Object o = countRes.getObject(1);
                        if (o != null) {
                            count = Integer.valueOf(o.toString(), 10);
                        }
                    }

                    listStat = conn.prepareStatement(listSql);
                    listStat.setInt(1, start);
                    listStat.setInt(2, stop);
                    listRes = listStat.executeQuery(listSql);
                    while (listRes.next()) {
                        ResultSetMetaData rsmd = listRes.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        Map<String, Object> map = new HashedMap();
                        for (int i = 0; i < columnCount; i++) {
                            String name = rsmd.getColumnName(i + 1);
                            Object value = listRes.getObject(i + 1);
                            map.put(name, value);
                        }
                        result.add(map);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    this.closeOther(countRes, countStat,listStat,listRes, conn);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return Pair.of(count, result);
    }

    /**
     * 检索数据表中数据字段
     *
     * @param dataSourceResource
     * @param dataSourceTable
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> retrieveFieldByTable(DataSourceResource dataSourceResource
            , DataSourceTable dataSourceTable) {
        String username = dataSourceResource.getUserName();
        String password = dataSourceResource.getPassWord();
        String hostname = dataSourceResource.getHost();
        Integer port = dataSourceResource.getPort();
        String dbName = dataSourceResource.getDbName();
        String encode = dataSourceResource.getEncode();
        String dataType = dataSourceResource.getDataType(); // MYSQL, ...
        String url =dataSourceResource.getConnString();
        List<Map<String, Object>> result = new ArrayList<>();
        
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        if ("MYSQL".equalsIgnoreCase(dataType)) {
            String driver = "com.mysql.jdbc.Driver";
            if(url==null)
            	url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=" + encode;
            try {
                Class.forName(driver);

                String sql = "select * from information_schema.'COLUMNS' c \n" +
                        "where c.table_schema = "+ dataSourceResource.getDbName()+" \n" +
                        "and c.table_name = "+dataSourceTable.getTableName();
                try {
                    conn = DriverManager.getConnection(url, username, password);
                    stat = conn.prepareCall(sql);
                    res = stat.executeQuery();
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
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    this.closeOther(res, stat, conn);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 结构检索数据表数量
     *
     * @param dataSourceResource
     * @return
     */
    public Integer retrieveTablesCountBySchema(DataSourceResource dataSourceResource) {

        String username = dataSourceResource.getUserName();
        String password = dataSourceResource.getPassWord();
        String hostname = dataSourceResource.getHost();
        Integer port = dataSourceResource.getPort();
        String dbName = dataSourceResource.getDbName();
        String encode = dataSourceResource.getEncode();
        String dataType = dataSourceResource.getDataType(); // MYSQL, ...
        String url =dataSourceResource.getConnString();
        Integer result = 0;
        
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        if ("MYSQL".equalsIgnoreCase(dataType)) {

            String driver = "com.mysql.jdbc.Driver";
            if(url==null)
            	url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=" + encode;

            try {
                Class.forName(driver);
                String sql = "select count(*) from information_schema.tables where table_schema = "+dataSourceResource.getDbName();
                try {
                    conn = DriverManager.getConnection(url, username, password);
                    stat = conn.prepareCall(sql);
                    res = stat.executeQuery();
                    if (res.next()) {
                        result = res.getInt(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    this.closeOther(res, stat, conn);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 结构检索数据表
     *
     * @param dataSourceResource
     * @param offset
     * @param limit
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> retrieveTablesBySchema(DataSourceResource dataSourceResource
            , Integer offset, Integer limit) {
        String username = dataSourceResource.getUserName();
        String password = dataSourceResource.getPassWord();
        String hostname = dataSourceResource.getHost();
        Integer port = dataSourceResource.getPort();
        String dbName = dataSourceResource.getDbName();
        String encode = dataSourceResource.getEncode();
        String dataType = dataSourceResource.getDataType(); // MYSQL, ...
        String url =dataSourceResource.getConnString();

        Integer start, stop;
        start = offset;
        stop = start + limit;
        
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        List<Map<String, Object>> result = new ArrayList<>();
        if ("MYSQL".equalsIgnoreCase(dataType)) {
            String driver = "com.mysql.jdbc.Driver";
            if(url==null)
            	url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=" + encode;

            try {
                Class.forName(driver);
                String sql = "select * from information_schema.tables where table_schema = "+dataSourceResource.getDbName()+" limit ? , ? ";
                try {
                    conn = DriverManager.getConnection(url, username, password);
                    stat = conn.prepareCall(sql);
                    stat.setInt(1, start);
                    stat.setInt(2, stop);
                    res = stat.executeQuery();
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
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    this.closeOther(res, stat, conn);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 新建数据源数据库
     *
     * @param daName
     * @param type
     * @param host
     * @param port
     * @param username
     * @param password
     * @param encode
     * @param connstring
     * @return
     */
    @SuppressWarnings("unused")
    public boolean createDataSourceDB(String daName, String type, String host, Integer port, String username,
                                      String password, String encode, String connstring) {
    	 Connection conn = null;
         PreparedStatement stat = null;
         ResultSet res = null;
    	 try {
            String url = connstring;
            if (type.equalsIgnoreCase("MYSQL")) {
                Class.forName("com.mysql.jdbc.Driver");

                //打开连接
                conn = this.dataSource.getConnection();

                //创建的数据库
                String sql = "create database "+daName;
                stat = conn.prepareStatement(sql);
                stat.executeQuery();
                return true;
            } else if (type.equalsIgnoreCase("Oracle")) {

            } else if (type.equalsIgnoreCase("SQLsever")) {

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeOther(stat, conn);
        }
        return false;
    }

    /**
     * 新建一个空表
     *
     * @param dbName
     * @param type
     * @param host
     * @param port
     * @param username
     * @param password
     * @param encode
     * @param connstring
     * @param name
     * @return
     */
    public boolean addDataTable(String dbName, String type, String host, String port, String username,
                                String password, String encode, String connstring, String name) {
    	 Connection conn = null;
         PreparedStatement stat = null;
    	 try {
            String url = connstring;
            if (type.equalsIgnoreCase("MYSQL")) {
                Class.forName("com.mysql.jdbc.Driver");
                if(url==null)
                	url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=" + encode;
                conn = DriverManager.getConnection(url, username, password);
                //创建表
                String sql = "create table "+name+"(id bigint)";
                stat = conn.prepareStatement(sql);
                stat.executeQuery();

                return true;
            } else if (type.equalsIgnoreCase("Oracle")) {

            } else if (type.equalsIgnoreCase("SQLsever")) {

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeOther(stat, conn);
        }
        return false;
    }

    /**
     * 新建一个字段
     *
     * @param dbName
     * @param type
     * @param host
     * @param port
     * @param username
     * @param password
     * @param encode
     * @param connstring
     * @param tableName
     * @param fieldName
     * @param length
     * @param dataType
     * @param allowNull
     * @param decimalCount
     * @return
     */
    public boolean addDataField(String dbName, String type, String host, String port, String username,
                                String password, String encode, String connstring, String tableName, String fieldName,
                                Integer length, String dataType, Integer allowNull, Integer decimalCount) {
    	 Connection conn = null;
         PreparedStatement stat = null;
    	 try {
            String url = connstring;
            if (type.equalsIgnoreCase("MYSQL")) {
                Class.forName("com.mysql.jdbc.Driver");
                if(url==null)
                	url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=" + encode;
                conn = DriverManager.getConnection(url, username, password);
                //创建字段
                String isAllowNull = "";
                if (allowNull == 0) {
                    isAllowNull = " NOT NULL";
                }
                if (length == -1) {
                    length = 255;
                }
                if (decimalCount == -1) {
                    decimalCount = 255;
                }
                String sql = "";
                //根据字段类型确定SQL语句
                if (dataType.equalsIgnoreCase("DATE") || dataType.equalsIgnoreCase("TIME") || dataType.equalsIgnoreCase("YEAR") ||
                        dataType.equalsIgnoreCase("DATETIME") || dataType.equalsIgnoreCase("TIMESTAMP") ||
                        dataType.equalsIgnoreCase("ENUM") || dataType.equalsIgnoreCase("SET")) {
                    sql = " alter table "+tableName+" add "+fieldName+"  "+dataType+" " + isAllowNull;
                } else if (dataType.equalsIgnoreCase("DECIMAL") || dataType.equalsIgnoreCase("FLOAT") || dataType.equalsIgnoreCase("DOUBLE")) {
                    sql = " alter table "+tableName+" add "+fieldName+"  "+dataType+ "(" + length + "," + decimalCount + ") " + isAllowNull;
                } else {
                    sql = " alter table "+tableName+" add "+fieldName+"  "+dataType+ "(" + length + ") " + isAllowNull;
                }
                stat = conn.prepareStatement(sql);
                stat.executeQuery();
                return true;
            } else if (type.equalsIgnoreCase("Oracle")) {

            } else if (type.equalsIgnoreCase("SQLsever")) {

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeOther(stat, conn);
        }
        return false;
    }

    /**
     * 获取选定数据表中的数据
     *
     * @param dataSource
     * @param name
     * @param limit
     * @param offset
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Map getDataTable(DataSourceResource dataSource, String name, Integer limit, Integer offset) {

    	Connection conn = null;
        PreparedStatement allStat = null;
        PreparedStatement nameStat = null;
        ResultSet allRes = null;
        ResultSet nameRes = null;
        try {
            Integer start, stop;
            start = offset;
            stop = start + limit;
            String url = dataSource.getConnString();
            Map result = new HashMap();
            if (dataSource.getDataType().equalsIgnoreCase("MYSQL")) {
                Class.forName("com.mysql.jdbc.Driver");

                if(url==null)
                    url = "jdbc:mysql://" + dataSource.getHost() + ":" + dataSource.getPort() + "/" + dataSource.getDbName() +
                            "?useUnicode=true&characterEncoding=" + dataSource.getEncode();

                conn = DriverManager.getConnection(url, dataSource.getUserName(), dataSource.getPassWord());

                String allSql = "select SQL_CALC_FOUND_ROWS *  from "+name+" limit ?,?";

                allStat = conn.prepareStatement(allSql);
                allStat.setInt(1, start);
                allStat.setInt(2, stop);
                allRes = allStat.executeQuery();
                result.put("rows", resultSetToList(allRes));

                allRes = allStat.executeQuery("select FOUND_ROWS()");
                while (allRes.next()) {
                    result.put("total", allRes.getInt(1));
                }

                String nameSql = "select COLUMN_NAME from information_schema.COLUMNS "
                        + " where table_name = "+name+" and table_schema ="+dataSource.getDbName();
                nameStat = conn.prepareStatement(nameSql);
                nameRes = nameStat.executeQuery();
                result.put("column", resultSetToList(nameRes));

                return result;
            } else if (dataSource.getDataType().equalsIgnoreCase("Oracle")) {

            } else if (dataSource.getDataType().equalsIgnoreCase("SQLsever")) {

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeOther(allRes,allStat,nameRes, nameStat, conn);
        }
        return null;
    }


    /**
     * 获取选定数据表的字段名列表
     *
     * @param dbName
     * @param type
     * @param host
     * @param port
     * @param username
     * @param password
     * @param encode
     * @param connstring
     * @param tableId
     * @param tableName
     * @param caption
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public List<Map<String, Object>> getDtColumnNameByKey(String dbName, String type, String host, String port, String username,
                                                          String password, String encode, String connstring, Integer tableId, String tableName, String caption) {
    	Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
    	try {
            String url = connstring;
            if (type.equalsIgnoreCase("MYSQL")) {
                Class.forName("com.mysql.jdbc.Driver");

                String sql = "";
                if (caption.isEmpty()) {
                	if(url==null)
                		url = "jdbc:mysql://" + host + ":" + port + "/" + dbName
                            + "?useUnicode=true&characterEncoding=" + encode;

                    sql = "select COLUMN_NAME from information_schema.COLUMNS "
                            + " where table_name="+tableName+" and table_schema="+dbName;

                    conn = DriverManager.getConnection(url, username, password);
                    stat = conn.prepareStatement(sql);
                    res = stat.executeQuery();

                } else {
                    url = "jdbc:mysql://localhost:3306/wanyou?useUnicode=true&characterEncoding=UTF-8";

                    sql = "select fieldname from datasource_field "
                            + " where  tableid=? and caption like ?";

                    username = "root";
                    password = "";

                    conn = DriverManager.getConnection(url, username, password);
                    stat = conn.prepareStatement(sql);
                    stat.setInt(1, tableId);
                    stat.setString(2, '%' + caption + '%');
                    res = stat.executeQuery();
                }

                List<Map<String, Object>> columns = resultSetToList(res);
                return columns;
            } else if (type.equalsIgnoreCase("Oracle")) {

            } else if (type.equalsIgnoreCase("SQLsever")) {

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeOther(res, stat, conn);
        }
        return null;
    }

    /**
     * 修改数据表字段名
     *
     * @param dataSource
     * @param tableName
     * @param fieldName
     * @param dataField
     * @return
     */
    public boolean updateDtColumn(DataSourceResource dataSource, String tableName, String fieldName, DataSourceField dataField) {

    	Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
    	try {
            String url = dataSource.getConnString();
            if (dataSource.getDataType().equalsIgnoreCase("MYSQL")) {
                Class.forName("com.mysql.jdbc.Driver");
                if(url==null)
                	url = "jdbc:mysql://" + dataSource.getHost() + ":" + dataSource.getPort() + "/" + dataSource.getDbName() +
                        "?useUnicode=true&characterEncoding=" + dataSource.getEncode();
                conn = DriverManager.getConnection(url, dataSource.getUserName(), dataSource.getPassWord());

                //根据传入参数类型确定SQL语句中是否为空子句
                String isAllowNull = "";
                if (dataField.getAllowNull() == 0) {
                    isAllowNull = " NOT NULL";
                }

                //根据传入参数类型确定数据字段长度属性
                if (dataField.getLength().equals("-1")) {
                    dataField.setLength(255L);
                }

                //根据传入参数类型确定数据字段数据精度属性
                if (dataField.getDecimalCount().equals("-1")) {
                    dataField.setDecimalCount(255);
                }

                //根据字段类型确定SQL语句
                String sql = "";
                String dataType = dataField.getType();
                if (dataType.equalsIgnoreCase("DATE") || dataType.equalsIgnoreCase("TIME") || dataType.equalsIgnoreCase("YEAR") ||
                        dataType.equalsIgnoreCase("DATETIME") || dataType.equalsIgnoreCase("TIMESTAMP") ||
                        dataType.equalsIgnoreCase("ENUM") || dataType.equalsIgnoreCase("SET")) {
                    sql = " alter table "+tableName+" change "+fieldName+" "+dataField.getFieldName()+" "+dataType+" " + isAllowNull;
                } else if (dataType.equalsIgnoreCase("DECIMAL") || dataType.equalsIgnoreCase("FLOAT") || dataType.equalsIgnoreCase("DOUBLE")) {
                    sql = " alter table "+tableName+" change "+fieldName+" "+dataField.getFieldName()+" "+dataType+ "(" + dataField.getLength() + ","
                            + dataField.getDecimalCount() + ") " + isAllowNull;
                } else {
                    sql = " alter table "+tableName+" change "+fieldName+" "+dataField.getFieldName()+" "+dataType+ "(" + dataField.getLength() + ") "
                            + isAllowNull;
                }

                stat = conn.prepareStatement(sql);
                stat.executeQuery();
                return true;
            } else if (dataSource.getDataType().equalsIgnoreCase("Oracle")) {

            } else if (dataSource.getDataType().equalsIgnoreCase("SQLsever")) {

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeOther(res, stat, conn);
        }
        return false;
    }

    /**
     * 根据列名查询数据表数据
     *
     * @param dataSource
     * @param tableName
     * @param fieldName
     * @param limit
     * @param offset
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map searchDtByColumn(DataSourceResource dataSource, String tableName, String fieldName, Integer limit, Integer offset) {
        
    	Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        ResultSet total =null;
    	try {
            Map map = new HashMap();
            if (dataSource.getDataType().equalsIgnoreCase ("MYSQL")) {
            	
            	MySqlResourceHelper helper=new MySqlResourceHelper();
                conn = helper.getConnection(dataSource);   
             
                if (fieldName.isEmpty()) {
                    fieldName = "*";
                }
                String sql = "select "+fieldName+" from "+tableName+" limit ?,? ";
                stat = conn.prepareStatement(sql);
                stat.setInt(1, offset);
                stat.setInt(2, offset+limit);
                res = stat.executeQuery();              
                map.put("rows", resultSetToList(res));
               
                String totalSql = "select count("+fieldName+")  from "+tableName;
                total = stat.executeQuery(totalSql);
                while (total.next()) {
                    map.put("total", total.getInt(1));
                }
                
                return map;
            } else if (dataSource.getDataType().equalsIgnoreCase("Oracle")) {

            } else if (dataSource.getDataType().equalsIgnoreCase("SQLsever")) {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeOther(res, stat, conn,total);
        }
        return null;
    }


    /**
     * 根据SQL获取选定数据源中的数据
     *
     * @param dbName
     * @param type
     * @param host
     * @param port
     * @param username
     * @param password
     * @param encode
     * @param connstring
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Map sqlSearchDataSources(String dbName, String type, String host, String port, String username,
                                    String password, String encode, String connstring, String sql,
                                    Integer limit, Integer offset) {
    	Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
    	try {
            String url = connstring;
            Map result = new HashMap();
            if (type.equalsIgnoreCase("MYSQL")) {
                Class.forName("com.mysql.jdbc.Driver");
                if(url==null)
                	url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=" + encode;
                conn = DriverManager.getConnection(url, username, password);
                stat = conn.prepareStatement(sql);
                res = stat.executeQuery();
                List list = resultSetToList(res);
                PageUtil pm = new PageUtil(list, limit);
                Integer pageNow = (int) Math.ceil(offset / limit);
                List sublist = pm.getObjects(pageNow + 1);
                result.put("rows", sublist);
                result.put("total", list.size());
                return result;
            } else if (type.equalsIgnoreCase("Oracle")) {

            } else if (type.equalsIgnoreCase("SQLsever")) {

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeOther(res, stat, conn);
        }
        return null;
    }


    /**
     * 工具方法：关闭资源
     *
     * @param other
     */
    private void closeOther(AutoCloseable... other) {
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
    
    /**
     * 工具方法：将查询结果转换为List
     *
     * @param resultSet
     * @return
     * @throws java.sql.SQLException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List resultSetToList(ResultSet resultSet) throws java.sql.SQLException {
        if (resultSet == null)
            return Collections.EMPTY_LIST;
        ResultSetMetaData md = resultSet.getMetaData(); //得到结果集(rs)的结构信息，比如字段数、字段名等
        int columnCount = md.getColumnCount(); //返回此 ResultSet 对象中的列数
        List list = new ArrayList();
        Map rowData = new HashMap();
        while (resultSet.next()) {
            rowData = new HashMap(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), resultSet.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }

}
