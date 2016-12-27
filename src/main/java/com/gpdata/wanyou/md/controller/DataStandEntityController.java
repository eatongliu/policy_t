package com.gpdata.wanyou.md.controller;

import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.ds.util.FileUtil;
import com.gpdata.wanyou.md.entity.DataStandEntity;
import com.gpdata.wanyou.md.entity.DataStandard;
import com.gpdata.wanyou.md.service.DataStandEntityService;
import com.gpdata.wanyou.md.service.DataStandardService;
import com.gpdata.wanyou.utils.ConfigUtil;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据标准实体
 * <p>
 * Created by ligang on 16-10-25.
 */
@Controller
@RequestMapping
public class DataStandEntityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataStandEntityController.class);
    @Autowired
    private DataStandEntityService dataStandEntityService;
    @Autowired
    private DataStandardService dataStandardService;

    /**
     * 获取指定 id 的对象
     * 
     * @param standentId
     * @return DataStandEntity
     */
    @RequestMapping(value = "/md/dse/{standentId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getDataStandEntityInfoById(@PathVariable("standentId") Integer standentId) {
        LOGGER.debug("input : {}", standentId);
        try {
            // 根据传入参数获取实体
            DataStandEntity bean = this.dataStandEntityService.getById(standentId);
            if (bean == null) {
                return BeanResult.error("数据不存在!");
            }
            return BeanResult.success(bean);

        } catch (Exception e) {
            LOGGER.error("Exception : {}", e.getCause());
            return BeanResult.error("获取失败 ： " + e.getMessage());
        }

    }

    /**
     * 保存新对象
     *
     * @param input
     * @return standentid
     */
    @RequestMapping(value = "/md/dse", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public BeanResult addDataStandEntity(@RequestBody DataStandEntity input) {

        LOGGER.debug("input : {}", input);
        if (input.getStandentName() == null) {
            return BeanResult.error("中文名称 (standentName)不存在值");
        }
        if (input.getStandentCaption() == null) {
            return BeanResult.error("英文名称 (standentCaption)不存在值");
        }
        if (input.getStandId() == null) {
            return BeanResult.error("数据标准id (standId)不存在值");
        }
        if (input.getDataType() == null) {
            return BeanResult.error("数据类型 (dataType)不存在值");
        }
        if (input.getStandId() == null) {
            return BeanResult.error("数据标准id (standId)不存在值");
        }
        if (input.getDataScope() == null) {
            return BeanResult.error("数据范围 (dataScope)不存在值");
        }
        if (input.getCheckFormula() == null) {
            return BeanResult.error("校验公式 (checkFormula)不存在值");
        }
        try {
            int standentid = this.dataStandEntityService.save(input);
            return BeanResult.success(standentid);

        } catch (Exception e) {
            LOGGER.error("Exception : ", e);
            return BeanResult.error("保存失败 ： " + e.getMessage());
        }
    }

    /**
     * 修改
     * 
     * @param standentId
     * @return
     */
    @RequestMapping(value = "/md/dse/{standentId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    @ResponseBody
    public BeanResult updateDataStandEntity(@PathVariable("standentId") Integer standentId,
            @RequestBody DataStandEntity input) {
        LOGGER.debug("input : {}", input);
        if (input.getStandentName() == null) {
            return BeanResult.error("中文名称 (standentName)不存在值");
        }
        if (input.getStandentCaption() == null) {
            return BeanResult.error("英文名称 (standentcaption)不存在值");
        }
        if (input.getStandId() == null) {
            return BeanResult.error("数据标准id (standId)不存在值");
        }
        if (input.getDataType() == null) {
            return BeanResult.error("数据类型 (dataType)不存在值");
        }
        if (input.getStandId() == null) {
            return BeanResult.error("数据标准id (standId)不存在值");
        }
        if (input.getDataScope() == null) {
            return BeanResult.error("数据范围 (dataScope)不存在值");
        }
        if (input.getCheckFormula() == null) {
            return BeanResult.error("校验公式 (checkFormula)不存在值");
        }

        try {
            DataStandEntity bean = this.dataStandEntityService.getById(standentId);
            if (bean == null) {
                return BeanResult.error("数据标准实体不存在");
            } else {
                this.dataStandEntityService.update(input);
                return BeanResult.success("修改成功");
            }
        } catch (Exception e) {
            LOGGER.error("Exception : ", e);
            return BeanResult.error("修改失败 ： " + e.getMessage());
        }

    }

    /**
     * 删除
     * 
     * @param standentId
     * @return
     */
    @RequestMapping(value = "/md/dse/{standentId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.DELETE)
    @ResponseBody
    public BeanResult deleteDataStandEntity(@PathVariable("standentId") Integer standentId) {
        LOGGER.debug("input : {}", standentId);
        try {
            this.dataStandEntityService.delete(standentId);
            return BeanResult.success("删除成功");
        } catch (Exception e) {
            LOGGER.error("Exception : {}", e.getCause());
            return BeanResult.error("删除失败 ： " + e.getMessage());
        }
    }

    /**
     * 条件查询
     * 
     * @param input ,...,limit,offset
     * @return DataStandEntity
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/md/dse", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public BeanResult queryDataStandEntityInfo(@RequestBody DataStandEntity input,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset) {
        if (LOGGER.isDebugEnabled()) {
            Object[] argArray = { input, limit, offset };
            LOGGER.debug(
                    "searchData_Stand_Entity standentname ,standentcaption,datatype, limit ,offset: {} ,{},{}, {} , {}",
                    argArray);
        }
        try {
            if (limit > 100 || limit < 1) {
                limit = 10;
            }
            Map<String, Object> searchResult = dataStandEntityService.query(input, limit, offset);
            return BeanResult.success((int) searchResult.get("total"),
                    (List<DataStandEntity>) searchResult.get("rows"));
        } catch (Exception e) {
            LOGGER.error("searchOntologyException : ", e);
            return BeanResult.error("获取失败 ： " + e.getMessage());
        }

    }

    /**
     * 接收一个上传的导入文件
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/md/dse/upload/{standId}"
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE
            , method = RequestMethod.POST)
    @ResponseBody
    public BeanResult upload(@PathVariable("standId") Integer standId, @RequestBody MultipartFile file) {
        LOGGER.debug("input : {}", standId);
        // 判断文件是否存在
        DataStandard bean = this.dataStandardService.getById(standId);
        if (bean == null) {
            return BeanResult.error("数据标准不存在! ");
        }

        if (file.isEmpty()) {
            return BeanResult.error("文件不存在! ");
        }

        String originName = file.getOriginalFilename();
        // 文件的前缀名
        String prefixFileName = originName.substring(0, originName.lastIndexOf("."));
        // 文件的后缀名
        String suffixFileName = originName.substring(originName.lastIndexOf("."));

        String inString;
        List<DataStandEntity> dataStandEntityList = new ArrayList<>();

        if (suffixFileName.isEmpty()
                || (!suffixFileName.equalsIgnoreCase(".csv")
                && !suffixFileName.equalsIgnoreCase(".xls")
                && !suffixFileName.equalsIgnoreCase(".xlsx"))) {
            return BeanResult.error("上传文件格式错误! ");
        }

        try (
                InputStream inputStream = file.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader reader = new BufferedReader(inputStreamReader)) {

            // 处理CSV文件
            if (suffixFileName.equalsIgnoreCase(".csv")) {
                while ((inString = reader.readLine()) != null) {
                    //  String[] data = inString.split("\\s+,\\s+");
                    String[] data = inString.split(",");
                    if (data.length >= 13) {
                        DataStandEntity dse = new DataStandEntity();
                        // 中文名称
                        dse.setStandentName(data[0]);
                        // 英文名称
                        dse.setStandentCaption(data[1]);
                        // 字段类型
                        dse.setDataType(data[2]);
                        // 数据长度
                        dse.setDataLength(Long.valueOf(data[3]));
                        // 是否允许空值
                        dse.setAllowNull(data[4]);
                        // 是否唯一
                        dse.setIsUnique(data[5]);
                        // 数据范围
                        dse.setDataScope(data[6]);
                        // 校验公式
                        dse.setCheckFormula(data[7]);
                        // 数据精度
                        dse.setDataPrecision(Integer.valueOf(data[8]));
                        // 数据刻度
                        dse.setDataScale(Integer.valueOf(data[9]));
                        // 默认值
                        dse.setDefVal(data[10]);
                        // 枚举数值
                        dse.setEnumVal(data[11]);
                        // 数据单位
                        dse.setDataUnit(data[12]);
                        // 数据标准ID
                        dse.setStandId(standId);
                        dataStandEntityList.add(dse);

                    } else {
                        return BeanResult.error("上传文件格式错误! ");
                    }
                }
            }
            // 处理EXCEL文件
            // else if (suffixFileName.equals(".xls") || suffixFileName.equals(".xlsx")) {
            if (suffixFileName.equalsIgnoreCase(".xls") || suffixFileName.equalsIgnoreCase(".xlsx")) {
                // HSSFWorkbook excel = new HSSFWorkbook(is);
                // int rows = excel.getSheetAt(0).getLastRowNum();
                Workbook book = Workbook.getWorkbook(inputStream);
                // 获取第一个工作表格
                Sheet sheet = book.getSheet(0);
                // 获取总的行数
                int rows = sheet.getRows();
                // 获取总的列数
                int cols = sheet.getColumns();
                if (cols >= 13) {
                    for (int i = 0; i < rows; i++) {
                        DataStandEntity dse = new DataStandEntity();
                        // 中文名称
                        dse.setStandentName(sheet.getCell(0, i).getContents().trim());
                        // 英文名称
                        dse.setStandentCaption(sheet.getCell(1, i).getContents().trim());
                        // 字段类型
                        dse.setDataType(sheet.getCell(2, i).getContents().trim());
                        // 数据长度
                        dse.setDataLength(Long.valueOf(sheet.getCell(3, i).getContents().trim()));
                        // 是否允许空值
                        dse.setAllowNull(sheet.getCell(4, i).getContents().trim());
                        // 是否唯一
                        dse.setIsUnique(sheet.getCell(5, i).getContents().trim());
                        // 数据范围
                        dse.setDataScope(sheet.getCell(6, i).getContents().trim());
                        // 校验公式
                        dse.setCheckFormula(sheet.getCell(7, i).getContents().trim());
                        // 数据精度
                        dse.setDataPrecision(Integer.valueOf(sheet.getCell(8, i).getContents().trim()));
                        // 数据刻度
                        dse.setDataScale(Integer.valueOf(sheet.getCell(9, i).getContents().trim()));
                        // 默认值
                        dse.setDefVal(sheet.getCell(10, i).getContents().trim());
                        // 枚举数值
                        dse.setEnumVal(sheet.getCell(11, i).getContents().trim());
                        // 数据单位
                        dse.setDataUnit(sheet.getCell(12, i).getContents().trim());
                        // 数据标准ID
                        dse.setStandId(standId);
                        dataStandEntityList.add(dse);
                    }
                } else {
                    return BeanResult.error("上传文件格式错误! ");
                }
            }

            dataStandEntityService.upload(dataStandEntityList);

                /*
                 * // 服务器保存文件的根路径 String Upload_Path =
                 * ConfigUtil.getConfig("UPLOAD_PATH"); Path filePath =
                 * Paths.get(Upload_Path); // 文件在服务器上更名为原始名+系统时间long值+原始后缀
                 * String newFileName = prefixFileName +
                 * System.currentTimeMillis() + suffixFileName;
                 * FileUtil.write(file.getInputStream(), filePath, newFileName);
                 */

            return BeanResult.success(standId);
        } catch (Exception e) {
            LOGGER.error("searchOntologyException : ", e);
            return BeanResult.error("上传失败 ： " + e.getMessage());
        }

    }

    /**
     * 获取指定数据标准下的所有数据标准实体
     * 
     * @param standId
     * @param limit
     * @param offset
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/md/ds/{standId}/dses"
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE
            , method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getDataStandEntityInfoByStandId(@PathVariable("standId") Integer standId,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset) {
        if (LOGGER.isDebugEnabled()) {
            Object[] argArray = { standId, limit, offset };
            LOGGER.debug("searchOntologyBaseinfo standId , limit ,offset: {} , {} , {}", argArray);
        }
        try {
            if (limit > 100 || limit < 1) {
                limit = 10;
            }
            Map<String, Object> searchResult = dataStandEntityService.getAllByStandId(standId, limit, offset);
            return BeanResult.success(Integer.valueOf(searchResult.get("total").toString()),
                    (List<DataStandEntity>) searchResult.get("rows"));
        } catch (Exception e) {
            LOGGER.error("searchOntologyException : ", e);
            return BeanResult.error("获取失败 ： " + e.getMessage());
        }
    }

}
