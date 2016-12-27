package com.gpdata.wanyou.news.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.news.dao.AdminArticleCategoryDao;
import com.gpdata.wanyou.news.entity.ArticleCategory;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guoxy on 2016/12/7.
 */
@Repository
public class AdminArticleCategoryDaoImpl extends BaseDao implements AdminArticleCategoryDao {
    @Override
    public ArticleCategory get(Long articleCategoryId) {
        ArticleCategory articleCategory = (ArticleCategory) this.getCurrentSession().get(ArticleCategory.class, articleCategoryId);
        return articleCategory;
    }

    /**
     * 新建
     *
     * @param articleCategory
     */
    @Override
    public void save(ArticleCategory articleCategory) {
        this.getCurrentSession().save(articleCategory);
    }

    /**
     * 删除
     *
     * @param articleCategoryId
     */
    @Override
    public void delete(Long articleCategoryId) {
        ArticleCategory articleCategory = get(articleCategoryId);
        this.getCurrentSession().delete(articleCategory);
    }

    /**
     * 修改
     *
     * @param articleCategory
     */
    @Override
    public void update(ArticleCategory articleCategory) {
        this.getCurrentSession().merge(articleCategory);
    }

    /**
     * params Map<String ,Object>    key==>id , category_type , parent_id , offset , limit
     *
     * @return Map<String ,Object>  key--> rows  total
     */
    @Override
    public Map<String, Object> getArticleCategoryList(Long id, String category_type, Long parent_id, Integer offset, Integer limit) {
        String hql = "from ArticleCategory where 1=1";
        String hqlTotal = "SELECT count(*) from ArticleCategory where 1=1";
        if (id != null) {
            hql += " and id =:id";
            hqlTotal += " and id =:id";
        }
        if (category_type != null) {
            hql += " and category_type =:category_type";
            hqlTotal += " and category_type =:category_type";
        }
        if (parent_id != null) {
            hql += " and parent_id  = :parent_id";
            hqlTotal += " and parent_id  = :parent_id";
        }
        Query query = getCurrentSession().createQuery(hql);
        Query totalQuery = getCurrentSession().createQuery(hqlTotal);
        if (id != null) {
            query.setLong("id", id);
            totalQuery.setLong("id", id);
        }
        if (category_type != null) {
            query.setString("category_type", category_type);
            totalQuery.setString("category_type", category_type);
        }
        if (parent_id != null) {
            query.setLong("parent_id", parent_id);
            totalQuery.setLong("parent_id", parent_id);
        }
        List<ArticleCategory> subscriberList = null;
        if (offset != null && limit != null) {
            subscriberList = query
                    .setFirstResult(offset)
                    .setMaxResults(limit).list();
        } else {
            subscriberList = query.list();
        }
        String totalResult = totalQuery.uniqueResult().toString();
        Integer total = Integer.valueOf(totalResult, 10);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", subscriberList);
        result.put("total", total);
        return result;
    }
}
