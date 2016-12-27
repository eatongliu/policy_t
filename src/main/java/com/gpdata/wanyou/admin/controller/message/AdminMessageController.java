package com.gpdata.wanyou.admin.controller.message;

import com.gpdata.wanyou.admin.service.AdminMessageService;
import com.gpdata.wanyou.base.controller.AdminBaseController;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.user.entity.UserMessage;
import com.gpdata.wanyou.user.service.UserMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 后台消息系统
 * Created by guoxy on 2016/12/12.
 */
@Controller
@RequestMapping("/admin")
public class AdminMessageController extends AdminBaseController {
    private static final Logger logger = LoggerFactory.getLogger(AdminMessageController.class);
    @Autowired
    private AdminMessageService messageService;
    @Autowired
    private UserMessageService userMessageService;

    /**
     * 新增系统消息
     *
     * @param message
     * @param request
     * @return
     */
    @RequestMapping(value = "/msg/a", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public BeanResult addSysMessage(UserMessage message, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
               /*TODO 判断管理登录*/
            // AdminUser admin = getCurrentAdminUser(request);
            /*TODO 获取后台管理员id*/
            message.setIsAnswer("1");
            //message.setAnswerMan(admin.getAdminUsername());
            message.setAnswerMan("admin");
            message.setaTime(new Date());
            message.setIsShow("1");
            message.setIsRead("0");
            message.setqType("系统消息");
            List<String> idList = messageService.getUseridList();
            messageService.multiThreadImport(message, idList);
            result = BeanResult.success("SUCCESS");
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("异常 ： " + e);
        }
        return result;
    }

    /**
     * 系统消息列表
     *
     * @param offset
     * @param limit
     * @param map     {"uid":"","id":"","keyword":"","isShow":"","qType":""}作为搜索用    key--> uid 用户id ,id   消息id,keyword  问题关键字,isShow  是否显示,qType   消息类型
     * @param request
     * @return
     */
    @RequestMapping(value = "/msg/ls/{offset}/{limit}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public BeanResult getSysMessageList(@PathVariable Integer offset, @PathVariable Integer limit, @RequestBody Map<String, Object> map, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
              /*TODO 判断管理登录*/
            // AdminUser admin = getCurrentAdminUser(request);
            Long uid = null;
            Integer id = null;
            String uidStr = map.get("uid").toString();
            String idStr = map.get("id").toString();
            if (uidStr != null && uidStr.length() != 0) {
                uid = Long.parseLong(uidStr);
            }
            if (idStr != null && idStr.length() != 0) {
                id = Integer.parseInt(idStr);
            }
            String keyword = map.get("keyword").toString();
            String isShow = map.get("isShow").toString();
            String qType = map.get("qType").toString();

               /*TODO 判断管理登录*/
            // AdminUser admin = getCurrentAdminUser(request);
            /*TODO 获取后台管理员id*/
            Map<String, Object> resultMap = userMessageService.searchUserMessage(uid, id, keyword, isShow, qType, offset, limit);
            result = BeanResult.success(resultMap);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("异常 ： " + e);
        }
        return result;
    }

    
//系统消息

    /**
     * 获取消息列表
     * /**
     *
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @param keyWord   查询关键词
     * @param qType     消息类型
     * @param qStatus   消息状态
     * @param offset    起始
     * @param limit     偏移量
     * @return Map<String ,Object>  key--> rows  total
     */
    @RequestMapping(value = "/msg", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getAdminMsgList(@RequestParam(name = "startDate", required = false) String startDate,
                                      @RequestParam(name = "endDate", required = false) String endDate,
                                      @RequestParam(name = "keyWord", required = false) String keyWord,
                                      @RequestParam(name = "qType", required = false) String qType,
                                      @RequestParam(name = "qStatus", required = false) String qStatus,
                                      @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                      @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset) {
        Object[] argArray = {startDate, endDate, keyWord, qType, qStatus, limit, offset};
        logger.debug("input : {}", argArray);
        try {
            Map<String, Object> resultMap = userMessageService.getUserMessageList(startDate, endDate, keyWord, qType, qStatus, limit, offset);
            return BeanResult.success((int) resultMap.get("total"),
                    (List<UserMessage>) resultMap.get("rows"));
        } catch (Exception e) {
            logger.error("searchOntologyException : ", e);
            return BeanResult.error("获取失败 ： " + e.getMessage());
        }
    }

    /**
     * 修改消息是否为重点
     */
    @RequestMapping(value = "/msg/ispoint/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    @ResponseBody
    public BeanResult uploadMsgIsPoint(@PathVariable("id") Integer id, @RequestParam(name = "isPoint") String isPoint) {
        logger.debug("input : {}", id + "," + isPoint);
        try {
            if (!isPoint.equals("0") && !isPoint.equals("1")) {
                isPoint = "0";
            }
            userMessageService.uploadMsgIsPoint(id, isPoint);
            return BeanResult.success("修改成功");
        } catch (Exception e) {
            logger.error("Exception : {}", e.getMessage());
            return BeanResult.error("修改失败 ： " + e.getMessage());
        }
    }

    /**
     * 修改消息是否为已解决
     */
    @RequestMapping(value = "/msg/isresolve/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    @ResponseBody
    public BeanResult uploadMsgIsResolve(@PathVariable("id") Integer id, @RequestParam(name = "isResolve") String isResolve) {
        logger.debug("input : {}", id + "," + isResolve);
        try {
            if (!isResolve.equals("0") && !isResolve.equals("1")) {
                isResolve = "0";
            }
            userMessageService.uploadMsgIsResolve(id, isResolve);
            return BeanResult.success("修改成功");
        } catch (Exception e) {
            logger.error("Exception : {}", e.getMessage());
            return BeanResult.error("修改失败 ： " + e.getMessage());
        }
    }

    /**
     * 修改消息状态备注
     */
    @RequestMapping(value = "/msg/status/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    @ResponseBody
    public BeanResult uploadMsgStatus(@PathVariable("id") Integer id, @RequestParam(name = "qStatus") String qStatus, @RequestParam(name = "remark") String remark) {
        logger.debug("input : {}", id + "," + qStatus + "," + remark);
        try {
            if (!qStatus.equals("0") && !qStatus.equals("1") && !qStatus.equals("2") && !qStatus.equals("3") && !qStatus.equals("4")) {
                qStatus = "0";
            }
            userMessageService.uploadMsgStatus(id, qStatus, remark);
            return BeanResult.success("修改成功");
        } catch (Exception e) {
            logger.error("Exception : {}", e.getMessage());
            return BeanResult.error("修改失败 ： " + e.getMessage());
        }
    }
}
