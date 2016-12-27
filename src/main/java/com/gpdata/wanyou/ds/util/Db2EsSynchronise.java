package com.gpdata.wanyou.ds.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import com.gpdata.wanyou.ds.entity.DataSourceResource;

/**
 * es与db同步工具类
 * 1获取配置信息
 * 2生成配置文件
 * 3运行配置文件
 * 
 * @author qyl
 *
 */
public class Db2EsSynchronise {
	/**
	 * mysql地址 例如："192.168.1.61:3306"
	 */
	private String myAddr;
	/**
	 * mysql登录名 例如："root"
	 */
	private String myUser;
	/**
	 * mysql登录密码 例如："gaosong"
	 */
	private String myPass;
	/**
	 * ES路径 例如："192.168.1.120:9200"
	 */
	private String esAddr;
	/**
	 * 数据暂存路径，不可重复 例如："./var"
	 */
	private String dataDir;
	/**
	 * 运行时所需的路径 IP地址需要生成，端口号不变："192.168.1.120:12805"
	 */
	private String statAddr;
	/**
	 * Server的ID好，不可重复 例如：1
	 */
	private Long serverId;
	/**
	 * 数据库类型  固定："mysql"
	 */
	private String flavor;
	/**
	 * 固定配置 固定："mysqldump"
	 */
	private String mysqldump;

	/**
	 * [[source]]
	 * 数据库名 例如："world"
	 */
	private String sourceSchema;
	/**
	 * [[source]] tables
	 * [[rule]] table 
	 * [[rule]] type  
	 * 数据表名 例如：["city", "country"]
	 */
	private List<String> tables;

	/**
	 * [[rule]]：mysql与es之间的对照（mapping）的规则
	 */
	private String ruleSchema;
	private String ruleIndex;

	
	/**
	 * 构造同步类并配置
	 * 
	 * @param ds 待同步的数据源
	 * @param es_addr ES路径 例如："192.168.1.120:9200"
	 * @param superDataDir 数据暂存路径的父目录
	 * @param stat_addr 运行时所需的路径 IP地址需要生成，端口号不变："192.168.1.120:12805"
	 * @param tables 待同步的数据源的数据表名数组
	 */
	public Db2EsSynchronise(
			DataSourceResource ds,
			String es_addr,
			String superDataDir,
			String stat_addr,
			List<String> tables) {
		super();
		this.myAddr = ds.getHost()+":"+ds.getPort();
		this.myUser = ds.getUserName();
		this.myPass = ds.getPassWord();
		this.esAddr = es_addr;
		this.serverId = System.currentTimeMillis();
		this.dataDir = superDataDir+serverId;	
		this.statAddr = stat_addr;
		this.flavor = ds.getDataType();
		this.mysqldump = "mysqldump";
		this.sourceSchema = ds.getDbName();
		this.tables = tables;
		this.ruleSchema = ds.getDbName();
		this.ruleIndex = ds.getDbName();
	}
	
	
	@Override
	public String toString() {
		String string =   "my_addr=" + "\"" +myAddr + "\""  + "\n"
						+ "my_user=" + "\""  + myUser + "\""  + "\n"
						+ "my_pass=" + "\""  + myPass + "\""  + "\n"
						+ "es_addr=" + "\""  + esAddr + "\""  + "\n"
						+ "data_dir=" + "\""  + dataDir + "\""  + "\n"
						+ "stat_addr=" + "\""  + statAddr + "\""  + "\n"
						+ "server_id=" + serverId + "\n"
						+ "flavor=" + "\""  + flavor + "\"" + "\n"
						+ "mysqldump=" + "\""  + mysqldump + "\""  + "\n"
						+ "[[source]]" + "\n"
						+ "schema=" + "\""  + sourceSchema + "\""  + "\n"
						+ "tables=" + List2String(tables) + "\n";
		for(String table:tables){
			string += "[[rule]]" + "\n"
					+ "schema=" + "\""  + ruleSchema + "\""  + "\n"
					+ "table=" + "\""  + table + "\""  + "\n"
					+ "index=" + "\""  + ruleIndex + "\""  + "\n"
					+ "type=" + "\""  + table + "\""  + "\n";
		}
		return string;
	}
	
	/**
     * 创建配置文件并添加内容 
     * 
	 * @throws IOException 
     */
	private String  createRiverToml(String riverTomlPath) throws IOException{
		File file = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		File riverTomlPathFolder  = new File(riverTomlPath);
		if(!riverTomlPathFolder.exists())riverTomlPathFolder.mkdirs();//执行路径不存在则创建
		try{
			String fileContent = this.toString();//配置内容
			String fileName = "river" + sourceSchema + serverId + ".toml";
			String filePathAndName = riverTomlPath + fileName; 
			file = new File(filePathAndName);
	        file.createNewFile();
	        fos = new FileOutputStream(filePathAndName);
	        osw = new OutputStreamWriter(fos, "utf-8");
	        osw.write(fileContent);
	        osw.flush(); 
	        return filePathAndName;
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			osw.close();
		}
		return null;
	}
	
	/**
	 * 执行生成的配置文件
	 * 
	 * @param runPath cd命令要进入的执行目录
	 * @param riverTomlPath 存放river.toml文件的目录
	 * @return
	 * @throws IOException
	 */
	public String run(String runPath,String riverTomlPath) throws IOException{
		File folder  = new File(dataDir);
		folder.mkdirs();//创建配置文件执行所需数据暂存路径
		
		File runPathFolder  = new File(runPath);
		if(!runPathFolder.exists())runPathFolder.mkdirs();//执行路径不存在则创建
		
		
		String riverPathAndName = this.createRiverToml(riverTomlPath);
		if(riverPathAndName==null){
			return null;
		}
		String log="";   
		String line=null;
		
		//runPath在pom中配置
		String cmd=runPath+" -config="+ riverPathAndName;
		log="cmd:"+cmd+"\n";
//		String[] cmdArr ={ "cmd", "/c", cmd};//Windows下shell命令
		String[] cmdArr ={ "/bin/sh","-c", cmd};//Linux下shell命令
		try {
			 Process proc = Runtime.getRuntime().exec(cmdArr); //执行shell命令
			 InputStream fis=proc.getInputStream();     
             InputStreamReader isr=new InputStreamReader(fis);    
             BufferedReader br=new BufferedReader(isr);     
             while((line=br.readLine())!=null){  //读取执行结果
            	 log+=line+"\n";
             }
         }catch (Exception e){    
             e.printStackTrace();    
         }    
		return log;
	}
	
	
	
	
	
	
	/**
	 * List类型转字符串
	 * 
	 * @param tables
	 * @return
	 */
	private  String List2String(List<String> tables) {
        if (tables == null) return "null";
        int iMax = tables.size() - 1;
        if (iMax == -1)
            return "[]";
        StringBuilder b = new StringBuilder();
        
        b.append('[');
        
        for (int i = 0; ; i++) {
            b.append("\""+tables.get(i)+"\"");
            if (i == iMax)
            	
        return b.append(']').toString();
            
        b.append(", ");
        }
    }
	
	 
}
