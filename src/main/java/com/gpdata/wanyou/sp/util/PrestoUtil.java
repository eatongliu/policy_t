package com.gpdata.wanyou.sp.util;

import com.gpdata.wanyou.utils.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

/**
 * 根据传递的表名、字段名创建数据表
 *
 * @author gaosong
 */
public class PrestoUtil {
    private static final Logger logger = LoggerFactory.getLogger(PrestoUtil.class);
    /**
     * 创建外部表
     *
     * @param tablename 数据表名称
     * @param fields    字段名集合
     * @return
     * @throws Exception 
     * @throws Exception
     */
    public static boolean CreateHiveExternalTable(String tablename, List<String> fields) throws Exception  {
    	String hdfsPath = ConfigUtil.getConfig("HDFS.url");
    	String hiveThriftServer;// = "jdbc:hive2://192.168.1.120:10000";
    	hiveThriftServer = ConfigUtil.getConfig("HIVE.ThriftServer");
    	
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        Connection con = DriverManager.getConnection(hiveThriftServer + "/default", "hadoop", "wanyou");
        Statement stmt = con.createStatement();
        String querySQL;// = "create external table test1(a String, b String, c String) location 'hdfs://192.168.1.120:9000/spider/'";
        String field = "";
        for(String f : fields){
        	field += f + " String,";
        }
        field = field.substring(0,field.length()-1);
        querySQL = "create external table " + tablename + "(" + field + ") location '" + hdfsPath + "/spider/" + tablename + "/'";
        try{
	        stmt.execute(querySQL);
	        con.close();
	        return true;
        }
        catch(Exception ex){
            logger.error("Exception:{}",ex);
        	return false;
        }
        finally{
        	con.close();
        }
        
//        ResultSet res = stmt.executeQuery(querySQL);
//        while (res.next()) {
//            System.out.println(res.getString(0) + "\t" + res.getString(1) + "\t" + res.getString(2));
//        }
    }
}
