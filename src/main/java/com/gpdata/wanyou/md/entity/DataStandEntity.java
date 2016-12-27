package com.gpdata.wanyou.md.entity;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 数据标准实体实体类
 * <p>
 * Created by ligang on 16-10-25.
 */


@Entity
@Table(name = "data_stand_entity")
public class DataStandEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //数据标准实体 id
    private Integer standentId;
    //数据标准id
    private Integer standId;
    //中文名称
    private String standentName;
    //英文名称
    private String standentCaption;
    //数据类型
    private String dataType;
    //数据长度
    private Long dataLength;
    //数据精度
    private Integer dataPrecision;
    //数据刻度
    private Integer dataScale;
    //是否唯一j
    private String isUnique;
    //是否允许空值
    private String allowNull;
    //默认值
    private String defVal;
    //枚举数值
    private String enumVal;
    //数据范围
    private String dataScope;
    //校验公式
    private String checkFormula;
    //数据单位
    private String dataUnit;


    public Integer getStandentId() {
        return standentId;
    }

    public void setStandentId(Integer standentId) {
        this.standentId = standentId;
    }

    public String getStandentName() {
        return standentName;
    }

    public void setStandentName(String standentName) {
        this.standentName = standentName;
    }

    public Integer getStandId() {
        return standId;
    }

    public void setStandId(Integer standId) {
        this.standId = standId;
    }

    public String getStandentCaption() {
        return standentCaption;
    }

    public void setStandentCaption(String standentCaption) {
        this.standentCaption = standentCaption;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Long getDataLength() {
        return dataLength;
    }

    public void setDataLength(Long dataLength) {
        this.dataLength = dataLength;
    }

    public Integer getDataPrecision() {
        return dataPrecision;
    }

    public void setDataPrecision(Integer dataPrecision) {
        this.dataPrecision = dataPrecision;
    }

    public Integer getDataScale() {
        return dataScale;
    }

    public void setDataScale(Integer dataScale) {
        this.dataScale = dataScale;
    }

    public String getIsUnique() {
        return isUnique;
    }

    public void setIsUnique(String isUnique) {
        this.isUnique = isUnique;
    }

    public String getAllowNull() {
        return allowNull;
    }

    public void setAllowNull(String allowNull) {
        this.allowNull = allowNull;
    }

    public String getDefVal() {
        return defVal;
    }

    public void setDefVal(String defVal) {
        this.defVal = defVal;
    }

    public String getEnumVal() {
        return enumVal;
    }

    public void setEnumVal(String enumVal) {
        this.enumVal = enumVal;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public String getCheckFormula() {
        return checkFormula;
    }

    public void setCheckFormula(String checkFormula) {
        this.checkFormula = checkFormula;
    }

    public String getDataUnit() {
        return dataUnit;
    }

    public void setDataUnit(String dataUnit) {
        this.dataUnit = dataUnit;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
