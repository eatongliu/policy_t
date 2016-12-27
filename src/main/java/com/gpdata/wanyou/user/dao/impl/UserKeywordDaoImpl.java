package com.gpdata.wanyou.user.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.user.dao.UserKeywordDao;
import com.gpdata.wanyou.user.entity.UserKeyword;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by acer_liuyutong on 2016/12/14.
 */
@Repository
public class UserKeywordDaoImpl extends BaseDao implements UserKeywordDao{

    @Override
    public UserKeyword getKeyword(Long userId, String keyWord) {
        String hql = "from UserKeyword where keyWord = :keyWord ";
        if (userId == null){
            hql += "and userId is null ";
        }else{
            hql += "and userId = :userId";
        }
        Query query = getCurrentSession().createQuery(hql);
        if (userId != null){
            query.setLong("userId",userId);
        }
        return (UserKeyword)query.setString("keyWord",keyWord).uniqueResult();
    }

    @Override
    public void heatRise(UserKeyword word) {
        word.setHeat(word.getHeat() + 1);
        getCurrentSession().update(word);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getKeyWords(Long userId,int offset,int limit){
        String hql = "select keyWord from UserKeyword ";
        if (userId == null){
            hql += "where userId is null ";
        }else{
            hql += "where userId = :userId ";
        }
        hql += "order by heat desc ";

        Query query = getCurrentSession().createQuery(hql);
        if(userId != null){
            query.setLong("userId",userId);
        }

        return query.setFirstResult(offset)
                    .setMaxResults(limit)
                    .list();
    }
}
