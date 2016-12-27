package com.gpdata.wanyou.user.dao;


import com.gpdata.wanyou.user.entity.UserFavorite;

import java.util.Map;

/**
 * 收藏
 * Created by guoxy on 2016/12/5.
 */
public interface UserFavoriteDao {
    UserFavorite getUserFavorite(Integer favorId);

    Integer addUserFavorite(UserFavorite favorite);

    void updateUserFavorite(UserFavorite favorite);

    void deleteUserFavorite(Integer favorId);

    /**
     * @param userId     用户id
     * @param favorId      收藏id
     * @param pdName 关键词
     * @param offset  起始
     * @param limit   偏移量
     * @return Map<String ,Object>  key--> rows  total
     */
    Map<String, Object> searchUserFavorite(Long userId, Integer favorId, String pdName, Integer offset, Integer limit);
}
