package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.edit.GraphApi;
import ai.plantdata.kg.api.edit.resp.SchemaVO;
import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.resp.GraphVO;
import com.plantdata.kgcloud.domain.app.converter.graph.GraphRspConverter;
import com.plantdata.kgcloud.domain.app.service.GraphHelperService;
import com.plantdata.kgcloud.domain.app.util.DefaultUtils;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.graph.attr.entity.GraphAttrGroupDetails;
import com.plantdata.kgcloud.domain.graph.attr.repository.GraphAttrGroupDetailsRepository;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.GraphStatisticRsp;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.StatisticRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
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
    private EntityApi entityApi;
    @Autowired
    private GraphApi editGraphApi;
    @Autowired
    private GraphAttrGroupDetailsRepository graphAttrGroupDetailsRepository;

    @Override
    public CommonBasicGraphExploreRsp buildExploreRspWithConcept(String kgName, GraphVO graph) {
        //填充概念 此处是消耗时间的一个点 可优化 底层提供查询自己及父概念的接口
        Optional<SchemaVO> schemaOpt = RestRespConverter.convert(editGraphApi.schema(kgName));

        return schemaOpt.map(schemaVO -> GraphRspConverter.graphVoToCommonRsp(graph, schemaVO.getConcepts()))
                .orElse(CommonBasicGraphExploreRsp.EMPTY);
    }

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
    public <T extends BasicGraphExploreReq> T dealGraphReq(T exploreReq) {
        //todo 底层提供接口
        //replace attrKey

        //replace conceptKey

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

}
