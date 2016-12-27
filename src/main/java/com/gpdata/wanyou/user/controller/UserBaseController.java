package com.gpdata.wanyou.user.controller;

import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.flexible.FlexibleFileType;
import com.gpdata.wanyou.base.flexible.FlexibleFileUtil;
import com.gpdata.wanyou.base.service.RedisOperateService;
import com.gpdata.wanyou.base.service.SimpleService;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.system.service.UserService;
import com.gpdata.wanyou.user.service.UserKeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 * 个人中心用户资料
 * Created by acer_liuyutong on 2016/12/10.
 */
@Controller
@RequestMapping(value = "/user")
public class UserBaseController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private SimpleService simpleService;
    @Autowired
    private RedisOperateService redisOperateService;
    @Autowired
    private FlexibleFileUtil flexibleFileUtil;
    @Autowired
    private UserKeywordService userKeywordService;

    /**
     * 用户的个人资料
     *
     * @param request
     */
    @RequestMapping(value = "/showself", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult userProfile(HttpServletRequest request) {
        BeanResult beanResult = null;
        try {
            User user = getCurrentUser(request);

            beanResult = BeanResult.success(user);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            beanResult = BeanResult.error("获取信息失败");
        }
        return beanResult;
    }

    /**
     * 用户的个人资料左下角的关键词
     *
     * @param request
     */
    @RequestMapping(value = "/keywords", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult userKeywords(HttpServletRequest request) {
        BeanResult beanResult = null;
        try {
            User user = getCurrentUser(request);
            List<String> keyWords = userKeywordService.getKeyWords(user.getUserId());

            beanResult = BeanResult.success(keyWords);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            beanResult = BeanResult.error("获取信息失败");
        }
        return beanResult;
    }

    /**
     * 完善用户信息
     */
    @RequestMapping(value = "/update/{userId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult userPerfect(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {
        BeanResult beanResult;
        try {
            simpleService.update(userId, user);
            beanResult = BeanResult.success("修改成功");
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            beanResult = BeanResult.error("修改信息失败");
        }
        return beanResult;
    }

    /**
     * 上传用户头像
     */
    @RequestMapping(value = "/uploaduserlogo",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult uploadUserLogo( @RequestParam(value = "userLogo",required = false) MultipartFile userLogo, HttpServletRequest request) {
        logger.info("上传用户头像 ->");
        logger.debug("头像路径策略为： /upload/userLoge/用户Id/uuid值.扩展名");
        BeanResult beanResult = null;
        try {
            if (userLogo == null || userLogo.isEmpty()){
                return BeanResult.error("上传失败，上传文件为空！");
            }
            User user = getCurrentUser(request);

            //拼接头像保存路径
            StringBuilder src = new StringBuilder();
            src.append("/upload/userLogo/").append(user.getUserId()).append("/");

            String fileName = userLogo.getOriginalFilename();
            logger.debug("上传的头像名: {}", fileName);
            String extension = StringUtils.getFilenameExtension(fileName);
            String newFileName = UUID.randomUUID().toString();
            logger.debug("生成的头像名: {}.{}", newFileName, extension);

            src.append(newFileName).append(".").append(extension);
            logger.debug("保存到数据库的路径值为：{}",src);

            //保存到阿里 OSS
            flexibleFileUtil.saveUploadFile(userLogo.getInputStream(), src.toString(), FlexibleFileType.PUB);

            //图像处理完成，将数据写入数据库中
            user.setHeadPic(src.toString());
            simpleService.update(user);
            logger.info("头像上传完成");
            beanResult = BeanResult.success("上传成功");
        } catch (Exception e) {
            logger.error("Exception: {}",e);
            beanResult = BeanResult.error("上传头像失败");
        }
        return beanResult;
    }

}
