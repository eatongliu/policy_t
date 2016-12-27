package com.gpdata.wanyou.ds.controller;

import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.ds.service.HDFSService;
import com.gpdata.wanyou.ds.service.ResourceService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * HDFS资源操作
 *
 * @author gaosong 2016-10-31
 */
@Controller
public class HDFSController {
    private static final Logger logger = Logger.getLogger(HDFSController.class);

    @Autowired
    private HDFSService hdfsService;
    @Autowired
    private ResourceService resourceService;

    /**
     * 具体获取子目录
     */
    @RequestMapping(value = "/ds/{resourceid}/hdfsdir", method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getHDFSDir(
            @PathVariable Integer resourceid, //资源ID
            @RequestParam(required = false, defaultValue = "/") String parent //完整父路径
    ) {
        if (null == resourceid || resourceid <= 0) {
            return BeanResult.error("无效数据源id");
        }
        try {
            return BeanResult.success(hdfsService.getHDFSDir(resourceService.getDataSourceById(resourceid), parent));
        } catch (Exception e) {
            logger.error("查询异常，{}", e);
            return BeanResult.error("查询异常，请联系管理员！");
        }
    }

    /**
     * 具体获取子文件
     */
    @RequestMapping(value = "/ds/{resourceid}/hdfsfile", method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getHDFSFile(
            @PathVariable Integer resourceid, //资源ID
            @RequestParam(required = false, defaultValue = "/") String parent //完整父路径
    ) {
        if (null == resourceid || resourceid <= 0) {
            return BeanResult.error("无效数据源id");
        }
        try {
            return BeanResult.success(hdfsService.getHDFSFile(resourceService.getDataSourceById(resourceid), parent));
        } catch (Exception e) {
            logger.error("查询异常，{}", e);
            return BeanResult.error("查询异常，请联系管理员！");
        }
    }

    /**
     * 具体获取子文件
     */
    @RequestMapping(value = "/ds/{resourceid}/hdfscontent", method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getHDFSContent(
            @PathVariable Integer resourceid, //资源ID
            @RequestParam(required = false, defaultValue = "/") String filepath, //完整父路径
            @RequestParam(required = false, defaultValue = "20") Integer limit, //获取条数（默认20）
            @RequestParam(required = false, defaultValue = "0") Integer offset //从第几条记录开始（默认0）
    ) {
        if (null == resourceid || resourceid <= 0) {
            return BeanResult.error("无效数据源id");
        }
        try {
            return BeanResult.success(hdfsService.getHDFSContent(resourceService.getDataSourceById(resourceid), filepath, limit, offset));
        } catch (Exception e) {
            logger.error("查询异常，{}", e);
            return BeanResult.error("查询异常，请联系管理员！");
        }
    }
}
