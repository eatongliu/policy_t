package com.gpdata.wanyou.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by guoxy on 2016/10/31.
 */
public class HDFSUtil {
    private static final Logger logger = LoggerFactory.getLogger(HDFSUtil.class);
    private static final String HDFS_URL = ConfigUtil.getConfig("HDFS.baseurl");
    private static final Configuration conf = new Configuration();

    /**
     * 获取HDFS文件系统
     *
     * @param hdfsPath hdfs根路径
     */
    private static FileSystem getFileSystem(String hdfsPath) throws URISyntaxException, IOException {
        System.setProperty("HADOOP_USER_NAME", ConfigUtil.getConfig("HDFS.HADOOP_USER_NAME"));
        Configuration conf = new Configuration();
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
        //   URI uri = new URI(hdfsPath);
        logger.debug("HDFS_URL:{}", hdfsPath);
        URI uri = new URI(hdfsPath);
        final FileSystem fileSystem = FileSystem.get(uri, conf);
        return fileSystem;
    }


    /**
     * 文件上传
     *
     * @param src 原始文件路径
     * @param dst hadoop根路径
     * @return
     */
    public static boolean put2HSFS(String hdfs, String src, String dst) {
        FileSystem fileSystem = null;
        try {
            Path srcPath = new Path(src);
            Path dstPath = new Path(dst);
            fileSystem = getFileSystem(hdfs);
//            fileSystem.create(dstPath);
            fileSystem.copyFromLocalFile(srcPath, dstPath);
        } catch (URISyntaxException ue) {
            logger.error("URISyntaxException", ue);
            return false;
        } catch (IOException ie) {
            logger.error("IOException", ie);
            return false;
        } finally {
            try {
                fileSystem.close();
            } catch (IOException e) {
                logger.debug("IOException:{}", e);
            }
        }
        return true;
    }

    /**
     * 文件下载
     *
     * @param src hadoop根路径
     * @param dst 原始文件路径
     * @return
     */
    public static boolean getFromHDFS(String hdfs, String src, String dst) {
//        Path dstPath = new Path(dst);
        FileSystem fileSystem = null;
        try {
            Path srcPath = new Path(src);
            Path dstPath = new Path(dst);
            fileSystem = getFileSystem(hdfs);
//            FileSystem dhfs = dstPath.getFileSystem(conf);
            fileSystem.copyToLocalFile(false, srcPath, dstPath);
        } catch (URISyntaxException ue) {
            logger.error("URISyntaxException", ue);
            return false;
        } catch (IOException ie) {
            logger.error("IOException", ie);
            return false;
        } finally {
            try {
                fileSystem.close();
            } catch (IOException e) {
                logger.debug("IOException:{}", e);
            }
        }
        return true;
    }

    /**
     * 文件检测并删除
     *
     * @param path
     * @return
     */
    public static boolean checkAndDel(final String path) {
        Path dstPath = new Path(path);
        try {
            FileSystem dhfs = dstPath.getFileSystem(conf);
            if (dhfs.exists(dstPath)) {
                dhfs.delete(dstPath, true);
            } else {
                return false;
            }
        } catch (IOException ie) {
            logger.error("IOException", ie);
            return false;
        }
        return true;
    }
}
