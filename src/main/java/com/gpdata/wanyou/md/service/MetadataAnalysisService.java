package com.gpdata.wanyou.md.service;

import com.gpdata.wanyou.md.dto.AnalysisResultBean;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by chengchao on 16-10-8.
 */
public interface MetadataAnalysisService {


    /**
     * 影响分析
     *
     * @param metadataId
     */
    public Optional<Pair<AnalysisResultBean, Map<Integer, AnalysisResultBean>>>
    getAffectAnalysis(Integer metadataId);

    /**
     * 血缘分析
     *
     * @param metadataId
     */
    public Optional<Pair<AnalysisResultBean, Map<Integer, AnalysisResultBean>>>
    getConsanguinityAnalysis(Integer metadataId);

    /**
     * 映射分析 (使用元数据 ID)
     *
     * @param metadataId
     */
    public AnalysisResultBean
    getMappingAnalysisByMetadataId(Integer metadataId);


    /**
     * 映射分析 (使用元本体 ID)
     *
     * @param ontologyId
     */
    public List<AnalysisResultBean> getMappingAnalysisByOntologyId(Integer ontologyId);

}
