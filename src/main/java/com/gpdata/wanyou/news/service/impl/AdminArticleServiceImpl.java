package com.gpdata.wanyou.news.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.news.dao.AdminArticleCategoryDao;
import com.gpdata.wanyou.news.dao.AdminArticleDao;
import com.gpdata.wanyou.news.entity.Article;
import com.gpdata.wanyou.news.entity.ArticleCategory;
import com.gpdata.wanyou.news.service.AdminArticleService;
import com.gpdata.wanyou.utils.SimpleBeanPropertiesUtil;
import com.gpdata.wanyou.utils.page.PageDataList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class AdminArticleServiceImpl extends BaseService implements AdminArticleService {

    private static final Integer PAGE_SIZE = Integer.valueOf(10);


    private AdminArticleDao adminArticleDao;
    @Autowired
    private AdminArticleCategoryDao adminArticleCategoryDao;

    @Resource
    public void setAdminArticleDao(AdminArticleDao adminArticleDao) {
        this.adminArticleDao = adminArticleDao;
    }

    /*
     * (non-Javadoc)
     * @see com.gpdata.trade.admin.service.AdminArticleService#getHelperCategoryList()
     */
    @Override
    public List<ArticleCategory> getHelperCategoryList() {
        return this.getArticleCateogrysByType(ArticleCategory.ARTICLE_CATEGORY_TYPE_HELPER);
    }

    /*
     * (non-Javadoc)
     * @see com.gpdata.trade.admin.service.AdminArticleService#getNewsCategoryList()
     */
    @Override
    public List<ArticleCategory> getNewsCategoryList() {
        return this.getArticleCateogrysByType(ArticleCategory.ARTICLE_CATEGORY_TYPE_NEWS);
    }


    /* (non-Javadoc)
     * @see com.gpdata.trade.admin.service.AdminArticleService#getExpertCategoryList()
     */
    @Override
    public List<ArticleCategory> getExpertCategoryList() {
        return this.getArticleCateogrysByType(ArticleCategory.ARTICLE_CATEGORY_TYPE_EXPERT);
    }

    /*
     * (non-Javadoc)
     * @see com.gpdata.trade.admin.service.AdminArticleService#getArticleCateogrysByType(java.lang.String)
     */
    @Override
    public List<ArticleCategory> getArticleCateogrysByType(String articleType) {
        // return this.articleCategoryMapper.getArticleCategoryList(articleType);
        Map<String, Object> result = this.adminArticleCategoryDao.getArticleCategoryList(null, articleType, null, null, null);
        return (List<ArticleCategory>) result.get("rows");
    }

    /* (non-Javadoc)
     * @see com.gpdata.trade.admin.service.AdminArticleService#getArticleCategoryById(java.lang.Long)
     */
    @Override
    public ArticleCategory getArticleCategoryById(Long articleCategoryId) {
        //return articleCategoryMapper.getArticleCategoryById(articleCategoryId);
        return this.adminArticleCategoryDao.get(articleCategoryId);
    }


    /* (non-Javadoc)
     * @see com.gpdata.trade.admin.service.AdminArticleService#saveArticleCategory(com.gpdata.trade.domain.news.ArticleCategory)
     */
    @Override
    public Long saveArticleCategory(ArticleCategory articleCategory) {

        this.adminArticleCategoryDao.save(articleCategory);
        return articleCategory.getId();
    }


    /* (non-Javadoc)
     * @see com.gpdata.trade.admin.service.AdminArticleService#updateArticleCategory(com.gpdata.trade.domain.news.ArticleCategory)
     */
    @Override
    public void updateArticleCategory(ArticleCategory articleCategory) {
        ArticleCategory articleCategoryOld = adminArticleCategoryDao.get(articleCategory.getId());
        SimpleBeanPropertiesUtil.copyNotNullProperties(articleCategory, articleCategoryOld);
        adminArticleCategoryDao.update(articleCategoryOld);
    }

    /* (non-Javadoc)
     * @see com.gpdata.trade.admin.service.AdminArticleService#deleteArticleCategory(java.lang.Long)
     */
    @Override
    public void deleteArticleCategory(Long articleCategoryId) {
        this.adminArticleCategoryDao.delete(articleCategoryId);

    }

    /* (non-Javadoc)
     * @see com.gpdata.trade.admin.service.AdminArticleService#getArticlePageDataListByCategoryType(java.lang.String, java.lang.Integer)
     */
    @Override
    public PageDataList<Article> getArticlePageDataListByCategoryType(String categoryType, Integer page) {

        Integer count = this.adminArticleDao.getArticleCountByCategoryType(categoryType);
        List<Article> pList = this.adminArticleDao
                .getArticlePageDataListByCategoryType(categoryType, page, PAGE_SIZE);


        return new PageDataList<Article>(count.intValue(), PAGE_SIZE.intValue(), page.intValue(), pList);
    }

    /* (non-Javadoc)
     * @see com.gpdata.trade.admin.service.AdminArticleService#getArticlePageDataListByCategoryId(java.lang.Long, java.lang.Integer)
     */
    @Override
    public PageDataList<Article> getArticlePageDataListByCategoryId(Long categoryId, Integer page) {

        Integer count = this.adminArticleDao.getArticleCountByCategoryId(categoryId);
        List<Article> pList = this.adminArticleDao
                .getArticlePageDataListByCategoryId(categoryId, page, PAGE_SIZE);


        return new PageDataList<Article>(count.intValue(), PAGE_SIZE.intValue(), page.intValue(), pList);
    }

    /* (non-Javadoc)
     * @see com.gpdata.trade.admin.service.AdminArticleService#getArticleById(java.lang.Long)
     */
    @Override
    public Article getArticleById(Long articleId) {

        return this.adminArticleDao.getArticle(articleId);

    }

    /* (non-Javadoc)
     * @see com.gpdata.trade.admin.service.AdminArticleService#updateArticle(com.gpdata.trade.domain.news.Article)
     */
    @Override
    public void updateArticle(Article article) {
//        return this.adminArticleDao
//                .castToBaseDao()
//                .getPromisedEntityById(Article.class, articleId);
        Article articleOld = this.adminArticleDao.getArticle(article.getId());
        SimpleBeanPropertiesUtil.copyNotNullProperties(article, articleOld);
        this.adminArticleDao.updateArticle(articleOld);

    }

    /* (non-Javadoc)
     * @see com.gpdata.trade.admin.service.AdminArticleService#deleteArticle(java.lang.Long)
     */
    @Override
    public void deleteArticle(Long articleId) {
//        this.adminArticleDao.castToBaseDao().deletePromisedEntityById(Article.class, articleId);
        Article article = this.adminArticleDao.getArticle(articleId);
        this.adminArticleDao.deleteArticle(article);
    }


    @Override
    public void saveArticle(Article article) {

//        this.adminArticleDao.castToBaseDao().savePromisedEntity(article);
        this.saveArticle(article);
    }


}
