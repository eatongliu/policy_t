package com.gpdata.wanyou.tk.controller;

import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.utils.PinyinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 拼音转换
 * Created by guoxy on 2016/11/4.
 */
@Controller
@RequestMapping()
public class PinyinController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PinyinController.class);

    /**
     * 中文转全拼
     *
     * @param word
     * @return
     */
    @RequestMapping(value = "/pinyin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult reverseToPinyin(@RequestParam(name = "word") String word, HttpServletRequest request) {
        BeanResult result = null;
        try {
            result = BeanResult.success(PinyinUtil.getPin(word));
        } catch (Exception e) {
            result = BeanResult.error("转换失败 ：" + e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/pinyin/{word:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult reverseToPinyin2(@PathVariable(value = "word") String word, HttpServletRequest request) {
        BeanResult result = null;
        try {
            result = BeanResult.success(PinyinUtil.getPin(word));
        } catch (Exception e) {
            result = BeanResult.error("转换失败 ：" + e.getMessage());
        }
        return result;
    }

}
