package com.gpdata.wanyou.dq.controller;

import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.service.SimpleService;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.dq.dao.ValidateRangeRecordDao;
import com.gpdata.wanyou.dq.dto.ValidateRangeRuleDto;
import com.gpdata.wanyou.dq.entity.ValidateRangeRule;
import com.gpdata.wanyou.dq.service.ValidateRangeRuleService;
import com.gpdata.wanyou.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ligang 2016年11月14日
 */
@Controller
@RequestMapping
public class ValidateRangeRuleController extends BaseController {

    @Autowired
    private ValidateRangeRuleService validateRangeRuleService;
    @Autowired
    private ValidateRangeRecordDao validateRangeRecordDao;
    @Autowired
    private SimpleService simpleService;

    /**
     * 保存新对象
     *
     * @return ruleId
     */
    @RequestMapping(value = "/dq/vrr", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public BeanResult saveValidateRangeRule(@RequestBody ValidateRangeRuleDto input, HttpServletRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("input : {}", input);
        }
        // 获取user
        User user = this.getCurrentUser(request);
        // 获取当前日期
        Date date = new Date();
        ValidateRangeRule vrr = new ValidateRangeRule();
        vrr.setFormulaId(input.getFormulaId());
        vrr.setMaxVal(input.getMaxVal());
        vrr.setMinVal(input.getMinVal());
        vrr.setDataPrecision(input.getDataPrecision());
        vrr.setCreatorId(String.valueOf(user.getUserId()));
        vrr.setCreateDate(date);
        vrr.setRemark(input.getRemark());
        vrr.setRuleStatus(input.getRuleStatus());
        // 获取元数据实体idList
        if(input.getEntityIdList()!=null){
            List<Integer> entityIdList = new ArrayList<Integer>();
            entityIdList = input.getEntityIdList();
            if (entityIdList.size() != 0) {
                for (int entityId : entityIdList) {
                    vrr.setEntityId(entityId);
                    simpleService.save(vrr);
                }
                return BeanResult.success("");
            } else {
                return BeanResult.error("新增失败 ： 元数据实体不能为空！");
            }
        }
        //获取字段idList
        else if(input.getFieldIdList()!=null){
            List<Integer> fieldIdList = new ArrayList<Integer>();
            fieldIdList = input.getFieldIdList();
            if (fieldIdList.size() != 0) {
                for (int fieldId : fieldIdList) {
                    vrr.setEntityId(validateRangeRuleService.getEntityIdByFieldID(fieldId));
                    simpleService.save(vrr);
                }
                return BeanResult.success("");
            } else {
                return BeanResult.error("新增失败 ： 字段不能为空！");
            }
        }
        else{
            return BeanResult.error("新增失败 ！");
        }
    }

    /**
     * 删除
     *
     * @param ruleId
     * @return
     */
    @RequestMapping(value = "/dq/vrr/{ruleId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.DELETE)
    @ResponseBody
    public BeanResult deleteValidateRangeRule(@PathVariable("ruleId") Integer ruleId) {
        if (logger.isDebugEnabled()) {
            logger.debug("input : {}", ruleId);
        }
        try {
            validateRangeRuleService.delete(ruleId);
            return BeanResult.success("删除成功");
        } catch (Exception e) {
            logger.error("Exception : {}", e.getCause());
            return BeanResult.error("删除失败 ： " + e.getMessage());
        }
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/dq/vrr/{ruleId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    @ResponseBody
    public BeanResult updateValidateRangeRule(@PathVariable("ruleId") Integer ruleId,
            @RequestBody ValidateRangeRule input) {
        if (logger.isDebugEnabled()) {
            logger.debug("input : {}", ruleId);
        }

        try {
            input.setReviseDate(new Date());
            simpleService.update(ruleId, input);
            return BeanResult.success("修改成功");
        } catch (Exception e) {
            logger.error("Exception : ", e);
            return BeanResult.error("修改失败 ： " + e.getMessage());
        }
    }

    /**
     * 获取指定 id 的对象
     *
     * @param ruleId
     * @return ValidateRangeRule
     */
    @RequestMapping(value = "/dq/vrr/{ruleId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getValidateRangeRuleInfoById(@PathVariable("ruleId") Integer ruleId) {
        logger.debug("input : {}", ruleId);
        try {
            // 根据传入参数获取实体
            ValidateRangeRuleDto bean = this.validateRangeRuleService.getById(ruleId);
            if (bean == null) {
                return BeanResult.error("数据不存在!");
            }
            return BeanResult.success(bean);

        } catch (Exception e) {
            logger.error("Exception : {}", e.getCause());
            return BeanResult.error("获取失败 ： " + e.getMessage());
        }
    }

    /**
     * 条件查询
     *
     *            ,...,limit,offset
     * @return ValidateRangeRule
     */
    @RequestMapping(value = "/dq/vrr", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public BeanResult queryValidateRangeRuleInfo(
            @RequestParam(name = "metadataEntityCaption", required = false) String metadataEntityCaption,
            @RequestParam(name = "metadataCaption", required = false) String metadataCaption,
            @RequestParam(name = "createDate", required = false) String createDate,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset) {
        if (logger.isDebugEnabled()) {
            Object[] argArray = { metadataEntityCaption, metadataCaption, createDate, limit, offset };
            logger.debug("input : {}", argArray);
        }
        try {
            if (limit > 100 || limit < 1) {
                limit = 10;
            }
            ValidateRangeRuleDto input = new ValidateRangeRuleDto();
            if (metadataEntityCaption != null) {
                input.setMetadataEntityCaption(metadataEntityCaption);
            }
            if (metadataCaption != null) {
                input.setMetadataCaption(metadataCaption);
            }
            if (createDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(createDate);
                input.setCreateDate(date);
            }
            Map<String, Object> searchResult = validateRangeRuleService.query(input, limit, offset);
            return BeanResult.success((int) searchResult.get("total"),
                    (List<ValidateRangeRuleDto>) searchResult.get("rows"));
        } catch (Exception e) {
            logger.error("searchOntologyException : ", e);
            return BeanResult.error("获取失败 ： " + e.getMessage());
        }
    }

}
