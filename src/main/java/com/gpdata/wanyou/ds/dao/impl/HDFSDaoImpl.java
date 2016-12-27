package com.gpdata.wanyou.ds.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.ds.dao.HDFSDao;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.ds.util.HDFSUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * HDFS基本操作DAO层
 *
 * @author gaosong 2016-10-31
 */
@Component
public class HDFSDaoImpl extends BaseDao implements HDFSDao {

    /**
     * 获取HDFS子目录
     *
     * @throws Exception
     */
    @Override
    public List getHDFSDir(DataSourceResource dataSource, String parent) throws Exception {
        return HDFSUtil.getDir(dataSource.getHdfsPath(), parent);
    }

    /**
     * 获取HDFS文件
     */
    @Override
    public List getHDFSFile(DataSourceResource dataSource, String parent) throws Exception {
        return HDFSUtil.getFile(dataSource.getHdfsPath(), parent);
    }

    /**
     * 获取HDFS文件内容
     */
    @Override
    public List getHDFSContent(DataSourceResource dataSource, String filePath,
                               int limit, int offset) throws Exception {
        // TODO Auto-generated method stub
        return HDFSUtil.readFile(dataSource.getHdfsPath(), filePath, limit, offset);
    }

    /**
     * 测试HDFS连接
     */
    @Override
    public Boolean testHDFS(DataSourceResource dataSource) throws Exception {
        // TODO Auto-generated method stub
        return HDFSUtil.test(dataSource.getHdfsPath());
    }
}
