package com.gpdata.wanyou.head.controller;

import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.head.entity.Picture1Map;
import com.gpdata.wanyou.head.entity.Picture2Contrast;
import com.gpdata.wanyou.head.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 获取首页三张图所需数据
 *
 * @author yaz
 * @create 2016-12-16 11:20
 */
@Controller
public class PictureController extends BaseController {
    @Autowired
    private PictureService service;

    /**
     * 获取第一张图数据 ：地图
     *
     * @Author yaz
     * @Date 2016/12/16 11:23
     */
    @RequestMapping(value = "/h/p1", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getPicture1Data() {
        try {
            //TODO 查询假数据表，后期需改
            return BeanResult.success(service.getAll().get("p1"));
        } catch (Exception e) {
            logger.error("查询出错：{}", e);
            return BeanResult.error("数据不可用！");
        }
    }

    @RequestMapping(value = "/h/p2", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getPicture2Data() {
        try {
            //TODO 查询假数据表，后期需改
            return BeanResult.success(service.getAll().get("p2"));
        } catch (Exception e) {
            logger.error("查询出错：{}", e);
            return BeanResult.error("数据不可用！");
        }
    }

    @RequestMapping(value = "/h/p3", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getPicture3Data() {
        try {
            //TODO 查询假数据表，后期需改
            return BeanResult.success(service.getAll().get("p3"));
        } catch (Exception e) {
            logger.error("查询出错：{}", e);
            return BeanResult.error("数据不可用！");
        }
    }
}
