package com.gpdata.wanyou.md.controller;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.md.dto.MetadataCompareDto;
import com.gpdata.wanyou.md.service.MetadataCompareService;

/**
 * 元数据实体比对
 * @author acer_liuyutong
 */
@Controller
public class MetadataCompareController extends BaseController{
	
	@Autowired
	private MetadataCompareService metadataCompareService;
	
	/**
	 * 元数据比对列表
	 */
	@RequestMapping(value="/md/mc"
	            , method=RequestMethod.GET
	            , produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String queryDataStandards(@RequestParam(name="resourceId",required=false) Integer resourceId
			, @RequestParam(name="tableId",required=false) Integer tableId
			, @RequestParam(name="limit",required=false,defaultValue="10") Integer limit
			, @RequestParam(name="offset",required=false,defaultValue="0") Integer offset ) {
		
		if (logger.isDebugEnabled()) {
			Object[] argArray = {resourceId,tableId,limit,offset};
			logger.debug("resourceId,tableId,limit,offset:{},{},{},{}",argArray);
		}
		
		BeanResult beanResult = null;
		
		try {
			Pair<Integer, List<MetadataCompareDto>> pair = metadataCompareService.getMetadataCompareList(resourceId,tableId,offset,limit);
	    	beanResult = BeanResult.success(pair.getLeft(),pair.getRight());
	    	
	    } catch (Exception e) {
	    	logger.error("Exception : {}", e);
	    	beanResult = BeanResult.error("获取比对结果失败！");
	    }

		String resultString = JSONObject.toJSONString(beanResult);
		
		if (logger.isDebugEnabled()) {
			logger.debug("result : {} ",resultString);
		}
		
		return resultString;
	}
	
}