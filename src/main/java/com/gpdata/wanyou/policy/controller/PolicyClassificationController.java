package com.gpdata.wanyou.policy.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.policy.entity.PolicyClassification;
import com.gpdata.wanyou.policy.service.PolicyClassificationService;

/**
 * 主题分类
 * Created by wenjie on 2016/12/12
 */
@Controller
@RequestMapping
public class PolicyClassificationController extends BaseController{

	@Autowired 
	private PolicyClassificationService policyClassificationService;
	 private static final Logger LOGGER = LoggerFactory.getLogger(PolicyClassificationController.class);

    /**
     * 获取主题分类一级菜单
     */
	 @RequestMapping(value = "/zt/s"
			 , produces = MediaType.APPLICATION_JSON_UTF8_VALUE
			 , method = RequestMethod.GET)
	 @ResponseBody
	 public BeanResult getPolicyClassinfocation(HttpServletRequest request){
		 BeanResult result = null;
		 try {
			 Map<String, Object> data = new HashMap<>();
			 List<PolicyClassification> bean = policyClassificationService.queryClass();
			 data.put("data1",bean);
			 //循环获取二级分类
			 List<PolicyClassification> data2all = new ArrayList<>();
			 for (int i = 0; i < bean.size(); i++) {
				 Long policyClassificationId = bean.get(i).getPolicyclassificationId();
				 List<PolicyClassification> data2 = policyClassificationService.queryClass2(policyClassificationId);
				 if (!policyClassificationId.equals(1L)){
					 data2all.addAll(data2);
				 }
				 data.put(policyClassificationId+"",data2);
			}
			 data.put("1", data2all);
			 
			 LOGGER.debug("data :{}", data);
			 result = BeanResult.success(data);
		} catch (Exception e) {
			result = BeanResult.error("异常" + e);
			LOGGER.debug("e :{}", e);
		}
		 return result;
	 }
	 
	
}
