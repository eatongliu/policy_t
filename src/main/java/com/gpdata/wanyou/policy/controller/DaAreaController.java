package com.gpdata.wanyou.policy.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gpdata.wanyou.base.controller.BaseController;
/**
 * 省市县3级联动数据
 * @author admin
 *
 */
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.policy.entity.DaArea;
import com.gpdata.wanyou.policy.service.DaAreaService;
@Controller
@RequestMapping
public class DaAreaController extends BaseController{

	private static final Logger LOGGER = LoggerFactory.getLogger(DaAreaController.class);
	
	@Autowired 
	private DaAreaService daAreaService;
	/**
	 * 获取省级
	 */
	@RequestMapping(value = "/ad/p",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
	@ResponseBody
	public BeanResult getProvince(HttpServletRequest request){
		BeanResult result = null;
		
		try {
			 List<DaArea> bean = daAreaService.queryClass();
			 LOGGER.debug("bean : {}", bean);
			 result = BeanResult.success(bean);
		} catch (Exception e) {
			result = BeanResult.error("异常" + e);
			LOGGER.debug("e :{}",e );
		}
		return result;
	}
	
	/**
	 * 根据省级ID   codeid ，获取市
	 */
	@RequestMapping( value ="/ad/c/{codeid}" 
			, method = RequestMethod.GET
//			, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
//			, produces = MediaType.APPLICATION_JSON_UTF8_VALUE 
			)
	@ResponseBody
	
	public BeanResult getCity(HttpServletRequest request,@PathVariable("codeid") Long codeid){
		
		BeanResult result = null;
		try {
			List<DaArea> city = daAreaService.querycity(codeid);
			LOGGER.debug("city : {}", city);
			
			result = BeanResult.success(city);
			
		} catch (Exception e) {
			result = BeanResult.error("异常" + e);
			LOGGER.debug("e :{}", e);
		}
		return result;
	}
}
