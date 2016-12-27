package com.gpdata.wanyou.user.controller;

import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.user.entity.UserFavorite;
import com.gpdata.wanyou.user.service.UserFavoriteService;
import com.gpdata.wanyou.utils.SimpleBeanPropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 个人中心收藏管理
 * Created by guoxy on 2016/12/6.
 */
@Controller
@RequestMapping(value = "/user")
public class UserFavoriteController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserFavoriteController.class);
    @Autowired
    private UserFavoriteService favoriteService;


    /**
     * 2.1  收藏提交 /fa/a
     */
    @RequestMapping(value = "/fa/a", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult addUserFavorite(@RequestBody UserFavorite favorite, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
            User user = (User) request.getAttribute("currentUser");
            logger.debug("user: {}",user);
            Date date = new Date();
            favorite.setCreateDate(date);
            favorite.setUserId(user.getUserId());
            Integer sid = favoriteService.addUserFavorite(favorite);
            data.put("favorite", sid);
            result = BeanResult.success(data);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("收藏失败(￣▽￣)");
        }

        return result;
    }

    /**
     * 2.2  列表 /fa/ls/{offset}/{limit}
     */
    @RequestMapping(value = "/fa/ls/{offset}/{limit}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult listUserFavorite(@PathVariable Integer offset
                                    , @PathVariable Integer limit
                                    , @RequestParam(required = false) String pdName
                                    , HttpServletRequest request) {
        BeanResult result = null;
        try {
            User user = getCurrentUser(request);
            Long userId = user.getUserId();
            Map<String, Object> resultMap = favoriteService.searchUserFavorite(userId, null, pdName, offset, limit);
            result = BeanResult.success(resultMap);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("获取列表失败┗|｀O′|┛");
        }

        return result;
    }

    /**
     * 2.3  收藏查看 /fa/s/{faverId}
     */
    @RequestMapping(value = "/fa/s/{faverId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getUserFavorite(@PathVariable Integer faverId, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
            UserFavorite favorite = favoriteService.getUserFavorite(faverId);
            //TODO 查看收藏具体数据
            data.put("data", favorite);
            result = BeanResult.success(data);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("数据走丢了(;′⌒`)");
        }

        return result;
    }

    /**
     * 2.4  收藏修改 /fa/u/{sid}
     */
    @RequestMapping(value = "/fa/u/{faverId}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult updateUserFavorite(@RequestBody UserFavorite favorite, @PathVariable Integer faverId, HttpServletRequest request) {
        BeanResult result = null;
        try {
            User user = (User) request.getAttribute("currentUser");
            UserFavorite oldFavorite = favoriteService.getUserFavorite(faverId);
            if (oldFavorite == null || !oldFavorite.getUserId().equals(user.getUserId())) {
                result = BeanResult.error("异常 ： 修改错误,您没有收藏该文章");
            } else {
                /*里面的 copyNotnullProperites 方法， 可以将一个对象中的非空的值复制给另一个对象*/
                SimpleBeanPropertiesUtil.copyNotNullProperties(favorite, oldFavorite);
                favoriteService.updateUserFavorite(oldFavorite);
                result = BeanResult.success("SUCCESS");
            }

        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("出现异常，请联系管理员。");
        }

        return result;
    }

    /**
     * 2.5  收藏删除 /fa/d/{sid}
     */
    @RequestMapping(value = "/fa/d/{faverId}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult deleteUserFavorite(@PathVariable Integer faverId, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
            favoriteService.deleteUserFavorite(faverId);
            result = BeanResult.success("SUCCESS");
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("删除失败");
        }

        return result;
    }
}