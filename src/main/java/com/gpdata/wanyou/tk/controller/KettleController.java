package com.gpdata.wanyou.tk.controller;

import com.gpdata.wanyou.azkaban.AzkabanOperator;
import com.gpdata.wanyou.azkaban.FileUploadZip;
import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.tk.entity.TaskKettle;
import com.gpdata.wanyou.tk.service.TaskKettleService;
import com.gpdata.wanyou.utils.ConfigUtil;
import com.gpdata.wanyou.utils.PinyinUtil;
import com.gpdata.wanyou.utils.SimpleBeanPropertiesUtil;
import com.gpdata.wanyou.utils.ZipUtil;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.*;

/**
 * kettle任务上传和管理
 * Created by guoxy on 2016/11/10.
 */
@Controller
@RequestMapping()
public class KettleController extends BaseController {
    private static final String KETTLE_BASE = ConfigUtil.getConfig("kettle.basePath");
    private static final String KETTLE_LEVEL = ConfigUtil.getConfig("kettle.level");
    private static final String KETTLE_MAINJOB = ConfigUtil.getConfig("kettle.mainjob");
    private static final String KETTLE_KITCHEN = ConfigUtil.getConfig("kettle.kitchen");
    private static final String KETTLE_PAN = ConfigUtil.getConfig("kettle.pan");
    @Autowired
    private TaskKettleService taskKettleService;

    /**
     * 新增kettle任务
     */
    @RequestMapping(value = "/tk/ktr", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult addKettle(@RequestBody TaskKettle taskKettle, HttpServletRequest request) {
        BeanResult result = null;
        Map data = new HashMap();
        try {
            if (taskKettle.getFilePath() == null || taskKettle.getFilePath().length() == 0) {
                result.error("上传文件不存在!");
                return result;
            }
            User user = (User) request.getAttribute("currentUser");
            Date date = new Date();
            taskKettle.setCreateDate(date);
            taskKettle.setReviseDate(date);
            taskKettle.setCreator(user.getUserName());
            taskKettle.setKtlType(0);
            Integer kettleId = taskKettleService.addKettle(taskKettle);
            BeanResult result1 = createKettleWorkZip(PinyinUtil.getPin(taskKettle.getKettleName()), PinyinUtil.getPin(taskKettle.getRemark()), taskKettle.getFilePath(), request);
            data.put("kettleId", kettleId);
            data.put("upload", result1);
            return BeanResult.success(data);
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e.getMessage());
        }
        return result;
    }


