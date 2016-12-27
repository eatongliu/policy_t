package com.gpdata.wanyou.dq.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
public class ValidateRangeRecordController extends BaseController{
    @Autowired
    private ValidateRangeRecordService validateRangeRecordService;
    @Autowired
    private ValidateRecordDetailsService validateRecordDetailsService;
    /**
     * 跟据规则Id获取验证记录
     * @param ruleId
     */
    @RequestMapping(value="/dq/{ruleId}/vrre",method=RequestMethod.GET)
    @ResponseBody
    public BeanResult getRecordByRuleId(@PathVariable("ruleId") Integer ruleId){
        if (logger.isDebugEnabled()) {
            logger.debug("ruleId : {}", ruleId);
        }
        BeanResult beanResult = null;
        try {
            beanResult = validateRangeRecordService.getRecordByRuleId(ruleId);
        } catch (Exception e) {
            logger.error("Exception : {}",e);
            beanResult = BeanResult.error("获取记录发生异常！");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("result : {}",JSONObject.toJSONString(beanResult));
        }
        return beanResult;
    }
    
    /**
     * 根据记录Id获取记录详细
     * @param recordId
     */
    @RequestMapping(value="/dq/{recordId}/vrde",method=RequestMethod.GET)
    @ResponseBody
    public BeanResult getDetailsByRecordId(@PathVariable("recordId") Integer recordId
            , @RequestParam(name="offset",required=false,defaultValue="0") Integer offset
            , @RequestParam(name="limit",required=false,defaultValue="10") Integer limit){
        if (logger.isDebugEnabled()) {
            Object[] argArr = {recordId,offset,limit};
            logger.debug("recordId,offset,limit : {},{},{}", argArr);
        }
        BeanResult beanResult = null;
        try {
            beanResult = validateRecordDetailsService.getDetailsByRecordId(recordId,offset,limit);
        } catch (Exception e) {
            logger.error("Exception : {}",e);
            beanResult = BeanResult.error("获取详情列表失败！");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("result : {}",JSONObject.toJSONString(beanResult));
        }
        return beanResult;
    }
    
    /**
     * 根据指定条件获取记录详细
     * @param recordId
     */
    @RequestMapping(value="/dq/vrde",method=RequestMethod.GET)
    @ResponseBody
    public BeanResult getDetailsByConditions( @RequestParam(required=false,defaultValue="0") Integer offset
            , @RequestParam(required=false,defaultValue="10") Integer limit
            , @RequestParam(required=false) String metadataName
            , @RequestParam(required=false) String metadataEntityName
            , @RequestParam(required=false) Integer formulaId
            , @RequestParam(required=false) Integer maxVal
            , @RequestParam(required=false) Integer minVal
            , @RequestParam(required=false) Double dataPrecision
            , @RequestParam(required=false) Date validateDate){
        if (logger.isDebugEnabled()) {
            Object[] argArr = {metadataName,metadataEntityName,formulaId,maxVal,minVal,dataPrecision,validateDate,offset,limit};
            logger.debug("argArr: {},{},{},{},{},{},{},{},{}", argArr);
        }
        
        Map<String, Object> params = new HashMap<>();
        //查询参数
        params.put("metadataName", metadataName);
        params.put("metadataEntityName", metadataEntityName);
        params.put("formulaId", formulaId);
        params.put("validateDate", validateDate);
        params.put("dataPrecision", dataPrecision);
        params.put("maxVal", maxVal);
        params.put("minVal", minVal);
        //分页条件
        params.put("offset", offset);
        params.put("limit", limit);
        BeanResult beanResult = null;
        try {
            beanResult = validateRecordDetailsService.getDetailsByConditions(params);
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