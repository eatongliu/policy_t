package com.gpdata.wanyou.sp.controller;

import com.alibaba.fastjson.JSON;
import com.gpdata.wanyou.azkaban.AzkabanOperator;
import com.gpdata.wanyou.azkaban.FileUploadZip;
import com.gpdata.wanyou.base.cache.RedisCache;
import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.flexible.FlexibleFileUtil;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.sp.entity.SpiderBaseInfo;
import com.gpdata.wanyou.sp.service.SpiderWorkService;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.utils.ConfigUtil;
import com.gpdata.wanyou.utils.HDFSUtil;
import com.gpdata.wanyou.utils.ZipUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 爬虫任务组建
 * Created by guoxy on 2016/10/31.
 */
@Controller
@RequestMapping()
public class SpiderWorkController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SpiderWorkController.class);
    /*无端口号的url,不知道为啥只能这样用，加了端口号就会提示权限问题*/
    private static final String HDFS_URL = ConfigUtil.getConfig("HDFS.baseurl");
    private static final String NUTCH_CONFIG = ConfigUtil.getConfig("nutchconfig");
    private static final String NUTCH_URL = ConfigUtil.getConfig("nutchurl");
    @Autowired
    private SpiderWorkService spiderWorkService;
    @Autowired
    private FlexibleFileUtil flexibleFileUtil;
    @Autowired
    private RedisCache redisCache;

    /**
     * 第一步 生成爬虫标识
     * URI	/sp/ch
     * POST	说明：将任务名和按照相应规则生成的spiderid存入cache中，并把返回的spiderid显示在“任务标识”栏。
     * 参数1：taskname={任务名}（必填）
     * 成功：{"status": "SUCCESS","data": {"spider": "爬虫标识"} }
     * 失败：{"status": "ERROR","cause": "错误原因" }
     * 如果spiderid为空则新增，否则覆盖原对象
     */
    @RequestMapping(value = "/sp/ch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult createSpiderParam(@RequestBody SpiderBaseInfo spider, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, String> data = new HashMap<>();
        User user = (User) request.getAttribute("currentUser");
        if (!hasNonNullAndNonEmptyValue(spider.getTaskName())) {
            result = BeanResult.error("爬虫任务(taskname)为空");
        }
        try {
            Date d = new Date();
            spider.setCreateDate(d);
            spider.setReviseDate(d);
            spider.setCreator(user.getUserName());
            String spiderID = spiderWorkService.savaSpiderCache(spider, 3600);
            data.put("spider", spiderID);
            result = BeanResult.success(data);
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e);
        }
        return result;
    }

    /**
     * 获取爬虫标识
     * URI	/sp/ch
     * POST	说明：将任务名和按照相应规则生成的spiderid存入cache中，并把返回的spiderid显示在“任务标识”栏。
     * 参数1：taskname={任务名}（必填）
     * 成功：{"status": "SUCCESS","data": {"spider": "爬虫标识"} }
     * 失败：{"status": "ERROR","cause": "错误原因" }
     */
    @RequestMapping(value = "/sp/ch/{spiderid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getSpiderParam(@PathVariable String spiderid, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        User user = (User) request.getAttribute("currentUser");

        try {
            SpiderBaseInfo spider = spiderWorkService.getSpiderCache(spiderid);
            data.put("spider", JSON.toJSONString(spider));
            result = BeanResult.success(data);
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e);
        }
        return result;
    }

    /**
     * 获取爬虫备注和深度
     * URI	/sp/ch
     * POST	说明：将任务名和按照相应规则生成的spiderid存入cache中，并把返回的spiderid显示在“任务标识”栏。
     * 参数1：taskname={任务名}（必填）
     * 成功：{"status": "SUCCESS","data": {"spider": "爬虫标识"} }
     * 失败：{"status": "ERROR","cause": "错误原因" }
     */
    @RequestMapping(value = "/sp/ch/{spiderid}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult updateSpiderParam(@PathVariable String spiderid, @RequestBody SpiderBaseInfo spider, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        User user = (User) request.getAttribute("currentUser");

        try {
                /*cache中获取*/
            SpiderBaseInfo spiderBefore = spiderWorkService.getSpiderCache(spiderid);
//                SimpleBeanPropertiesUtil.copyNotNullProperties(spider, spiderBefore);
                /*TODO  不确定  目前需求只有这两个*/
            if (spider.getRemark() != null) {
                spiderBefore.setRemark(spider.getRemark());
            }
            if (spider.getDepth() != null) {
                spiderBefore.setDepth(spider.getDepth());
            }
            Date d = new Date();
            spiderBefore.setReviseDate(d);
            String spiderID = spiderWorkService.savaSpiderCache(spiderBefore, 3600);
            data.put("spider", spiderID);
            result = BeanResult.success(data);
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e);

        }
        return result;
    }

    /**
     * 4.3.2 爬取范围信息
     * 文件linux根路径：/nutch/config/{爬虫标识}/crawlscope.json
     * URI	/sp/cs
     * POST	说明：将“采集来源”中的所有内容拼接成的json内容保存到/nutch/config/{爬虫标识}/crawlscope.json文件中，并将该文件按照路径全部上传至HDFS.
     * 参数1：spiderid={爬虫标识}（必填）
     * 参数2：由“采集来源”中的所有内容拼接成的json串（必填）【具体格式见备注】
     * 成功： { "status": "SUCCESS", "data": { "spider": "爬虫标识" } }
     * 失败： { "status": "ERROR",  "cause": "错误原因"  }
     */
    @RequestMapping(value = "/sp/cs/{spiderid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult writeCrawlScope(@PathVariable String spiderid, @RequestParam String crawlscopejson, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        FileWriter fw = null;
        BufferedWriter bw = null;
        User user = (User) request.getAttribute("currentUser");

        if (!hasNonNullAndNonEmptyValue(crawlscopejson)) {
            result = BeanResult.error("爬取范围(crawlscopejson)为空");
        }
        try {
            /**
             * 存文件
             */
            String realpath = NUTCH_CONFIG + spiderid + "/crawlscope.json";
            BeanResult crawlStr = writeFile(spiderid, crawlscopejson, realpath);
            data.put("crawl", crawlStr);
            String urlPath = NUTCH_URL + spiderid + "/url.txt";
                /*将“采集来源url”写入到/nutch/urls/{爬虫标识}/url.txt文件中，并将该文件按照路径全部上传至HDFS.*/
            Pattern p = Pattern.compile("\\\"url\\\"\\s*\\:\\s*\\\"([^\"]+)\\\"");
            Matcher m = p.matcher(crawlscopejson);
            //System.out.println(m.groupCount());
            StringBuilder urljson = new StringBuilder();
            while (m.find()) {
                urljson.append(m.group(1)).append("\n");
            }
            logger.debug("stb:{}", urljson);
            BeanResult urlStr = writeFile(spiderid, urljson.toString(), urlPath);
            data.put("url", urlStr);
            result = BeanResult.success(data);
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e);
        }
        return result;
    }

    @RequestMapping(value = "/sp/cs/{spiderid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult readCrawlScope(@PathVariable String spiderid, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        FileWriter fw = null;
        BufferedWriter bw = null;
        User user = (User) request.getAttribute("currentUser");
        try {
            /**
             * 读文件
             */
            String realpath = NUTCH_CONFIG + spiderid + "/crawlscope.json";
            logger.debug("爬取源信息realpath", realpath);
            return readFile(spiderid, realpath);
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e);
        }
        return result;
    }

    /**
     * 4.3.3 爬取源信息(已合并到4.3.2)
     * 文件：/nutch/urls/{爬虫标识}/url.txt
     * URI	/sp/cs
     * POST	说明：将“采集来源url”写入到/nutch/urls/{爬虫标识}/url.txt文件中，并将该文件按照路径全部上传至HDFS.
     * 参数1：spiderid={爬虫标识}（必填）
     * 参数2：采集来源url（必填）
     * 成功：
     * {"status": "SUCCESS","data": {"spiderid": "爬虫标识", "taskname": "任务名","remark": "备注",  "depth": "爬取深度" }}
     * 失败：{"status": "ERROR", "cause": "错误原因"}
     */
    @RequestMapping(value = "/sp/us/{spiderid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult writeURL(@PathVariable String spiderid, @RequestParam String urljson, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        FileWriter fw = null;
        BufferedWriter bw = null;
        User user = (User) request.getAttribute("currentUser");
        if (!hasNonNullAndNonEmptyValue(urljson)) {
            result = BeanResult.error("爬取范围(urljson)为空");
        }
        try {
            /**
             * 存文件
             */
            /*TODO*/
            String realpath = NUTCH_URL + spiderid + "/url.txt";
            logger.debug("爬取源信息realpath", realpath);
            return writeFile(spiderid, urljson, realpath);
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e);
        }
        return result;
    }

    @RequestMapping(value = "/sp/us/{spiderid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult readURL(@PathVariable String spiderid, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        FileWriter fw = null;
        BufferedWriter bw = null;
        User user = (User) request.getAttribute("currentUser");
        try {
            /**
             * 读文件
             */
            String realpath = NUTCH_URL + spiderid + "/url.txt";
            logger.debug("爬取源信息realpath", realpath);
            return readFile(spiderid, realpath);
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e);

        }
        return result;
    }

    /**
     * 4.3.4 解析页面
     * 文件：/nutch/config/{爬虫标识}/parse.json
     * URI	/sp/ps
     * POST	说明：将“采集维度”中的所有内容拼接成的json内容保存到/nutch/config/{爬虫标识}/parse.json文件中
     * 参数1：spiderid={爬虫标识}（必填）
     * 参数2：由“采集维度”中的所有内容拼接成的json串（必填）【具体格式见备注】
     * 成功：{"status": "SUCCESS","data": {"spider": "爬虫标识"}}
     * 失败：{"status": "ERROR","cause": "错误原因"}
     * <p>
     * 后来:
     * <p>
     * 不需要存本地, 直接存 Redis (2016-11-23)
     * <p>
     * 将爬虫配置文件 parse.json (json 格式) 写入到 Redis 中
     * <p>
     * redis 中的 KEY : spiderid_[spiderId]
     * 例如: spiderd_xinlangboke_1479880143107
     * <p>
     * redis 中的 VALUE : JSON 串
     */
    @RequestMapping(value = "/sp/ps/{spiderid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult writeParse(@PathVariable String spiderid, @RequestParam String parsejson, HttpServletRequest request) {
        BeanResult result = null;
        try (Jedis jedis = this.redisCache.getShard()) {
            String key = "spiderid_" + spiderid;
            logger.debug("save parse.json key : {}", key);
            logger.debug("save parse.json value : {}", parsejson);

            jedis.set(key, parsejson);
            result = BeanResult.success("OK");
        } catch (Exception e) {
            result = BeanResult.error(e.getMessage());
        }

        return result;


    }

    /**
     * 这段代码已经不用了, 暂时放在这里做参考 (程超)
     * <p>
     * 4.3.4 解析页面
     * 文件：/nutch/config/{爬虫标识}/parse.json
     * URI	/sp/ps
     * POST	说明：将“采集维度”中的所有内容拼接成的json内容保存到/nutch/config/{爬虫标识}/parse.json文件中
     * 参数1：spiderid={爬虫标识}（必填）
     * 参数2：由“采集维度”中的所有内容拼接成的json串（必填）【具体格式见备注】
     * 成功：{"status": "SUCCESS","data": {"spider": "爬虫标识"}}
     * 失败：{"status": "ERROR","cause": "错误原因"}
     *
     * @param spiderid
     * @param parsejson
     * @param request
     * @return
     */
    private BeanResult unuseMethod(String spiderid, String parsejson, HttpServletRequest request) {

        BeanResult result = null;

        Map<String, Object> data = new HashMap<>();
        FileWriter fw = null;
        BufferedWriter bw = null;
        User user = (User) request.getAttribute("currentUser");
        if (!hasNonNullAndNonEmptyValue(parsejson)) {
            result = BeanResult.error("采集维度(parsejson)为空");
            return result;
        }
        try {
            /**
             * 存文件
             */
            /*TODO*/
            String realpath = NUTCH_URL + spiderid + "/url.txt";
            logger.debug("解析页面realpath", realpath);
            String rootPath = this.getFileRootPath();        // 项目根路基
            File parseFile = new File(rootPath + realpath);
            if (!parseFile.exists()) {
                parseFile.getParentFile().mkdirs();
                parseFile.createNewFile();
            }
                /*在原有字符后追加*/
            //fw = new FileWriter(f, true);
                    /*替换原有字符*/
            fw = new FileWriter(parseFile);
            bw = new BufferedWriter(fw);
            bw.write(parsejson);


            data.put("spider", spiderid);
            result = BeanResult.success(data);
                /*不需要存hadoop，直接存本地*/

            return result;
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e);
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (Exception e) {
                logger.error("Exception", e);
                result = BeanResult.error("异常 ： " + e);
            }
        }
        return result;
    }

    /**
     * GET	说明：修改已有的/nutch/config/{爬虫标识}/parse.json。
     * 参数1：spiderid={爬虫标识}（必填）
     * 参数2：由“采集维度”中的所有内容拼接成的json串（必填）
     * 成功：{"status": "SUCCESS","data": {"spider": "爬虫标识"}}
     * 失败：{"status": "ERROR", "cause": "错误原因"}
     */

    @RequestMapping(value = "/sp/ps/{spiderid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult readParse(@PathVariable String spiderid, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        BufferedReader br = null;
        User user = (User) request.getAttribute("currentUser");
        try {
            /**
             * 读文件
             */
            String realpath = NUTCH_CONFIG + spiderid + "/crawlscope.json";
            logger.debug("修改采集维度realpath:{}", realpath);
            // 项目根路基
            String rootPath = this.getFileRootPath();
            File file = new File(rootPath + realpath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            StringBuilder readurl = new StringBuilder();
            //构造一个BufferedReader类来读取文件
            br = new BufferedReader(new FileReader(file));
            String urlstr = null;
            while ((urlstr = br.readLine()) != null) {
                readurl.append(System.lineSeparator() + urlstr);
            }

            data.put("spiderid", spiderid);
            data.put("crawlscope", readurl.toString());
            result = BeanResult.success(data);
            return result;
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e);
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                logger.error("Exception", e);
                result = BeanResult.error("异常 ： " + e);
            }

        }
        return result;
    }


    public BeanResult createWorkZip(@PathVariable String spiderid, @RequestParam String parsejson, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();

        //ObjectOutputStream objectOutputStream = null;
        //User user = (User) request.getAttribute("currentUser");
        try {
            /*写文件*/
            // 项目根路基
            /*文件*/
            String rootPath = this.getFileRootPath();
            String realpath = NUTCH_CONFIG + spiderid + "/run.job";
            logger.debug("修改采集维度realpath:{}", realpath);

            File runjob = new File(rootPath + realpath);
            if (!runjob.exists()) {
                runjob.getParentFile().mkdirs();
                runjob.createNewFile();
            }
            /*job内容*/
            Map<String, Object> jobMap = new LinkedHashMap<>();
            jobMap.put("type", "command");
            jobMap.put("command", "pwd");
            /*
            jobMap.put("command.1", "hadoop jar /usr/local/nutch/test1/release-1.6/runtime/deploy/apache-nutch-1.6.job" +
                    "  " + "org.apache.nutch.crawl.Crawl" +
                    "  " + "/nutch/" + spiderid + "/urls" +
                    "  " + "-dir  /nutch/" + spiderid + "/data_custom");
            */
            jobMap.put("command.1", "bash /work/nutch/deploy/run-nutch.sh " + spiderid);

            /*
             * 写job文件流
             * 在原有字符后追加
             */
            //fw = new FileWriter(f, true);
            StringBuilder sb = new StringBuilder();
            jobMap.forEach((key, value) ->
                    sb.append(key).append("=").append(value.toString()).append('\n'));

            try (FileWriter fw = new FileWriter(runjob);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(sb.toString());
            } catch (Exception e) {
                logger.error("Exception:{}", e);
            }


            /* 压缩包 */
            //定义zip路径
            String zipFile = NUTCH_CONFIG + spiderid + ".zip";
            File zip = new File(rootPath + zipFile);
            if (!zip.exists()) {
                zip.getParentFile().mkdirs();
                zip.createNewFile();
            }
            List<File> files = new LinkedList<>();
            files.add(0, runjob);

            try (FileOutputStream fous = new FileOutputStream(zip);
                 ZipOutputStream zipOut = new ZipOutputStream(fous)) {

                /*压缩*/
                ZipUtil.zipFile(files, zipOut);
            } catch (Exception e) {
                logger.error("Exception:{}", e);
            }

            // 上传hdfs
            Configuration conf = new Configuration();
            // HDFS路径
            // String dst = HDFS_URL + realpath;
            // boolean status = HDFSUtil.put2HSFS(rootPath + realpath, dst, conf);
            /**
             * 上传Azkaban
             */
            AzkabanOperator azkabanOperator = new AzkabanOperator();
            net.sf.json.JSONObject azLoginJson = azkabanOperator.login();
            String sessionid = azLoginJson.get("session.id").toString();
            logger.debug("sessionid:{}", sessionid);
            /**
             * sessionid,项目名,项目描述
             */
            net.sf.json.JSONObject azCreateProject = azkabanOperator.creatProject(sessionid, "spider_" + spiderid, spiderid);
            logger.debug("azCreateProject:{}", azCreateProject);
            //上传
            String filename = rootPath + zipFile;
            String uploadUrl = ConfigUtil.getConfig("azkaban.url") + "manager";
//                String projectname = spiderid;
            String uploadEntry = FileUploadZip.uploadFile(uploadUrl, "spider_" + spiderid, filename, sessionid);
            data.put("spider", spiderid);
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
        }
        return result;
    }


    /**
     * 上传hdfs
     *
     * @param spiderid
     * @param jsonStr
     * @param realpath
     * @return
     */
    public BeanResult writeFile(String spiderid, String jsonStr, String realpath) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            /* 存文件*/