    /**
     * 文件上传并解压到指定目录
     */
    @RequestMapping(value = "/tk/ktr/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult asyncUpFile(@RequestParam MultipartFile kettleFile, HttpServletRequest request) {
        logger.debug("上传kettle任务");
        BeanResult result = null;
        Map data = new HashMap();
        // 公有
        String suffix = "";
        /*kettle的Zip路径*/
        String realpath = "";
        /*kettle路径*/
        String ktrPath = "";

        // 获取当前用户
        User user = (User) request.getAttribute("currentUser");
        String rootPath = this.getFileRootPath();
        try {
            /*kettle的zip包*/
            String kettleName = kettleFile.getOriginalFilename();
            logger.debug("上传的文件名: {}", kettleName);
            suffix = StringUtils.getFilenameExtension(kettleName);
            if (!"zip".equalsIgnoreCase(suffix)) {
                return BeanResult.error("格式错误请选择zip格式上传!");
            }
            /*zip路径*/
            //realpath = "ktrfile/" + user.getUserId() + "/" + kettleName;
            realpath = "ktrfile/" + user.getUserId() + "/" + UUID.randomUUID().toString() + "/";
            File zipFile = new File(rootPath + realpath + kettleName);
            if (!zipFile.exists()) {
                zipFile.getParentFile().mkdirs();
                zipFile.createNewFile();
            }
            kettleFile.transferTo(zipFile);
            /*解压zip到指定路径,文件名是压缩包内文件名*/
            ZipUtil.unZipFiles(zipFile, rootPath + realpath);

            data.put("kettleName", kettleName);
            data.put("filePath", rootPath + realpath);
            return BeanResult.success(data);
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e.getMessage());
        }
        return result;
    }

    /**
     * 打包生成azkaban任务zip,
     *
     * @param kettleId   kettle任务id
     * @param remark     kettle备注
     * @param kettleFile kettle文件压缩包路径
     * @param request
     * @return
     */
    public BeanResult createKettleWorkZip(@PathVariable String kettleId, @RequestParam String remark, String kettleFile, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        User user = (User) request.getAttribute("currentUser");
        try {
            /*写文件*/
            // 项目根路基
            String rootPath = this.getFileRootPath();
            File runjob = null;
            String kettleName = kettleId.replace(".zip", "");
            String realpath = "/kettle/config/" + kettleName + "/run.job";
            /*文件*/
            runjob = new File(rootPath + realpath);
            if (!runjob.exists()) {
                runjob.getParentFile().mkdirs();
                runjob.createNewFile();
            }
             /*job内容*/
            Map<String, Object> jobMap = new LinkedHashMap<>();
            /*{rootpath,level,filepath},主程序master*/
            String params[] = {KETTLE_BASE, KETTLE_LEVEL, kettleFile + KETTLE_MAINJOB};
            /*KETTLE_KITCHEN*/
            String kitchen = ConfigUtil.getConfig("kettle.kitchen", params);
            jobMap.put("type", "command");
            jobMap.put("command", "pwd");
            jobMap.put("command.1", kitchen);
                /*写job文件流,替换原有字符*/
            StringBuilder sb = new StringBuilder();
            jobMap.forEach((key, value) -> {
                sb.append(key).append("=").append(value.toString()).append('\n');
            });
            try (FileWriter fw = new FileWriter(runjob);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                System.out.println(sb);
                bw.write(sb + "");
                logger.debug("kettle  realpath:{}", realpath);
            } catch (Exception e) {
                logger.error("Exception", e);
                result = BeanResult.error("异常 ： " + e);
            }
            String zipFile = "/kettle/config/" + kettleName + ".zip";
            /*压缩包,定义zip路径*/
            File zip = new File(rootPath + zipFile);
            if (!zip.exists()) {
                zip.getParentFile().mkdirs();
                zip.createNewFile();
            }
            List<File> files = new LinkedList<File>();
            files.add(0, runjob);
            try (FileOutputStream fous = new FileOutputStream(zip);
                 ZipOutputStream zipOut = new ZipOutputStream(fous);) {
                /*压缩*/
                ZipUtil.zipFile(files, zipOut);
            } catch (Exception e) {
                logger.error("Exception", e);
                result = BeanResult.error("异常 ： " + e);
            }
            /* 上传Azkaban */
            AzkabanOperator azkabanOperator = new AzkabanOperator();
            net.sf.json.JSONObject azLoginJson = azkabanOperator.login();
            String sessionid = azLoginJson.get("session.id").toString();
            logger.debug("sessionid:{}", sessionid);
            /*sessionid,项目名,项目描述*/
            net.sf.json.JSONObject azCreateProject = azkabanOperator.creatProject(sessionid, "kettle_" + kettleName, remark);
            logger.debug("azCreateProject:{}", azCreateProject);
            //上传
            String filename = rootPath + zipFile;
            String uploadUrl = ConfigUtil.getConfig("azkaban.url") + "manager";
            //  String projectname = spiderid;
            String uploadEntry = FileUploadZip.uploadFile(uploadUrl, "kettle_" + kettleName, filename, sessionid);
            data.put("spider", kettleId);
            data.put("return", uploadEntry);
            if (hasNonNullAndNonEmptyValue(uploadEntry)) {
                result = BeanResult.success(data);
            } else {
                result = BeanResult.error("文件上传AZKABAN失败");
            }
            return result;
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e);
        } finally {
        }
        return result;
    }


/*以下代码暂时无用*/

    /**
     * 查询
     */
    @RequestMapping(value = "/tk/ktr/{kettleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getKettle(@PathVariable Integer kettleId) {
        BeanResult result = null;
        Map data = new HashMap();
        try {
            TaskKettle kettle = taskKettleService.getKettle(kettleId);
            data.put("kettle", kettle);
            result.success(data);
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e.getMessage());
        }
        return result;
    }

    /**
     * kettle列表
     */
    @RequestMapping(value = "/tk/ktr/{offset}/{limit}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult listKettle(@PathVariable Integer offset, @PathVariable Integer limit, @RequestParam String kettleName, HttpServletRequest request) {
        BeanResult result = null;
        Map data = new HashMap();
        try {
            Map param = new HashMap();
            param.put("offset", offset);
            param.put("limit", limit);
            param.put("kettleName", kettleName);
            List<TaskKettle> kettleList = taskKettleService.searchTaskKettle(param);
            Integer total = taskKettleService.searchTaskKettleTotal(param);
            data.put("rows", kettleList);
            data.put("total", total);
            result.success(data);
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e.getMessage());
        }
        return result;
    }

    /**
     * kettle修改
     */
    @RequestMapping(value = "/tk/ktr/{kettleId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult updateKettle(@PathVariable Integer kettleId, @RequestBody TaskKettle taskKettle, HttpServletRequest request) {
        BeanResult result = null;
        Map data = new HashMap();
        try {
            User user = (User) request.getAttribute("currentUser");
            Date date = new Date();
            taskKettle.setReviseDate(date);
            taskKettle.setCreator(user.getUserName());
            TaskKettle oldKettle = taskKettleService.getKettle(kettleId);
            SimpleBeanPropertiesUtil.copyNotNullProperties(taskKettle, oldKettle);
            data.put("kettleId", oldKettle.getKettleId());
            result.success(data);
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e.getMessage());
        }
        return result;
    }

    /**
     * kettle删除
     */
    @RequestMapping(value = "/tk/ktr/{kettleId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult deleteKettle(@PathVariable Integer kettleId) {
        BeanResult result = null;
        Map data = new HashMap();
        try {
            TaskKettle oldKettle = taskKettleService.getKettle(kettleId);
            taskKettleService.deleteKettle(oldKettle);
            result.success("删除成功");
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e.getMessage());
        }
        return result;
    }
}
