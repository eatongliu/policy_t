package com.gpdata.wanyou.system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chengchao on 2016/10/31.
 */
@Controller
@RequestMapping
public class DevHelperController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DevHelperController.class);


    @RequestMapping(value = "/dev-helper/ti", method = {RequestMethod.GET})
    @ResponseBody
    public String testInput(HttpServletRequest request) {
        Enumeration<String> names = request.getParameterNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getParameter(name);
            LOGGER.info(" {} -> {}", name, value);
        }

        return "hello 杨静 ...";
    }

    @RequestMapping(value = "/dev-helper/index", method = {RequestMethod.GET})
    public String index(HttpServletRequest request, Model model) {

        String name = "hello ";
        model.addAttribute("name ", name);

        return "dev-helper/index";
    }


    @RequestMapping(value = "/dev-helper/map-test", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> mapTest() {

        Map<String, Object> result = new HashMap<>();

        Map<String, Object> other = new HashMap<>();
        other.put("name", "cheng");
        other.put("alias", "chao");

        result.put("other", other);

        return result;
    }
}
