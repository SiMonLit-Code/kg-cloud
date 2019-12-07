package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.edit.BatchApi;
import ai.plantdata.kg.api.edit.resp.BatchDeleteAttrValueVO;
import ai.plantdata.kg.api.edit.resp.BatchEntityVO;
import ai.plantdata.kg.api.edit.resp.BatchResult;
import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.req.SearchByAttributeFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.domain.app.converter.EntityConverter;
import com.plantdata.kgcloud.domain.app.service.GraphHelperService;
import com.plantdata.kgcloud.domain.app.service.KgDataService;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.sdk.req.app.BatchEntityAttrDeleteReq;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 11:00
 */
@Service
public class KgDataServiceImpl implements KgDataService {

    @Autowired
    private GraphHelperService graphHelperService;
    @Autowired
    private EntityApi entityApi;
    @Autowired
    private BatchApi batchApi;

    @Override
    public List<OpenEntityRsp> queryEntityList(String kgName, EntityQueryReq entityQueryReq) {
        if (entityQueryReq.getConceptId() == null && StringUtils.isNotEmpty(entityQueryReq.getConceptKey())) {
            List<Long> longList = graphHelperService.replaceByConceptKey(kgName, Lists.newArrayList(entityQueryReq.getConceptKey()));
            if (!CollectionUtils.isEmpty(longList)) {
                entityQueryReq.setConceptId(longList.get(0));
            }
        }
        SearchByAttributeFrom attributeFrom = EntityConverter.entityQueryReqToSearchByAttributeFrom(entityQueryReq);
        Optional<List<EntityVO>> entityOpt = RestRespConverter.convert(entityApi.searchByAttribute(kgName, attributeFrom));
        return entityOpt.orElse(new ArrayList<>()).stream().map(EntityConverter::voToOpenEntityRsp).collect(Collectors.toList());
    }

    @Override
    public List<OpenBatchSaveEntityRsp> saveOrUpdate(String kgName, boolean update, List<OpenBatchSaveEntityRsp> batchEntity) {
        List<BatchEntityVO> entityList = batchEntity.stream()
                .map(a -> ConvertUtils.convert(BatchEntityVO.class).apply(a))
                .collect(Collectors.toList());
        Optional<BatchResult<BatchEntityVO>> entityOpt = RestRespConverter.convert(batchApi.addEntities(kgName, update, entityList));
        return entityOpt.map(result -> result.getSuccess().stream()
                .map(a -> ConvertUtils.convert(OpenBatchSaveEntityRsp.class).apply(a))
                .collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    @Override
    public void batchDeleteEntityAttr(String kgName, BatchEntityAttrDeleteReq deleteReq) {
        BatchDeleteAttrValueVO deleteAttrValueVO = new BatchDeleteAttrValueVO();
        deleteAttrValueVO.setAttributeIds(deleteReq.getAttributeIds());
        deleteAttrValueVO.setAttrNames(deleteReq.getAttrNames());
        deleteAttrValueVO.setEntityIds(deleteReq.getEntityIds());
        RestRespConverter.convertVoid(batchApi.deleteEntities(kgName, deleteAttrValueVO));
    }
}