//        String realpath = "/nutch/urls/" + spiderid + "/url.txt";
            String rootPath = this.getFileRootPath();        // 项目根路基
            File parseFile = new File(rootPath + realpath);
            if (!parseFile.exists()) {
                parseFile.getParentFile().mkdirs();
                parseFile.createNewFile();
            }
                /*在原有字符后追加*/
            //fw = new FileWriter(f, true);
                    /*替换原有字符*/
            fw = new FileWriter(parseFile);
            bw = new BufferedWriter(fw);
            bw.write(jsonStr);

            bw.close();
            fw.close();
                /*OSS服务器*/
//            flexibleFileUtil.saveUploadFile(parseFile, realpath,
//                    FlexibleFileType.PUB);
//            Configuration conf = new Configuration();
                /*HDFS路径*/
            String hdfs = HDFS_URL;
            boolean status = HDFSUtil.put2HSFS(hdfs, rootPath + realpath, rootPath + realpath);
            if (status) {
                data.put("spider", spiderid);
                result = BeanResult.success(data);

            } else {
                data.put("spider", spiderid);
                result = BeanResult.error("文件上传HDFS失败");
            }

            return result;
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e);
        }
        return result;
    }

    /**
     * 从hdfs读取
     *
     * @param spiderid
     * @param realpath
     * @return
     */
    public BeanResult readFile(String spiderid, String realpath) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
            /*读文件 */
            // String realpath = "/nutch/config/" + spiderid + "/crawlscope.json";
            // 项目根路基
            String rootPath = this.getFileRootPath();
                /*HDFS路径*/
            String hdfs = HDFS_URL;
