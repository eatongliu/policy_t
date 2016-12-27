package com.gpdata.wanyou.md.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.md.dao.MetadataInfoDao;
import com.gpdata.wanyou.md.entity.MetadataInfo;
import com.gpdata.wanyou.md.service.MetadataInfoService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 3.2.2元数据基本信息
 *
 * @author chengchao
 */
@Service
public class MetadataInfoServiceImpl extends BaseService implements MetadataInfoService {

    public MetadataInfoDao metadataInfoDao;

    @Resource
    public void setMetadataInfoDao(MetadataInfoDao metadataInfoDao) {
        this.metadataInfoDao = metadataInfoDao;
    }

    /*
     * (non-Javadoc)
     * @see com.gpdata.wanyou.md.service.MetadataInfoService#getById(int)
     */
    @Override
    public MetadataInfo getById(int metadataid) {
        return metadataInfoDao.getById(metadataid);
    }

    /*
     * (non-Javadoc)
     * @see com.gpdata.wanyou.md.service.MetadataInfoService#save(com.gpdata.wanyou.md.entity.MetadataInfo)
     */
    @Override
    public MetadataInfo save(MetadataInfo input) {
        return metadataInfoDao.save(input);
    }

    /*
     * (non-Javadoc)
     * @see com.gpdata.wanyou.md.service.MetadataInfoService#update(com.gpdata.wanyou.md.entity.MetadataInfo)
     */
    @Override
    public MetadataInfo update(MetadataInfo input) {
        return metadataInfoDao.update(input);
    }

    /*
     * (non-Javadoc)
     * @see com.gpdata.wanyou.md.service.MetadataInfoService#delete(com.gpdata.wanyou.md.entity.MetadataInfo)
     */
    @Override
    public void delete(MetadataInfo input) {

        this.metadataInfoDao.delete(input);

    }

    /*
     * (non-Javadoc)
     * @see com.gpdata.wanyou.md.service.MetadataInfoService#query(com.gpdata.wanyou.md.entity.MetadataInfo)
     */
    @Override
    public Pair<Integer, List<MetadataInfo>> query(MetadataInfo input) {

        Integer total = this.metadataInfoDao.queryTotal(input);
        List<MetadataInfo> rows = this.metadataInfoDao.queryRows(input);


        return Pair.of(total, rows);
    }

    /*
     * (non-Javadoc)
     * @see com.gpdata.wanyou.md.service.MetadataInfoService#updateRelyon(com.gpdata.wanyou.md.entity.MetadataInfo)
     */
    @Override
    public MetadataInfo updateRelyon(MetadataInfo info) {
        return metadataInfoDao.updateRelyon(info);
    }

    @Override
    public String getMetadataSynonyms(int metadataid) {
        return metadataInfoDao.getMetadataSynonyms(metadataid);
    }

    @Override
    public void updateSynonyms(int metadataid, String synmetadataids) {
        MetadataInfo metadataInfo = metadataInfoDao.getById(metadataid);
        System.out.println(metadataInfo);
        Date now = new Date();
        java.sql.Date date = new java.sql.Date(now.getTime()); //转换为sql.Date对象
        metadataInfo.setSynmetadataids(synmetadataids);
        metadataInfo.setRevisedate(date);
        metadataInfoDao.update(metadataInfo);
    }


}
