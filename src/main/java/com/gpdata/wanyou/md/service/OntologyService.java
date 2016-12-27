package com.gpdata.wanyou.md.service;

import com.gpdata.wanyou.md.entity.OntologyBaseinfo;
import com.gpdata.wanyou.md.entity.OntologyGroup;
import com.gpdata.wanyou.system.entity.User;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OntologyService {

    OntologyGroup getGroupById(Integer groupId);

    int addOntologyGroup(OntologyGroup ontologyGroup);

    void updateOntologyGroup(OntologyGroup ontologyGroup);

    void deleteOntologyGroup(Integer groupid);


    Pair<Integer, List<OntologyGroup>> searchOntologyGroup(
            String caption, Integer limit, Integer offset);

    /**
     * 获取本体对象
     * @param ontologyId
     * @return
     */
    OntologyBaseinfo getOntologyById(Integer ontologyId);

    Integer addOntology(OntologyBaseinfo ontologyBaseinfo, User creator);

    void updateOntology(OntologyBaseinfo ontologyBaseinfo);

    void deleteOntology(Integer ontologyId);


    Pair<Integer, List<OntologyBaseinfo>> searchOntologyBaseinfo(
            OntologyBaseinfo ontologyBaseinfo, Integer limit, Integer offset);

    /**
     * 修改本体对象相关联的元数据的关系
     * @param ontologyId
     * @param metadataIdSet
     */
    void updateMetadataBeanMatch(Integer ontologyId, Set<Integer> metadataIdSet);
}
