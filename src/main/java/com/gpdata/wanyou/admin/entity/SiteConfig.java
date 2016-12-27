package com.gpdata.wanyou.admin.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ligang on 2016/12/10.
 * <p>
 * 站点配置
 */
@Entity
@Table(name = "site_config")
public class SiteConfig {

    /**
     * 站点配置编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer scID;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 联系电话
     */
    private String telephone;
    /**
     * 联系地址
     */
    private String address;
    /**
     * 网站备案号
     */
    private String icp;
    /**
     * SEO关键字
     */
    private String seoKeyword;
    /**
     * SEO描述
     */
    private String seoDescription;
    /**
     * 二维码图片地址
     */
    private String qrCode;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 创建人id
     */
    private String creatorId;
    /**
     * 修改日期
     */
    private Date reviseDate;

    public Integer getScID() {
        return scID;
    }

    public void setScID(Integer scID) {
        this.scID = scID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIcp() {
        return icp;
    }

    public String getSeoKeyword() {
        return seoKeyword;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public String getQrCode() {
        return qrCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Date getReviseDate() {
        return reviseDate;
    }

    public void setReviseDate(Date reviseDate) {
        this.reviseDate = reviseDate;
    }

    @Override
    public String toString() {
        return "SiteConfig{" +
                "scID=" + scID +
                ", companyName='" + companyName + '\'' +
                ", telephone='" + telephone + '\'' +
                ", address='" + address + '\'' +
                ", icp='" + icp + '\'' +
                ", seoKeyword='" + seoKeyword + '\'' +
                ", seoDescription='" + seoDescription + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", createDate=" + createDate +
                ", creatorId='" + creatorId + '\'' +
                ", reviseDate=" + reviseDate +
                '}';
    }
}
