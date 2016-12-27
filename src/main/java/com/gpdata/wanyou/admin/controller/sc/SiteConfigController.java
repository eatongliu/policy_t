package com.gpdata.wanyou.admin.controller.sc;

import com.gpdata.wanyou.admin.entity.SiteConfig;
import com.gpdata.wanyou.admin.service.SiteConfigService;
import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.service.SimpleService;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by ligang on 2016/12/10.
 */
@Controller
@RequestMapping
public class SiteConfigController extends BaseController {

    @Autowired
    private SiteConfigService siteConfigService;
    @Autowired
    private SimpleService simpleService;

    /**
     * 查询站点配置
     *
     * @param
     * @return SiteConfig
     */
    @RequestMapping(value = "/admin/sc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getSiteConfig() {
        try {

            List<SiteConfig> siteConfig = simpleService.getAll(SiteConfig.class,"scID");
            if (siteConfig.isEmpty()) {
                return BeanResult.success(null);
            }
            return BeanResult.success(siteConfig.get(0));
        } catch (Exception e) {
            logger.error("Exception : {}", e.getCause());
            return BeanResult.error("获取失败 ： " + e.getMessage());
        }
    }

    /**
     * 新增站点配置
     */
    @RequestMapping(value = "/admin/sc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public BeanResult saveSiteConfig(@RequestBody SiteConfig input, HttpServletRequest request) {
        logger.debug("input : {}", input.toString());
        if (input.getCompanyName() == null) {
            return BeanResult.error("公司名称不能为空");
        }
        try {
            // 获取user
            User user = this.getCurrentUser(request);
            // 获取当前日期
            Date date = new Date();
            input.setCreateDate(date);
            input.setCreatorId(String.valueOf(user.getUserId()));
            simpleService.save(input);
            return BeanResult.success("");
        } catch (Exception e) {
            logger.error("Exception : {}", e.getCause());
            return BeanResult.error("保存失败 ： " + e.getMessage());
        }

    }

    /**
     * 修改站点配置
     */
    @RequestMapping(value = "/admin/sc/{scId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    @ResponseBody
    public BeanResult updateSiteConfig(@RequestBody SiteConfig input, @PathVariable("scId") Integer scId) {
        logger.debug("id,input : {}", scId + "," + input);
        if (input.getCompanyName() == null) {
            return BeanResult.error("公司名称不能为空");
        }
        try {
            // 获取当前日期
            Date date = new Date();
            input.setReviseDate(date);
            simpleService.update(scId, input);
            return BeanResult.success("");
        } catch (Exception e) {
            logger.error("Exception : {}", e.getCause());
            return BeanResult.error("修改失败 ： " + e.getMessage());
        }
    }

    /**
     * 二维码上传
     * @param MultipartFile
     * @return filePath
     */
    @RequestMapping(value = "/admin/sc/upload", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public BeanResult uploadQRCodeFile(@RequestParam() MultipartFile qrCodeFile) {
        logger.debug("上传二维码");
        try {

            if (qrCodeFile.isEmpty()) {
                return BeanResult.error("上传失败，上传文件为空！");
            }
            String rootPath = this.getFileRootPath();
            //文件名
            String tempName = qrCodeFile.getOriginalFilename();
            logger.debug("上传的文件名: {}", tempName);
            //文件后缀名
            String suffix = StringUtils.getFilenameExtension(tempName);
/*            if (!"jpg".equalsIgnoreCase(suffix) || !"png".equalsIgnoreCase(suffix)) {
                return BeanResult.error("格式错误,请选择JPG或PNG格式上传!");
            }*/
            String filePath = rootPath + "QRCodefile/" + UUID.randomUUID().toString() + "." + suffix;
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            qrCodeFile.transferTo(file);
            System.err.print(filePath);
            return BeanResult.success(filePath);
        } catch (Exception e) {
            logger.error("Exception : {}", e.getCause());
            return BeanResult.error("上传失败 ： " + e.getMessage());
        }

    }

}
