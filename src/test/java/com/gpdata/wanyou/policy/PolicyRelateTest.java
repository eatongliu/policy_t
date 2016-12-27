package com.gpdata.wanyou.policy;

import com.gpdata.wanyou.base.service.SimpleService;
import com.gpdata.wanyou.policy.entity.PolicyIndex;
import com.gpdata.wanyou.policy.service.PolicyRelateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * Created by acer_liuyutong on 2016/12/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext.xml"
})
public class PolicyRelateTest {
    private static final Logger logger = LoggerFactory.getLogger(PolicyRelateTest.class);
    @Autowired
    private PolicyRelateService policyRelateService;
    @Autowired
    private SimpleService simpleService;

    @Test
    public void testGetRelateDocs(){
        String[] indexs = {"zcjd","jyta","zcfg","zcwj"};
        Long pdId = 1L;
        Map<String, Object> map = policyRelateService.getRelatedDocs(pdId, indexs);
        logger.debug("enjoy:{}",map.get("enjoy"));
        logger.debug("relate:{}",map.get("relate"));
    }
    @Test
    public void tesetMerge(){
        PolicyIndex policyIndex = new PolicyIndex();
        policyIndex.setPdId(5L);
        policyIndex.setIndex_("zcfg");
        policyIndex.setCreateDate("2016-12-21");
        policyIndex.setPdName("我的奋斗");
        policyIndex.setTopicClassify("励志");
        simpleService.merge(policyIndex);
    }

}
