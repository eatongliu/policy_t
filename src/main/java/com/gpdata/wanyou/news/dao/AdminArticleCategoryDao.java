package com.gpdata.wanyou.news.dao;

import com.gpdata.wanyou.news.entity.ArticleCategory;

import java.util.Map;

/**
 * Created by guoxy on 2016/12/7.
 */
public interface AdminArticleCategoryDao {
    /**
     * 得到对象
     *
     * @param articleCategoryId
     * @return
     */
    public ArticleCategory get(Long articleCategoryId);

    /**
     * 新建
     *
     * @param articleCategory
     */
    void save(ArticleCategory articleCategory);

    /**
     * 删除
     *
     * @param articleCategoryId
     */
    void delete(Long articleCategoryId);

    /**
     * 修改
     *
     * @param articleCategory
     */
    void update(ArticleCategory articleCategory);

    /**
     * params Map<String ,Object>    key==>id , category_type , parent_id , offset , limit
     *
     * @return Map<String ,Object>  key--> rows  total
     */
    Map<String, Object> getArticleCategoryList(Long id, String category_type, Long parent_id, Integer offset, Integer limit);
}
