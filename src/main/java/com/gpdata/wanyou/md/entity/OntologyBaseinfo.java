package com.gpdata.wanyou.md.entity;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
@Table(name = "ontology_baseinfo")
public class OntologyBaseinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 本体 id */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ontologyId;

    /* 本体组 id */
    private Integer groupId;

    /* 标题 */
    private String caption;

    /* 创建人 id */
    private String userId;

    /* 创建人 */
    @Formula(value="(select a.nickName from sys_user a where a.userId = userId)")
    private String creator;

    /* 创建时间 */
    private Date createDate;

    /* 修改时间 */
    private Date reviseDate;

    /* 说明 */
    private String remark;


    @Transient
    private List<Integer> metadataBeanIdList = Collections.emptyList();

    @Transient
    private Map<Integer, MetadataBean> metadataBeanMap = Collections.emptyMap();


    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getOntologyId() {
        return ontologyId;
    }

    public void setOntologyId(Integer ontologyId) {
        this.ontologyId = ontologyId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getReviseDate() {
        return reviseDate;
    }

    public void setReviseDate(Date reviseDate) {
        this.reviseDate = reviseDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public List<Integer> getMetadataBeanIdList() {
        return metadataBeanIdList;
    }

    public void setMetadataBeanIdList(List<MetadataBean> metadataBeanList) {

        if (metadataBeanList != null && !metadataBeanList.isEmpty()) {
            this.metadataBeanIdList = metadataBeanList.stream()
                    .map(MetadataBean::getMetadataId)
                    .collect(Collectors.toList());

            this.metadataBeanMap = metadataBeanList.stream()
                    .collect(Collectors.toMap(MetadataBean::getMetadataId, Function.identity()));
        }

    }

    public Map<Integer, MetadataBean> getMetadataBeanMap() {
        return metadataBeanMap;
    }


    @Override
    public String toString() {
        return "OntologyBaseinfo [ontologyId=" + ontologyId + ", groupId="
                + groupId + ", caption=" + caption + ", createDate="
                + createDate + ", creator=" + creator + ", reviseDate="
                + reviseDate + ", remark=" + remark + "]";
    }

}