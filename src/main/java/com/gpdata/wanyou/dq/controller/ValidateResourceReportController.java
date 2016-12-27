package com.gpdata.wanyou.dq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.dq.service.ValidateRangeRecordService;
import com.gpdata.wanyou.dq.service.ValidateRecordDetailsService;
/**
 * @author acer_liuyutong
 */
@Controller
public class ValidateResourceReportController extends BaseController{
    @Autowired
    private ValidateRangeRecordService validateRangeRecordService;
    @Autowired
    private ValidateRecordDetailsService validateRecordDetailsService;
    /**
     * 获取指定数据源下的验证记录
     * @param ruleId
     */
    @RequestMapping(value="/dq/vrp/{resourceId}",method=RequestMethod.GET)
    @ResponseBody
    public BeanResult getRecordByResourceId(@PathVariable("resourceId") Integer resourceId){
        if (logger.isDebugEnabled()) {
            logger.debug("resourceId : {}", resourceId);
        }
        BeanResult beanResult = null;
        try {
            beanResult = validateRangeRecordService.getResourceReport(resourceId);
        } catch (Exception e) {
            logger.error("Exception : {}",e);
            beanResult = BeanResult.error("获取记录失败！");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("result : {}",JSONObject.toJSONString(beanResult));
        }
        return beanResult;
    }
    
    /**
     * 获取指定数据源下的记录详细
     * @param recordId
     */
    @RequestMapping(value="/dq/vrp/{resourcedId}/vrpd",method=RequestMethod.GET)
    @ResponseBody
    public BeanResult getDetailsByResourceId(@PathVariable("resourcedId") Integer resourcedId
            , @RequestParam(name="offset",required=false,defaultValue="0") Integer offset
            , @RequestParam(name="limit",required=false,defaultValue="10") Integer limit){
        if (logger.isDebugEnabled()) {
            Object[] argArr = {resourcedId,offset,limit};
            logger.debug("resourcedId,offset,limit : {},{},{}", argArr);
        }
        BeanResult beanResult = null;
        try {
            beanResult = validateRecordDetailsService.getResourceReportDetails(resourcedId,offset,limit);
        } catch (Exception e) {
            logger.error("Exception : {}",e);
            beanResult = BeanResult.error("获取详情列表失败！");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("result : {}",JSONObject.toJSONString(beanResult));
        }
        return beanResult;
    }
}