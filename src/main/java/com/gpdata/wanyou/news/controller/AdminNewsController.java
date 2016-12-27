package com.gpdata.wanyou.news.controller;

import com.gpdata.wanyou.base.controller.AdminBaseController;
import com.gpdata.wanyou.base.core.JsonResult;
import com.gpdata.wanyou.base.flexible.FlexibleFileType;
import com.gpdata.wanyou.base.flexible.FlexibleFileUtil;
import com.gpdata.wanyou.news.entity.Article;
import com.gpdata.wanyou.news.entity.ArticleCategory;
import com.gpdata.wanyou.news.service.AdminArticleService;
import com.gpdata.wanyou.utils.ConfigUtil;
import com.gpdata.wanyou.utils.page.PageDataList;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chengchaos
 */
@Controller
@RequestMapping
public class AdminNewsController extends AdminBaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminNewsController.class);
    private static final Map<String, Pair<String, String>> CATEGORY_TYPE_MPA;

    static {
        CATEGORY_TYPE_MPA = new HashMap<>();
        CATEGORY_TYPE_MPA.put("news", Pair.of("NEWS", "新闻资讯"));
        CATEGORY_TYPE_MPA.put("helper", Pair.of("HELPER", "遇到问题"));
        CATEGORY_TYPE_MPA.put("expert", Pair.of("EXPERT", "数据专家"));
        CATEGORY_TYPE_MPA.put("pagefooter", Pair.of("PAGEFOOTER", "页脚信息"));
    }


    private AdminArticleService adminArticleService;
    private FlexibleFileUtil flexibleFileUtil;

    @Resource
    public void setAdminArticleService(AdminArticleService adminArticleService) {
        this.adminArticleService = adminArticleService;
    }

    @Resource
    public void setFlexibleFileUtil(FlexibleFileUtil flexibleFileUtil) {
        this.flexibleFileUtil = flexibleFileUtil;
    }


    /**
     * @param categoryType
     * @param model
     * @return
     */
    @RequestMapping(value = "/news/{categoryType}/articles", method = RequestMethod.GET)
    public String showArticlesByCategoryType(@PathVariable String categoryType
            , @RequestParam(required = false) Long categoryId
            , @RequestParam(defaultValue = "1") Integer page
            , Model model) {

        Pair<String, String> flag = CATEGORY_TYPE_MPA.get(categoryType);
        Assert.notNull(flag);

        String articleType = flag.getLeft();
        List<ArticleCategory> articleCategorys = this.adminArticleService
                .getArticleCateogrysByType(articleType);

        Map<Long, ArticleCategory> articleCategoryMap = articleCategorys.stream()
                .collect(Collectors.toMap(ArticleCategory::getId, bean -> bean));

        model.addAttribute("flag", flag);
        model.addAttribute("categoryType", categoryType);
        model.addAttribute("articleCategorys", articleCategorys);
        model.addAttribute("articleCategoryMap", articleCategoryMap);

        PageDataList<Article> pageDataList = null;
        if (categoryId != null) {
            pageDataList = this.adminArticleService
                    .getArticlePageDataListByCategoryId(categoryId, page);
        } else {
            pageDataList = this.adminArticleService
                    .getArticlePageDataListByCategoryType(categoryType.toUpperCase(), page);

        }

        model.addAttribute("pageDataList", pageDataList);
        return "news/article-list";
    }


    /**
     * 新建
     *
     * @param categoryType
     * @param categoryId
     * @param model
     * @return
     */
    @RequestMapping(value = "/news/{categoryType}/articles", method = RequestMethod.GET, params = "new")
    public String newArticle(@PathVariable String categoryType, Model model) {

        Pair<String, String> flag = CATEGORY_TYPE_MPA.get(categoryType);
        Assert.notNull(flag);

        String articleType = flag.getLeft();
        List<ArticleCategory> articleCategorys = this.adminArticleService.getArticleCateogrysByType(articleType);

        model.addAttribute("flag", flag);
        model.addAttribute("categoryType", categoryType);
        model.addAttribute("articleCategorys", articleCategorys);

        return "news/article-new";
    }


    /**
     * 保存新建数据
     *
     * @param categoryType
     * @param article
     * @param model
     */
    @RequestMapping(value = "/news/{categoryType}/articles", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createArticle(@PathVariable String categoryType
            , Article article
            , Model model) {


        Pair<String, String> flag = CATEGORY_TYPE_MPA.get(categoryType);
        Assert.notNull(flag);

        article.setOt(new Date());
        article.setClickCount(Integer.valueOf(0));

        this.adminArticleService.saveArticle(article);

    }


    /**
     * 编辑
     *
     * @param categoryType
     * @param categoryId
     * @param articleId
     * @param model
     * @return
     */
    @RequestMapping(value = "/news/{categoryType}/{articleId}"
            , method = RequestMethod.GET)
    public String editArticle(@PathVariable String categoryType
            , @PathVariable Long articleId
            , Model model) {


        Pair<String, String> flag = CATEGORY_TYPE_MPA.get(categoryType);
        Assert.notNull(flag);
        System.out.println(flag);

        String articleType = flag.getLeft();
        List<ArticleCategory> articleCategorys = this.adminArticleService.getArticleCateogrysByType(articleType);

        model.addAttribute("flag", flag);
        model.addAttribute("categoryType", categoryType);
        model.addAttribute("articleCategorys", articleCategorys);

        Article article = this.adminArticleService.getArticleById(articleId);
        model.addAttribute("article", article);

        return "news/article-edit";
    }


    /**
     * 保存编辑数据
     *
     * @param categoryType
     * @param categoryId
     * @param articleId
     * @param model
     * @return
     */
    @RequestMapping(value = "/news/{categoryType}/{articleId}"
            , method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateArticle(@PathVariable String categoryType
            , @PathVariable Long articleId
            , Article article
            , Model model) {


        this.adminArticleService.updateArticle(article);

    }


    @RequestMapping(value = "/news/{categoryType}/{articleId}"
            , method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteArticle(@PathVariable String categoryType
            , @PathVariable Long articleId
            , Model model) {

        LOGGER.debug("DELETE ARTICLEID : {}", articleId);
        try {

            this.adminArticleService.deleteArticle(articleId);
            return "OK";
        } catch (Exception e) {
            LOGGER.error("{}", e);
            return "NO-OK";
        }

    }


    @RequestMapping(value = "/news/{categoryType}/title-image")
    public HttpEntity<JsonResult> uploadTitleImage(@PathVariable String categoryType
            , MultipartFile multipartFile) {

        JsonResult result = null;

        LOGGER.debug("上传文章标题图片");


        LOGGER.debug("文章标题图片路径策略: /upload/news/title/categoryType/uuid值.扩展名");

        StringBuilder savePathAndFileName = new StringBuilder();
        savePathAndFileName.append("upload/news/title/");
        savePathAndFileName.append(categoryType);
        savePathAndFileName.append("/");
        ;

        String fileName = multipartFile.getOriginalFilename();
        LOGGER.debug("上传的商品图片名: {}", fileName);
        String extension = StringUtils.getFilenameExtension(fileName);
        String newFileName = UUID.randomUUID().toString();
        LOGGER.debug("生成的商品图片名: {}.{}", newFileName, extension);

        savePathAndFileName.append(newFileName).append(".").append(extension);

        LOGGER.debug("保存文章标题图片到数据库的值: {}", savePathAndFileName);

        try {
            //FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), destination);
            flexibleFileUtil.saveUploadFile(multipartFile.getInputStream(), savePathAndFileName.toString(), FlexibleFileType.PUB);
            result = JsonResult.ok(savePathAndFileName);
        } catch (Exception e) {
            result = JsonResult.failure(e.getMessage());
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", MediaType.TEXT_HTML_VALUE);

        ResponseEntity<JsonResult> entity = new ResponseEntity<JsonResult>(result, httpHeaders, HttpStatus.OK);
        return entity;
    }


    /**
     * @param type
     * @param CKEditorFuncNum
     * @param upload
     * @return
     */
    @RequestMapping(value = "/news/ckeditor-uploader")
    public HttpEntity<String> uploadFckImage(@RequestParam String type
            , String CKEditorFuncNum
            , MultipartFile upload) {

        StringBuilder result = new StringBuilder();

        LOGGER.debug("上传文章内容图片");
        LOGGER.debug("文章内容路径策略: /upload/news/content/uuid值.扩展名");

        String staticResourceFilePrefix = ConfigUtil.getConfig("static_resource_file");
        // CEKditor中file域的name值是upload
        // 获取文件扩展名
        String fileName = upload.getOriginalFilename();
        LOGGER.debug("上传的商品图片名: {}", fileName);
        String extension = StringUtils.getFilenameExtension(fileName);

        String newFileName = UUID.randomUUID().toString();
        LOGGER.debug("生成的商品图片名: {}.{}", newFileName, extension);
        StringBuilder savePathAndFileName = new StringBuilder();

        savePathAndFileName.append("upload/news/content/");
        savePathAndFileName.append(newFileName).append(".").append(extension);

        LOGGER.debug("保存文章标题图片到数据库的值: {}", savePathAndFileName);

        // CKEditorFuncNum 是回调时显示的位置，这个参数必须有
        String callback = CKEditorFuncNum;
        result.append("<script type=\"text/javascript\">");
        result.append("window.parent.CKEDITOR.tools.callFunction(");
        result.append(callback);
        result.append(",'");
        result.append(staticResourceFilePrefix + savePathAndFileName);
        result.append("',''");
        result.append(")");
        result.append("</script>");

        try {
            //FileUtils.copyInputStreamToFile(upload.getInputStream(), destination);
            flexibleFileUtil.saveUploadFile(upload.getInputStream(), savePathAndFileName.toString(), FlexibleFileType.PUB);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", MediaType.TEXT_HTML_VALUE);

        ResponseEntity<String> entity = new ResponseEntity<>(result.toString(), httpHeaders, HttpStatus.OK);
        return entity;
    }
}
