package ai.plantdata.kgcloud.domain.edit.service.impl;

import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.web.util.ConvertUtils;
import ai.plantdata.kg.api.edit.ConceptEntityApi;
import ai.plantdata.kg.api.edit.req.UpdateBasicInfoFrom;
import ai.plantdata.kg.common.bean.BasicInfo;
import ai.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import ai.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import ai.plantdata.kgcloud.constant.MetaDataInfo;
import ai.plantdata.kgcloud.domain.common.util.KGUtil;
import ai.plantdata.kgcloud.domain.edit.req.basic.AdditionalModifyReq;
import ai.plantdata.kgcloud.domain.edit.req.basic.ConceptReplaceReq;
import ai.plantdata.kgcloud.domain.edit.req.basic.GisModifyReq;
import ai.plantdata.kgcloud.domain.edit.service.ConceptService;
import ai.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @Author: LinHo
 * @Date: 2019/11/16 18:36
 * @Description:
 */
@Service
public class ConceptServiceImpl implements ConceptService {

    @Autowired
    private ConceptEntityApi conceptEntityApi;

    @Override
    public List<BasicInfoVO> getConceptTree(String kgName, Long conceptId) {
        Optional<List<BasicInfo>> optional = RestRespConverter.convert(conceptEntityApi.tree(KGUtil.dbName(kgName), conceptId));
        return optional.orElse(new ArrayList<>())
                .stream()
                .map(ConvertUtils.convert(BasicInfoVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updateGis(String kgName, GisModifyReq gisModifyReq) {
        Map<String, Object> metadata = new HashMap<>(1);
        metadata.put(MetaDataInfo.OPEN_GIS.getFieldName(), gisModifyReq.getOpenGised());
        RestRespConverter.convertVoid(conceptEntityApi.updateMetaData(KGUtil.dbName(kgName), gisModifyReq.getId(),
                metadata));
    }

    @Override
    public void updateAdditional(String kgName, AdditionalModifyReq additionalModifyReq) {
        Map<String, Object> metadata = new HashMap<>(1);
        metadata.put(MetaDataInfo.ADDITIONAL.getFieldName(), additionalModifyReq.getAdditional());
        RestRespConverter.convertVoid(conceptEntityApi.updateMetaData(KGUtil.dbName(kgName), additionalModifyReq.getId(),
                metadata));
    }

    @Override
    public void replaceConceptId(String kgName, ConceptReplaceReq conceptReplaceReq) {
        if (conceptReplaceReq.getId().equals(conceptReplaceReq.getConceptId())){
            throw BizException.of(KgmsErrorCodeEnum.YOURSELF_NOT_AS_PARENT);
        }
        UpdateBasicInfoFrom updateBasicInfoFrom =
                ConvertUtils.convert(UpdateBasicInfoFrom.class).apply(conceptReplaceReq);
        RestRespConverter.convertVoid(conceptEntityApi.update(KGUtil.dbName(kgName), updateBasicInfoFrom));
    }
}
