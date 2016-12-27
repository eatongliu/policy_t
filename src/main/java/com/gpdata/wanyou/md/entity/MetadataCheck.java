package com.gpdata.wanyou.md.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "metadata_check")
public class MetadataCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer checkid;

    private Integer metadataid;
    private String checkformula;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getCheckid() {
        return checkid;
    }

    public void setCheckid(Integer checkid) {
        this.checkid = checkid;
    }

    public Integer getMetadataid() {
        return metadataid;
    }

    public void setMetadataid(Integer metadataid) {
        this.metadataid = metadataid;
    }

    public String getCheckformula() {
        return checkformula;
    }

    public void setCheckformula(String checkformula) {
        this.checkformula = checkformula;
    }

    @Override
    public String toString() {
        return "MetadataCheck [checkid=" + checkid + ", metadataid="
                + metadataid + ", checkformula=" + checkformula + "]";
    }

}