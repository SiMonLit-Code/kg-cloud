package com.plantdata.kgcloud.domain.graph.quality.service.impl;

import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.graph.quality.entity.GraphAttrQuality;
import com.plantdata.kgcloud.domain.graph.quality.entity.GraphQuality;
import com.plantdata.kgcloud.domain.graph.quality.repository.GraphAttrQualityRepository;
import com.plantdata.kgcloud.domain.graph.quality.repository.GraphQualityRepository;
import com.plantdata.kgcloud.domain.graph.quality.rsp.GraphAttrQualityRsp;
import com.plantdata.kgcloud.domain.graph.quality.rsp.GraphQualityRsp;
import com.plantdata.kgcloud.domain.graph.quality.service.GraphQualityService;
import com.plantdata.kgcloud.domain.graph.quality.vo.AttrQualityVO;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: LinHo
 * @Date: 2020/3/21 15:22
 * @Description:
 */
@Service
public class GraphQualityServiceImpl implements GraphQualityService {

    @Autowired
    private GraphQualityRepository graphQualityRepository;

    @Autowired
    private GraphAttrQualityRepository graphAttrQualityRepository;

    @Override
    public List<GraphQualityRsp> listConceptQuality(String kgName) {
        GraphQuality graphQuality = new GraphQuality();
        graphQuality.setKgName(kgName);
        List<GraphQuality> graphQualities = graphQualityRepository.findAll(Example.of(graphQuality));
        return graphQualities.stream().map(ConvertUtils.convert(GraphQualityRsp.class)).collect(Collectors.toList());
    }


    @Override
    public List<GraphQualityRsp> sonConceptCount(String kgName, Long conceptId) {
        GraphQuality graphQuality = new GraphQuality();
        graphQuality.setKgName(kgName);
        graphQuality.setConceptId(conceptId);
        List<GraphQuality> graphQualities = graphQualityRepository.findAll(Example.of(graphQuality));
        graphQualities.add(check(kgName,conceptId));
        return graphQualities.stream().map(ConvertUtils.convert(GraphQualityRsp.class)).collect(Collectors.toList());
    }

    private GraphQuality check(String kgName, Long selfId) {
        GraphQuality graphQuality = new GraphQuality();
        graphQuality.setKgName(kgName);
        graphQuality.setSelfId(selfId);
        Optional<GraphQuality> optional = graphQualityRepository.findOne(Example.of(graphQuality));
//        return optional.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.GRAPH_QUALITY_NOT_EXIST));
        return optional.orElseGet(() -> graphQuality);
    }

    @Override
    public GraphAttrQualityRsp detailByConceptId(String kgName, Long selfId) {
        GraphQuality quality = this.check(kgName, selfId);
        GraphAttrQuality graphAttrQuality = new GraphAttrQuality();
        graphAttrQuality.setKgName(kgName);
        graphAttrQuality.setSelfId(selfId);
        List<GraphAttrQuality> graphAttrQualities = graphAttrQualityRepository.findAll(Example.of(graphAttrQuality));
        List<AttrQualityVO> vos =
                graphAttrQualities.stream().map(ConvertUtils.convert(AttrQualityVO.class)).collect(Collectors.toList());
        GraphAttrQualityRsp graphAttrQualityRsp = ConvertUtils.convert(GraphAttrQualityRsp.class).apply(quality);
        graphAttrQualityRsp.setConceptName(quality.getName());
        graphAttrQualityRsp.setAttrQualities(vos);
        return graphAttrQualityRsp;
    }
}
