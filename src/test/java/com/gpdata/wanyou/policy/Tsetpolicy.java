package com.gpdata.wanyou.policy;

import java.util.List;

import org.apache.hadoop.hive.common.Pool.PoolObjectHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.facebook.presto.jdbc.internal.guava.util.concurrent.CycleDetectingLockFactory.Policy;
import com.gpdata.wanyou.policy.entity.PolicyDocument;
import com.gpdata.wanyou.policy.entity.PolicyTags;
import com.gpdata.wanyou.policy.service.PolicyTagsService;
import com.gpdata.wanyou.policy.util.FtRetrievalUtil;



/**
 * 
 * @author wenjie
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext.xml"
})
public class Tsetpolicy {

	@Autowired
	private PolicyTagsService policyTagsService;
	
	/**
	 * 权重
	 */
	@Test
	public void testPolicy(){
//		String pdId = "1";
	 
	    List<PolicyTags> res = policyTagsService.getTags("1");
	    
	    System.out.println(res.size());
	    
	}
	
	@Test
	public void getESTest(){
		 PolicyDocument policy = new PolicyDocument();
		 policy.setIndex("zcfg");
		 int offset = 0;
		 int limit = 4;
		 String pair = FtRetrievalUtil.search(policy,offset,limit);
		 
		 System.out.println(pair);
	}
}
