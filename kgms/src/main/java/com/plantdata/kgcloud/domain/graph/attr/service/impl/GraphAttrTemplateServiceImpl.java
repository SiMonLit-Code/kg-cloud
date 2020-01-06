package com.plantdata.kgcloud.domain.graph.attr.service.impl;

import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrTemplateReq;
import com.plantdata.kgcloud.domain.edit.service.ConceptService;
import com.plantdata.kgcloud.domain.edit.util.MapperUtils;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import com.plantdata.kgcloud.domain.graph.attr.entity.GraphAttrTemplate;
import com.plantdata.kgcloud.domain.graph.attr.repository.GraphAttrTemplateRepository;
import com.plantdata.kgcloud.domain.graph.attr.rsp.GraphAttrTemplateRsp;
import com.plantdata.kgcloud.domain.graph.attr.service.GraphAttrTemplateService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: LinHo
 * @Date: 2019/12/4 17:40
 * @Description:
 */
@Service
public class GraphAttrTemplateServiceImpl implements GraphAttrTemplateService {

    @Autowired
    private GraphAttrTemplateRepository graphAttrTemplateRepository;

    @Autowired
    private ConceptService conceptService;

    @Override
    public List<GraphAttrTemplateRsp> listAttrTemplate() {
        List<GraphAttrTemplate> templates = graphAttrTemplateRepository.findAll();
        return templates.stream()
                .map(graphAttrTemplate -> MapperUtils.map(graphAttrTemplate, GraphAttrTemplateRsp.class))
                .collect(Collectors.toList());
//        return templates.stream().map(ConvertUtils.convert(GraphAttrTemplateRsp.class)).collect(Collectors.toList());
    }

    @Override
    public GraphAttrTemplateRsp getDetails(String kgName, Long id) {
        Optional<GraphAttrTemplate> optional = graphAttrTemplateRepository.findById(id);
        GraphAttrTemplate graphAttrTemplate =
                optional.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.ATTR_TEMPLATE_NOT_EXISTS));
        List<AttrTemplateReq> attrTemplateReqs = graphAttrTemplate.getConfig();
        if (CollectionUtils.isEmpty(attrTemplateReqs)) {
            return ConvertUtils.convert(GraphAttrTemplateRsp.class).apply(graphAttrTemplate);
        }
        List<BasicInfoVO> conceptTree = conceptService.getConceptTree(kgName, 0L);
        Map<String, Long> conceptMap = conceptTree.stream().collect(Collectors.toMap(BasicInfoVO::getName,
                BasicInfoVO::getId, (k1, k2) -> k1));
        attrTemplateReqs.stream()
                .filter(attrTemplateReq -> !CollectionUtils.isEmpty(attrTemplateReq.getRange()))
                .forEach(attrTemplateReq -> attrTemplateReq.getRange()
                        .forEach(idNameVO -> idNameVO.setId(conceptMap.get(idNameVO.getName())))
                );
        graphAttrTemplate.setConfig(attrTemplateReqs);
        return ConvertUtils.convert(GraphAttrTemplateRsp.class).apply(graphAttrTemplate);
    }
}
