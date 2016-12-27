package com.gpdata.wanyou.policy.controller;

import com.gpdata.wanyou.ansj.util.AnsjUtil;
import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.service.SimpleService;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.policy.entity.PolicyDocument;
import com.gpdata.wanyou.policy.entity.PolicyIndex;
import com.gpdata.wanyou.policy.entity.PolicyTags;
import com.gpdata.wanyou.policy.service.PolicyRelateService;
import com.gpdata.wanyou.policy.service.TranslateService;
import com.gpdata.wanyou.policy.util.FtRetrievalUtil;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.user.service.UserKeywordService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by wenjie on 2016/12/13.
 */
@Controller
@RequestMapping
public class TranslateController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TranslateController.class);

    @Autowired
    private AnsjUtil ansjUtil;
    @Autowired
    private UserKeywordService userKeywordService; //用户关键字存储
    @Autowired
    private PolicyRelateService policyRelateService;
    @Autowired
    private SimpleService simpleService;
    @Autowired
    private TranslateService translateService;

    /**
     * 根据用户选择的类型关键字和用户所选的类型搜索ES
     *
     * @param request
     * @param str
     * @return
     */
    @RequestMapping(value = "/ts/q", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public BeanResult getTranslate(HttpServletRequest request
            , @RequestBody PolicyDocument policy
            , @RequestParam(name = "str", required = false) String str
            , @RequestParam(name = "offset", required = false, defaultValue = "0") int offset
            , @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {

        BeanResult result = null;
        try {
            Map<String, Object> resultMap = new HashMap<>();
            //切词
            List<String> keyWords = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            if (!StringUtils.isAnyBlank(str)) {
                keyWords = ansjUtil.parseString(str, true);
                keyWords.forEach(str1 -> sb.append(str1).append(" "));
                //返回关键字

                resultMap.put("keyWords", sb);
                policy.setKeyWords(sb.toString());
                //存入用户的keyword
                User currentUser = getCurrentUser(request);
                userKeywordService.appendKeywords(currentUser, keyWords);
            }
            LOGGER.debug("关键字 : {}", sb);

            String results = FtRetrievalUtil.search(policy, offset, limit);//查询ES
            Pair<Integer, List<Map<String, Object>>> resultPair = FtRetrievalUtil.parseHits(results);

            resultMap.put("total", resultPair.getLeft());
            List<Map<String, Object>> rows = resultPair.getRight(); //定义一个Map来装pdID

            //循环
            for(Map<String, Object> map : rows){

            	Object pdIdObj = map.get("pdId");
            	if (pdIdObj != null) {
            		Long pdId = Long.valueOf(pdIdObj.toString(), 10);
            		List<PolicyTags> tags = this.translateService.getPolicyTags(pdId);
            		if (tags != null ) {
            			map.put("tags", tags);
            		} else {
            			map.put("tags", Collections.emptyList());
            		}
            	}

            }

            resultMap.put("rows", rows);

            result = BeanResult.success(resultMap);
        } catch (Exception e) {
            result = BeanResult.error("异常" + e);
            LOGGER.debug("e : {}", e);
        }
        return result;
    }

    /**
     * 获取单条政策文件
     */
    @RequestMapping(value = "/list/q/{pdId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getPolicyLits(HttpServletRequest request, @PathVariable("pdId") Long pdId) {
        BeanResult results = null;
        PolicyDocument policy = new PolicyDocument();
        policy.setPdId(pdId);
        policy.setIndex("");
        try {
            String result = FtRetrievalUtil.search(policy, 0, 1);
            Pair<Integer, List<Map<String, Object>>> resultPair = FtRetrievalUtil.parseHits(result);
            LOGGER.debug("pdId : {}", pdId);
            Map<String, Object> maps = new HashMap<>();
            maps.put("total", resultPair.getLeft());
            maps.put("rows", resultPair.getRight());
            results = BeanResult.success(maps);

        } catch (Exception e) {
            results = BeanResult.error("异常" + e);
            LOGGER.debug("e :{}", e);
        }
        return results;
    }

    /**
     *  猜你喜欢和相关政策
     *  @author lyt
     */
    @RequestMapping(value = "/relate/{pdId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public BeanResult relatedPolicy(HttpServletRequest request,@PathVariable("pdId") Long pdId){
        BeanResult results = null;
        try {
            if (simpleService.getById(PolicyIndex.class,pdId) == null){
                return BeanResult.error("该文章不存在");
            }
            String[] indexs = {"zcjd","jyta","zcfg","zcwj"};
            Map<String, Object> map = policyRelateService.getRelatedDocs(pdId, indexs);
            results = BeanResult.success(map);
        } catch (Exception e) {
            results = BeanResult.error("异常" + e);
            logger.debug("出现未知异常");
        }
        logger.debug("result",results);
        return results;
    }
}
