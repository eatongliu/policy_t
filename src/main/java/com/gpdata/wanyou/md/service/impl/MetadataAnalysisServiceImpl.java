package com.gpdata.wanyou.md.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.ds.dao.FieldDao;
import com.gpdata.wanyou.ds.dao.ResourceDao;
import com.gpdata.wanyou.ds.dao.TableDao;
import com.gpdata.wanyou.ds.entity.DataSourceField;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.ds.entity.DataSourceTable;
import com.gpdata.wanyou.md.dao.MetadataInfoDao;
import com.gpdata.wanyou.md.dao.OntologyDao;
import com.gpdata.wanyou.md.dto.AnalysisResultBean;
import com.gpdata.wanyou.md.entity.MetadataInfo;
import com.gpdata.wanyou.md.entity.OntologyBaseinfo;
import com.gpdata.wanyou.md.service.MetadataAnalysisService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by chengchao on 16-10-8.
 */
@Service
public class MetadataAnalysisServiceImpl extends BaseService implements MetadataAnalysisService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataAnalysisServiceImpl.class);

    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

    @Autowired
    private MetadataInfoDao metadataInfoDao;

    @Autowired
    private OntologyDao ontologyDao;

    @Autowired
    private FieldDao fieldDao;

    @Autowired
    private TableDao tableDao;

    @Autowired
    private ResourceDao resourceDao;


    /**
     * @param pattern
     * @param input
     * @return
     */
    private List<String> splitToList(Pattern pattern, CharSequence input) {
        return splitToList(pattern, input, 0);
    }

    /**
     * @param pattern
     * @param input
     * @param group
     * @return
     */
    private List<String> splitToList(Pattern pattern, CharSequence input, int group) {

        List<String> result = new ArrayList<>();

        if (StringUtils.isNotBlank(input)) {
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                String s = matcher.group(group);
                result.add(s);
            }
        }

        return result;
    }

    /**
     * 使用 metadataId 获取 MetadataInfo
     *
     * @param metadataId
     * @return
     */
    private MetadataInfo getMetadataInfo(Integer metadataId) {
        return this.metadataInfoDao.getById(metadataId);
    }

    /**
     * 使用 ontologyBaseinfoId 获取 OntologyBaseinfo
     *
     * @param ontologyid
     * @return
     */
    private OntologyBaseinfo getOntologyBaseinfo(Integer ontologyid) {
        return this.ontologyDao.getOntologyById(ontologyid);
    }


    /**
     * 递归查找血缘分析相关数据
     *
     * @param metadataId
     * @param container
     */
    private void putConsanguinityAnalysisResultBean(Integer metadataId
            , Map<Integer, AnalysisResultBean> container
            , Set<Integer> guard) {

        Optional<AnalysisResultBean> optional = this.assemblyConsanguinityAnalysisResultBean(metadataId, guard);

        if (optional.isPresent()) {
            AnalysisResultBean analysisResultBean = optional.get();
            container.put(metadataId, analysisResultBean);
            List<Integer> dependentIdList = analysisResultBean.getDependentIdList();

            for (Integer id : dependentIdList) {
                putConsanguinityAnalysisResultBean(id, container, guard);
            }
        }

    }

    /**
     * 使用数据库中的数据, 封装为用于血缘分析 DTO 对象.
     *
     * @param metadataId
     * @return
     */
    private Optional<AnalysisResultBean> assemblyConsanguinityAnalysisResultBean(Integer metadataId
            , Set<Integer> guard) {

        if (guard.contains(metadataId)) {
            return Optional.empty();
        }

        // 根据 metadataId 找到 MetadataInfo
        MetadataInfo metadataInfo = this.getMetadataInfo(metadataId);

        if (metadataInfo != null) {
            guard.add(metadataId);

            // 根据 metadataInfo 找到 OntologyBaseinfo
            OntologyBaseinfo ontologyBaseinfo = this.getOntologyBaseinfo(metadataInfo.getOntologyid());

            if (ontologyBaseinfo != null) {

                AnalysisResultBean analysisResultBean = new AnalysisResultBean();
                analysisResultBean.setMetadataId(metadataId);
                analysisResultBean.setMetadataInfo(metadataInfo);
                analysisResultBean.setOntologyBaseinfo(ontologyBaseinfo);

                List<Integer> dependentIdList = this.splitToList(NUMBER_PATTERN, metadataInfo.getDepmetadataid())
                        .stream()
                        .map(input -> Integer.valueOf(input, 10))
                        .collect(Collectors.toList());
                analysisResultBean.setDependentIdList(dependentIdList);

                return Optional.of(analysisResultBean);
            }
        }

        return Optional.empty();
    }


    private List<Integer> getAffectDependentIdList(Integer metadataId) {


        List<Integer> result = new ArrayList<>();

        List<MetadataInfo> affectList = this.metadataInfoDao.getAffectMetadataInfoList(metadataId);

        affectList.stream().forEach(metadataInfo -> {
            String dependentIds = metadataInfo.getDepmetadataid();
            Set<Integer> idSet = this.splitToList(NUMBER_PATTERN, dependentIds)
                    .stream()
                    .map(input -> Integer.valueOf(input, 10))
                    .collect(Collectors.toSet());
            if (idSet.contains(metadataId)) {
                result.add(metadataInfo.getMetadataid());
            }
        });

        return result;
    }

    /**
     * 递归查找影响分析相关数据
     *
     * @param metadataId
     * @return
     */

    private void putAffectAnalysisResultBean(Integer metadataId
            , Map<Integer, AnalysisResultBean> container
            , Set<Integer> guard) {

        Optional<AnalysisResultBean> optional = this.assemblyAffectAnalysisResultBean(metadataId, guard);

        if (optional.isPresent()) {
            AnalysisResultBean analysisResultBean = optional.get();
            container.put(metadataId, analysisResultBean);
            List<Integer> dependentIdList = this.getAffectDependentIdList(metadataId);

            for (Integer id : dependentIdList) {
                putAffectAnalysisResultBean(id, container, guard);
            }
        }
    }


    /**
     * 使用数据库中的数据, 封装为用于影响分析 DTO 对象.
     *
     * @param metadataId
     * @return
     */
    private Optional<AnalysisResultBean> assemblyAffectAnalysisResultBean(Integer metadataId
            , Set<Integer> guard) {


        if (guard.contains(metadataId)) {
            return Optional.empty();
        }
        // 根据 metadataId 找到 MetadataInfo
        MetadataInfo metadataInfo = this.getMetadataInfo(metadataId);

        if (metadataInfo != null) {

            guard.add(metadataId);

            // 根据 metadataInfo 找到 OntologyBaseinfo
            OntologyBaseinfo ontologyBaseinfo = this.getOntologyBaseinfo(metadataInfo.getOntologyid());

            if (ontologyBaseinfo != null) {

                AnalysisResultBean analysisResultBean = new AnalysisResultBean();
                analysisResultBean.setMetadataId(metadataId);
                analysisResultBean.setMetadataInfo(metadataInfo);
                analysisResultBean.setOntologyBaseinfo(ontologyBaseinfo);

                List<Integer> dependentIdList = this.getAffectDependentIdList(metadataId);
                analysisResultBean.setDependentIdList(dependentIdList);

                return Optional.of(analysisResultBean);
            }
        }

        return Optional.empty();
    }


    /**
     * 影响分析
     *
     * @param metadataId
     */
    @Override
    public Optional<Pair<AnalysisResultBean, Map<Integer, AnalysisResultBean>>>
    getAffectAnalysis(Integer metadataId) {


        Set<Integer> guard = new HashSet<>();
        Optional<AnalysisResultBean> optional = this.assemblyAffectAnalysisResultBean(metadataId, guard);

        if (optional.isPresent()) {
            AnalysisResultBean analysisResultBean = optional.get();
            List<Integer> dependentIdList = this.getAffectDependentIdList(metadataId);
            Map<Integer, AnalysisResultBean> container = new HashMap<>();

            for (Integer id : dependentIdList) {
                this.putAffectAnalysisResultBean(id, container, guard);
            }

            return Optional.of(Pair.of(analysisResultBean, container));
        }

        return Optional.empty();

    }

    /**
     * 血缘分析
     *
     * @param metadataId
     */
    @Override
    public Optional<Pair<AnalysisResultBean, Map<Integer, AnalysisResultBean>>>
    getConsanguinityAnalysis(Integer metadataId) {


        Set<Integer> guard = new HashSet<>();
        Optional<AnalysisResultBean> optional = this.assemblyConsanguinityAnalysisResultBean(metadataId, guard);


        if (optional.isPresent()) {
            AnalysisResultBean analysisResultBean = optional.get();
            List<Integer> dependentIdList = analysisResultBean.getDependentIdList();
            Map<Integer, AnalysisResultBean> container = new HashMap<>();

            for (Integer id : dependentIdList) {
                this.putConsanguinityAnalysisResultBean(id, container, guard);
            }

            return Optional.of(Pair.of(analysisResultBean, container));
        }

        return Optional.empty();
    }


    private AnalysisResultBean assemblyMpppingAnalysisResultBean(OntologyBaseinfo ontologyBaseinfo, MetadataInfo metadataInfo) {

        AnalysisResultBean analysisResultBean = new AnalysisResultBean();

        analysisResultBean.setMetadataId(metadataInfo.getMetadataid());
        analysisResultBean.setMetadataInfo(metadataInfo);

        analysisResultBean.setOntologyBaseinfo(ontologyBaseinfo);

        List<Integer> synmetadataIdList = this.splitToList(NUMBER_PATTERN, metadataInfo.getSynmetadataids())
                .stream()
                .map(input -> Integer.valueOf(input, 10))
                .collect(Collectors.toList());

        analysisResultBean.setSynmetadataIdList(synmetadataIdList);

        int fieldId = metadataInfo.getFieldid();

        if (fieldId != 0) {
            DataSourceField dataSourceField = this.fieldDao.getDataFieldById(metadataInfo.getFieldid());
            analysisResultBean.setDataSourceField(dataSourceField);

            if (dataSourceField != null && dataSourceField.getTableId() != 0) {
                DataSourceTable dataSourceTable = this.tableDao.getDataTableById(dataSourceField.getTableId());

                analysisResultBean.setDataSourceTable(dataSourceTable);

                if (dataSourceTable != null && dataSourceTable.getResourceId() != 0) {
                    DataSourceResource dataSourceResource = this.resourceDao.getDataSourceById(dataSourceTable.getResourceId());

                    analysisResultBean.setDataSourceResource(dataSourceResource);
                }
            }
        }

        return analysisResultBean;
    }

    /**
     * 映射分析 (使用元数据 ID)
     *
     * @param metadataId
     */
    @Override
    public AnalysisResultBean getMappingAnalysisByMetadataId(Integer metadataId) {

        MetadataInfo metadataInfo = this.getMetadataInfo(metadataId);

        if (metadataInfo != null) {
            OntologyBaseinfo ontologyBaseinfo = this.getOntologyBaseinfo(metadataInfo.getOntologyid());
            return this.assemblyMpppingAnalysisResultBean(ontologyBaseinfo, metadataInfo);
        }

        return null;

    }

    /**
     * 映射分析 (使用元本体 ID)
     *
     * @param ontologyId
     */
    @Override
    public List<AnalysisResultBean> getMappingAnalysisByOntologyId(Integer ontologyId) {

        List<AnalysisResultBean> result = new ArrayList<>();
        OntologyBaseinfo ontologyBaseinfo = this.getOntologyBaseinfo(ontologyId);

        List<MetadataInfo> metadataInfoList = this.metadataInfoDao.getMetadataInfosByOntologyId(ontologyId);

        metadataInfoList.forEach(info ->
                result.add(assemblyMpppingAnalysisResultBean(ontologyBaseinfo, info)));

        return result;
    }
}

