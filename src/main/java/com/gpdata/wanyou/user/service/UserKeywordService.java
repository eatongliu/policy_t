package com.gpdata.wanyou.user.service;

import com.gpdata.wanyou.system.entity.User;

import java.util.List;

public interface UserKeywordService {

    /**
     * 添加关键词
     * 分有用户登录和无用户登录两种情况
     */
    public void appendKeywords(User user, List<String> keyWords);

    /**
     * 获取当前用户下的关键字列表，并按热度排序
     * @param userId
     */
    public List<String> getKeyWords(Long userId);

}
