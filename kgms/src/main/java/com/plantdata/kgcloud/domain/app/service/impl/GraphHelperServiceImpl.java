package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.edit.GraphApi;
import ai.plantdata.kg.api.edit.resp.SchemaVO;
import ai.plantdata.kg.api.pub.SchemaApi;
import ai.plantdata.kg.api.pub.resp.GraphVO;
import com.plantdata.kgcloud.domain.app.converter.graph.GraphRspConverter;
import com.plantdata.kgcloud.domain.app.service.GraphHelperService;
import com.plantdata.kgcloud.domain.app.util.DefaultUtils;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.graph.attr.entity.GraphAttrGroupDetails;
import com.plantdata.kgcloud.domain.graph.attr.repository.GraphAttrGroupDetailsRepository;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.GraphStatisticRsp;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.StatisticRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/2 16:33
 */
@Service
public class GraphHelperServiceImpl implements GraphHelperService {
    @Autowired
    private GraphApi editGraphApi;
    @Autowired
    private GraphAttrGroupDetailsRepository graphAttrGroupDetailsRepository;
    @Autowired
    private SchemaApi schemaApi;



    @Override
    public <T extends StatisticRsp> T buildExploreRspWithStatistic(String kgName, List<BasicStatisticReq> configList, GraphVO graphVO, T pathAnalysisRsp) {
        //统计
        List<GraphStatisticRsp> statisticRspList = DefaultUtils.getIfNoNull(configList, GraphRspConverter.buildStatisticResult(graphVO, configList));
        //组装结果
        Optional<SchemaVO> schemaOpt = RestRespConverter.convert(editGraphApi.schema(kgName));
        return schemaOpt.map(schemaVO -> GraphRspConverter.graphVoToStatisticRsp(graphVO, statisticRspList, schemaVO.getConcepts(), pathAnalysisRsp))
                .orElse(pathAnalysisRsp);
    }

    @Override
    public <T extends BasicGraphExploreReq> T dealGraphReq(String kgName, T exploreReq) {

        //replace attrKey
        if (CollectionUtils.isEmpty(exploreReq.getAllowAttrs()) && !CollectionUtils.isEmpty(exploreReq.getAllowAttrsKey())) {
            exploreReq.setAllowAttrs(replaceByAttrKey(kgName, exploreReq.getAllowAttrsKey()));
        }
        //replace conceptKey
        if (CollectionUtils.isEmpty(exploreReq.getAllowConceptsKey()) && !CollectionUtils.isEmpty(exploreReq.getAllowConceptsKey())) {
            exploreReq.setAllowConcepts(replaceByConceptKey(kgName, exploreReq.getAllowConceptsKey()));
        }
        if (CollectionUtils.isEmpty(exploreReq.getReplaceConceptIds()) && !CollectionUtils.isEmpty(exploreReq.getReplaceConceptKeys())) {
            exploreReq.setReplaceConceptIds(replaceByConceptKey(kgName, exploreReq.getReplaceConceptKeys()));
        }

        //replace attrGroupIds->attrIds
        if (!CollectionUtils.isEmpty(exploreReq.getAllowAttrGroups())) {
            List<GraphAttrGroupDetails> detailsList = graphAttrGroupDetailsRepository.findAllByGroupIdIn(exploreReq.getAllowAttrGroups());
            List<Integer> attrDefIds = detailsList.stream().map(GraphAttrGroupDetails::getAttrId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(exploreReq.getAllowAttrs())) {
                exploreReq.setAllowAttrs(attrDefIds);
            } else {
                exploreReq.getAllowAttrs().addAll(attrDefIds);
            }
        }
        return exploreReq;
    }
    @Override
    public List<Long> replaceByConceptKey(String kgName, List<String> keys) {
        Optional<Map<String, Long>> keyConvertOpt = RestRespConverter.convert(schemaApi.getConceptIdByKey(kgName, keys));
        if (!keyConvertOpt.isPresent()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(keyConvertOpt.orElse(Collections.emptyMap()).values());
    }
    @Override
    public List<Integer> replaceByAttrKey(String kgName, List<String> keys) {
        Optional<Map<String, Integer>> keyConvertOpt = RestRespConverter.convert(schemaApi.getAttrIdByKey(kgName, keys));
        if (!keyConvertOpt.isPresent()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(keyConvertOpt.orElse(Collections.emptyMap()).values());
    }

}
