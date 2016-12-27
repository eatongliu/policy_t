package com.gpdata.wanyou.ds.controller;

import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.ds.enums.DataResourceStatus;
import com.gpdata.wanyou.ds.service.HDFSService;
import com.gpdata.wanyou.ds.service.ResourceService;
import com.gpdata.wanyou.ds.service.TableService;
import com.gpdata.wanyou.ds.util.Db2EsSynchronise;
import com.gpdata.wanyou.ds.util.HDFSUtil;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.utils.ConfigUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.sql.Connection;
import java.util.*;

/**
 * 数据源相关API
 */
@Controller
public class ResourceController {
    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private TableService tableService;

    /**
     * @author gaosong 2016-10-31
     * 增加hdfsService用来实现连通性测试等
     */
    @Autowired
    private HDFSService hdfsService;

    /**
     * 具体数据源列表
     */
    @RequestMapping(value = "/ds", method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getDataSourceList(@RequestParam(required = false, defaultValue = "") String position, //数据源位置信息，默认全部
                                        @RequestParam(required = false, defaultValue = "DBMS") String resourceType, //数据源类型，默认数据库型
                                        @RequestParam(required = false, defaultValue = "") String caption, //数据源名称搜索词
                                        @RequestParam(required = false, defaultValue = "20") Integer limit,
                                        @RequestParam(required = false, defaultValue = "0") Integer offset) {
        HashMap<String, String> params = new HashMap<>();
        //查询条件
        params.put("resourceType", resourceType);
        params.put("position", position);
        params.put("caption", caption);
        //分页条件
        params.put("limit", limit.toString());
        params.put("offset", offset.toString());

        try {
            return resourceService.getDataSourceList(params);
        } catch (Exception e) {
            logger.error("查询异常，{}", e);
            return BeanResult.error("查询异常，请联系管理员！");
        }
    }

    /**
     * 获取数据源列表，只获取id和名字，不分页
     */
    @RequestMapping(value = "/ds/all", method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getAllDataSourceIdAndNameList(@RequestParam(required = false, defaultValue = "DBMS") String resourceType) {
        try {
            return resourceService.getAllDataSourceIdAndNameList(resourceType);
        } catch (Exception e) {
            logger.error("查询异常，{}", e);
            return BeanResult.error("查询异常，请联系管理员！");
        }
    }


    /**
     * 具体某个数据源的详细信息
     */
    @RequestMapping(value = "/ds/{resourceid}", method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getDataSourceDetail(@PathVariable Integer resourceid) {
        if (null == resourceid || resourceid <= 0) {
            return BeanResult.error("无效数据源id");
        }
        try {
            return BeanResult.success(resourceService.getDataSourceById(resourceid));
        } catch (Exception e) {
            logger.error("查询异常，{}", e);
            return BeanResult.error("查询异常，请联系管理员！");
        }
    }

    /**
     * 新增DBMS类数据源
     */
    @RequestMapping(value = "/ds/db", consumes = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public BeanResult addDbDataSource(@RequestBody DataSourceResource dataSourceResource, HttpServletRequest req) {
        User u = (User) req.getAttribute("currentUser");
        dataSourceResource.setResourceType("DBMS");
        BeanResult testResult = resourceService.testDataSourceConn(dataSourceResource);

        if (testResult.getStatus().equals(BeanResult.ERROR)) {
            return BeanResult.error("连接测试失败，请确认信息，测试成功后再进行添加！");
        }

        return this.addDataResourceCommon(dataSourceResource, u);
    }

    /**
     * 修改DBMS类数据源
     */
    @RequestMapping(value = "/ds/db/{resourceid}", method = RequestMethod.PUT)
    @ResponseBody
    public BeanResult updateDbDataSource(@PathVariable Integer resourceid,
                                         @RequestBody DataSourceResource dataSourceResource) {
        dataSourceResource.setResourceId(resourceid);
        dataSourceResource.setResourceType("DBMS");
        dataSourceResource.setReviseDate(new Date());

        BeanResult testResult = resourceService.testDataSourceConn(dataSourceResource);
        if (testResult.getStatus().equals(BeanResult.ERROR)) {
            return BeanResult.error("连接测试失败，请确认信息，测试成功后再进行添加！");
        }

        try {
            dataSourceResource.setStatus(DataResourceStatus.SUCCESS.getStateInfo());
            resourceService.updateDataSourceResource(dataSourceResource);
            return BeanResult.success(dataSourceResource.getResourceId());
        } catch (Exception e) {
            logger.error("修改数据库型数据源异常，{}", e);
            return BeanResult.error("修改失败，请联系管理员！");
        }
    }

    /**
     * 测试DBMS类数据源
     */
    @RequestMapping(value = "/ds/testdb", consumes = "application/json;charset=utf-8", method = RequestMethod.PUT)
    @ResponseBody
    public BeanResult testDbDataSource(@RequestBody DataSourceResource dataSourceResource) {
        return resourceService.testDataSourceConn(dataSourceResource);
    }

    /**
     * 新增HDFS类数据源
     */
    @RequestMapping(value = "/ds/hdfs", consumes = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public BeanResult addHdfsDataSource(@RequestBody DataSourceResource dataSourceResource, HttpServletRequest req) {
        User u = (User) req.getAttribute("currentUser");
        dataSourceResource.setResourceType("HDFS");
        return this.addDataResourceCommon(dataSourceResource, u);
    }

    /**
     * 修改HDFS类数据源
     */
    @RequestMapping(value = "/ds/hdfs/{resourceid}", consumes = "application/json;charset=utf-8", method = RequestMethod.PUT)
    @ResponseBody
    public BeanResult updateHdfsDataSource(@PathVariable Integer resourceid,
                                           @RequestBody DataSourceResource dataSourceResource) {
        BeanResult testRes;

        try {
            testRes = hdfsService.test(dataSourceResource);
        } catch (Exception e) {
            logger.error("用户的hdfs测试失败！{}", e);
            return BeanResult.error("测试连接失败，请确认链接信息后再修改！");
        }

        if (null == testRes || testRes.getStatus().equals(BeanResult.ERROR)) {
            return BeanResult.error("测试连接失败，请确认链接信息后再修改！");
        }

        dataSourceResource.setResourceId(resourceid);
        dataSourceResource.setResourceType("HDFS");
        dataSourceResource.setReviseDate(new Date());

        try {
            resourceService.updateDataSourceResource(dataSourceResource);
            return BeanResult.success(dataSourceResource.getResourceId());
        } catch (Exception e) {
            logger.error("修改HDFS数据源异常，{}", e);
            return BeanResult.error("修改失败，请重试！");
        }
    }

    /**
     * 测试HDFS类数据源
     */
    @RequestMapping(value = "/ds/testhdfs", consumes = "application/json;charset=utf-8", method = RequestMethod.PUT)
    @ResponseBody
    public BeanResult testHdfsDataSource(@RequestBody DataSourceResource dataSourceResource) {
        try {
            return hdfsService.test(dataSourceResource);
        } catch (Exception e) {
            logger.error("连接HDFS数据源异常，{}", e);
            return BeanResult.error("连接失败，请重试！");
        }
    }

    /**
     * 根据sql语句检索某个数据源内的内容
     */
    @RequestMapping(value = "/ds/{resourceid}", consumes = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public BeanResult queryDataBySql(@PathVariable Integer resourceid, @RequestBody Map<String, String> params) {
        String sql = params.get("sql");

        if (sql == null || sql.equals("")) {
            return BeanResult.error("无效的SQL语句！");
        }

        DataSourceResource dataSourceResource = resourceService.getDataSourceById(resourceid);

        if (null == dataSourceResource) {
            return BeanResult.error("无效的数据源！");
        }

        return resourceService.getDataFromDataSourceBySql(dataSourceResource, sql);
    }

    /**
     * 添加数据源公共方法
     */
    private BeanResult addDataResourceCommon(DataSourceResource dataSourceResource, User currentUser) {
        dataSourceResource.setCreator(currentUser.getUserId().toString());
        dataSourceResource.setCreateDate(new Date());
        dataSourceResource.setReviseDate(new Date());
        dataSourceResource.setStatus(DataResourceStatus.SUCCESS.getStateInfo());

        try {
            resourceService.addDataSourceResource(dataSourceResource);

            runDb2EsSynchronise(dataSourceResource);

            return BeanResult.success(dataSourceResource.getResourceId());
        } catch (Exception e) {
            logger.error("增加【" + dataSourceResource.getResourceType() + "】类型的数据源异常，{}", e);
            return BeanResult.error("新增失败，请重试！");
        }
    }

    /**
     * es与内部数据源数据库的同步操作
     *
     * @param dataSourceResource
     * @author qyl
     */
    private void runDb2EsSynchronise(DataSourceResource dataSourceResource) {

        if (dataSourceResource.getPosition().equalsIgnoreCase("inner")
                & dataSourceResource.getResourceType().equalsIgnoreCase("DBMS")) {

            //以下参数从配置文件config.properties读取

            //ES路径 例如："192.168.1.120:9200"
            String esAddr = ConfigUtil.getConfig("es.addr");
            //数据暂存路径data_dir的父目录
            String superDataDir = ConfigUtil.getConfig("data.super.dir");
            //运行时所需的路径，IP地址需要生成，端口号不变："192.168.1.120:12805"
            String statAddr = ConfigUtil.getConfig("stat.addr");
            //cd命令要进入的执行目录，例如cd $GOPATH/src/github.com/siddontang/go-mysql-elasticsearch
            String runPath = ConfigUtil.getConfig("run.path");
            //存放river.toml文件的目录
            String riverTomlPath = ConfigUtil.getConfig("river.toml.path");

            //获取新建数据源的tablelist
            List<String> tables = tableService.getDtNameList(dataSourceResource.getResourceId());

            //构造river.toml文件并配置
            Db2EsSynchronise sy = new Db2EsSynchronise(dataSourceResource, esAddr, superDataDir, statAddr, tables);

            //shell命令运行river.toml文件并记录操作结果
            try {
                logger.info("运行shell命令同步成功：" + sy.run(runPath, riverTomlPath));
            } catch (IOException e) {
                logger.error("运行shell命令同步失败，{}", e);
            }
        }
    }


}