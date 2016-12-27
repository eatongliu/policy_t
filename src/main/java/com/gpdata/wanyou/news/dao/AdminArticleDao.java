package com.gpdata.wanyou.news.dao;


import com.gpdata.wanyou.news.entity.Article;

import java.util.List;

public interface AdminArticleDao {

    /**
     * @param categoryType
     * @return
     */
    Integer getArticleCountByCategoryType(String categoryType);

    /**
     * @param categoryType
     * @param page
     * @param pageSize
     * @return
     */
    List<Article> getArticlePageDataListByCategoryType(String categoryType, Integer page, Integer pageSize);

    /**
     * @param categoryId
     * @return
     */
    Integer getArticleCountByCategoryId(Long categoryId);

    /**
     * @param categoryId
     * @param page
     * @param pageSize
     * @return
     */
    List<Article> getArticlePageDataListByCategoryId(Long categoryId, Integer page, Integer pageSize);

    /**
     * 得到对象
     * @param id
     * @return
     */
    Article getArticle(Long id);

    /**
     * 修改
     * @param article
     */
    void updateArticle(Article article);

    /**
     * 删除
     * @param article
     */
    void deleteArticle(Article article);



}
