package com.gpdata.wanyou.md.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.md.dao.MetadataBeanDao;
import com.gpdata.wanyou.md.entity.MetadataBean;
import com.gpdata.wanyou.md.entity.MetadataEntity;
import com.gpdata.wanyou.utils.SimpleBeanPropertiesUtil;

/**
 * Created by chengchao on 2016/10/28.
 */
@Repository
public class MetadataBeanDaoImpl extends BaseDao implements MetadataBeanDao {

    /**
     * 说明：基于本体 id 过滤元数据
     * 参数1：ontologyId本体 id（必填）
     * 参数2：caption标题
     * 参数3：offset
     * 参数4：limit
     *
     * @param params
     * @return
     */
	@Override
	public Pair<Integer, List<MetadataBean>> getByConditions(Map<String, Object> params) {

		Integer ontologyId = (Integer)params.get("ontologyId");
		String caption = (String) params.get("caption");
        String metadataName = (String) params.get("metadataName");
		Integer offset = (Integer) params.get("offset");
        Integer limit = (Integer) params.get("limit");

		//根据查询条件拼接SQL
		/*
		String sql = "select * from metadata_entity me "
				+ "LEFT JOIN metadata_bean mb "
				+ "ON me.metadataId = mb.metadataId "
				+ "WHERE mb.ontologyid =:ontologyid";
		String totalSql = "select count(me.entityId) from metadata_entity me "
				+ "LEFT JOIN metadata_bean mb "
				+ "ON me.metadataId = mb.metadataId "
				+ "WHERE mb.ontologyid =:ontologyid";
				*/

		String sql = " from MetadataBean a where a.metadataId > 0 ";

		if (ontologyId != null) {
			sql +=" and a.ontologyId = :ontologyId ";
		}
		if(StringUtils.isNotBlank(caption)){
			sql +=" and a.caption like :caption ";
		}
        if(StringUtils.isNotBlank(metadataName)){
            sql +=" and a.metadataName like :metadataName ";
        }

        Session session = this.getCurrentSession();
		Query query = session.createQuery(sql + " order by a. metadataId desc")
                .setFirstResult(offset)
                .setMaxResults(limit);
		Query totalQuery = session.createQuery("select count(a) " + sql);

		if (ontologyId != null) {
			query.setInteger("ontologyId", ontologyId);
			totalQuery.setInteger("ontologyId", ontologyId);
		}
		if (StringUtils.isNotBlank(caption)) {
			query.setString("caption", "%" + caption + "%");
			totalQuery.setString("caption", "%" + caption + "%");
		}
        if (StringUtils.isNotBlank(metadataName)) {
            query.setString("metadataName", "%" + metadataName + "%");
            totalQuery.setString("metadataName", "%" + metadataName + "%");
        }
		@SuppressWarnings("unchecked")
		List<MetadataBean> metadataBeanList = query.list();
		String total =  totalQuery.uniqueResult().toString();

		return Pair.of(Integer.valueOf(total), metadataBeanList);
	}


    @Override
    public void updateMetadataEntities(Integer metadataId, Set<MetadataEntity> MetadataEntitySet) {

        Session session = this.getCurrentSession();

        for (MetadataEntity metadataEntity : MetadataEntitySet) {
            metadataEntity.setMetadataId(metadataId);
            MetadataEntity exists = (MetadataEntity) session.get(MetadataEntity.class, metadataEntity.getEntityId());
            if (exists != null) {
                SimpleBeanPropertiesUtil.copyNotNullProperties(metadataEntity, exists);
                session.update(exists);
            }
        }


    }

    /**
     * 元数据实体、标准实体连表查询并进行比对生成元数据列表    
     */
	@Override
    @SuppressWarnings("unchecked")
	public List<MetadataBean> generateMetadataBeanList(Integer standId, Integer sourceId) {
		String sql = "SELECT me.metadataEntityName metadataName,me.caption caption,me.entityId standMetadataEntityId "
				+ "FROM metadata_entity me "
				+ "INNER JOIN data_stand_entity de "
				+ "ON me.metadataEntityName = de.standentName "
				+ "AND me.caption = de.standentCaption "
				+ "AND (me.dataType = de.dataType OR(me.dataType is NULL AND de.dataType is NULL)) "
				+ "AND (me.dataLength = de.dataLength OR(me.dataLength is NULL AND de.dataLength is NULL)) "
				+ "AND (me.dataPrecision = de.dataPrecision OR(me.dataPrecision is NULL AND de.dataPrecision is NULL)) "
				+ "AND (me.dataUnit = de.dataUnit OR(me.dataUnit is NULL AND de.dataUnit is NULL)) "
				+ "WHERE de.standId = :standId AND me.sourceId = :sourceId ";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		ResultTransformer resultTransformer = Transformers.aliasToBean(MetadataBean.class);
        List<MetadataBean> list = query.setResultTransformer(resultTransformer)
                    .setParameter("standId", standId)
                    .setParameter("sourceId", sourceId).list();
		return list;
	}

	/**
	 * 保存并执行flush操作
	 */
	@Override
	public Integer saveMetadataBean(MetadataBean metadataBean){
		Session session = this.getCurrentSession();
        Integer id = (Integer) session.save(metadataBean);
        session.flush();	
        return id;
	}

    /**
     * 使用本体 id 获取与之相关联的元数据
     * @param ontologyId
     * @return
     */
	@Override
    @SuppressWarnings("unchecked")
	public List<MetadataBean> getMetadataBeansByOntologyId(Integer ontologyId) {

        String hql = " from MetadataBean a where a.ontologyId = :ontologyId order by a.metadataId ";

        return this.getCurrentSession()
                .createQuery(hql)
                .setInteger("ontologyId", ontologyId)
                .list();

	}

    /**
     * 删除元数据中关联本体的关系数据
     * @param metadataId
     */
	@Override
	public void deleteBeanMatch(Integer metadataId) {

        String hql = " update MetadataBean set ontologyId = null where metadataId = :metadataId ";
        this.getCurrentSession().createQuery(hql)
                .setInteger("metadataId", metadataId)
                .executeUpdate();

	}

    /**
     * 添加元数据中本体的关联数据
     * @param ontologyId
     * @param metadataId
     */
	@Override
	public void addBeanMatch(Integer ontologyId, Integer metadataId) {

        String hql = " update MetadataBean set ontologyId = :ontologyId where metadataId = :metadataId ";
        this.getCurrentSession().createQuery(hql)
                .setInteger("ontologyId", ontologyId)
                .setInteger("metadataId", metadataId)
                .executeUpdate();
	}
}
