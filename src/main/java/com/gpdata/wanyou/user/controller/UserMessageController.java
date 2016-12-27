package com.gpdata.wanyou.user.controller;

import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.service.SimpleService;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.user.entity.UserMessage;
import com.gpdata.wanyou.user.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 个人中心用户消息管理
 * lyt
 */
@Controller
@RequestMapping(value ="/user")
        public class UserMessageController extends BaseController {
    @Autowired
    private UserMessageService messageService;
    @Autowired
    private SimpleService simpleService;
    /**
     * 用户消息列表
     */
    @RequestMapping(value = "/msg/ls",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getMessageList(@RequestParam(required = false) String keyWord
                                    , @RequestParam(required = false) String isShow
                                    , @RequestParam(required = false) String qType
                                    , @RequestParam(required = false,defaultValue = "0") int offset
                                    , @RequestParam(required = false,defaultValue = "10") int limit
                                    , HttpServletRequest request){
        BeanResult beanResult = null;
        try{
            User user = getCurrentUser(request);
            Map<String,Object> resultMap = messageService.searchUserMessage(user.getUserId(), null, keyWord, isShow, qType, offset, limit);
            beanResult = BeanResult.success(resultMap);
        }catch (Exception e){
            logger.error("Exception: {}",e);
            beanResult = BeanResult.error("列表拉取失败");
        }
        return beanResult;
    }

    /**
     * 删除消息
     */
    @RequestMapping(value = "/msg/d/{messageId}",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult deleteMessage(@PathVariable Long messageId){
        BeanResult beanResult = null;
        try{
            simpleService.delete(UserMessage.class,messageId);
            beanResult = BeanResult.success("删除成功");
        }catch (Exception e){
            logger.error("Exception: {}",e);
            beanResult = BeanResult.error("删除失败");
        }
        return beanResult;
    }

}
