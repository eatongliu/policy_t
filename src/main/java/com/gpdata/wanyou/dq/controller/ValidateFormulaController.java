package com.gpdata.wanyou.dq.controller;

import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.service.SimpleService;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.dq.entity.ValidateFormula;
import com.gpdata.wanyou.dq.service.ValidateFormulaService;
import com.gpdata.wanyou.system.entity.User;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author acer_liuyutong
 */
@Controller
public class ValidateFormulaController extends BaseController{
    
	@Autowired
	private SimpleService simpleService;
	
	@Autowired
	private ValidateFormulaService validateFormulaService;

	/**
     * 列表显示
     */
    @RequestMapping(value = "/dq/vf"
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String queryValidateFormulas(
              @RequestParam(name = "formulaName", required = false) String formulaName
            , @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit
            , @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset) {

        if (logger.isDebugEnabled()) {
            Object[] argArray = {formulaName, limit, offset};
            logger.debug("String formulaName, int limit,int offset: {} , {} , {}", argArray);
        }

        BeanResult beanResult = null;

        try {
            Pair<Integer, List<ValidateFormula>> pair = validateFormulaService.getByConditions(formulaName, offset, limit);
            beanResult = BeanResult.success(pair.getLeft(), pair.getRight());
        } catch (Exception e) {
            logger.error("Exception : {}", e);
            beanResult = BeanResult.error("获取列表失败！");
        }

        String resultString = JSONObject.toJSONString(beanResult);

        if (logger.isDebugEnabled()) {
            logger.debug("result : {} ", resultString);
        }

        return resultString;
    }

    /**
     * 获得一个验证公式的详细信息
     */
    @RequestMapping(value = "/dq/vf/{formulaId}"
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getValidateFormula(@PathVariable("formulaId") Integer formulaId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Integer formulaId : {}", formulaId);
        }

        BeanResult beanResult = null;

        try {
            ValidateFormula bean = this.simpleService.getById(ValidateFormula.class,formulaId);

            if (bean == null) {
                beanResult = BeanResult.error("验证公式不存在!");
                logger.error("验证公式不存在!");
                return JSONObject.toJSONString(beanResult);
            }

            beanResult = BeanResult.success(bean);

        } catch (Exception e) {
            logger.error("Exception : {}", e.getCause());
            beanResult = BeanResult.error("信息获取出现异常！");
        }

        String resultString = JSONObject.toJSONString(beanResult);

        if (logger.isDebugEnabled()) {
            logger.debug("result : {}", resultString);
        }

        return resultString;
    }

    /**
     * 删除一个验证公式
     */
    @RequestMapping(value = "/dq/vf/{formulaId}"
            , method = RequestMethod.DELETE
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteValidateFormula(@PathVariable("formulaId") Integer formulaId) {

        if (logger.isDebugEnabled()) {
            logger.debug("Integer formulaId : {}", formulaId);
        }

        BeanResult beanResult = null;
        try {
            ValidateFormula formula = simpleService.getById(ValidateFormula.class,formulaId);
            if (formula == null) {
                logger.error("被删除的对象不存在, 操作被忽略, 没有数据被改变!");
                beanResult = BeanResult.error("被删除的对象不存在, 操作被忽略, 没有数据被改变!");
                return JSONObject.toJSONString(beanResult);
            }
            
            simpleService.delete(formula);
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
     * 新增验证公式
     */
    @RequestMapping(value = "/dq/vf"
            , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
            , method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String saveValidateFormula(HttpServletRequest request
            , @RequestBody ValidateFormula input) {
    	if (logger.isDebugEnabled()) {
            logger.debug("input : {}", input);
        }

        BeanResult beanResult = null;
        
        try {
        	User currentUser = this.getCurrentUser(request);
        	Date date = new Date();
        	
        	input.setCreatorId(String.valueOf(currentUser.getUserId()));
        	input.setCreateDate(date);
        	input.setReviseDate(date);
        	
            Integer formulaId = (Integer) simpleService.save(input);
            Map<String, Object> map = new HashMap<>();
            map.put("id", formulaId);
            beanResult = BeanResult.success(map);
        } catch (Exception e) {
            logger.error("Exception : ", e);
            beanResult = BeanResult.error("保存失败！");
        }

        String resultString = JSONObject.toJSONString(beanResult);
        if (logger.isDebugEnabled()) {
            logger.debug("result : {} ", resultString);
        }

        return resultString;
    }

    /**
     * 更新验证公式
     */
    @RequestMapping(value = "/dq/vf/{formulaId}"
            , consumes = "application/json"
            , method = RequestMethod.PUT
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateValidateFormula(@RequestBody ValidateFormula input
            , @PathVariable("formulaId") Integer formulaId) {

        if (logger.isDebugEnabled()) {
            logger.debug("Integer standId : {}", formulaId);
        }
        BeanResult beanResult = null;

        try {
        	Date date = new Date();
        	input.setReviseDate(date);
        	
            simpleService.update(formulaId,input);
            beanResult = BeanResult.success(null);
        } catch (Exception e) {
            logger.error("Exception : {}", e.getCause());
            beanResult = BeanResult.error("修改公式发生错误！");
        }

        String resultString = JSONObject.toJSONString(beanResult);
        if (logger.isDebugEnabled()) {
            logger.debug("result : {} ", resultString);
        }
        return resultString;
    }
}