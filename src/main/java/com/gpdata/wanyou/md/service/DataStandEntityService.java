package com.gpdata.wanyou.md.service;

import com.gpdata.wanyou.md.entity.DataStandEntity;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

/**
 * 数据标准实体
 * <p>
 * Created by ligang on 16-10-25.
 */
public interface DataStandEntityService {


	/**
	 * 说明：获取指定 id 的对象 
	 * 参数：standentid数据标准实体 id（必填）
	 * 成功：表data_stand_entity中指定standentid的记录
	 * 
	 * @param standentid
	 * @return DataStandEntity/[“error”:”错误原因”]
	 */
	public DataStandEntity getById(Integer standentid);
	
	/**
	 * 说明：保存新对象 
	 * 参数：数据标准实体对象DataStandEntity（必填）
	 * 成功：standentid
	 * 
	 * @param DataStandEntity
	 * @return standentid/[“error”:”错误原因”]
	 */
	public int save(DataStandEntity input);

	/**
	 * 说明：修改 
	 * 参数：数据标准实体对象DataStandEntity（必填） 
	 * 成功：status : SUCCESS
	 * 
	 * @param DataStandEntity
	 * @return status : SUCCESS/[“error”:”错误原因”]
	 */
	public void update(DataStandEntity input);
	
	/**
	 * 说明：删除 
	 * 参数：standentid（必填） 
	 * 成功：status : SUCCESS
	 * 
	 * @param DataStandEntity
	 * @return status : SUCCESS/[“error”:”错误原因”]
	 */
	public void delete(Integer standentid);
	
	/**
	 * 说明：查询 
	 * 参数：
	 * 成功：数据对象 
	 * 
	 * @param DataStandEntity
	 * @return 数据对象/[“error”:”错误原因”]
	 */
	public Map<String, Object> query(DataStandEntity input,Integer limit,Integer offset) ;
	
	/**
     * 说明： 接收一个上传的导入文件 
     * 参数：导入的文件
     * 成功：{"status":"SUCCESS"}
     * 失败： {"status":"ERROR","cause":"具体错误信息"}
     * @param MultipartFile file
     * @return 
     */
	public void upload(List<DataStandEntity> list);
	
	/**
	 * 获取指定数据标准下的所有数据标准实体
	 * @param standId
	 * @param limit
	 * @param offset
	 * @return
	 */
	public Map<String, Object> getAllByStandId(Integer standId,Integer limit,Integer offset);
	
}
