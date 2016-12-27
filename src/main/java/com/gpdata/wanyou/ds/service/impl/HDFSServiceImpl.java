package com.gpdata.wanyou.ds.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.ds.dao.HDFSDao;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.ds.service.HDFSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * HDFS文件系统操作
 *
 * @author gaosong 2016-10-31
 */
@Service
public class HDFSServiceImpl extends BaseService implements HDFSService {

    @Autowired
    private HDFSDao hdfsDao;

    /**
     * 获取HDFS子目录
     *
     * @throws Exception
     */
    @Override
    public BeanResult getHDFSDir(DataSourceResource dataSourceResource,
                                 String parent) throws Exception {
        return BeanResult.success(hdfsDao.getHDFSDir(dataSourceResource, parent));
    }

    /**
     * 获取HDFS文件
     *
     * @throws Exception
     */
    @Override
    public BeanResult getHDFSFile(DataSourceResource dataSourceResource,
                                  String parent) throws Exception {
        return BeanResult.success(hdfsDao.getHDFSFile(dataSourceResource, parent));
    }

    /*
     * 获取HDFS文件内容
     */
    @Override
    public BeanResult getHDFSContent(DataSourceResource dataSourceResource,
                                     String filePath, Integer limit, Integer offset) throws Exception {
        return BeanResult.success(hdfsDao.getHDFSContent(dataSourceResource, filePath, limit, offset));
    }

    /**
     * 测试HDFS连通性
     */
    @Override
    public BeanResult test(DataSourceResource dataSourceResource)
            throws Exception {
        // TODO Auto-generated method stub
        return BeanResult.success(hdfsDao.testHDFS(dataSourceResource));
    }
}
