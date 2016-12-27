package com.gpdata.wanyou.md.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.gpdata.wanyou.base.vo.KvPair;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.md.entity.DataStandard;
import com.gpdata.wanyou.md.service.DataStandardService;

/**
 * 3.1数据标准
 *
 * @author acer_liuyutong
 */
@Controller
public class DataStanardController extends BaseController{

	@Autowired
	private DataStandardService dataStandardService;
	
    /**
     * 列表显示
     */
    @RequestMapping(value = "/md/ds"
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String queryDataStandards(
              @RequestParam(name = "caption", required = false) String standName
            , @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit
            , @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset) {

        if (logger.isDebugEnabled()) {
            Object[] argArray = {standName, limit, offset};
            logger.debug("String caption, int limit,int offset: {} , {} , {}", argArray);
        }

        BeanResult beanResult = null;

        try {
            Pair<Integer, List<DataStandard>> pair = dataStandardService.getDataStandards(standName, offset, limit);
            beanResult = BeanResult.success(pair.getLeft(), pair.getRight());

        } catch (Exception e) {
            logger.error("Exception : {}", e);
            beanResult = BeanResult.error("获取数据标准列表失败！");
        }

        String resultString = JSONObject.toJSONString(beanResult);

        if (logger.isDebugEnabled()) {
            logger.debug("result : {} ", resultString);
        }

        return resultString;
    }

    /**
     * 获得一个数据标准的详细信息
     */
    @RequestMapping(value = "/md/ds/{standId}"
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getDataStandard(HttpServletRequest request
            , @PathVariable("standId") Integer standId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Integer standId : {}", standId);
        }

        BeanResult beanResult = null;

        try {
            DataStandard bean = this.dataStandardService.getById(standId);

            if (bean == null) {
                beanResult = BeanResult.error("数据标准不存在!");
                return JSONObject.toJSONString(beanResult);
            }

            beanResult = BeanResult.success(bean);

        } catch (Exception e) {
            logger.error("Exception : {}", e.getCause());
            beanResult = BeanResult.error("获取数据标准失败！");
        }

        String resultString = JSONObject.toJSONString(beanResult);

        if (logger.isDebugEnabled()) {
            logger.debug("result : {}", resultString);
        }

        return resultString;
    }

    /**
     * 删除一个对象
     */
    @RequestMapping(value = "/md/ds/{standId}"
            , method = RequestMethod.DELETE
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteDataStandard(HttpServletRequest request
            , @PathVariable("standId") Integer standId) {

        if (logger.isDebugEnabled()) {
            logger.debug("Integer standId : {}", standId);
        }

        BeanResult beanResult = null;

        try {
            this.dataStandardService.delete(standId);
            beanResult = BeanResult.success(null);
        } catch (Exception e) {
            logger.error("Exception : {}", e.getCause());
            beanResult = BeanResult.error("删除失败！");
        }

        String resultString = JSONObject.toJSONString(beanResult);

        if (logger.isDebugEnabled()) {
            logger.debug("result : {} ", resultString);
        }

        return resultString;
    }

    /**
     * 新增一个对象
     */
    @RequestMapping(value = "/md/ds"
            , consumes = "application/json"
            , method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String saveDataStandard(HttpServletRequest request
            , @RequestBody DataStandard input) {
    	if (logger.isDebugEnabled()) {
            logger.debug("input : {}", input);
        }

        BeanResult beanResult = null;

        if (input == null) {
            beanResult = BeanResult.error("没有数据提交到服务器!");
            return JSONObject.toJSONString(beanResult);
        }

        if (StringUtils.isBlank(input.getStandName())) {
            beanResult = BeanResult.error("请输入数据标准名称!");
            return JSONObject.toJSONString(beanResult);
        }

        try {
            Integer standId = dataStandardService.save(input);
            Map<String, Object> map = new HashMap<>();
            map.put("id", standId);
            beanResult = BeanResult.success(map);

        } catch (Exception e) {
            logger.error("Exception : ", e.getCause());
            beanResult = BeanResult.error("添加失败！");
        }

        String resultString = JSONObject.toJSONString(beanResult);

        if (logger.isDebugEnabled()) {
            logger.debug("result : {} ", resultString);
        }

        return resultString;
    }

    /**
     * 更新一个对象
     */
    @RequestMapping(value = "/md/ds/{standId}"
            , consumes = "application/json"
            , method = RequestMethod.PUT
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateDataStandard(HttpServletRequest request
            , @RequestBody DataStandard input
            , @PathVariable("standId") Integer standId) {

        if (logger.isDebugEnabled()) {
            logger.debug("Integer standId : {}", standId);
        }
        BeanResult beanResult = null;

        try {
            dataStandardService.update(input);
            beanResult = BeanResult.success(null);
        } catch (Exception e) {
            logger.error("Exception : {}", e.getCause());
            beanResult = BeanResult.error("修改发生错误！");
        }

        String resultString = JSONObject.toJSONString(beanResult);

        if (logger.isDebugEnabled()) {
            logger.debug("result : {} ", resultString);
        }

        return resultString;
    }

    /**
     * 返回一个数据源列表, 只包括没进行和数据标准匹配过的
     *
     * @return
     */
    @RequestMapping(value = "md/ds/rs/nomap"
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<KvPair> retrieveAllNonMappingDataSources(
            @RequestParam(required = false, defaultValue = "") Integer standId) {

        logger.debug("standId : {}", standId);
        List<DataSourceResource> dataSourceResourceList = this.dataStandardService
                .getNonMappingDataSourceResources(standId);

        List<KvPair> result = dataSourceResourceList.stream()
                .map(dsr ->
                    KvPair.of(dsr.getResourceId(), dsr.getCaption()))
                .collect(Collectors.toList());

        return result;
    }
}