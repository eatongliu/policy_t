package com.gpdata.wanyou.admin.controller.user;

import com.gpdata.wanyou.base.controller.AdminBaseController;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.user.service.UserFavoriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员管理用户收藏
 * Created by guoxy on 2016/12/10.
 */
@Controller
@RequestMapping("/admin")
public class AdminFavoriteController extends AdminBaseController {
    private static final Logger logger = LoggerFactory.getLogger(AdminFavoriteController.class);
    @Autowired
    private UserFavoriteService favoriteService;

    /**
     * 管理收藏列表
     */
    @RequestMapping(value = "/fa/ls/{offset}/{limit}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getAdminUserFavoriteList(@PathVariable Integer offset, @PathVariable Integer limit, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
            /*TODO 获取用户手机号和用户id  收藏ID*/
            String mobile = "";
            Long uid = null;
            Integer fid = null;
            String keyword = null;
            /*key--> rows  total*/
            Map<String, Object> resultMap = favoriteService.searchUserFavorite(uid, fid, keyword, offset, limit);
            data.put("data", resultMap);
            result = BeanResult.success(resultMap);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("异常 ： " + e);
        }
        return result;
    }

}
