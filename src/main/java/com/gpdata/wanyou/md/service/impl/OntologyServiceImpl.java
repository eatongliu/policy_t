package com.gpdata.wanyou.md.service.impl;

import com.gpdata.wanyou.base.dao.SimpleDao;
import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.md.dao.MetadataBeanDao;
import com.gpdata.wanyou.md.dao.OntologyDao;
import com.gpdata.wanyou.md.entity.MetadataBean;
import com.gpdata.wanyou.md.entity.OntologyBaseinfo;
import com.gpdata.wanyou.md.entity.OntologyGroup;
import com.gpdata.wanyou.md.service.OntologyService;
import com.gpdata.wanyou.system.entity.User;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OntologyServiceImpl extends BaseService implements OntologyService {

    @Autowired
    private OntologyDao ontologyDao;

    @Autowired
    private SimpleDao simpleDao;

    @Autowired
    private MetadataBeanDao metadataBeanDao;

    @Override
    public OntologyGroup getGroupById(Integer groupId) {
        return ontologyDao.getGroupById(groupId);
    }

    @Override
    public int addOntologyGroup(OntologyGroup ontologyGroup) {
        Date date = new Date();
        ontologyGroup.setCreateDate(date);
        ontologyGroup.setReviseDate(date);
        return ontologyDao.addOntologyGroup(ontologyGroup);
    }

    @Override
    public void updateOntologyGroup(OntologyGroup ontologyGroup) {
        Date date = new Date();
        ontologyGroup.setReviseDate(date);

        simpleDao.update(ontologyGroup.getGroupId(), ontologyGroup);
    }

    @Override
    public void deleteOntologyGroup(Integer groupId) {
        ontologyDao.deleteOntologyGroup(groupId);
    }


    @Override
    public Pair<Integer, List<OntologyGroup>> searchOntologyGroup(String caption
            , Integer limit, Integer offset) {

        return ontologyDao.searchOntologyGroup(caption, limit, offset);
    }

    /**
     * 获取本体对象
     * @param ontologyId
     * @return
     */
    @Override
    public OntologyBaseinfo getOntologyById(Integer ontologyId) {

        OntologyBaseinfo result = ontologyDao.getOntologyById(ontologyId);
        if (result != null) {
            List<MetadataBean> metadataBeanList = this.metadataBeanDao
                    .getMetadataBeansByOntologyId(ontologyId);
            result.setMetadataBeanIdList(metadataBeanList);
        }

        return result;
    }

    @Override
    public Integer addOntology(OntologyBaseinfo ontologyBaseinfo, User creator) {

        Date current = new Date();

        ontologyBaseinfo.setCreateDate(current);
        ontologyBaseinfo.setReviseDate(current);
        ontologyBaseinfo.setUserId(String.valueOf(creator.getUserId()));

        return (Integer) this.simpleDao.save(ontologyBaseinfo);
    }

    @Override
    public void updateOntology(OntologyBaseinfo ontologyBaseinfo) {
        Date current = new Date();
        ontologyBaseinfo.setReviseDate(current);
        this.simpleDao.update(ontologyBaseinfo.getOntologyId(), ontologyBaseinfo);
    }

    /**
     * 删除单个本体
     * @param ontologyId
     */
    @Override
    public void deleteOntology(Integer ontologyId) {
        ontologyDao.deleteOntology(ontologyId);

    }

    @Override
     public Pair<Integer, List<OntologyBaseinfo>> searchOntologyBaseinfo(OntologyBaseinfo bean
            , Integer limit, Integer offset) {

        return ontologyDao.searchOntologyBaseinfo(bean, limit, offset);
    }

    /**
     * 修改本体对象相关联的元数据的关系
     * @param ontologyId
     * @param metadataIdSet
     */
    @Override
    public void updateMetadataBeanMatch(Integer ontologyId, Set<Integer> metadataIdSet) {

        OntologyBaseinfo ontologyBaseinfo = this.simpleDao.getById(OntologyBaseinfo.class, ontologyId);

        if (ontologyBaseinfo == null) {
            return;
        }
        List<MetadataBean> metadataBeanList = this.metadataBeanDao
                .getMetadataBeansByOntologyId(ontologyId);

        metadataBeanList.stream()
                .map(MetadataBean::getMetadataId)
                .filter(metadataId -> !metadataIdSet.contains(metadataId))
                .forEach(metadataId ->
                        this.metadataBeanDao.deleteBeanMatch(metadataId));

        metadataIdSet.forEach(metadataId -> {
            this.metadataBeanDao.addBeanMatch(ontologyId, metadataId);
        });
    }
}
