package com.gpdata.wanyou.user.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.user.dao.UserKeywordDao;
import com.gpdata.wanyou.user.entity.UserKeyword;
import com.gpdata.wanyou.user.service.UserKeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by acer_liuyutong on 2016/12/14.
 */
@Service
public class UserKeywordServiceImpl extends BaseService implements UserKeywordService{
    @Autowired
    private UserKeywordDao userKeywordDao;

    @Override
    public void appendKeywords(User user, List<String> keyWords){
        //没有登陆   或者   登陆
        Long userId = null;
        if (user != null){
            userId = user.getUserId();
        }
        for (String keyWord : keyWords) {
            //检查关键词是否存在
            UserKeyword word = userKeywordDao.getKeyword(userId,keyWord);
            if (word != null){
                userKeywordDao.heatRise(word);
            }else {
                UserKeyword newWord = new UserKeyword(userId,keyWord);
                simpleDao.save(newWord);
            }
        }
    }

    @Override
    public List<String> getKeyWords(Long userId){
        int count = 20;
        List<String> keyWords = userKeywordDao.getKeyWords(userId,0,count);
        if(userId == null){
            return keyWords;
        }

        int t  = 0;//上次的差值
        //查出20个搜索热词，不够20个，查找所有用户的搜索热词补全
        for (int i = 0;keyWords.size() < count;i++) {
            int differ = count - keyWords.size();//所需热词的个数

            //用Collections的frequency方法去重
            List<String> sources = userKeywordDao.getKeyWords(null, t * i, differ);
            t = differ;
            for (String s:sources) {
                if (Collections.frequency(keyWords,s) < 1) {
                    keyWords.add(s);
                }
                if (keyWords.size() > count){
                    break;
                }
            }
        }
        return keyWords;
    }
}
