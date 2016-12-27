package com.gpdata.wanyou.news.service;


import com.gpdata.wanyou.news.entity.Article;
import com.gpdata.wanyou.news.entity.ArticleCategory;
import com.gpdata.wanyou.utils.page.PageDataList;

import java.util.List;

/**
 * 文章和文章分类分类管理
 * @author chengchaos
 *
 */
public interface AdminArticleService {

    /**
     * 
     * @return
     */
    List<ArticleCategory> getHelperCategoryList();
    
    /**
     * 
     * @return
     */
    List<ArticleCategory> getNewsCategoryList();
    
    /**
     * 
     * @return
     */
    List<ArticleCategory> getExpertCategoryList();
    

    /**
     * 
     * @param articleType
     * @return
     */
    List<ArticleCategory> getArticleCateogrysByType(String articleType);

    /**
     * 取一个分类
     * @param articleCategoryId
     * @return
     */
    ArticleCategory getArticleCategoryById(Long articleCategoryId);

    /**
     * 存一个分类
     * @param articleCategory
     * @return
     */
    Long saveArticleCategory(ArticleCategory articleCategory);
    
    /**
     * 改一个分类
     * @param articleCategory
     */
    void updateArticleCategory(ArticleCategory articleCategory);

    /**
     * 删...还是不删?一个分类
     * @param articleCategoryId
     */
    void deleteArticleCategory(Long articleCategoryId);
    
    /**
     * 使用分类类型获取文章, 例如 "NEWS" 下面有 "媒体报道" 和 "行业新闻" 等等.
     * @param categoryType 分类类型(例如 "NEWS")
     * @param page
     * @return
     */
    PageDataList<Article> getArticlePageDataListByCategoryType(String categoryType, Integer page);
    
    /**
     * 使用分类 ID 获取文章, 例如 "公告" 下面的所有文章.
     * @param categoryId
     * @param page
     * @return
     */
    PageDataList<Article> getArticlePageDataListByCategoryId(Long categoryId, Integer page);
    
    /**
     * 
     * @param articleId
     * @return
     */
    Article getArticleById(Long articleId);
    
    /**
     * 
     * @param article
     */
    void updateArticle(Article article);
    
    /**
     * 
     * @param articleId
     */
    void deleteArticle(Long articleId);

    
    /**
     * 
     * @param article
     */
    void saveArticle(Article article);

}
