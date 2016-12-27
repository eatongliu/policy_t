package com.gpdata.wanyou.news.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.news.dao.AdminArticleDao;
import com.gpdata.wanyou.news.entity.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chengchaos
 */
@Repository
public class AdminArticleDaoImpl extends BaseDao implements AdminArticleDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminArticleDaoImpl.class);

    @Override
    public Integer getArticleCountByCategoryType(String categoryType) {

        StringBuilder sqlbuff = new StringBuilder();
        sqlbuff.append("SELECT count(a.id) FROM article a, article_category b \n");
        sqlbuff.append("WHERE a.category_id = b.id \n");
        sqlbuff.append("AND b.category_type = :categoryType \n");
        Object o = this.getCurrentSession().createSQLQuery(sqlbuff.toString()).setString("categoryType", categoryType).uniqueResult();
//        MapSqlParameterSource paramSource = new MapSqlParameterSource("categoryType", categoryType);
//        Integer result = this.namedParameterJdbcTemplate
//                .queryForObject(sqlbuff.toString(), paramSource, Integer.class);
        Integer result = Integer.valueOf(o.toString(), 10);
        return result;
    }

    @Override
    public List<Article> getArticlePageDataListByCategoryType(String categoryType, Integer page, Integer pageSize) {

        int begin = (page - 1) * pageSize;

        StringBuilder sqlbuff = new StringBuilder();
        sqlbuff.append("SELECT a.* FROM article a, article_category b \n");
        sqlbuff.append("WHERE a.category_id = b.id \n");
        sqlbuff.append("AND b.category_type = :categoryType \n");
        sqlbuff.append("ORDER BY a.id DESC \n");
        sqlbuff.append("LIMIT ").append(begin).append(", ").append(pageSize);
        String sql = sqlbuff.toString();

        List<Article> result = this.getCurrentSession().createSQLQuery(sqlbuff.toString()).setString("categoryType", categoryType).list();

        LOGGER.debug("sql ==> {}", sql);
        LOGGER.debug("result ==> {}", result);
//        MapSqlParameterSource paramSource = new MapSqlParameterSource("categoryType", categoryType);
//        BeanPropertyRowMapper<Article> rowMapper = BeanPropertyRowMapper.newInstance(Article.class);


//        List<Article> result = this.namedParameterJdbcTemplate
//                .query(sql, paramSource, rowMapper);

        return result;
    }

    @Override
    public Integer getArticleCountByCategoryId(Long categoryId) {

        StringBuilder sqlbuff = new StringBuilder();
        sqlbuff.append("SELECT count(a.id) FROM article a\n");
        sqlbuff.append("WHERE a.category_id = :categoryId \n");


        Object o = this.getCurrentSession().createSQLQuery(sqlbuff.toString()).setLong("categoryId", categoryId).uniqueResult();
        Integer result = Integer.valueOf(o.toString(), 10);
//        MapSqlParameterSource paramSource = new MapSqlParameterSource("categoryId", categoryId);
//        Integer result = this.namedParameterJdbcTemplate
//                .queryForObject(sqlbuff.toString(), paramSource, Integer.class);

        return result;
    }

    @Override
    public List<Article> getArticlePageDataListByCategoryId(Long categoryId, Integer page, Integer pageSize) {

        int begin = (page - 1) * pageSize;

        StringBuilder sqlbuff = new StringBuilder();
        sqlbuff.append("SELECT a.* FROM article a\n");
        sqlbuff.append("WHERE a.category_id = :categoryId \n");
        sqlbuff.append("ORDER BY a.id DESC \n");
        sqlbuff.append("LIMIT ").append(begin).append(", ").append(pageSize);
        String sql = sqlbuff.toString();

        List<Article> result = this.getCurrentSession().createSQLQuery(sqlbuff.toString()).setLong("categoryId", categoryId).list();
//        MapSqlParameterSource paramSource = new MapSqlParameterSource("categoryId", categoryId);
//        BeanPropertyRowMapper<Article> rowMapper = BeanPropertyRowMapper.newInstance(Article.class);

        LOGGER.debug("sql ==> {}", sql);
//        LOGGER.debug("paramSource ==> {}", paramSource);
//
//        List<Article> result = this.namedParameterJdbcTemplate
//                .query(sql, paramSource, rowMapper);

        return result;
    }

    /**
     * 得到对象
     *
     * @param id
     * @return
     */
    @Override
    public Article getArticle(Long id) {
        return (Article) this.getCurrentSession().get(Article.class, id);
    }

    /**
     * 修改
     *
     * @param article
     */
    @Override
    public void updateArticle(Article article) {
        this.getCurrentSession().merge(article);
    }

    /**
     * 删除
     *
     * @param article
     */
    @Override
    public void deleteArticle(Article article) {
        this.getCurrentSession().delete(article);
    }
}
