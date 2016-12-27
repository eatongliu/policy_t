package com.gpdata.wanyou.md.controller;

import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.service.SimpleService;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.md.entity.MetadataBean;
import com.gpdata.wanyou.md.entity.MetadataEntity;
import com.gpdata.wanyou.md.service.MetadataBeanService;
import com.gpdata.wanyou.system.entity.User;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 元数据
 * Created by chengchao on 2016/10/28.
 */
@Controller
@RequestMapping
public class MetadataBeanController extends BaseController {


    @Autowired
    private MetadataBeanService metadataBeanService;
    @Autowired
    private SimpleService simpleService;


    /**
     * 基于本体 id 过滤元数据
     *
     * @param ontologyId
     * @param offset
     * @param limit
     * @param caption
     * @return
     */
    @RequestMapping(value = "/md/mds"
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getMetadataEntitiesByConditions(@RequestParam(required = false, defaultValue = "") Integer ontologyId
            , @RequestParam(required = false, defaultValue = "0") Integer offset
            , @RequestParam(required = false, defaultValue = "10") Integer limit
            , @RequestParam(required = false, defaultValue = "") String metadataName
            , @RequestParam(required = false, defaultValue = "") String caption) {

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> params = createParametersMap
                    (pair("ontologyId", ontologyId)
                            , pair("metadataName", metadataName)
                            , pair("offset", offset)
                            , pair("limit", limit)
                            , pair("caption", caption));

            Pair<Integer, List<MetadataBean>> resultPair = this.metadataBeanService.getByConditions(params);

            if (resultPair != null) {
                return BeanResult.success(resultPair.getLeft(), resultPair.getRight());
            } else {
                return BeanResult.success(0, Arrays.asList());
            }

        } catch (Exception e) {
            logger.error("获取元数据列表时 : ", e);
            return BeanResult.error("获取元数据列表失败: " + e.getMessage());
        }

    }

    /**
     * 获取单个对象
     *
     * @param metadataId
     * @return
     */
    @RequestMapping(value = "/md/mds/{metadataId}"
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getMetadataEntity(@PathVariable Integer metadataId) {

        BeanResult result = null;

        try {
            MetadataBean metadataBean = this.metadataBeanService
                    .getMetadataEntitiesByMetadataId(metadataId);

            result = BeanResult.success(metadataBean);
        } catch (Exception e) {
            logger.error("exception ", e);
            result = BeanResult.error(e.getMessage());
        }
        return result;
    }


    /**
     * 修改对象
     *
     * @param metadataId
     * @return
     */
    @RequestMapping(value = "/md/mds/{metadataId}"
            , method = RequestMethod.PUT
            , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult updateMetadataEntity(@PathVariable Integer metadataId
            , @RequestBody MetadataBean metadataBean) {

        metadataBean.setMetadataId(metadataId);

        BeanResult result = null;

        try {
            this.simpleService.update(metadataId, metadataBean);
            result = BeanResult.success(metadataId);
        } catch (Exception e) {
            logger.error("exception ", e);
            result = BeanResult.error(e.getMessage());
        }
        return result;
    }


    /**
     * 修改多个元数据实体对象, 并关联到一个元数据上
     *
     * @param metadataId       元数据 id
     * @param eetadataEntities
     * @return
     */
    @RequestMapping(value = "/md/mds/{metadataId}/mes"
            , method = RequestMethod.PUT
            , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult updateMetadataEntities(@PathVariable Integer metadataId
            , @RequestBody MetadataEntity[] eetadataEntities) {

        BeanResult result = null;

        Set<MetadataEntity> set = Stream.of(eetadataEntities).collect(Collectors.toSet());

        try {
            this.metadataBeanService.updateMetadataEntities(metadataId, set);
            result = BeanResult.success(metadataId);
        } catch (Exception e) {
            logger.error("exception ", e);
            result = BeanResult.error(e.getMessage());
        }
        return result;
    }


    /**
     * 删除对象
     *
     * @param metadataId
     * @return
     */
    @RequestMapping(value = "/md/mds/{metadataId}"
            , method = RequestMethod.DELETE
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult deleteMetadataEntity(@PathVariable Integer metadataId) {

        BeanResult result = null;

        try {
            this.simpleService.delete(MetadataBean.class, metadataId);
            result = BeanResult.success(metadataId);
        } catch (Exception e) {
            logger.error("exception ", e);
            result = BeanResult.error(e.getMessage());
        }
        return result;
    }


    /**
     * 创建新对象
     *
     * @param metadataBean
     * @return
     */
    @RequestMapping(value = "/md/mds"
            , method = RequestMethod.POST
            , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult createMetadataEntity(@RequestBody MetadataBean metadataBean
            , HttpServletRequest request) {

        BeanResult result = null;

        try {
            User user = this.getCurrentUser(request);
            metadataBean.setCreateDate(new Date());
            metadataBean.setUserId(String.valueOf(user.getUserId()));
            metadataBean.setIsStand("否");
            this.simpleService.save(metadataBean);
            result = BeanResult.success(metadataBean.getMetadataId());
        } catch (Exception e) {
            logger.error("exception ", e);
            result = BeanResult.error(e.getMessage());
        }
        return result;
    }

}
