package com.gpdata.wanyou.ds.dao;

import com.gpdata.wanyou.ds.entity.DataSourceResource;

import java.util.List;

/**
 * HDFS文件系统操作
 *
 * @author gaosong 2016-10-31
 */
public interface HDFSDao {
    /**
     * 获取子目录
     *
     * @param dataSource 数据资源
     * @param parent     父路径
     * @return
     * @throws Exception
     */
    List getHDFSDir(DataSourceResource dataSource, String parent) throws Exception;

    /**
     * 获取文件
     *
     * @param dataSource 数据资源
     * @param parent     父路径
     * @return
     * @throws Exception
     */
    List getHDFSFile(DataSourceResource dataSource, String parent) throws Exception;

    /**
     * 获取文件
     *
     * @param dataSource 数据资源
     * @param filePath   文件路径
     * @return
     * @throws Exception
     */
    List getHDFSContent(DataSourceResource dataSource, String filePath, int limit, int offset) throws Exception;

    /**
     * 测试HDFS连接
     *
     * @param dataSource 数据资源
     * @return
     * @throws Exception
     */
    Boolean testHDFS(DataSourceResource dataSource) throws Exception;

}
