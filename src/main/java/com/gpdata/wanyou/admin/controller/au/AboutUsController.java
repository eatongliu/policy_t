package com.gpdata.wanyou.admin.controller.au;

import com.gpdata.wanyou.admin.entity.AboutUs;
import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.service.SimpleService;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by ligang on 2016/12/12.
 */
@Controller
@RequestMapping
public class AboutUsController extends BaseController {
    @Autowired
    private SimpleService simpleService;

    /**
     * 获取关于我们
     *
     * @param
     * @return AboutUsList
     */
    @RequestMapping(value = "/admin/au", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getAboutUs() {
        try {
            List<AboutUs> list = simpleService.getAll(AboutUs.class, "sortId");
            return BeanResult.success(list);
        } catch (Exception e) {
            logger.error("Exception : {}", e.getCause());
            return BeanResult.error("获取失败 ： " + e.getMessage());
        }
    }

    /**
     * 新增关于我们
     * @param "AboutUs"
     * @return
     */
    @RequestMapping(value = "/admin/au", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public BeanResult saveAboutUs(@RequestBody AboutUs input, HttpServletRequest request) {
        logger.debug("input : {}", input);
        try {
            // 获取user
            User user = this.getCurrentUser(request);
            input.setCreatorId(String.valueOf(user.getUserId()));
            // 获取当前日期
            Date date = new Date();
            input.setCreateDate(date);
            simpleService.save(input);
            return BeanResult.success("");
        } catch (Exception e) {
            logger.error("Exception : {}", e.getCause());
            return BeanResult.error("保存失败 ： " + e.getMessage());
        }
    }

    /**
     * 修改关于我们
     */
    @RequestMapping(value = "/admin/au/{sortId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    @ResponseBody
    public BeanResult updateAboutUs(@RequestBody AboutUs input, @PathVariable("sortId") Integer sortId) {
        logger.debug("id,input : {}", sortId + "," + input);
        try {
            simpleService.update(sortId, input);
            return BeanResult.success("");
        } catch (Exception e) {
            logger.error("Exception : {}", e.getCause());
            return BeanResult.error("修改失败 ： " + e.getMessage());
        }
    }

    /**
     * 删除关于我们
     */
    @RequestMapping(value = "/admin/au/{sortId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.DELETE)
    @ResponseBody
    public BeanResult deleteAboutUs(@PathVariable("sortId") Integer sortId) {
        logger.debug("id : {}", sortId);
        try {
            simpleService.delete(AboutUs.class, sortId);
            return BeanResult.success("删除成功");
        } catch (Exception e) {
            logger.error("Exception : {}", e.getCause());
            return BeanResult.error("删除失败 ： " + e.getMessage());
        }

    }
}
