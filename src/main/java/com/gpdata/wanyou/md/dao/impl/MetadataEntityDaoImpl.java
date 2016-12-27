package com.gpdata.wanyou.md.dao.impl;

import java.util.List;
import java.util.Map;

import com.gpdata.wanyou.md.entity.StandSourceMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.md.dao.MetadataEntityDao;
import com.gpdata.wanyou.md.entity.MetadataEntity;

import javax.print.DocFlavor;

/**
 * Created by chengchao on 2016/10/28.
 */
@Repository
public class MetadataEntityDaoImpl extends BaseDao implements MetadataEntityDao {


    /**
     * 说明：基于数据映射表 id 过滤元数据实体
     * 参数1：ssmId (必填)
     * 参数2：caption 标题
     * 参数3：offset
     * 参数4：limit
     * 参数5：metadataEntityName 中文名称
     * 参数6：matchStatus 匹配状态
     *
     * @param params
     * @return
     */
    @Override
    public Pair<Integer, List<MetadataEntity>> getByConditions(Map<String, Object> params) {

        String ssmId = (String) params.get("ssmId");
        Session session = this.getCurrentSession();
        StandSourceMap standSourceMap = (StandSourceMap) session.get(StandSourceMap.class, ssmId);

        if (standSourceMap == null) {
            throw new RuntimeException("映射不存在!");
        }
        String caption = (String) params.get("caption");
        String metadataEntityName = (String) params.get("metadataEntityName");

        Integer matchStatus =  (Integer) params.get("matchStatus");

        Integer offset = (Integer) params.get("offset");
        Integer limit = (Integer)  params.get("limit");

        String sql = " from MetadataEntity a "
                + " , StandSourceMap b "
                + " where a.sourceId = b.sourceId "
                + " and   a.standId = b.standId "
                + " and   b.ssmId = :ssmId ";

        if (StringUtils.isNotBlank(caption)) {
            sql += " and a.caption like :caption ";
        }
        if (StringUtils.isNotBlank(metadataEntityName)) {
            sql += " and a.metadataEntityName like :metadataEntityName ";
        }
        if (matchStatus != null) {
            sql += " and a.matchStatus = :matchStatus ";
        }


        Query listQuery = session.createQuery("select a " + sql + " order by a.entityId asc ")
                .setFirstResult(offset)
                .setMaxResults(limit);
        Query totalQuery = session.createQuery("select count(a) "+ sql);

        listQuery.setString("ssmId", ssmId);
        totalQuery.setString("ssmId", ssmId);

        if (StringUtils.isNotBlank(caption)) {
            listQuery.setString("caption", "%" +caption+ "%");
            totalQuery.setString("caption", "%" +caption+ "%");
        }
        if (StringUtils.isNotBlank(metadataEntityName)) {
            listQuery.setString("metadataEntityName", "%" +metadataEntityName+ "%");
            totalQuery.setString("metadataEntityName", "%" +metadataEntityName+ "%");
        }
        if (matchStatus != null) {
            listQuery.setInteger("matchStatus", matchStatus);
            totalQuery.setInteger("matchStatus", matchStatus);
        }
        @SuppressWarnings("unchecked")
        List<MetadataEntity> metadataBeanList = listQuery.list();
        String total =  totalQuery.uniqueResult().toString();

        return Pair.of(Integer.valueOf(total), metadataBeanList);

    }


