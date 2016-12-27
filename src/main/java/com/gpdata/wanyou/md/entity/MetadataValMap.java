package com.gpdata.wanyou.md.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by chengchao on 2016/10/25.
 */
@Entity
@Table(name = "metadata_val_map")
public class MetadataValMap implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 元数据实体值映射id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer mvmId;

    /**
     * 元数据 id
     */
    private Integer metadataId;

    /**
     * 元数据实体 id
     */
    private Integer metaentityId;

    /**
     * 值映射规则
     */
    private String valRuler;

    /**
     * 值映射公式
     */
    private String valFormula;

    public Integer getMvmId() {
        return mvmId;
    }

    public void setMvmId(Integer mvmId) {
        this.mvmId = mvmId;
    }

    public Integer getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(Integer metadataId) {
        this.metadataId = metadataId;
    }

    public Integer getMetaentityId() {
        return metaentityId;
    }

    public void setMetaentityId(Integer metaentityId) {
        this.metaentityId = metaentityId;
    }

    public String getValRuler() {
        return valRuler;
    }

    public void setValRuler(String valRuler) {
        this.valRuler = valRuler;
    }

    public String getValFormula() {
        return valFormula;
    }

    public void setValFormula(String valFormula) {
        this.valFormula = valFormula;
    }

    @Override
    public String toString() {
        return "MetadataValMap [mvmId=" + mvmId + ", metadataId=" + metadataId + ", metaentityId=" + metaentityId
                + ", valRuler=" + valRuler + ", valFormula=" + valFormula + "]";
    }

}
