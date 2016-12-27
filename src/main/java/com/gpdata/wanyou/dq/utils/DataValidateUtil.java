package com.gpdata.wanyou.dq.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.base.dao.SimpleDao;
import com.gpdata.wanyou.dq.dao.ValidateRangeRecordDao;
import com.gpdata.wanyou.dq.dao.ValidateRangeRuleDao;
import com.gpdata.wanyou.dq.entity.ValidateFormula;
import com.gpdata.wanyou.dq.entity.ValidateRangeRecord;
import com.gpdata.wanyou.dq.entity.ValidateRangeRule;
import com.gpdata.wanyou.dq.entity.ValidateRecordDetails;
import com.gpdata.wanyou.ds.util.HDFSUtil;
import com.gpdata.wanyou.utils.ConfigUtil;
import com.gpdata.wanyou.utils.EsClientUtil;

/**
 * @author ligang 2016年11月16日
 * 
 *         数据质量验证
 */
@Component
public class DataValidateUtil {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SimpleDao simpleDao;
    @Autowired
    private ValidateRangeRuleDao validateRangeRuleDao;
    @Autowired
    private ValidateRangeRecordDao validateRangeRecordDao;



    // 范围验证主线方法
    public List<List> rangeValidate(Integer datasource, Integer table, List<List> messageList) {
        
        String hdfs = ConfigUtil.getConfig("HDFS.url");

        // 生成原始数据、处理数据和Es数据
        List<String> orginalRowsList = new ArrayList<String>();
        List<String> dealedRowsList = new ArrayList<String>();
        List<Map<String, Object>> ESList = new ArrayList<Map<String, Object>>();
        //表头
        List fieldList= messageList.get(0);
        //kafka生产所需信息
        List<List> kafkaList = new ArrayList<>();
        kafkaList.add(fieldList);
        //处理数据
        for (int i = 1; i < messageList.size(); i++) {
            //原始数据
            orginalRowsList.add(createOrginalRow(messageList.get(i)));
            //处理后数据
            Pair<String,Boolean> pair= createDealedRow(messageList.get(i),fieldList,datasource,table);
            dealedRowsList.add(pair.getLeft());
            //验证通过放入kafka消息队列
            if(pair.getRight()){
                kafkaList.add(messageList.get(i));
                //本条数据验证通过 放入
            }else{
                //本条数据验证未通过
            }
            //ES数据
            ESList.add(createESRow(messageList.get(i),fieldList));
        }

        String[] orginalRows = (String[]) orginalRowsList.toArray(new String[orginalRowsList.size()]);
        String[] dealedRows = (String[]) dealedRowsList.toArray(new String[dealedRowsList.size()]);
        String dealedJson = JSONObject.toJSONString(ESList);
        // 存入原始数据
        String orginal = ConfigUtil.getConfig("HDFS.orginal") + "/" + datasource + "/" + table + "/content";
        HDFSUtil.appendFile(hdfs, orginal, orginalRows);
        // 存入处理后数据
        String dealed = ConfigUtil.getConfig("HDFS.dealed") + "/" + datasource + "/" + table + "/content";
        HDFSUtil.appendFile(hdfs, dealed, dealedRows);
        // 存入ES
        EsClientUtil.save2Es(datasource.toString(), table.toString(), dealedJson);
        return kafkaList;
    }

    // 生成原始数据
    private String createOrginalRow(List input) {
        char s = 0x001;

        StringBuilder buff = new StringBuilder();
        for (int i = 0; i < input.size(); i++) {
            buff.append(input.get(i).toString()).append(s);
        }
        if (buff.length() > 0) {
            buff.delete(buff.length() - 1, buff.length());
        }
        return buff.toString();
    }

