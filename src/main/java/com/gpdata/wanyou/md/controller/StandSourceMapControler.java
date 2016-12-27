package com.gpdata.wanyou.md.controller;

import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.md.entity.StandSourceMap;
import com.gpdata.wanyou.md.service.StandSourceMapService;
import com.gpdata.wanyou.system.entity.User;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据标准数据源映射
 *
 * @author acer_liuyutong
 */
@Controller
public class StandSourceMapControler extends BaseController{

    @Autowired
    private StandSourceMapService standSourceMapService;

    /**
     * 查询映射信息
     *
     * @param caption
     * @param limit
     * @param offset
     * @return
     */
    @RequestMapping(value = "/md/ssm"
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String queryStandSourceMaps(HttpServletRequest request
            , @RequestParam(name = "caption", required = false) String caption
            , @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit
            , @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset) {

        if (logger.isDebugEnabled()) {
            Object[] argArray = {caption, limit, offset};
            logger.debug("String caption , int limit ,int offset: {} , {} , {}", argArray);
        }

		BeanResult result = null;
		
		try {
			Pair<Integer, List<StandSourceMap>> pair = standSourceMapService.queryStandSourceMaps(caption, limit, offset);
			
			Integer total = pair.getLeft();
			List<StandSourceMap> rows = pair.getRight();
			
			result = BeanResult.success(total, rows);
			
		} catch (Exception e) {
			logger.error("Exception : {}", e.getCause());
			result = BeanResult.error("查询映射列表发生错误！");
		}
		
		String resultString = JSONObject.toJSONString(result);
		
		if (logger.isDebugEnabled()) {
			logger.debug("resultString : {}" ,resultString);
		}
		
		return resultString;
	}
	
	/**
	 * 使用数据标准和数据源生成映射对象, 同时生成元数据实体和元数据
	 * @param request
	 * @param standSourceMap
	 */
	@RequestMapping(value="/md/ssm"
			, method=RequestMethod.POST
			, consumes="application/json"
			, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String addSandSourceMap(HttpServletRequest request
			, @RequestBody StandSourceMap standSourceMap){
		
		if (logger.isDebugEnabled()) {
			logger.debug("standSourceMap : {} ", standSourceMap);
		}

        Integer standId = standSourceMap.getStandId();
        Integer sourceId = standSourceMap.getSourceId();

        if (standId == null || sourceId == null) {
            return JSONObject.toJSONString(BeanResult.error("数据标准 id / 数据源 id 需要同时存在!"));
        }
		
		BeanResult beanResult = null;
		
		try {
			User creator = this.getCurrentUser(request);
            standSourceMap.setUserId(creator.getUserId().toString());

			standSourceMap.setCreateDate(new Date());

			String ssmId = standSourceMapService.saveStandSourceMap(standSourceMap);
			Map<String, Object> map = new HashMap<>();
			map.put("ssmId", ssmId);
			beanResult = BeanResult.success(map);
			
		} catch (Exception e) {
			logger.error("Exception : {} ", e.getCause());
			beanResult = BeanResult.error(e.getMessage());
		}
		
		String resultString = JSONObject.toJSONString(beanResult);
		
		if (logger.isDebugEnabled()) {
			logger.debug("result : {} ", resultString);
		}
		
		return resultString;
	}
	
	/**
	 * 删除一个映射, 如果有于此对象相关联的对象存在,不许删除.
	 * @param request
	 * @param ssmId
	 */
	@RequestMapping(value="/md/ssm/{ssmId}"
			, method=RequestMethod.DELETE
			, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String deleteSandSourceMap(HttpServletRequest request
			, @PathVariable("ssmId") String ssmId){
		
		if (logger.isDebugEnabled()) {
			logger.debug("String ssmId : {} ", ssmId);
		}
		
		BeanResult beanResult = null;
		
        try {
        	standSourceMapService.delete(ssmId);
        	beanResult = BeanResult.success(null);
        	
        } catch (Exception e) {
        	logger.error("Exception : {} ", e);
        	beanResult = BeanResult.error(e.getMessage());
        }
            
		String resultString = JSONObject.toJSONString(beanResult);
		
		if (logger.isDebugEnabled()) {
			logger.debug("result : {} ", resultString);
		}
		
		return resultString;
	}

}