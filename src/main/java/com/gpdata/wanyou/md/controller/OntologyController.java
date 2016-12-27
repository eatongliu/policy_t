package com.gpdata.wanyou.md.controller;

import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.md.entity.OntologyBaseinfo;
import com.gpdata.wanyou.md.service.OntologyService;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.utils.ArgumentsUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 3.1.2本体
 */
@Controller
@RequestMapping()
public class OntologyController extends BaseController {



    @Autowired
    private OntologyService ontologyService;


    /**
     * 查看选定本体详细信息
     * <p>
     * /ot/ba/g 说明：显示具体某个本体的详细信息。 参数：ontologyid本体ID（必填）
     * 成功：表ontology_baseinfo中指定ontologyid的记录 失败：[“error”:”错误原因”]
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/ot/ba/{ontologyId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE
            , method = RequestMethod.GET)
    @ResponseBody
    public BeanResult viewOntology(@PathVariable(value = "ontologyId") Integer ontologyId ) {

        try {
            logger.debug("viewOntologyInput : {}", ontologyId);
            OntologyBaseinfo ontology = ontologyService.getOntologyById(ontologyId);
            return BeanResult.success(ontology);
        } catch (Exception e) {
            logger.error("viewOntologyGroupException : ", e);
            return BeanResult.error("获取失败 ： " + e.getMessage());
        }
    }

    /**
     * 更新本体
     *
     * @param ontologyId
     * @return
     */
    @RequestMapping(value = "/ot/ba/{ontologyId}"
            , method = RequestMethod.PUT
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE
            )
    @ResponseBody
    public BeanResult updateOntology(@PathVariable(value = "ontologyId") Integer ontologyId
            , @RequestBody OntologyBaseinfo ontologyBaseinfo ) {


        logger.debug("updateOntologyInput : {}", ontologyBaseinfo);
        ontologyBaseinfo.setOntologyId(ontologyId);

        if (ontologyBaseinfo == null) {
            return BeanResult.error("修改失败 ： 没有提交数据到服务器!");
        }
        try {

            ontologyService.updateOntology(ontologyBaseinfo);
            return BeanResult.success(ontologyId);
        } catch (Exception e) {
            logger.error("updateOntologyGroupException : ", e);
            return BeanResult.error("修改失败 ： " + e.getMessage());
        }
    }

    /**
     * 添加本体
     *
     * @param ontologyBaseinfo
     * @return
     */
    @RequestMapping(value = "/ot/ba"
            , method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE
            )
    @ResponseBody
    public BeanResult addOntology(@RequestBody OntologyBaseinfo ontologyBaseinfo, HttpServletRequest request) {

        logger.debug("addOntologyInput : {}", ontologyBaseinfo);

        if (ontologyBaseinfo == null) {
            return BeanResult.error("修改失败 ： 没有提交数据到服务器!");
        }

        if (StringUtils.isBlank(ontologyBaseinfo.getCaption())) {
            return BeanResult.error("修改失败 ： 请填写标题 (Caption) !");
        }

        User currentUser = (User) request.getAttribute("currentUser");

        if (currentUser == null) {
            return BeanResult.error("保存失败 ： 用户未登录.");
        }

        try {

            Integer ontologyId = ontologyService.addOntology(ontologyBaseinfo, currentUser);

            return BeanResult.success(ontologyId);

        } catch (Exception e) {
            logger.error("addOntologyGroupException : ", e);
            return BeanResult.error("保存失败 ： " + e.getMessage());
        }

    }

    /**
     * 删除本体组
     *
     * @param ontologyid
     * @return
     */
    @RequestMapping(value = "/ot/ba/{ontologyid}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            method = RequestMethod.DELETE)
    @ResponseBody
    public BeanResult deleteOntology(HttpServletResponse response,
                                     @PathVariable(value = "ontologyid") Integer ontologyid) {

        logger.debug("deleteOntologyInput : {}", ontologyid);
        try {
            ontologyService.deleteOntology(ontologyid);
            return BeanResult.success("");
        } catch (Exception e) {
            logger.error("deleteOntologyGroupException : ", e);
            return BeanResult.error("删除失败 ： " + e.getMessage());
        }
    }

    /**
     * 查询本体 GET : /ot/gp/{groupId}/ba
     *
     * 说明：检索本体信息 参数1：groupid 本体组id（必填） 参数2：caption 标题
     * 成功：检索本体信息列表，caption模糊匹配 失败：[“error”:”错误原因”]
     *
     * @param groupId
     * @param caption
     * @param limit
     * @param offset
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/ot/gp/{groupId}/ba",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            method = RequestMethod.GET)
    @ResponseBody
    public BeanResult searchOntology(@PathVariable(value = "groupId") Integer groupId,
                                     @RequestParam(name = "caption", required = false) String caption,
                                     @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                     @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset) {

        if (logger.isDebugEnabled()) {
            Object[] argArray = {groupId, caption, limit, offset};
            logger.debug("searchOntologyBaseinfo groupid , caption , limit ,offset: {} , {} , {}", argArray);
        }
        try {

            if (limit > 100 || limit < 1) {
                limit = 10;
            }
            OntologyBaseinfo ontologyBaseinfo = new OntologyBaseinfo();
            ontologyBaseinfo.setGroupId(groupId);
            ontologyBaseinfo.setCaption(caption);

            Pair<Integer, List<OntologyBaseinfo>> searchResult = ontologyService
                    .searchOntologyBaseinfo(ontologyBaseinfo, limit, offset);

            return BeanResult.success(searchResult.getLeft(), searchResult.getRight());
        } catch (Exception e) {
            logger.error("searchOntologyException : ", e);
            return BeanResult.error("获取失败 ： " + e.getMessage());
        }

    }


    /**
     * 修改元数据和本体的关系
     * @return
     */
    @RequestMapping(value = "/ot/ba/{ontologyId}/mat",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            method = RequestMethod.PUT)
    @ResponseBody
    public BeanResult matchMetadataBeanToOntology(@PathVariable Integer ontologyId
        , @RequestBody Integer[] metadataIds) {
        BeanResult beanResult = null;

        Set<Integer> metadataIdSet = new HashSet<>();

        if (metadataIds != null && metadataIds.length > 0) {
            metadataIdSet.addAll(Arrays.asList(metadataIds));
        }

        try {
            this.ontologyService.updateMetadataBeanMatch(ontologyId, metadataIdSet);
            beanResult = BeanResult.success(ontologyId);
        } catch (Exception e) {
            logger.error("exception : ", e);
            beanResult = BeanResult.error(e.getMessage());
        }

        return beanResult;
    }
}