    public Pair<Integer, List<MetadataEntity>> getByConditions_old(Map<String, Object> params) {
		String ssmId = (String) params.get("ssmId");
		
		String caption = null;
		if(params.get("caption") != null){
		    caption = params.get("caption").toString();
		}
		String cName = null;
		if(params.get("cName") != null){
		    cName = params.get("cName").toString();
        }
		String matchStatus = null;
		if(params.get("matchStatus") != null){
			matchStatus = params.get("matchStatus").toString();
		}
		Integer offset = Integer.parseInt(params.get("offset").toString());
		Integer limit = Integer.parseInt(params.get("limit").toString());

		// 根据查询条件拼接SQL

		String sql = "SELECT * FROM metadata_entity me "
				+ "LEFT JOIN data_stand_entity de ON me.standEntityId = de.standentId "
				+ "LEFT JOIN stand_source_map sm ON de.standId = sm.standId "
				+ "WHERE sm.ssmId =:ssmId";
		String totalSql = "SELECT count(me.entityId) FROM metadata_entity me "
				+ "LEFT JOIN data_stand_entity de ON me.standEntityId = de.standentId "
				+ "LEFT JOIN stand_source_map sm ON de.standId = sm.standId "
				+ "WHERE sm.ssmId =:ssmId";
		if (StringUtils.isNotBlank(caption)) {
			sql += " and me.caption like :caption ";
			totalSql += " and me.caption like :caption ";
		}
		if (StringUtils.isNotBlank(cName)) {
			sql += " and me.cName like :cName ";
			totalSql += " and me.cName like :cName ";
		}
		if (StringUtils.isNotBlank(matchStatus)) {
			sql += " and me.matchStatus like :matchStatus ";
			totalSql += " and me.matchStatus like :matchStatus ";
		}

		SQLQuery query = (SQLQuery) getCurrentSession().createSQLQuery(sql).setFirstResult(offset).setMaxResults(limit);
		SQLQuery totalQuery = getCurrentSession().createSQLQuery(totalSql);
		
		query.setString("ssmId", ssmId);
		totalQuery.setString("ssmId", ssmId);

		if (StringUtils.isNotBlank(caption)) {
			query.setString("caption", "%" +caption+ "%");
			totalQuery.setString("caption", "%" +caption+ "%");
		}
		if (StringUtils.isNotBlank(cName)) {
			query.setString("cName", "%" +cName+ "%");
			totalQuery.setString("cName", "%" +cName+ "%");
		}
		if (StringUtils.isNotBlank(matchStatus)) {
			query.setString("matchStatus", "%" +matchStatus+ "%");
			totalQuery.setString("matchStatus", "%" +matchStatus+ "%");
		}
		@SuppressWarnings("unchecked")
		List<MetadataEntity> metadataBeanList = query.addEntity(MetadataEntity.class).list();
		
		String total =  totalQuery.uniqueResult().toString();
		return Pair.of(Integer.valueOf(total), metadataBeanList);
	}

    /**
     * 字段、表连表查询生成MetadataEntity
     */
	@Override
	public void generateMetadataEntities(StandSourceMap standSourceMap, String userId) {
        String sql = "INSERT INTO metadata_entity "
                + "(  fieldId, allowNull, caption, createDate, "
        		+ "   dataPrecision, metadataEntityName, dataLength, dataType, "
                + "   dataUnit, sourceId, userId, standId, "
                + "   isStand, matchStatus "
                + ") "
        		+ "SELECT "
                + "   fieldId, allowNull, df.caption, SYSDATE() createDate, "
                + "   decimalCount dataPrecision, fieldName metadataEntityName, length dataLength, type dataType, "
                + "   unit dataUnit, dt.resourceid sourceId, :userId, :standId , "
                + "   '否', 0 "
                + "FROM  datasource_field df "
                + "JOIN  datasource_table dt "
                + "ON    df.tableid = dt.tableid "
                + "WHERE dt.resourceId = :sourceId ";

        logger.debug("sql : {}", sql);

        Session session = this.getCurrentSession();

		Query query = session.createSQLQuery(sql);
		query.setParameter("userId", userId)
                .setInteger("sourceId", standSourceMap.getSourceId())
                .setInteger("standId", standSourceMap.getStandId())
                .executeUpdate();

        session.flush();

	}

	/*
	 *
	 */
    @Override

    public List<MetadataEntity> getMetadataEntitiesByMetadataId(Integer metadataId) {

        String hql = "from MetadataEntity a where a.metadataId = :metadataId "
                + " order by a.entityId desc ";

        Query query = this.getCurrentSession()
                .createQuery(hql)
                .setInteger("metadataId", metadataId);

        @java.lang.SuppressWarnings("unchecked")
        List<MetadataEntity> result = (List<MetadataEntity>) query.list();

        return result;
    }

	/**
	 * 删除元数据实体中和元数据的关系
	 * @param entityId
	 */
	@Override
	public void deleteEntityMatch(Integer entityId) {

        String hql = "update MetadataEntity a set metadataId = null where entityId = :entityId ";

        Session session = this.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setInteger("entityId", entityId);
        query.executeUpdate();
        session.flush();

	}

    /**
     * 添加元数据实体中和元数据的关系
     * @param metadataId
     * @param entityId
     */
	@Override
	public void addEntityMatch(Integer metadataId, Integer entityId) {
        Session session = this.getCurrentSession();
        MetadataEntity metadataEntity = (MetadataEntity) session.get(MetadataEntity.class, entityId);

        if (metadataEntity != null) {
            metadataEntity.setMetadataId(metadataId);
            session.update(metadataEntity);
            session.flush();
        }

	}

    /**
     *
     * @param tableId
     * @return
     */
    @Override
    public List<MetadataEntity> getMetadataEntityListByTableId(Integer tableId) {

        String hql = " select a from MetadataEntity a, DataSourceField b"
                + " where a.fieldId = b.fieldId "
                + " and b.tableId = :tableId "
                + " order by a.entityId asc ";

        @SuppressWarnings("unchecked")
        List<MetadataEntity> result = this.getCurrentSession()
                .createQuery(hql)
                .setInteger("tableId", tableId)
                .list();

        return result;
    }
}
