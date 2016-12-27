package com.gpdata.wanyou.md.service.impl;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.md.dao.MetadataCompareDao;
import com.gpdata.wanyou.md.dto.MetadataCompareDto;
import com.gpdata.wanyou.md.service.MetadataCompareService;

/**
 * 元数据比对
 * @author acer_liuyutong
 */
@Service
public class MetadataCompareServiceImpl extends BaseService implements MetadataCompareService{
	
	@Autowired
	private MetadataCompareDao metadataCompareDao;

	/**
	 * 元数据比对列表展示
	 */
	@Override
	public Pair<Integer, List<MetadataCompareDto>> getMetadataCompareList(Integer resourceId,Integer tableId,Integer offset, Integer limit) {
		Integer total = metadataCompareDao.getTotal(resourceId,tableId,offset,limit);
		List<MetadataCompareDto> rows = metadataCompareDao.getRows(resourceId,tableId,offset,limit);
		return Pair.of(total, rows);
	}
	
}
