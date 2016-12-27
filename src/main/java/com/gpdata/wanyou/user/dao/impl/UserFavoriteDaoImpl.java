package com.gpdata.wanyou.user.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.user.dao.UserFavoriteDao;
import com.gpdata.wanyou.user.entity.UserFavorite;
import com.gpdata.wanyou.user.entity.UserFavoriteDto;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收藏功能
 * Created by guoxy on 2016/12/5.
 */
@Repository
public class UserFavoriteDaoImpl extends BaseDao implements UserFavoriteDao {
    @Override
    public UserFavorite getUserFavorite(Integer favorId) {
        return (UserFavorite) this.getCurrentSession().get(UserFavorite.class, favorId);
    }

    @Override
    public Integer addUserFavorite(UserFavorite favorite) {
        this.getCurrentSession().save(favorite);
        return favorite.getFavorId();
    }

    @Override
    public void updateUserFavorite(UserFavorite favorite) {
        this.getCurrentSession().merge(favorite);

    }

    @Override
    public void deleteUserFavorite(Integer favorId) {
        UserFavorite oldFavorite = getUserFavorite(favorId);
        this.getCurrentSession().delete(oldFavorite);
    }

    /**
     * @param userId     用户id
     * @param favorId      收藏id
     * @param pdName 关键词
     * @param offset  起始
     * @param limit   偏移量
     * @return Map<String , Object>  key--> rows  total
     */
    @Override
    public Map<String, Object> searchUserFavorite(Long userId, Integer favorId, String pdName, Integer offset, Integer limit) {
        String sql = "SELECT a.favorId,a.pdId,a.createDate," +
                        "CASE b.index_ " +
                        "WHEN \"zcfg\" THEN \"政策法规\" " +
                        "WHEN \"zcjd\" THEN \"政策解读\" " +
                        "WHEN \"zcwj\" THEN \"政策文件\" " +
                        "WHEN \"jyta\" THEN \"建议提案\" " +
                        "ELSE \"其他\" "+
                        "END AS `index_`, " +
                    "b.pdName,b.topicClassify " +
                    "FROM user_favorite a " +
                    "LEFT JOIN policy_index b " +
                    "ON a.pdId = b.pdId where 1 = 1 ";
        String sqlTotal = "SELECT count(*) "+
                        "FROM user_favorite a " +
                        "LEFT JOIN policy_index b " +
                        "ON a.pdId = b.pdId where 1 = 1 ";
        if (userId != null) {
            sql += " and a.userId =:userId ";
            sqlTotal += " and a.userId =:userId ";
        }
        if (favorId != null) {
            sql += " and a.favorId =:favorId ";
            sqlTotal += " and a.favorId =:favorId ";
        }
        if (StringUtils.isNotBlank(pdName)) {
            sql += " and b.pdName  like :pdName ";
            sqlTotal += " and b.pdName  like :pdName ";
        }
        sql += " order by createDate ";
        Query query = getCurrentSession().createSQLQuery(sql);
        Query totalQuery = getCurrentSession().createSQLQuery(sqlTotal);
        if (userId != null) {
            query.setLong("userId", userId);
            totalQuery.setLong("userId", userId);
        }
        if (favorId != null) {
            query.setInteger("favorId", favorId);
            totalQuery.setInteger("favorId", favorId);
        }
        if (StringUtils.isNotBlank(pdName)) {
            query.setString("pdName", "%" + pdName + "%");
            totalQuery.setString("pdName", "%" + pdName + "%");
        }

        query.setResultTransformer(Transformers.aliasToBean(UserFavoriteDto.class));
        List<UserFavoriteDto> favoriteList = null;
        if (offset != null && limit != null) {
            favoriteList = query
                    .setFirstResult(offset)
                    .setMaxResults(limit).list();
        } else {
            favoriteList = query.list();
        }
        String totalResult = totalQuery.uniqueResult().toString();
        Integer total = Integer.valueOf(totalResult, 10);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", favoriteList);
        result.put("total", total);
        return result;
    }
}
