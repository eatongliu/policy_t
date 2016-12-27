package com.gpdata.wanyou.md.entity;

import org.hibernate.annotations.Formula;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by chengchao on 2016/10/25.
 * 元数据实体实体类
 */
@Entity
@Table(name = "metadata_entity")
public class MetadataEntity implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * 匹配状态
     * 0 == 完全不匹配
     * 10 == 部分匹配
     * 20 == 完全机器匹配
     * 30 == 人工匹配
     */
    public static final Integer NON_MATCH = 0;
    public static final Integer HALF_MATCH = 10;
    public static final Integer COMPUTER_MATCH = 20;
    public static final Integer MANUAL_MATCH = 30;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //元数据实体 id
    private Integer entityId;
    //元数据 id
    private Integer metadataId;
    //数据源 id
    private Integer sourceId;
    //字段 id
    private Integer fieldId;

    // 数据标准 id
    private Integer standId;

    //英文名称
    private String metadataEntityName;
    //中文名称
    private String caption;
    //创建时间
    private Date createDate;

    // 创建用户 id
    private String userId;

    //创建人
    @Formula(value="(select a.nickName from sys_user a where a.userId = userId)")
    private String creator;


    //源自标准数据
    private String isStand;
    //数据标准属性 id
    private Integer standEntityId;
    //datatype
    private String dataType;
    //数据长度
    private Long dataLength;
    //数据精度
    private Integer dataPrecision;
    //数据刻度
    private Integer dataScale;
    //是否允许空值
    private Integer allowNull;
    //是否唯一
    private String isUnique;
    //默认值
    private String defVal;
    //数据单位
    private String dataUnit;
    //标准数据转换公式
    private String transFormula;
    //数据校验公式
    private String checkFormula;
    //枚举范围
    private String enumVal;
    //取值范围
    private String dataScope;


    /**
     * 匹配状态
	 * 0 == 完全不匹配
	 * 10 == 部分匹配
	 * 20 == 完全机器匹配
	 * 30 == 人工匹配
     */
    private Integer matchStatus = NON_MATCH;


	public Integer getEntityId() {
		return entityId;
	}
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	public Integer getMetadataId() {
		return metadataId;
	}
	public void setMetadataId(Integer metadataId) {
		this.metadataId = metadataId;
	}
	public Integer getFieldId() {
		return fieldId;
	}
	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}
	public Integer getSourceId() {
		return sourceId;
	}
	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

    public Integer getStandId() {
        return standId;
    }

    public void setStandId(Integer standId) {
        this.standId = standId;
    }

    public String getMetadataEntityName() {
		return metadataEntityName;
	}
	public void setMetadataEntityName(String metadataEntityName) {
		this.metadataEntityName = metadataEntityName;
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
	public Integer getStandEntityId() {
		return standEntityId;
	}
	public void setStandEntityId(Integer standEntityId) {
		this.standEntityId = standEntityId;
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
	public Integer getAllowNull() {
		return allowNull;
	}
	public void setAllowNull(Integer allowNull) {
		this.allowNull = allowNull;
	}
	public String getIsUnique() {
		return isUnique;
	}
	public void setIsUnique(String isUnique) {
		this.isUnique = isUnique;
	}
	public String getDefVal() {
		return defVal;
	}
	public void setDefVal(String defVal) {
		this.defVal = defVal;
	}
	public String getDataUnit() {
		return dataUnit;
	}
	public void setDataUnit(String dataUnit) {
		this.dataUnit = dataUnit;
	}
	public String getTransFormula() {
		return transFormula;
	}
	public void setTransFormula(String transFormula) {
		this.transFormula = transFormula;
	}
	public String getCheckFormula() {
		return checkFormula;
	}
	public void setCheckFormula(String checkFormula) {
		this.checkFormula = checkFormula;
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
	public Integer getMatchStatus() {
		return matchStatus;
	}
	public void setMatchStatus(Integer matchStatus) {
		this.matchStatus = matchStatus;
	}

    @Override
    public String toString() {
        return "MetadataEntity{" +
                "entityId=" + entityId +
                ", metadataId=" + metadataId +
                ", sourceId=" + sourceId +
                ", fieldId=" + fieldId +
                ", standId=" + standId +
                ", metadataEntityName='" + metadataEntityName + '\'' +
                ", caption='" + caption + '\'' +
                ", createDate=" + createDate +
                ", creator='" + creator + '\'' +
                ", isStand='" + isStand + '\'' +
                ", standEntityId=" + standEntityId +
                ", dataType='" + dataType + '\'' +
                ", dataLength=" + dataLength +
                ", dataPrecision=" + dataPrecision +
                ", dataScale=" + dataScale +
                ", allowNull=" + allowNull +
                ", isUnique='" + isUnique + '\'' +
                ", defVal='" + defVal + '\'' +
                ", dataUnit='" + dataUnit + '\'' +
                ", transFormula='" + transFormula + '\'' +
                ", checkFormula='" + checkFormula + '\'' +
                ", enumVal='" + enumVal + '\'' +
                ", dataScope='" + dataScope + '\'' +
                ", matchStatus=" + matchStatus +
                '}';
    }
}