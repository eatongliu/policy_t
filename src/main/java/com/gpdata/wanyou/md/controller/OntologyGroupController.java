package com.gpdata.wanyou.md.controller;

import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.service.SimpleService;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.md.entity.OntologyGroup;
import com.gpdata.wanyou.md.service.OntologyService;
import com.gpdata.wanyou.system.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 3.1.1本体组 Created by chengchao on 16-10-12.
 */

@Controller
@RequestMapping()
public class OntologyGroupController extends BaseController {


    @Autowired
    private OntologyService ontologyService;


    @Autowired
    private SimpleService simpleService;

    /**
     * 查看选定本体组详细信息
     *
     * @param groupid
     * @return
     */
    @RequestMapping(value = "/ot/gp/{groupid}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getOntologyGroup(@PathVariable(value = "groupid") Integer groupid) {
        try {
            logger.debug("viewOntologyGroupInput : {}", groupid);
            OntologyGroup ontologyGroup = ontologyService.getGroupById(groupid);
            return BeanResult.success(ontologyGroup);
        } catch (Exception e) {
            logger.error("viewOntologyGroupException : ", e);
            return BeanResult.error("获取失败 ： " + e.getMessage());
        }
    }

    /**
     * 添加本体组
     *
     * @param ontologyGroup
     * @return
     */
    @RequestMapping(value = "/ot/gp",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public BeanResult addOntologyGroup(@RequestBody OntologyGroup ontologyGroup, HttpServletRequest request) {

        logger.debug("addOntologyGroupInput : {}", ontologyGroup);

        if (StringUtils.isBlank(ontologyGroup.getCaption())) {
            return BeanResult.error("Caption 不可为空");
        }



        try {

            User currentUser = (User) request.getAttribute("currentUser");
            ontologyGroup.setUserId(String.valueOf(currentUser.getUserId()));


            int groupid = ontologyService.addOntologyGroup(ontologyGroup);

            return BeanResult.success(groupid);
        } catch (Exception e) {
            logger.error("addOntologyGroupException : ", e);
            return BeanResult.error("添加失败 ： " + e.getMessage());
        }
    }

    /**
     * 更新本体组
     *
     * @param ontologyGroup
     * @return
     */
    @RequestMapping(value = "/ot/gp/{groupId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    @ResponseBody
    public BeanResult updateOntologyGroup(@PathVariable Integer groupId, @RequestBody OntologyGroup ontologyGroup) {

        logger.debug("updateOntologyGroupInput : {}", ontologyGroup);

        try {
            ontologyGroup.setGroupId(groupId);
            this.simpleService.update(groupId, ontologyGroup);
            return BeanResult.success(groupId);
        } catch (Exception e) {
            logger.error("updateOntologyGroupException : ", e);
            return BeanResult.error("修改失败 ： " + e.getMessage());
        }
    }

    /**
     * 删除本体组
     *
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/ot/gp/{groupId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.DELETE)
    @ResponseBody
    public BeanResult deleteOntologyGroup(@PathVariable(value = "groupId") Integer groupId) {

        logger.debug("deleteOntologyGroupInput : {}", groupId);
        try {
            ontologyService.deleteOntologyGroup(groupId);
            return BeanResult.success("");
        } catch (Exception e) {
            logger.error("deleteOntologyGroupException : ", e);
            return BeanResult.error("删除失败 ： " + e.getMessage());
        }
    }


    /**
     * 查询本体组
     *
     * @return
     */
    @RequestMapping(value = "/ot/gp", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public BeanResult searchOntologyGroup(@RequestParam(name = "caption", required = false) String caption
            , @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit
            , @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset) {

        if (logger.isDebugEnabled()) {
            Object[] argArray = {caption, limit, offset};
            logger.debug("searchOntologyGroup caption , limit ,offset: {} , {} , {}", argArray);
        }

        BeanResult beanResult;
        try {
            if (limit > 100 || limit < 1) {
                limit = 10;
            }

            Pair<Integer, List<OntologyGroup>> searchResult = ontologyService
                    .searchOntologyGroup(caption, limit, offset);

            beanResult = BeanResult.success(searchResult.getLeft(), searchResult.getRight());

        } catch (Exception e) {
            logger.error("searchOntologyException : ", e);
            beanResult =  BeanResult.error("获取失败 ： " + e.getMessage());
        }

        return beanResult;
    }

}