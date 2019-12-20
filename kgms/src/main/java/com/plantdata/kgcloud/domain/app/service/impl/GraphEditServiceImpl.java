package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.edit.ConceptEntityApi;
import ai.plantdata.kg.api.edit.req.BasicInfoFrom;
import com.plantdata.kgcloud.domain.app.converter.ConceptConverter;
import com.plantdata.kgcloud.domain.app.service.GraphEditService;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.sdk.req.edit.ConceptAddReq;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/20 10:02
 */
@Service
public class GraphEditServiceImpl implements GraphEditService {

    @Autowired
    private ConceptEntityApi conceptEntityApi;

    @Override
    public Long createConcept(String kgName, ConceptAddReq conceptAddReq) {
        BasicInfoFrom basicInfoFrom = ConceptConverter.conceptAddReqToBasicInfoFrom(conceptAddReq);
        Optional<Long> opt = RestRespConverter.convert(conceptEntityApi.add(KGUtil.dbName(kgName), basicInfoFrom));
        return opt.orElse(NumberUtils.LONG_MINUS_ONE);
    }
}
