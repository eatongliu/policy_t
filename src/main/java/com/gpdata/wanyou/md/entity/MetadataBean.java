package com.gpdata.wanyou.md.entity;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 元数据
 * Created by chengchao on 2016/10/25.
 */
@Entity
@Table(name = "metadata_bean")
public class MetadataBean {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer metadataId;

    /**
     * 与之相关联的 本体 ID
     */
    private Integer ontologyId;

    /**
     * 中文名
     */
    private String metadataName;

    /**
     * 标题或者叫英文名
     */
    private String caption;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建用户 id
     */
    private String userId;

    /**
     * 创建人名称
     */
    @Formula(value="(select a.nickName from sys_user a where a.userId = userId)")
    private String creator;

    /**
     * 是否来自数据标准("是"|"否")
     */
    private String isStand;

    /**
     * 与数据标准相关联的元数据实体的 ID
     */
    private Integer standMetadataEntityId;

    /**
     * 备注
     */
    private String remark;

    @Transient
    private List<Integer> metadataEntityList = Collections.emptyList();

    @Transient
    private Map<Integer, MetadataEntity> metadataEntityMap = Collections.emptyMap();



    public Integer getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(Integer metadataId) {
        this.metadataId = metadataId;
    }

    public Integer getOntologyId() {
        return ontologyId;
    }

    public void setOntologyId(Integer ontologyId) {
        this.ontologyId = ontologyId;
    }

    public String getMetadataName() {
        return metadataName;
    }

    public void setMetadataName(String metadataName) {
        this.metadataName = metadataName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getIsStand() {
        return isStand;
    }

    public void setIsStand(String isStand) {
        this.isStand = isStand;
    }

    public Integer getStandMetadataEntityId() {
        return standMetadataEntityId;
    }

    public void setStandMetadataEntityId(Integer standMetadataEntityId) {
        this.standMetadataEntityId = standMetadataEntityId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Integer> getMetadataEntityList() {
        return metadataEntityList;
    }

    public void setMetadataEntityList(List<MetadataEntity> metadataEntityList) {
        if (Objects.nonNull(metadataEntityList) && !metadataEntityList.isEmpty()) {

            this.metadataEntityList = metadataEntityList.stream()
                    .map(MetadataEntity::getEntityId)
                    .collect(Collectors.toList());

            this.metadataEntityMap = metadataEntityList.stream()
                    .collect(Collectors.toMap(MetadataEntity::getEntityId, Function.identity()));
        }

    }

    public Map<Integer, MetadataEntity> getMetadataEntityMap() {
        return metadataEntityMap;
    }

}
