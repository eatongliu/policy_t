package com.gpdata.wanyou.md.entity;


import com.alibaba.fastjson.JSONObject;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "metadata_info")
public class MetadataInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer metadataid;


    /**
     * 本体 ID
     */
    private Integer ontologyid;

    /**
     * 本体 Caption
     */
    @Formula(value = "( select ob.caption from ontology_baseinfo ob where ob.ontologyid = ontologyid )")
    private String ontologycaption;

    /**
     * 方言 ID
     */
    private Integer dialectid;

    /**
     * 方言 Caption
     */
    @Formula(value = "( select md.caption from metadata_dialect md where md.dialectid = dialectid )")
    private String dialectcaption;


    /**
     * 字段id
     */
    private Integer fieldid;

    /**
     *
     */
    @Formula(value = "( select a.fieldname "
            + " from datasource_field a "
            + " where a.fieldid = fieldid )")
    private String fieldname;

    @Formula(value = "( select b.tableid "
            + " from datasource_field a, datasource_table b "
            + " where a.tableid = b.tableid "
            + " and a.fieldid = fieldid )")
    private Integer tableid;

    @Formula(value = "( select b.tablename "
            + " from datasource_field a, datasource_table b "
            + " where a.tableid = b.tableid "
            + " and a.fieldid = fieldid )")
    private String tablename;

    @Formula(value = "( select c.resourceid "
            + " from datasource_field a, datasource_table b, datasource_resource c "
            + " where a.tableid = b.tableid "
            + " and b.resourceid = c.resourceid "
            + " and a.fieldid = fieldid )")
    private Integer resourceid;

    @Formula(value = "( select c.caption "
            + " from datasource_field a, datasource_table b, datasource_resource c "
            + " where a.tableid = b.tableid "
            + " and b.resourceid = c.resourceid "
            + " and a.fieldid = fieldid )")
    private String resourcecaption;

    private String metaname;
    private String caption;
    private Date createdate;
    private String creator;
    private Date revisedate;
    private String type;
    private String unit;

    @Column(name = "max_value")
    private Double maxvalue;

    @Column(name = "min_value")
    private Double minvalue;

    private Integer allownull;
    private Integer decimalcount;
    private Integer maxlength;

    /**
     * 依赖的元数据
     */
    private String depmetadataid;

    /**
     * 元数据同义词id集合
     */
    private String synmetadataids;
    private String remark;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static void main(String[] args) {
        MetadataInfo metadataInfo = new MetadataInfo();
        System.out.println(metadataInfo);
    }

    public Integer getMetadataid() {
        return metadataid;
    }

    public void setMetadataid(Integer metadataid) {
        this.metadataid = metadataid;
    }

    public Integer getOntologyid() {
        return ontologyid;
    }

    public void setOntologyid(Integer ontologyid) {
        this.ontologyid = ontologyid;
    }

    public Integer getDialectid() {
        return dialectid;
    }

    public void setDialectid(Integer dialectid) {
        this.dialectid = dialectid;
    }

    /**
     * 字段id
     *
     * @return
     */
    public Integer getFieldid() {
        return fieldid;
    }

    /**
     * 字段id
     *
     * @param fieldid
     */
    public void setFieldid(Integer fieldid) {
        this.fieldid = fieldid;
    }

    public String getMetaname() {
        return metaname;
    }

    public void setMetaname(String metaname) {
        this.metaname = metaname;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getRevisedate() {
        return revisedate;
    }

    public void setRevisedate(Date revisedate) {
        this.revisedate = revisedate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getMaxvalue() {
        return maxvalue;
    }

    public void setMaxvalue(Double maxvalue) {
        this.maxvalue = maxvalue;
    }

    public Double getMinvalue() {
        return minvalue;
    }

    public void setMinvalue(Double minvalue) {
        this.minvalue = minvalue;
    }

    public Integer getAllownull() {
        return allownull;
    }

    public void setAllownull(Integer allownull) {
        this.allownull = allownull;
    }

    public Integer getDecimalcount() {
        return decimalcount;
    }

    public void setDecimalcount(Integer decimalcount) {
        this.decimalcount = decimalcount;
    }

    public Integer getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(Integer maxlength) {
        this.maxlength = maxlength;
    }

    /**
     * 依赖的元数据
     *
     * @return
     */
    public String getDepmetadataid() {
        return depmetadataid;
    }

    /**
     * 依赖的元数据
     *
     * @param depmetadataid
     */
    public void setDepmetadataid(String depmetadataid) {
        this.depmetadataid = depmetadataid;
    }

    /**
     * 元数据同义词id集合
     *
     * @return
     */
    public String getSynmetadataids() {
        return synmetadataids;
    }

    /**
     * 元数据同义词id集合
     *
     * @param synmetadataids
     */
    public void setSynmetadataids(String synmetadataids) {
        this.synmetadataids = synmetadataids;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOntologycaption() {
        return ontologycaption;
    }

    public void setOntologycaption(String ontologycaption) {
        this.ontologycaption = ontologycaption;
    }

    public String getDialectcaption() {
        return dialectcaption;
    }

    public void setDialectcaption(String dialectcaption) {
        this.dialectcaption = dialectcaption;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public Integer getTableid() {
        return tableid;
    }

    public void setTableid(Integer tableid) {
        this.tableid = tableid;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public Integer getResourceid() {
        return resourceid;
    }

    public void setResourceid(Integer resourceid) {
        this.resourceid = resourceid;
    }

    public String getResourcecaption() {
        return resourcecaption;
    }

    public void setResourcecaption(String resourcecaption) {
        this.resourcecaption = resourcecaption;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}