//            Configuration conf = new Configuration();
            boolean status = HDFSUtil.getFromHDFS(hdfs, rootPath + realpath, rootPath + realpath);
            File file = new File(rootPath + realpath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            StringBuilder sb = new StringBuilder();
            try {
                //构造一个BufferedReader类来读取文件
                BufferedReader br = new BufferedReader(new FileReader(file));
                String s = null;
                //使用readLine方法，一次读一行
                while ((s = br.readLine()) != null) {
                    sb.append(System.lineSeparator() + s);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            data.put("spiderid", spiderid);
            data.put("crawlscope", sb.toString());
            result = BeanResult.success(data);
            return result;
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e);
        }
        return result;
    }

    /**
     * 第一步  将文件写入hadoop
     * 第二部  将任务生成zip并上传
     * 第三步  前两步成功后把cache中的数据存入数据库
     *
     * @param spiderid
     * @param parsejson
     * @param request
     * @return
     */
    @RequestMapping(value = "/sp/zip/{spiderid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult forStepOver(@PathVariable String spiderid, @RequestParam String parsejson, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
            /**
             * 为高嵩准备标题的拼音(TODO)
             */
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(parsejson);
//        JSONArray jsonresult = jsonObject.getJSONObject("parse").getJSONObject("page").getJSONObject("fields").getJSONArray("field");
//        logger.debug("result:{}", jsonresult);
        /*获取英文值*/
            Pattern p = Pattern.compile("\\\"name\\\"\\s*\\:\\s*\\\"(\\w+)\\\"");
            Matcher m = p.matcher(parsejson);
            System.out.println(m.groupCount());
            StringBuilder stb = new StringBuilder();
            List<String> fields = new ArrayList<String>();
            while (m.find()) {
                fields.add(m.group(1));
                stb.append(m.group(1)).append(":");
            }
            logger.debug("stb:{}", stb);
            //创建Hive外部表
            /*TODO*/
            //PrestoUtil.CreateHiveExternalTable(spiderid, fields);
        /*页面第四步----第一步  */
            writeParse(spiderid, parsejson, request);
        /*页面第四步----第二部  将任务生成zip并上传*/
            createWorkZip(spiderid, parsejson, request);
        /*页面第四步----第三步  前两步成功后把cache中的数据存入数据库*/
        /*从cache中读取*/
            SpiderBaseInfo spider = spiderWorkService.getSpiderCache(spiderid);
        /*存入数据库*/
            String spiderID = spiderWorkService.addSpider(spider);
            if (spider.equals(spiderID)) {
                data.put("spiderid", spiderID);
                result = BeanResult.success(data);
                return result;

            } else {
                data.put("spider", spiderID);
                result = BeanResult.success("爬虫配置成功");
            }
            return result;
        } catch (Exception e) {
            logger.error("Exception", e);
            result = BeanResult.error("异常 ： " + e);
        }
        return result;
    }
}
