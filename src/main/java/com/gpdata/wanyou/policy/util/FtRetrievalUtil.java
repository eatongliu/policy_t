package com.gpdata.wanyou.policy.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.policy.entity.PolicyDocDTO;
import com.gpdata.wanyou.policy.entity.PolicyDocument;
import com.gpdata.wanyou.utils.ConfigUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FtRetrievalUtil {
    protected static final Logger logger = LoggerFactory.getLogger(FtRetrievalUtil.class);

    /**
     * 查询ES中的信息
     *
     * @param policyDocument
     * @return 政策信息结果
     * @author gaosong 2016-12-16
     */
    public static String search(PolicyDocument policyDocument, int offset, int limit) {
        //1、拼接JSON请求
        /*
         精确匹配：
    	“isEffect”:””,      //是否有效 0 否 1 是    “精确“
		“isPilot”:””,      //是否试点 0 否 1 是     “精确“
		“isPub”:””,        //是否发布  0 否 1 是   默认 0    “精确“
		“isHide”:””,        //是否隐藏  0 否 1 是   默认 1    “精确“
		
		模糊匹配：
		“pdName”:””,       //文件名称   “模糊”
		“topicClassify”:””,  //主题分类   “模糊”
		“pubOrg”:””,      //颁布机构   “模糊”
		“placed”:””,       //具体单位   “模糊”
		“createDate”:””,    //成文日期   区间查询  startTime   endTime  
		“issuedNum”:””,    //发文字号   “模糊”
		“content”:””     //文件内容   “模糊”
		“province”:””     //省      “模糊”
		“city”:””     //市            “模糊”
		“county”:””     //县       “模糊”
		
		关键字：
		“keyWords”:””     //查询关键字    “模糊”		
    	 */

        String match = "";
        //模糊匹配：
        //“keyWords”:””     //查询关键字    “模糊”
        match += getKeyMatch("content", policyDocument.getKeyWords());
        //“pdName”:””,       //文件名称   “模糊”
        match += getMatch("pdName", policyDocument.getPdName());
        //“topicClassify”:””,  //主题分类   “模糊”
        match += getMatch("topicClassify", policyDocument.getTopicClassify());
        //“pubOrg”:””,      //颁布机构   “模糊”
        match += getMatch("pubOrg", policyDocument.getPubOrg());
        //“placed”:””,       //具体单位   “模糊”
        match += getMatch("placed", policyDocument.getPlaced());
        //“createDate”:””,    //成文日期   区间查询  startTime   endTime
        match += getMatch("createDate", policyDocument.getCreateDate());
        //“issuedNum”:””,    //发文字号   “模糊”
        match += getMatch("issuedNum", policyDocument.getIssuedNum());
        //“province”:””     //省      “模糊”
        match += getMatch("province", policyDocument.getProvince());
        //“city”:””     //市            “模糊”
        match += getMatch("city", policyDocument.getCity());
        //“county”:””     //县       “模糊”
        match += getMatch("county", policyDocument.getCounty());

        String term = "";
        //精确匹配：
        //“pdId”:””,        //是否隐藏  0 否 1 是   默认 1    “精确“
        term += getTerm("pdId", (policyDocument.getPdId() == null) ? "" : policyDocument.getPdId().toString());
        //“isEffect”:””,      //是否有效 0 否 1 是    “精确“
        term += getTerm("isEffect", policyDocument.getIsEffect());
        //“isPilot”:””,      //是否试点 0 否 1 是     “精确“
        term += getTerm("isPilot", policyDocument.getIsPilot());
        //“isPub”:””,        //是否发布  0 否 1 是   默认 0    “精确“
        term += getTerm("isPub", policyDocument.getIsPub());
        //“isHide”:””,        //是否隐藏  0 否 1 是   默认 1    “精确“
        term += getTerm("isHide", policyDocument.getIsHide());

        //范围匹配：
        String range = "";
        //“createDate”:””,    //成文日期   区间查询  startTime   endTime
        range = getRange("createDate", policyDocument.getStartTime(), policyDocument.getEndTime());

        //排序
        String sort = "";
        sort = getSort("Order", policyDocument.getOrder());

    	/*
        String queryParamJson =
        		"{ " +
        			"\"query\": { " +
    	    			//模糊匹配：
    	    			match +
        	    		//精确匹配：
        	    		term +
        	    		//范围匹配：
    					range +
        	    	"}" +
        	    	//排序
        	    	sort +
        		"}";
        		*/
        String queryParamJson =
                "{" +
                        "\"query\": {" +
                        "\"bool\": {" +
                        "\"should\": [" +
                        match +
                        term +
                        range +
                        "]" +
                        "}" +
                        "}" +
                        sort +
                        "}";

        logger.debug("queryParamJson : {}", queryParamJson);

        //2、请求ES
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(queryParamJson, headers);
        String result = null;
        try {
            String strESUrl = ConfigUtil.getConfig("ES.url") + "/" + policyDocument.getIndex() + "/_search?size=" + limit + "&from=" + offset;
            logger.debug("strESUrl : {}", strESUrl);
            result = restTemplate.postForObject(strESUrl, formEntity, String.class);
        } catch (HttpClientErrorException e) {
            logger.error("Exception:{}", e);
            throw new RuntimeException("请求ES数据不存在！");
        } catch (Exception e) {
            logger.error("Exception:{}", e.getCause());
            throw new RuntimeException("请求ES发生异常！");
        }

        //3、返回结果JSON
        return result;
    }

    /**
     * 拼接Term查询
     *
     * @param value 查询条件
     * @return Term条件
     */
    private static String getTerm(String name, String value) {
        String termCondition = "";
        if (value != null && !value.equalsIgnoreCase("")) {
            //termCondition = "\"" + name + "\": \"" + value + "\"";
            termCondition = ",{ \"term\": { \"" + name + "\": \"" + value + "\" }}";
        }
        return termCondition;
    }

    /**
     * 拼接Match查询
     *
     * @param value 查询条件
     * @return Term条件
     */
    private static String getMatch(String name, String value) {
        String matchCondition = "";
        if (value != null && !value.equalsIgnoreCase("")) {
            //{ "match": { "title": "brown" }}
            matchCondition = ",{ \"match\": { \"" + name + "\": \"" + value + "\" }}";
        }
        return matchCondition;
    }

    /**
     * 拼接Match查询
     *
     * @param value 查询条件
     * @return Term条件
     */
    private static String getKeyMatch(String name, String value) {
        String matchCondition = "";
        if (value == null || value.equalsIgnoreCase("")) {
            matchCondition = "{ \"match_all\" : {  }}";
        } else {
            matchCondition = "{ \"match\": { \"" + name + "\": \"" + value + "\" }}";
        }
        return matchCondition;
    }

    /**
     * 排序
     *
     * @param name
     * @param value
     * @return 排序JSON
     * @author gaosong 2016-12-16
     */
    private static String getSort(String name, String value) {
        String sort = "";
        if (value != null && value.equalsIgnoreCase("n")) {
            sort = ",\"sort\": {\"createYear\": { \"order\": \"desc\"}}";
        }
        return sort;
    }

    /**
     * 拼接Match查询
     *
     * @param value 查询条件
     * @return Term条件
     */
    private static String getRange(String name, String gte, String lte) {
        String matchCondition = "";
        String matchte = "";
        if ((gte != null && !gte.equalsIgnoreCase("")) || (gte != null && !lte.equalsIgnoreCase(""))) {
            matchCondition = ",{\"range\": {\"" + name + "\": { ";
            if (!gte.equalsIgnoreCase("")) {
                matchte += "\"gte\":\"" + gte + "\"";
            }
            if (!matchte.equalsIgnoreCase("")) matchte += ",";
            if (!lte.equalsIgnoreCase("")) {
                matchte += "\"lte\":\"" + lte + "\"";
            }
            matchCondition += matchte + "}}}";
        }
        return matchCondition;
    }

    /**
     * 查询ES中的信息
     *
     * @param pdId 文件编号
     * @return 该文件详细信息JSON
     * @author gaosong 2016-12-16
     */
    public static String get(String pdId) {
        String queryParamJson =
                "{ " +
                        "\"query\": { " +
                        "\"bool\": {" +
                        "\"should\": [{" +
                        //“isEffect”:””,      //是否有效 0 否 1 是    “精确“
                        "\"term\": { \"pdId\": \"" + pdId + "\"" +
                        "}" +
                        "}" +
                        "]" +
                        "}" +
                        "}" +
                        "}";

        logger.debug("queryParamJson : {}", queryParamJson);

        //2、请求ES
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(queryParamJson, headers);
        String result = null;
        try {
            String strESUrl = ConfigUtil.getConfig("ES.url") + "/_search";
            result = restTemplate.postForObject(strESUrl, formEntity, String.class);
        } catch (HttpClientErrorException e) {
            logger.error("Exception:{}", e);
            throw new RuntimeException("请求ES数据不存在！");
        } catch (Exception e) {
            logger.error("Exception:{}", e.getCause());
            throw new RuntimeException("请求ES发生异常！");
        }

        //3、返回结果JSON
        return result;
    }

    /**
     * es全文检索
     *
     * @param offset
     * @param limit
     */
    public static Pair<Integer, List<Map<String, Object>>> ftRetrieval(String index, String mechanism, String theme, String source, String province, String city, String county, String startTime, String endTime, String status, int offset, int limit) {

        // 1. 串接url
        StringBuilder esUrl = new StringBuilder(ConfigUtil.getConfig("ES.url"));
        if (StringUtils.isNotBlank(index)) {
            esUrl.append("/").append(index);
        }
        esUrl.append("/_search");
        logger.debug("ESUrl : {}", esUrl.toString());

        // 2. 把查询参数拼成json串
        String queryParamJson = "{\"query\": { \"bool\": {\"should\": [{ \"match\": { \"province\": \"山西\" } },{ \"match\": { \"province\": \"河北\" } }] }}}";
        logger.debug("queryParamJson : {}", queryParamJson);

        //3. 访问ES的restful URL
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        //表单
        HttpEntity<String> formEntity = new HttpEntity<String>(queryParamJson, headers);
        String result = null;
        try {
            result = restTemplate.postForObject(esUrl.toString(), formEntity, String.class);
        } catch (HttpClientErrorException e) {
            logger.error("Exception:{}", e);
            throw new RuntimeException("请求ES数据不存在！");
        } catch (Exception e) {
            logger.error("Exception:{}", e.getCause());
            throw new RuntimeException("请求ES发生异常！");
        }

        //4.在返回结果json串中获取total和row
        return parseHits(result);
    }

    /**
     * 解析出hits中的total和rows
     */
    public static Pair<Integer, List<Map<String, Object>>> parseHits(String result) {
        if (StringUtils.isBlank(result)) {
            return Pair.of(0, new ArrayList<>());
        }
        JSONObject jsonObject = (JSONObject) JSON.parse(result);
        JSONObject hits = (JSONObject) jsonObject.get("hits");
        Integer total = hits.getInteger("total");
        JSONArray subHits = (JSONArray) hits.get("hits");
        JSONArray parseArray = JSON.parseArray(subHits.toJSONString());
        List<Map<String, Object>> rowList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < parseArray.size(); i++) {
            JSONObject json = (JSONObject) parseArray.get(i);
            JSONObject jsonSource = (JSONObject) json.get("_source");
            String esIndex = String.valueOf(json.get("_index"));
            String esType = String.valueOf(json.get("_type"));
            String esId = String.valueOf(json.get("_id"));
            Map<String, Object> map = (Map<String, Object>) JSONObject.parse(jsonSource.toJSONString());
            map.put("esIndex",esIndex);
            map.put("esType",esType);
            map.put("esId",esId);
            rowList.add(map);
        }
        return Pair.of(total, rowList);
    }

    /**
     * es修改
     */
    public static void update(PolicyDocDTO policyDocDTO,String esIndex,String esType,String esId){
        //请求ES
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        String updString = JSONObject.toJSONString(policyDocDTO);
        logger.debug("updString : {}", updString);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(updString, headers);
        try {
            String strESUrl = ConfigUtil.getConfig("ES.url") + "/" + esIndex+ "/" +esType+"/"+esId ;
            logger.debug("strESUrl : {}", strESUrl);
           // restTemplate.exchange(strESUrl, HttpMethod.PUT, formEntity,Map.class);
            restTemplate.put(strESUrl, policyDocDTO);
        } catch (HttpClientErrorException e) {
            logger.error("Exception:{}", e);
            throw new RuntimeException("请求ES数据不存在！");
        } catch (Exception e) {
            logger.error("Exception:{}", e.getCause());
            throw new RuntimeException("请求ES发生异常！");
        }
    }
}

