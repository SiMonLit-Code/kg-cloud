package com.plantdata.kgcloud.domain.edit.service.impl;

import ai.plantdata.kg.api.edit.BatchApi;
import ai.plantdata.kg.api.edit.ConceptEntityApi;
import ai.plantdata.kg.api.edit.GraphApi;
import ai.plantdata.kg.api.edit.req.BasicInfoFrom;
import ai.plantdata.kg.api.edit.req.MetaDataFrom;
import ai.plantdata.kg.api.edit.req.PromptFrom;
import ai.plantdata.kg.api.edit.req.SynonymFrom;
import ai.plantdata.kg.api.edit.req.UpdateBasicInfoFrom;
import ai.plantdata.kg.api.edit.resp.EntityVO;
import ai.plantdata.kg.api.edit.resp.PromptVO;
import ai.plantdata.kg.api.pub.CountApi;
import ai.plantdata.kg.api.pub.QlApi;
import ai.plantdata.kg.api.pub.StatisticsApi;
import ai.plantdata.kg.api.pub.req.ConceptStatisticsBean;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.constant.CountType;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.edit.req.basic.AbstractModifyReq;
import com.plantdata.kgcloud.domain.edit.req.basic.AdditionalReq;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoReq;
import com.plantdata.kgcloud.domain.edit.req.basic.ImageUrlReq;
import com.plantdata.kgcloud.domain.edit.req.basic.KgqlReq;
import com.plantdata.kgcloud.domain.edit.req.basic.PromptReq;
import com.plantdata.kgcloud.domain.edit.req.basic.StatisticsReq;
import com.plantdata.kgcloud.domain.edit.req.basic.SynonymReq;
import com.plantdata.kgcloud.domain.edit.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.domain.edit.rsp.GraphStatisRsp;
import com.plantdata.kgcloud.domain.edit.rsp.PromptRsp;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.domain.edit.util.ParserBeanUtils;
import com.plantdata.kgcloud.domain.edit.vo.StatisticVO;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: LinHo
 * @Date: 2019/11/20 11:45
 * @Description:
 */
@Service
public class BasicInfoServiceImpl implements BasicInfoService {

    @Autowired
    private ConceptEntityApi conceptEntityApi;

    @Autowired
    private GraphApi graphApi;

    @Autowired
    private CountApi countApi;

    @Autowired
    private StatisticsApi statisticsApi;

    @Autowired
    private BatchApi batchApi;

    @Autowired
    private QlApi qlApi;

    @Override
    public Long createBasicInfo(String kgName, BasicInfoReq basicInfoReq) {
        BasicInfoFrom basicInfoFrom = ConvertUtils.convert(BasicInfoFrom.class).apply(basicInfoReq);
        RestResp<Long> restResp = conceptEntityApi.add(kgName, basicInfoFrom);
        return RestRespConverter.convert(restResp).get();
    }

    @Override
    public void deleteBasicInfo(String kgName, Long id) {
        RestRespConverter.convertVoid(conceptEntityApi.delete(kgName, id));
    }

    @Override
    public void updateBasicInfo(String kgName, BasicInfoModifyReq basicInfoModifyReq) {
        UpdateBasicInfoFrom updateBasicInfoFrom =
                ConvertUtils.convert(UpdateBasicInfoFrom.class).apply(basicInfoModifyReq);
        RestRespConverter.convertVoid(conceptEntityApi.update(kgName, updateBasicInfoFrom));
    }

    @Override
    public BasicInfoRsp getDetails(String kgName, Long id) {
        RestResp<List<EntityVO>> restResp = conceptEntityApi.listByIds(kgName, Arrays.asList(id));
        Optional<List<EntityVO>> optional = RestRespConverter.convert(restResp);
        if (!optional.isPresent() || optional.get().isEmpty()) {
            throw BizException.of(KgmsErrorCodeEnum.BASIC_INFO_NOT_EXISTS);
        }
        return ParserBeanUtils.parserEntityVO(optional.get().get(0));
    }

    @Override
    public void updateAbstract(String kgName, AbstractModifyReq abstractModifyReq) {
        UpdateBasicInfoFrom updateBasicInfoFrom =
                ConvertUtils.convert(UpdateBasicInfoFrom.class).apply(abstractModifyReq);
        RestRespConverter.convertVoid(conceptEntityApi.update(kgName, updateBasicInfoFrom));
    }

    @Override
    public List<BasicInfoRsp> listByIds(String kgName, List<Long> ids) {
        RestResp<List<EntityVO>> restResp = conceptEntityApi.listByIds(kgName, ids);
        Optional<List<EntityVO>> optional = RestRespConverter.convert(restResp);
        return optional.orElse(new ArrayList<>()).stream().map(ParserBeanUtils::parserEntityVO).collect(Collectors.toList());
    }

    @Override
    public void addSynonym(String kgName, SynonymReq synonymReq) {
        SynonymFrom synonymFrom = ConvertUtils.convert(SynonymFrom.class).apply(synonymReq);
        RestRespConverter.convertVoid(conceptEntityApi.addSynonym(kgName, synonymFrom));
    }

    @Override
    public void deleteSynonym(String kgName, SynonymReq synonymReq) {
        SynonymFrom synonymFrom = ConvertUtils.convert(SynonymFrom.class).apply(synonymReq);
        RestRespConverter.convertVoid(conceptEntityApi.deleteSynonym(kgName, synonymFrom));
    }

