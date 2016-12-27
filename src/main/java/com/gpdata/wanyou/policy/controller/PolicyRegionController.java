package com.gpdata.wanyou.policy.controller;

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
import com.gpdata.wanyou.policy.entity.PolicyRegion;
import com.gpdata.wanyou.policy.service.PolicyRegionService;

/**
 * 颁布地区数据获取
 * @author wenjie
 *
 */
@Controller
@RequestMapping
public class PolicyRegionController extends BaseController{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PolicyRegionController.class);
	@Autowired 
	private PolicyRegionService solicyRegionService;
	/**
	 * 获取一级菜单
	 */
	
	@RequestMapping(value = "/bb/s"
			 , produces = MediaType.APPLICATION_JSON_UTF8_VALUE
			 , method = RequestMethod.GET)
	 @ResponseBody
	 public BeanResult getPolicyRegion(HttpServletRequest request){
		 BeanResult result = null;
		 try {
			 Map<String, Object> data = new HashMap<>();
			 List<PolicyRegion> bean = solicyRegionService.queryClass();
			 data.put("data1",bean);
			 //循环获取二级分类
			 for (int i = 0; i < bean.size(); i++) {
				Long PolicyclassificationId = bean.get(i).getRegionId();
				List<PolicyRegion> data2 = solicyRegionService.queryClass2(PolicyclassificationId);
				
				data.put(PolicyclassificationId+"",data2);
			}
			 LOGGER.debug("data :{}", data);
			 result = BeanResult.success(data);
		} catch (Exception e) {
			result = BeanResult.error("异常" + e);
			LOGGER.debug("e :{}", e);
		}
		 return result;
	 }
	
}
