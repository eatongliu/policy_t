package com.gpdata.wanyou.user.dao;

import com.gpdata.wanyou.user.entity.UserKeyword;

import java.util.List;

/**
 * Created by acer_liuyutong on 2016/12/14.
 */
public interface UserKeywordDao {
    /**
     * 查找关键词
     */
    UserKeyword getKeyword(Long userId, String keyWord);

    /**
     * 关键词的热度升高
     */
    void heatRise(UserKeyword word);

    /**
     * 获取用户的搜索热词20个，如果不够20个，从所有用户的搜索热词中补全
     */
    List<String> getKeyWords(Long userId,int offset,int limit);
}