    @Override
    public void saveImageUrl(String kgName, ImageUrlReq imageUrlReq) {
        UpdateBasicInfoFrom updateBasicInfoFrom = ConvertUtils.convert(UpdateBasicInfoFrom.class).apply(imageUrlReq);
        RestRespConverter.convertVoid(conceptEntityApi.update(kgName, updateBasicInfoFrom));
    }

    @Override
    public List<PromptRsp> prompt(String kgName, PromptReq promptReq) {
        PromptFrom promptFrom = ConvertUtils.convert(PromptFrom.class).apply(promptReq);
        Optional<List<PromptVO>> optional = RestRespConverter.convert(graphApi.prompt(kgName, promptFrom));
        return optional.orElse(new ArrayList<>()).stream().map(promptVO -> {
            PromptRsp promptRsp = new PromptRsp();
            BeanUtils.copyProperties(promptVO, promptRsp);
            promptRsp.setConceptId(promptVO.getClassId());
            return promptRsp;
        }).collect(Collectors.toList());
    }

    @Override
    public GraphStatisRsp graphStatis(String kgName) {
        long defaultValue = 0L;
        StatisticVO.StatisticVOBuilder builder = StatisticVO.builder();
        Optional<Long> concept = RestRespConverter.convert(countApi.countElement(kgName, CountType.CONCEPT.getCode()));
        Long conceptCount = concept.orElse(defaultValue);
        Optional<Long> entity = RestRespConverter.convert(countApi.countElement(kgName, CountType.ENTITY.getCode()));
        Long entityCount = entity.orElse(defaultValue);
        Optional<Long> number = RestRespConverter.convert(countApi.countElement(kgName,
                CountType.NUMERICAL_ATTR.getCode()));
        Long numberCount = entity.orElse(defaultValue);
        Optional<Long> privateNumber = RestRespConverter.convert(countApi.countElement(kgName,
                CountType.PRIVATE_NUMERICAL_ATTR.getCode()));
        Long privateNumberCount = entity.orElse(defaultValue);
        Optional<Long> object = RestRespConverter.convert(countApi.countElement(kgName,
                CountType.OBJECT_ATTR.getCode()));
        Long objectCount = entity.orElse(defaultValue);
        Optional<Long> privateObject = RestRespConverter.convert(countApi.countElement(kgName,
                CountType.PRIVATE_OBJECT_ATTR.getCode()));
        Long privateObjectCount = entity.orElse(defaultValue);
        int baseValue = 10000;
        if (conceptCount >= baseValue) {
            double n = (double) (conceptCount) / baseValue;
            String result = String.format("%.2f", n) + "万";
            builder.conceptTotal(result);
        } else {
            builder.conceptTotal(conceptCount);
        }

        if (entityCount >= baseValue) {
            double n = (double) (entityCount) / baseValue;
            String result = String.format("%.2f", n) + "万";
            builder.entityTotal(result);
        } else {
            builder.conceptTotal(entityCount);
        }

        if (numberCount >= baseValue) {
            double n = (double) (numberCount) / baseValue;
            String result = String.format("%.2f", n) + "万";
            builder.numericalAttrTotal(result);
        } else {
            builder.numericalAttrTotal(numberCount);
        }

        if (privateNumberCount >= baseValue) {
            double n = (double) (privateNumberCount) / baseValue;
            String result = String.format("%.2f", n) + "万";
            builder.privateNumericalAttrTotal(result);
        } else {
            builder.privateNumericalAttrTotal(privateNumberCount);
        }

        if (objectCount >= baseValue) {
            double n = (double) (objectCount) / baseValue;
            String result = String.format("%.2f", n) + "万";
            builder.objectAttrTotal(result);
        } else {
            builder.objectAttrTotal(objectCount);
        }

        if (privateObjectCount >= baseValue) {
            double n = (double) (privateObjectCount) / baseValue;
            String result = String.format("%.2f", n) + "万";
            builder.privateObjectAttrTotal(result);
        } else {
            builder.privateObjectAttrTotal(privateObjectCount);
        }
        StatisticVO statisticVO = builder.build();
        ConceptStatisticsBean statisticsBean =
                ConvertUtils.convert(ConceptStatisticsBean.class).apply(new StatisticsReq());
        Optional<List<Map<String, Object>>> optional =
                RestRespConverter.convert(statisticsApi.conceptStatistics(kgName, statisticsBean));
        return GraphStatisRsp.builder().statistics(statisticVO).conceptDetails(optional.orElse(new ArrayList<>())).build();
    }

    @Override
    public void batchAddMetaData(String kgName, AdditionalReq additionalReq) {
        MetaDataFrom metaDataFrom = ConvertUtils.convert(MetaDataFrom.class).apply(additionalReq);
        RestRespConverter.convertVoid(batchApi.addMetaData(kgName, metaDataFrom));
    }

    @Override
    public void clearMetaData(String kgName) {
        MetaDataFrom metaDataFrom = new MetaDataFrom();
        //默认清除meta 14
        metaDataFrom.setId(14);
        RestRespConverter.convertVoid(batchApi.deleteMetaData(kgName, metaDataFrom));
    }

    @Override
    public Object executeQl(KgqlReq kgqlReq) {
        return qlApi.executeQl(kgqlReq.getQuery());
    }
}
