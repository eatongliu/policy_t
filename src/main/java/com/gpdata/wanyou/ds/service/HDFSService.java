package com.gpdata.wanyou.ds.service;

import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.ds.entity.DataSourceResource;

/**
 * HDFS操作
 *
 * @author gaosong 2016-10-31
 */
public interface HDFSService {
    /**
     * 获取子目录
     *
     * @throws Exception
     */
    BeanResult getHDFSDir(DataSourceResource dataSourceResource, String parent) throws Exception;

    /**
     * 获取文件
     *
     * @throws Exception
     */
    BeanResult getHDFSFile(DataSourceResource dataSourceResource, String parent) throws Exception;

    /**
     * 获取文件内容
     *
     * @throws Exception
     */
    BeanResult getHDFSContent(DataSourceResource dataSourceResource, String filePath, Integer limit, Integer offset) throws Exception;

    /**
     * 测试连通性
     *
     * @throws Exception
     */
    BeanResult test(DataSourceResource dataSourceResource) throws Exception;
}
