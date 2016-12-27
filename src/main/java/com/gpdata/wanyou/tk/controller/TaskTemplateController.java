package com.gpdata.wanyou.tk.controller;

import com.gpdata.wanyou.azkaban.AzkabanOperator;
import com.gpdata.wanyou.azkaban.FileUploadZip;
import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.flexible.FlexibleFileUtil;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.tk.entity.TaskJob;
import com.gpdata.wanyou.tk.entity.TaskTemplate;
import com.gpdata.wanyou.tk.service.TaskJobService;
import com.gpdata.wanyou.tk.service.TaskTemplateService;
import com.gpdata.wanyou.utils.ConfigUtil;
import com.gpdata.wanyou.utils.PinyinUtil;
import com.gpdata.wanyou.utils.SimpleBeanPropertiesUtil;
import com.gpdata.wanyou.utils.ZipUtil;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * <p>
 * Created by guoxy on 2016/10/25.
 */
@Controller
@RequestMapping()
public class TaskTemplateController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TaskTemplateController.class);
    @Autowired
    private TaskTemplateService taskTemplateService;
    @Autowired
    private TaskJobService taskJobService;
    @Autowired
    private FlexibleFileUtil flexibleFileUtil;

    /**
     * 第一步，显示模板
     * URI	/tk/tp/{tid}
     * Method	PUT
     * 功能说明：读取模板信息
     * 参数1：templateid模板id（必填）
     * 成功：”success”
     * 失败：[“error”:”错误原因”]
     */
    @RequestMapping(value = "/tk/tp/{tid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult updateTemplateGet(@PathVariable Integer tid) {
        logger.debug("模板发布第一步，显示模板");

        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
//            String tid = map.get("tid");
        try {
            if (tid == null) {
                result = BeanResult.error("任务模板(tid)不存在或值不合理");
                return result;
            }
            TaskTemplate taskTemplate = taskTemplateService.getTaskTemplateById(tid);
            data.put("rows", taskTemplate);
            result = BeanResult.success(data);
            return result;
        } catch (Exception e) {
            result = BeanResult.error("异常 ： " + e.getMessage());
        }

        return result;
    }

    /**
     * 第一步，新增模板
     * URI	/tk/tp
     * Method	POST
     * 功能说明：在模板基本信息表中新增记录。
     * 参数1：caption标题 英文，作为文件名使用（必填）
     * 参数2：remark说明
     * 成功：[“templateid”:新增模板的templateid]
     * 失败：[“error”:”错误原因”]
     */
    @RequestMapping(value = "/tk/tp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult addTemplate(@RequestBody TaskTemplate taskTemplate, HttpServletRequest request) {
        logger.debug("模板发布第一步: 保存模板信息.");

        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        Integer tid = taskTemplate.getTemplateId();
        User user = (User) request.getAttribute("currentUser");

        try {

            taskTemplate.setCreator(user.getUserName());
            Date date = new Date();
            taskTemplate.setTempType(0);
            taskTemplate.setCreateDatetime(date);
            taskTemplate.setReviseDatetime(date);

            if (tid != null) {
                    /*如果id存在则修改*/
                taskTemplate.setReviseDatetime(date);
                TaskTemplate tasktemp = taskTemplateService.getTaskTemplateById(tid);
                SimpleBeanPropertiesUtil.copyNotNullProperties(taskTemplate, tasktemp);
                taskTemplateService.updateTaskTemplate(tasktemp);
                data.put("templateid", tid);
                result = BeanResult.success(data);
                return result;
            } else {
                int templateid = taskTemplateService.addTaskTemplate(taskTemplate);
                data.put("templateid", templateid);
                result = BeanResult.success(data);
                return result;
            }


        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e.getMessage());
        }
        return result;
    }

    /**
     * 修改模板
     */
    @RequestMapping(value = "/tk/tp/{templateId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult updateTemplate(@PathVariable Integer templateId, @RequestBody TaskTemplate taskTemplate, HttpServletRequest request) {
        logger.debug("模板发布 : 修改模板信息.");

        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        User user = (User) request.getAttribute("currentUser");

        try {

            taskTemplate.setCreator(user.getUserName());
            Date date = new Date();
            taskTemplate.setReviseDatetime(date);

            /*如果id存在则修改*/
            taskTemplate.setReviseDatetime(date);
            TaskTemplate tasktemp = taskTemplateService.getTaskTemplateById(templateId);
            SimpleBeanPropertiesUtil.copyNotNullProperties(taskTemplate, tasktemp);
            taskTemplateService.updateTaskTemplate(tasktemp);
            data.put("templateid", templateId);
            result = BeanResult.success(data);
            return result;


        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e.getMessage());
        }
        return result;
    }

    /**
     * 第二步，新增任务
     * 4.2.2新增
     * URI	/tk/jo
     * Method	POST
     * 功能说明：在任务基本信息表中新增记录。
     * 参数1：templateid模板ID（必填）
     * 参数2：taskname     标题 英文，作为文件名使用（必填）
     * 参数3：retries       自动尝试失败的次数 默认1次
     * 参数4：retrybackoff  每次重试尝试之间的毫秒时间，默认无
     * 参数5：type         命令类型如 command （必填）
     * 参数6：command    根据上边选择的类型编写具体的命令 （必填）
     * 参数7：dependencies  第一个job不可选依赖关系，第二个job可以依赖第一个
     * 成功：[“id”:新增任务的id]
     * 失败：[“error”:”错误原因”]
     */
    @RequestMapping(value = "/tk/jo", consumes = "application/json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult addTaskJob(@RequestBody TaskJob taskJob) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        Integer templateid = taskJob.getTemplateId();
        try {
                /*判断模板id是否存在*/
            if (templateid == null) {
                result = BeanResult.error("任务(templateid)不存在或值不合理");
                return result;
            }
            Integer jobId = taskJob.getTaskId();
                /*判断任务id是否存在*/
            if (jobId == null) {
                jobId = taskJobService.addTaskJob(taskJob);
            } else {
                taskJobService.updateTaskJob(taskJob);
            }
            /*每新增一个任务就调用一次生成zip*/
            data.put("createZip", createTaskTempZip(templateid));
            data.put("jobId", jobId);
            result = BeanResult.success(data);
            return result;


        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e.getMessage());
        }
        return result;
    }

    /**
     * 修改任务
     *
     * @param taskJob
     * @return
     */
    @RequestMapping(value = "/tk/jo", consumes = "application/json", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult updateTaskJob(@RequestBody TaskJob taskJob) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        Integer templateid = taskJob.getTemplateId();
        try {
                /*判断模板id是否存在*/
            if (templateid == null) {
                result = BeanResult.error("任务(templateid)不存在或值不合理");
                return result;
            }
            Integer jobId = taskJob.getTaskId();
                /*判断任务id是否存在*/
            if (jobId == null) {
                result = BeanResult.error("任务(taskId)不存在或值不合理");
                return result;
            } else {
                TaskJob tj = taskJobService.getTaskJobById(jobId);
                taskJob.setTaskId(jobId);
                SimpleBeanPropertiesUtil.copyNotNullProperties(taskJob, tj);
                taskJobService.updateTaskJob(tj);
            }
            /*每新增一个任务就调用一次生成zip*/
            data.put("createZip", createTaskTempZip(templateid));
            data.put("jobId", jobId);
            result = BeanResult.success(data);
            return result;


        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e.getMessage());
        }
        return result;
    }

    /**
     * 第二步 任务列表
     */
    @RequestMapping(value = "/tk/jo/{templateid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getTaskJobList(@PathVariable Integer templateid) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
//            Integer templateid = taskJob.getTemplateid();
        try {
                /*判断模板id是否存在*/
            if (templateid == null) {
                result = BeanResult.error("任务(templateid)不存在或值不合理");
                return result;
            }

            Map<String, String> params = new HashMap<>();
            params.put("templateid", templateid + "");
            List<TaskJob> jobLists = taskJobService.searchTaskJob(params);
            Integer total = taskJobService.searchTaskJobTotal(params);
            data.put("total", total);
                /*将已经保存的任务列放入data*/
            data.put("rows", jobLists);
                /*读取传入id的job*/
            result = BeanResult.success(data);
            return result;
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e.getMessage());
        }
        return result;
    }

    /**
     * 第二步，显示任务
     * URI	/tk/jo/{templateid}/{tid}
     * Method	GET
     * 功能说明：显示任务信息
     * 参数1：templateid    任务模板ID（必填）
     * 参数2：tid  任务ID（必填）
     * 成功：”success”
     * 失败：[“error”:”错误原因”]
     */
    @RequestMapping(value = "/tk/jo/{templateid}/{tid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult updateTaskJob(@PathVariable Integer templateid, @PathVariable Integer tid) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
                /*判断模板id是否存在*/
            if (templateid == null) {
                result = BeanResult.error("任务(templateid)不存在或值不合理");
                return result;
            }
                /*判断任务id是否存在*/
            if (tid == null) {
                result = BeanResult.error("新增任务失败");
                return result;
            }
            TaskJob job = taskJobService.getTaskJobById(tid);
            Map<String, String> params = new HashMap<>();
            params.put("templateid", templateid + "");
            List<TaskJob> jobLists = taskJobService.searchTaskJob(params);
            Integer total = taskJobService.searchTaskJobTotal(params);
            data.put("total", total);
                /*将已经保存的任务列放入data*/
            data.put("rows", jobLists);
                /*读取传入id的job*/
            data.put("job", job);

            result = BeanResult.success(data);
            return result;
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e.getMessage());
        }
        return result;
    }

    /**
     * 显示列表
     * URI	/tk/tp
     * Method	GET
     * 功能	说明：检索模板信息
     * 参数1：limit 每页条数
     * 参数2：offset 偏移量
     * 参数3：caption 标题
     * 参数4：temptype 模板类型（0添加    1上传）
     * 成功：检索模板信息列表，caption模糊匹配
     * 失败：[“error”:”错误原因”]
     */
    @RequestMapping(value = "/tk/tp", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult searchTaskTemp(@RequestParam Integer limit, @RequestParam Integer offset, @RequestParam String caption,
                                     @RequestParam String temptype, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, String> params = new HashMap<>();
        // User user = (User) request.getAttribute("currentUser");
        if (limit == null || offset == null) {
            result = BeanResult.error("分页参数(limit,offset)为空");
            return result;
        }
        try {
            params.put("caption", caption);
            params.put("temptype", temptype);
            params.put("limit", limit + "");
            params.put("offset", offset + "");
            List<TaskTemplate> templates = taskTemplateService.searchTaskTemplate(params);
            Integer total = taskTemplateService.searchTaskTemplateTotal(params);
            Map<String, Object> data = new HashMap<>();
            data.put("rows", templates);
            data.put("total", total);
            result = BeanResult.success(data);
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e.getMessage());
        }
        return result;
    }

    /**
     * 第三步，完成并生成zip并保存文件路径
     * URI	/tk/zip/{templateid}
     * Method	GET
     * 功能说明：显示任务信息
     * 参数1：templateid    任务模板ID（必填）
     * 成功：”success”
     * 失败：[“error”:”错误原因”]
     */
    @RequestMapping(value = "/tk/zip/{templateid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult createTaskTempZip(@PathVariable Integer templateid) {
        BeanResult result = null;
//            Integer templateid = taskTemplate.getTemplateid();
        FileOutputStream fous = null;
        ZipOutputStream zipOut = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            String rootPath = this.getFileRootPath();
            TaskTemplate taskTemplate = taskTemplateService.getTaskTemplateById(templateid);
            Map<String, String> params = new HashMap<>();
            params.put("templateid", taskTemplate.getTemplateId() + "");
                /*找到job列表并*/
            List<TaskJob> jobLists = taskJobService.searchTaskJob(params);
                /*zip名字*/
            //taskTemplate.getCaption();
            //定义zip路径
                /*TODO*/
            String zipFile = "/azfile/" + PinyinUtil.getPin(taskTemplate.getCaption()) + ".zip";
            File z = new File(rootPath + zipFile);

            if (!z.exists()) {
                z.getParentFile().mkdirs();
                z.createNewFile();
            }
            fous = new FileOutputStream(z);
            zipOut = new ZipOutputStream(fous);
            List<File> files = new LinkedList<File>();
            int i = 0;
            for (TaskJob job : jobLists) {

                File f = new File(rootPath + "/azfile/" + PinyinUtil.getPin(job.getTaskName()) + ".job");
                    /*在原有字符后追加*/
                //fw = new FileWriter(f, true);
                    /*替换原有字符*/
                fw = new FileWriter(f);
                bw = new BufferedWriter(fw);
                bw.write(job.toJobTemp());

                bw.close();
                fw.close();
                files.add(i, f);
                i++;
            }
            ZipUtil.zipFile(files, zipOut);
            /*TODO  省略根路径*/
            taskTemplate.setTempPath(zipFile);

//            String realpath = "/azfile/" + PinyinUtil.getPin(taskTemplate.getCaption()) + ".zip";
//            flexibleFileUtil.saveUploadFile(z, realpath,
//                    FlexibleFileType.PUB);
            taskTemplateService.updateTaskTemplate(taskTemplate);
            result = BeanResult.success("SUCCESS");
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e.getMessage());
        } finally {
            try {
                zipOut.close();
                fous.close();
            } catch (Exception e) {
                logger.error("Exception", e);
                result = BeanResult.error("异常 ： " + e.getMessage());
            }


        }
        return result;
    }

    /**
     * 任务模板zip上传
     */
    @RequestMapping(value = "/tk/zip/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult asyncUpFile(@RequestParam MultipartFile tempFile, HttpServletRequest request) {
        logger.debug("上传任务模板任务");
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
            String tempName = tempFile.getOriginalFilename();
            logger.debug("上传的文件名: {}", tempName);
            suffix = StringUtils.getFilenameExtension(tempName);
            if (!"zip".equalsIgnoreCase(suffix)) {
                return BeanResult.error("格式错误请选择zip格式上传!");
            }
            /*zip路径*/
            //realpath = "ktrfile/" + user.getUserId() + "/" + kettleName;
            realpath = "tempfile/" + user.getUserId() + "/" + UUID.randomUUID().toString() + "/";
            File zipFile = new File(rootPath + realpath + PinyinUtil.getPin(tempName));
            if (!zipFile.exists()) {
                zipFile.getParentFile().mkdirs();
                zipFile.createNewFile();
            }
            tempFile.transferTo(zipFile);
            /*解压zip到指定路径,文件名是压缩包内文件名*/
            //ktrPath = "ktrfile/" + user.getUserId() + "/";
            //ZipUtil.unZipFiles(zipFile, rootPath + realpath);
            TaskTemplate template = new TaskTemplate();
            String tmpPath = rootPath + realpath + PinyinUtil.getPin(tempName);
            template.setTempPath(tmpPath);
            template.setRemark("template");
            template.setCaption(PinyinUtil.getPin(tempName));
            Date date = new Date();
            template.setCreateDatetime(date);
            template.setReviseDatetime(date);
            template.setCreator(user.getUserName());
            template.setTempType(1);
            Integer templateId = taskTemplateService.addTaskTemplate(template);
            data.put("templateId", templateId);
            return BeanResult.success(data);
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e.getMessage());
        }
        return result;
    }


    /**
     * azkaban界面，选择模板
     * URI	/tk/zip/
     * Method	POST
     * 功能说明：显示任务信息
     * 参数1：param  任务名;模板id （必填）
     * 成功：”success”
     * 失败：[“error”:”错误原因”]
     */
    @RequestMapping(value = "/tk/uploadzip/{param}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult uploadTaskTempZip(@PathVariable String param, HttpServletRequest request) {
        /*任务名;模板id*/
        logger.debug("param:{}", param);
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
            String[] params = null;
            String projectName = null;
            Integer templateId = null;
            if (param == null || param.length() == 0) {
                result = BeanResult.error("param参数为空!");
                //param.matches(".*\\_.*")
            } else if (!param.matches(".*[,].*")) {
                result = BeanResult.error("param格式不合法,必须包含\",\"");
            } else {
                params = param.split(",");
                /*为上传azkaban使用*/
                projectName = params[0];
                /*从数据库得到模板的路径名*/
                templateId = Integer.parseInt(params[1]);
            }
            TaskTemplate taskTemplate = taskTemplateService.getTaskTemplateById(templateId);
            String rootPath = this.getFileRootPath();
            String zipFile = taskTemplate.getTempPath();
            /**
             * 上传Azkaban
             */
            AzkabanOperator azkabanOperator = new AzkabanOperator();
            net.sf.json.JSONObject azLoginJson = azkabanOperator.login();
            String sessionid = azLoginJson.get("session.id").toString();
            logger.debug("sessionid:{}", sessionid);
            //上传
            String filename = rootPath + zipFile;
            String uploadUrl = ConfigUtil.getConfig("azkaban.url") + "manager";
            String uploadEntry = FileUploadZip.uploadFile(uploadUrl, projectName, filename, sessionid);
            data.put("projectName", projectName);
            data.put("return", uploadEntry);
            if (hasNonNullAndNonEmptyValue(uploadEntry)) {
                result = BeanResult.success(data);
            } else {
                result = BeanResult.error("文件上传AZKABAN失败");
            }
            result = BeanResult.success("SUCCESS");
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e.getMessage());
        } finally {

        }
        return result;
    }


}