    // 生成ES数据
    private Map<String, Object> createESRow(List input,List fieldList) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < fieldList.size(); i++) {
            map.put((String) fieldList.get(i), input.get(i));
        }
        logger.debug("EsRow:{}", JSONObject.toJSONString(map));
        return map;
    }

    // 生成验证后数据
    private Pair<String,Boolean> createDealedRow(List input,List fieldList,Integer datasourceId,Integer tableId) {
        char s = 0x001;
        char d = 0x003;
        StringBuilder buff = new StringBuilder();
        boolean bool = true;
        for (int i = 0; i < input.size(); i++) {
            String fieldName = (String) fieldList.get(i);
            String value = input.get(i).toString().split(String.valueOf(d))[0];
            // 根据fieldName获取元数据实体Id 获取规则对象List
            List<ValidateRangeRule> ruleList = validateRangeRuleDao.getRuleByField(datasourceId, tableId, fieldName);
            
            buff.append(value);
            //判断元数据实体是否有验证规则
            if (ruleList.isEmpty()) {
                buff.append(s);
            }
            for (ValidateRangeRule rule : ruleList) {
                // 获取实体验证记录
                ValidateRangeRecord record = validateRangeRecordDao.getRecordByRuleId(rule.getRuleId());
                if (record == null) {
                    record = new ValidateRangeRecord();
                    record.setRuleId(rule.getRuleId());
                    record.initRecord();
                    simpleDao.save(record);
                }
                // 进行验证
                String reason = validateByFormula(value, rule.getFormulaId(), rule.getMaxVal(), rule.getMinVal(),
                        rule.getDataPrecision());
                if (StringUtils.isBlank(reason)) {
                    // 通过
                    buff.append(d).append("1" + rule.getRuleId());
                    record.incrPassData();
                } else {
                    // 未通过
                    buff.append(d).append("0" + rule.getRuleId());
                    record.incrNPassData();
                    // 未通过则添加验证详情
                    ValidateRecordDetails details = new ValidateRecordDetails();
                    details.setNpassData(value);
                    details.setRuleId(rule.getRuleId());
                    details.setRecordId(record.getRecordId());
                    details.setReason(reason.toString());
                    simpleDao.save(details);
                    bool = false;
                }
                validateRangeRecordDao.updateRecord(record);
            }
            buff.append(s);
        }
        if (buff.length() > 0) {
            buff.delete(buff.length() - 1, buff.length());
        }
        logger.debug("DealedRow:{}", buff.toString());
        
        return Pair.of(buff.toString(), bool);
    }

    // 验证方法
    private String validateByFormula(String data, Integer formulaId, String maxVal, String minVal,
            Integer dataPrecision) {
        data = data == null?"":data;
        StringBuilder reason = new StringBuilder("");
        // 1).利用验证公式进行验证
        if (formulaId != null) {
            ValidateFormula validateFormula = simpleDao.getById(ValidateFormula.class, formulaId);
            String formula = validateFormula.getformula();
            if (StringUtils.isNotBlank(formula)) {
                Matcher matcher = Pattern.compile(formula).matcher(data);
                if (!matcher.matches()) {
                    reason.append(" 验证公式不通过  ");
                }
            }
        }
        //判断待验证数据是否为数字
        boolean isNum = Pattern.matches("^(-)?\\d+[.]?\\d+$", data);
        
        // 2).利用其他条件进行验证
        //如果最大值不为空，判断数据是否为数字，判断数据是否小于最大值
        if(maxVal != null || minVal != null || dataPrecision != null ){
            if(isNum){
                if(maxVal != null && new BigDecimal(data).compareTo(new BigDecimal(maxVal))==1){
                    reason.append(" 超出验证范围 ");
                }
                if(minVal != null && new BigDecimal(data).compareTo(new BigDecimal(minVal))==-1){
                    reason.append(" 超出验证范围 ");
                }
                if(dataPrecision != null  ){
                    int i = data.indexOf('.');
                    if(i==-1){
                        reason.append(" 数据精度不符 ");
                    }else if(data.substring(data.indexOf('.') + 1).length()!=dataPrecision){
                        reason.append(" 数据精度不符 ");
                    }
                }
            }else{
                reason.append(" 数据类型错误 ");
            }
        }
        return reason.toString().trim();
    }
}
