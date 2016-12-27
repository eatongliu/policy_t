package com.gpdata.wanyou.admin.controller.pr;

import com.gpdata.wanyou.admin.entity.ProblemRecord;
import com.gpdata.wanyou.admin.service.ProblemRecordService;
import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.service.SimpleService;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ligang on 2016/12/10.
 */
@Controller
@RequestMapping
public class ProblemRecordController extends BaseController {

    @Autowired
    private ProblemRecordService problemRecordService;
    @Autowired
    private SimpleService simpleService;
    /**
     * 获取问题列表
     *
     * @param input
     * @return list
     */
    @RequestMapping(value = "/admin/pr", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getProblemRecordList(@RequestParam(name = "input", required = false) String input,
                                          @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                          @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset) {

        logger.debug("input : {}", input + "," + limit + "," + offset);
        try {
            Map<String, Object> searchResult = problemRecordService.getProblemRecordList(input, limit, offset);
            return BeanResult.success((int) searchResult.get("total"), (List<ProblemRecord>) searchResult.get("rows"));
        } catch (Exception e) {
            logger.error("searchOntologyException : ", e);
            return BeanResult.error("获取失败 ： " + e.getMessage());
        }
    }
    /**
     * 获取指定 id 的问题记录
     *
     * @param prId
     * @return ProblemRecord
     */
    @RequestMapping(value = "/admin/pr/{prId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getProblemRecordById( @PathVariable("prId") Integer prId){
        try {
            ProblemRecord bean = simpleService.getById(ProblemRecord.class,prId);
            return BeanResult.success(bean);
        }catch (Exception e){
            logger.error("Exception : {}", e.getCause());
            return BeanResult.error("获取失败 ： " + e.getMessage());
        }

    }

    /**
     * 保存
     *
     * @param ProblemRecord
     * @return prId
     */
    @RequestMapping(value = "/admin/pr", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public BeanResult saveProblemRecord(@RequestBody ProblemRecord input, HttpServletRequest request) {
        logger.debug("input : {}", input);
        try {
            // 获取当前日期
            Date date = new Date();
            // 获取user
            User user = this.getCurrentUser(request);
            input.setCreateDate(date);
            input.setCreatorId(String.valueOf(user.getUserId()));
            input.setDispStatus("1");
            simpleService.save(input);
            return BeanResult.success("");
        }catch (Exception e){
            logger.error("Exception : {}", e.getCause());
            return BeanResult.error("保存失败 ： " + e.getMessage());
        }

    }
    /**
     * 修改
     *
     * @param ProblemRecord,prid
     * @return
     */
    @RequestMapping(value = "/admin/pr/{prId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    @ResponseBody
    public BeanResult updateProblemRecord(@RequestBody ProblemRecord input, @PathVariable("prId") Integer prId) {
        logger.debug("id,input : {}", prId + "," + input);
        try {
            // 获取当前日期
            Date date = new Date();
            input.setReviseDate(date);
            simpleService.update(prId, input);
            return BeanResult.success("");
        } catch (Exception e) {
            logger.error("Exception : {}", e.getCause());
            return BeanResult.error("修改失败 ： " + e.getMessage());
        }
    }
    /**
     * 删除
     *
     * @param prId
     * @return
     */
    @RequestMapping(value = "/admin/pr/{prId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.DELETE)
    @ResponseBody
    public BeanResult deleteProblemRecord( @PathVariable("prId") Integer prId) {
        logger.debug("id,input : {}", prId );
        try {

            simpleService.delete(ProblemRecord.class,prId);
            return BeanResult.success("");
        } catch (Exception e) {
            logger.error("Exception : {}", e.getCause());
            return BeanResult.error("删除失败 ： " + e.getMessage());
        }
    }

}
