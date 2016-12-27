package com.gpdata.wanyou.md.dto;

import com.gpdata.wanyou.ds.entity.DataSourceField;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.ds.entity.DataSourceTable;
import com.gpdata.wanyou.md.entity.MetadataInfo;
import com.gpdata.wanyou.md.entity.OntologyBaseinfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chengchao on 16-10-8.
 */
public class AnalysisResultBean implements Serializable {

    /**
     * 元数据 ID
     */
    private Integer metadataId;

    /**
     * 元数据
     */
    private MetadataInfo metadataInfo;

    /**
     * 本体
     */
    private OntologyBaseinfo ontologyBaseinfo;

    /**
     * 依赖的元数据 ID
     */
    private List<Integer> dependentIdList;

    /**
     * 数据字段信息
     */
    private DataSourceField dataSourceField;

    /**
     * 数据表信息
     */
    private DataSourceTable dataSourceTable;

    /**
     * 数据资源信息
     */
    private DataSourceResource dataSourceResource;

    /**
     * 同义词 ID 列表
     */
    private List<Integer> synmetadataIdList;


    public Integer getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(Integer metadataId) {
        this.metadataId = metadataId;
    }

    public MetadataInfo getMetadataInfo() {
        return metadataInfo;
    }

    public void setMetadataInfo(MetadataInfo metadataInfo) {
        this.metadataInfo = metadataInfo;
    }

    public OntologyBaseinfo getOntologyBaseinfo() {
        return ontologyBaseinfo;
    }

    public void setOntologyBaseinfo(OntologyBaseinfo ontologyBaseinfo) {
        this.ontologyBaseinfo = ontologyBaseinfo;
    }

    public List<Integer> getDependentIdList() {
        return dependentIdList;
    }

    public void setDependentIdList(List<Integer> dependentIdList) {
        this.dependentIdList = dependentIdList;
    }

    public DataSourceField getDataSourceField() {
        return dataSourceField;
    }

    public void setDataSourceField(DataSourceField dataSourceField) {
        this.dataSourceField = dataSourceField;
    }

    public DataSourceTable getDataSourceTable() {
        return dataSourceTable;
    }

    public void setDataSourceTable(DataSourceTable dataSourceTable) {
        this.dataSourceTable = dataSourceTable;
    }

    public DataSourceResource getDataSourceResource() {
        return dataSourceResource;
    }

    public void setDataSourceResource(DataSourceResource dataSourceResource) {
        this.dataSourceResource = dataSourceResource;
    }

    public List<Integer> getSynmetadataIdList() {
        return synmetadataIdList;
    }

    public void setSynmetadataIdList(List<Integer> synmetadataIdList) {
        this.synmetadataIdList = synmetadataIdList;
    }
}
