package com.gpdata.wanyou.ds.util;

/**
 * @author gaosong 2016-10-27
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.gpdata.wanyou.utils.ConfigUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class HDFSUtil {

    /**
     * 获取HDFS文件系统
     */
    private static FileSystem getFileSystem(String hdfsPath) throws URISyntaxException, IOException {
	 	System.setProperty("HADOOP_USER_NAME", ConfigUtil.getConfig("HDFS.HADOOP_USER_NAME"));
        Configuration conf = new Configuration();
        conf.set("fs.hdfs.impl",org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());  
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER"); 
        conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true"); 
        URI uri = new URI(hdfsPath);
        final FileSystem fileSystem = FileSystem.get(uri, conf);
        return fileSystem;
    }

    /**
     * 判断该HDFS路径是否存在
     *
     * @param path String HDFS路径 如：hdfs://192.168.1.120:9000
     * @return boolean 存在：true  不存在：false
     * @throws IOException
     * @throws URISyntaxException
     * @throws IllegalArgumentException
     */
    public static boolean test(String hdfsPath) throws Exception {
        FileSystem fileSystem = getFileSystem(hdfsPath);
        boolean b = fileSystem.exists(new Path("/"));
        if (b)
            return true;
        return false;
    }

    /**
     * 读取文件，调用fileSystem的open(path)
     *
     * @throws Exception
     */
    public static List readFile(String hdfsPath, String filePath, Integer limit, Integer offset) throws Exception {
        FileSystem fileSystem = getFileSystem(hdfsPath);
        FSDataInputStream fin = fileSystem.open(new Path(filePath));
        BufferedReader in = null;
        List<String> content = new ArrayList<String>();
        String line;
        try {
            in = new BufferedReader(new InputStreamReader(fin, "UTF-8"));

            //跳过前offset行
            for (int i = 0; i < offset; i++) {
                in.readLine();
            }

            //读取limit行
            int ilimit = 0;
            while ((line = in.readLine()) != null) {
                content.add(line);

                //ilimit累加，如果达到指定行数，直接跳出
                ilimit++;
                if (ilimit >= limit) break;
            }
        } finally {
            if (in != null) {
                in.close();
            }
            fileSystem.close();
        }
        return content;
    }
    
    /**
     * 向HDFS文件内追加行
     * @param hdfsPath HDFS文件路径
     * @param filePath 
     * @param row
     * @return
     */
    public static boolean appendFile(String hdfsPath, String filePath, String[] rows)  {
		FSDataOutputStream output = null;  
		FileSystem fileSystem = null;
    	try {    	     			 
    		fileSystem = getFileSystem(hdfsPath);
    		Path file = new Path(filePath);
    		if(!fileSystem.exists(file)){
    			output = fileSystem.create(file); // 创建文件    	 
    		} else {
    			output = fileSystem.append(file); // 追加文件    	 
    		}
    		for(String line : rows) { // 写入数据    	 
    			output.write(line.getBytes("UTF-8"));    	 
    			output.write("\n".getBytes("UTF-8"));    	 
    			output.flush();    	 
    		}    	 
    		return true;
    	} catch (Exception e) {    	 
    		e.printStackTrace();   	 
    		return false;
    	} finally {    	 
    		try {
    			output.close();    			 
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		try {
    			fileSystem.close();   			 
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    }

    /**
     * 遍历目录，返回文件夹名称
     */
    public static List getDir(String hdfsPath, String parentPath) throws Exception {
        FileSystem fileSystem = getFileSystem(hdfsPath);
        FileStatus[] listStatus = fileSystem.listStatus(new Path(parentPath));
        List<String> dir = new ArrayList<String>();
        for (FileStatus fileStatus : listStatus) {
            if (fileStatus.isDirectory()) {
                dir.add(fileStatus.getPath().getName());
            }
        }
        return dir;
    }

    /**
     * 遍历目录，返回文件名称
     */
    public static List getFile(String hdfsPath, String parentPath) throws Exception {
        FileSystem fileSystem = getFileSystem(hdfsPath);
        FileStatus[] listStatus = fileSystem.listStatus(new Path(parentPath));
        List<String> dir = new ArrayList<String>();
        for (FileStatus fileStatus : listStatus) {
            if (!fileStatus.isDirectory()) {
                dir.add(fileStatus.getPath().getName());
            }
        }
        return dir;
    }
    
    /**
     * 获取文件夹大小
     */
    public static long getFolderSize(String hdfsPath, String parentPath) throws Exception {
        FileSystem fileSystem = getFileSystem(hdfsPath);
        return fileSystem.getContentSummary(new Path(parentPath)).getLength();
    }
    
    /**
     * 获取文件行数
     *
     * @throws Exception
     */
    public static long getFileRowCount(String hdfsPath, String filePath) throws Exception {
        FileSystem fileSystem = getFileSystem(hdfsPath);
        FSDataInputStream fin = fileSystem.open(new Path(filePath));
        BufferedReader in = null;
        long fileRowCount = 0;
        try {
            in = new BufferedReader(new InputStreamReader(fin, "UTF-8"));
            while((in.readLine()) != null) {  
            	fileRowCount++;  
            }  
        } finally {
            if (in != null) {
                in.close();
            }
            fileSystem.close();
        }
        return fileRowCount;
    }
}
