package com.gpdata.wanyou.policy.controller;

import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.service.SimpleService;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.policy.entity.PolicyDocDTO;
import com.gpdata.wanyou.policy.entity.PolicyDocument;
import com.gpdata.wanyou.policy.entity.PolicyIndex;
import com.gpdata.wanyou.policy.entity.PolicyTags;
import com.gpdata.wanyou.policy.service.PolicyTagsService;
import com.gpdata.wanyou.policy.util.FtRetrievalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ligang on 2016/12/16.
 */
@Controller
@RequestMapping
public class PolicyDocController extends BaseController {

    @Autowired
    private PolicyTagsService policyTagsService;
    @Autowired
    private SimpleService simpleService;

    /**
     * 获取ES文件列表
     */
    @RequestMapping(value = "/admin/policydoc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getPolicyDocList(@RequestParam(name = "startTime", required = false) String startTime,
                                       @RequestParam(name = "endTime", required = false) String endTime,
                                       @RequestParam(name = "keyWords", required = false) String keyWords,
                                       @RequestParam(name = "index", required = false) String index,
                                       @RequestParam(name = "isPub", required = false) String isPub,
                                       @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                       @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset) {

        Object[] argArray = {startTime, endTime, keyWords, index, isPub, limit, offset};
        logger.debug("input : {}", argArray);

        try {

            PolicyDocument pd = new PolicyDocument();
            pd.setStartTime(startTime);
            pd.setEndTime(endTime);
            pd.setKeyWords(keyWords);
            if ("".equals(index) || index == null) {
                pd.setIndex("");
            } else {
                pd.setIndex(index);
            }
            pd.setIsPub(isPub);
            // 从ES查询政策文件
            String result = FtRetrievalUtil.search(pd, offset, limit);
            Pair<Integer, List<Map<String, Object>>> pair = FtRetrievalUtil.parseHits(result);
            return BeanResult.success(pair.getLeft(), pair.getRight());
        } catch (Exception e) {
            logger.error("Exception : ", e);
            return BeanResult.error("查询失败 ： " + e.getMessage());
        }
    }


    /**
     * 通过ID获取ES文件
     */
    @RequestMapping(value = "/admin/policydoc/{pdId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getPolicyDocList(@PathVariable("pdId") String pdId) {
        try {
            logger.debug("input : {}", pdId);
            // 从ES查询政策文件
            String result = FtRetrievalUtil.get(pdId);
            Pair<Integer, List<Map<String, Object>>> pair = FtRetrievalUtil.parseHits(result);
            if (!pair.getLeft().equals(1)) {
                return BeanResult.error("查询失败");
            }
            List<Map<String, Object>> rList = pair.getRight();
            //获取标签云
            List<PolicyTags> tagsList = policyTagsService.getTags(pdId);
            //获取重复关联 目前定义的筛选条件为：权重前2的关键词匹配
            List<String> relationPolicyName = policyTagsService.getRelationPolicyName(pdId, 2);
            Map<String, Object> map = new HashMap<>();
            map.put("tagsList", tagsList);
            map.put("relationPolicyName", relationPolicyName);
            rList.add(map);
            return BeanResult.success(rList);
        } catch (Exception e) {
            logger.error("Exception : ", e);
            return BeanResult.error("查询失败 ： " + e.getMessage());
        }
    }

    /**
     * 修改ES文件
     */
    @RequestMapping(value = "/admin/policydoc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    @ResponseBody
    public BeanResult updatePolicyDocList(
            @RequestBody Map<String,String> input) {
        logger.debug("input : {}", input);
        try {
            PolicyIndex pi = new PolicyIndex();
            pi.setPdId(Long.valueOf(input.get("pdId")));
            pi.setPdName(input.get("pdName"));
            pi.setCreateDate(input.get("createDate"));
            pi.setTopicClassify(input.get("topicClassify"));
            pi.setIndex_(input.get("esIndex"));
            if (input.get("isPub") == "1"||"1".equals(input.get("isPub"))) {
                simpleService.merge(pi);
            } else {
                simpleService.delete(pi);
            }
            String esIndex = input.get("esIndex");
            String esType = input.get("esType");
            String esId = input.get("esId");
            if(StringUtils.isNotBlank(esIndex)&&StringUtils.isNotBlank(esType)&&StringUtils.isNotBlank(esId)){
                //将参数赋值到实体类 PolicyDocDTO
                PolicyDocDTO policyDocDTO = new PolicyDocDTO();
                policyDocDTO.setPdId(Long.valueOf(input.get("pdId")));
                policyDocDTO.setPdName(input.get("pdName"));
                policyDocDTO.setTopicClassify(input.get("topicClassify"));
                policyDocDTO.setPubOrg(input.get("pubOrg"));
                policyDocDTO.setPlaced(input.get("placed"));
                policyDocDTO.setCreateDate(input.get("createDate"));
                policyDocDTO.setCreateYear(input.get("createYear"));
                policyDocDTO.setIssuedNum(input.get("issuedNum"));
                policyDocDTO.setLink(input.get("link"));
                policyDocDTO.setLinkAddress(input.get("linkAddress"));
                policyDocDTO.setIsEffect(input.get("isEffect"));
                policyDocDTO.setIsPilot(input.get("isPilot"));
                policyDocDTO.setIsPub(input.get("isPub"));
                policyDocDTO.setIsHide(input.get("isHide"));
                policyDocDTO.setProvince(input.get("province"));
                policyDocDTO.setCity(input.get("city"));
                policyDocDTO.setCounty(input.get("county"));
                policyDocDTO.setContent(input.get("content"));
                FtRetrievalUtil.update(policyDocDTO,esIndex,esType,esId);
                return BeanResult.success("");
            }else{
                return BeanResult.error("查询失败");
            }

        } catch (Exception e) {
            logger.error("Exception : ", e);
            return BeanResult.error("查询失败 ： " + e.getMessage());
        }
    }

}
