package com.gpdata.wanyou.md.dao;

import com.gpdata.wanyou.md.entity.OntologyBaseinfo;
import com.gpdata.wanyou.md.entity.OntologyGroup;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

public interface OntologyDao {

    OntologyGroup getGroupById(int groupid);

    int addOntologyGroup(OntologyGroup ontologyGroup);

    void updateOntologyGroup(OntologyGroup ontologyGroup);

    void deleteOntologyGroup(int groupid);

    Pair<Integer, List<OntologyGroup>> searchOntologyGroup(String caption, Integer limit, Integer offset);

    OntologyBaseinfo getOntologyById(int ontologyid);

    int addOntology(OntologyBaseinfo ontology);

    void updateOntology(OntologyBaseinfo ontology);

    /**
     * 删除单个本体对象
     * @param ontologyId
     */
    void deleteOntology(Integer ontologyId);

    /**
     * 查询
     * @param bean
     * @param limit
     * @param offset
     * @return
     */
    Pair<Integer, List<OntologyBaseinfo>> searchOntologyBaseinfo(OntologyBaseinfo bean, Integer limit, Integer offset);


}
