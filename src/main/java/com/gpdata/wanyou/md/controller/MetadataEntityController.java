package com.gpdata.wanyou.md.controller;

import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.service.SimpleService;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.base.vo.KvPair;
import com.gpdata.wanyou.md.entity.MetadataEntity;
import com.gpdata.wanyou.md.service.MetadataEntityService;
import com.gpdata.wanyou.system.entity.User;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 元数据实体
 * Created by chengchao on 2016/10/28.
 */
@Controller
@RequestMapping
public class MetadataEntityController extends BaseController {


    @Autowired
    private MetadataEntityService metadataEntityService;
    @Autowired
    private SimpleService simpleService;

    /**
     * 可用选项的缓存
     */
    private static Map<String, List<KvPair>> cache = new ConcurrentHashMap<>();

    /**
     * 基于数据映射表 id 过滤元数据实体
     *
     * @param ssmId
     * @param offset
     * @param limit
     * @param caption
     * @param metadataEntityName
     * @param matchStatus
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/md/mms/{ssmId}/mes"
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getMetadataEntitiesByConditions
    (@PathVariable(value = "ssmId") String ssmId
            , @RequestParam(required = false, defaultValue = "0") Integer offset
            , @RequestParam(required = false, defaultValue = "10") Integer limit
            , @RequestParam(required = false, defaultValue = "") String caption
            , @RequestParam(required = false, defaultValue = "") String metadataEntityName
            , @RequestParam(required = false, defaultValue = "") Integer matchStatus ) {

        try {
            if (ssmId == null) {
                return BeanResult.error("获取元数据列表失败:数据映射表 ID不能为空。 ");
            }
            Map<String, Object> params = createParametersMap
                    (pair("ssmId", ssmId)
                            , pair("offset", offset)
                            , pair("limit", limit)
                            , pair("caption", caption)
                            , pair("metadataEntityName", metadataEntityName)
                            , pair("matchStatus", matchStatus));

            Pair<Integer, List<MetadataEntity>> resultPair = this.metadataEntityService.getByConditions(params);

            if (resultPair != null) {

                return BeanResult.success(resultPair.getLeft(), resultPair.getRight());
            } else {
                return BeanResult.success(0, Arrays.asList());
            }

        } catch (Exception e) {
            logger.error("获取元数据实体列表时 : ", e);
            return BeanResult.error("获取元数据实体列表失败: " + e.getMessage());
        }

    }

    /**
     * 获取单个对象
     *
     * @param entityId
     * @return
     */
    @RequestMapping(value = "/md/mes/{entityId}"
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getMetadataEntity(@PathVariable Integer entityId) {

        BeanResult result = null;

        try {
            MetadataEntity metadataEntity = this.simpleService
                    .getById(MetadataEntity.class, entityId);
            result = BeanResult.success(metadataEntity);
        } catch (Exception e) {
            logger.error("exception ", e);
            result = BeanResult.error(e.getMessage());
        }
        return result;
    }


    /**
     * 修改对象
     *
     * @param entityId
     * @return
     */
    @RequestMapping(value = "/md/mes/{entityId}"
            , method = RequestMethod.PUT
            , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult updateMetadataEntity(@PathVariable Integer entityId
            , @RequestBody MetadataEntity metadataEntity) {

        metadataEntity.setEntityId(entityId);

        BeanResult result = null;

        try {
            this.simpleService.update(entityId, metadataEntity);
            result = BeanResult.success(entityId);
        } catch (Exception e) {
            logger.error("exception ", e);
            result = BeanResult.error(e.getMessage());
        }
        return result;
    }


    /**
     * 删除对象
     *
     * @param entityId
     * @return
     */
    @RequestMapping(value = "/md/mes/{entityId}"
            , method = RequestMethod.DELETE
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult deleteMetadataEntity(@PathVariable Integer entityId) {

        BeanResult result = null;

        try {
            this.simpleService.delete(MetadataEntity.class, entityId);
            result = BeanResult.success(entityId);
        } catch (Exception e) {
            logger.error("exception ", e);
            result = BeanResult.error(e.getMessage());
        }
        return result;
    }


    /**
     * 创建新对象
     *
     * @param metadataEntity
     * @return
     */
    @RequestMapping(value = "/md/mes"
            , method = RequestMethod.POST
            , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult createMetadataEntity(@RequestBody MetadataEntity metadataEntity
            , HttpServletRequest request) {

        BeanResult result = null;


        try {
            User user = this.getCurrentUser(request);
            Date current = new Date();

            metadataEntity.setCreateDate(current);
            this.simpleService.save(metadataEntity);
            result = BeanResult.success(metadataEntity.getEntityId());
        } catch (Exception e) {
            logger.error("exception ", e);
            result = BeanResult.error(e.getMessage());
        }
        return result;
    }

    /**
     * 显示可用的数据类型的可用选项
     * @return
     */
    @RequestMapping(value = "/md/mes/data-types"
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<KvPair> getDataTypes() {

        List<KvPair> result = cache.computeIfAbsent("data-type", (Void) -> Arrays.asList(
                KvPair.of("int", "int"),
                KvPair.of("double", "double"),
                KvPair.of("string", "string"),
                KvPair.of("date", "date"),
                KvPair.of("blob", "blob")
        ));

        return result;
    }

    /**
     * 显示是否为空的可用选项
     * @return
     */
    @RequestMapping(value = "/md/mes/allow-null-options"
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<KvPair> getAllowNullOptions() {

        List<KvPair> result = cache.computeIfAbsent("allow-null", (Void) -> Arrays.asList(
                KvPair.of("1", "是"),
                KvPair.of("0", "否")
        ));

        return result;
    }

    /**
     * 显示是否唯一的可用选项
     * @return
     */
    @RequestMapping(value = "/md/mes/is-unique-options"
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<KvPair> getIsUniqueOptions() {

        List<KvPair> result = cache.computeIfAbsent("is-unique", (Void) -> Arrays.asList(
                KvPair.of("1", "是"),
                KvPair.of("0", "否")
        ));

        return result;
    }


}
