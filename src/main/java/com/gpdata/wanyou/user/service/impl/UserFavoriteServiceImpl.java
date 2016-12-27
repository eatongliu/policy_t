package com.gpdata.wanyou.user.service.impl;

import com.gpdata.wanyou.user.dao.UserFavoriteDao;
import com.gpdata.wanyou.user.entity.UserFavorite;
import com.gpdata.wanyou.user.service.UserFavoriteService;
import com.gpdata.wanyou.utils.HtmlParseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class UserFavoriteServiceImpl implements UserFavoriteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserFavoriteServiceImpl.class);
    @Autowired
    private UserFavoriteDao userFavoriteDao;

    @Override
    public UserFavorite getUserFavorite(Integer aid) {
        return userFavoriteDao.getUserFavorite(aid);
    }

    @Override
    public Integer addUserFavorite(UserFavorite favorite) {
        /*防止恶意注入*/
        favorite = HtmlParseUtils.htmlParseCode(favorite, UserFavorite.class);
        return userFavoriteDao.addUserFavorite(favorite);
    }

    @Override
    public void updateUserFavorite(UserFavorite favorite) {
        /*防止恶意注入*/
        favorite = HtmlParseUtils.htmlParseCode(favorite, UserFavorite.class);
        userFavoriteDao.updateUserFavorite(favorite);
    }

    @Override
    public void deleteUserFavorite(Integer aid) {
        userFavoriteDao.deleteUserFavorite(aid);
    }

    /**
     * @param userId     用户id
     * @param favorId      收藏id
     * @param pdName 关键词
     * @param offset  起始
     * @param limit   偏移量
     * @return Map<String ,Object>  key--> rows  total
     */
    @Override
    public Map<String, Object> searchUserFavorite(Long userId, Integer favorId, String pdName, Integer offset, Integer limit) {
        return userFavoriteDao.searchUserFavorite(userId, favorId, pdName, offset, limit);
    }
}
