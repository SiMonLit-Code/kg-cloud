package com.plantdata.kgcloud.domain.document.service;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.document.req.AttrReq;
import com.plantdata.kgcloud.domain.document.req.ConceptReq;
import com.plantdata.kgcloud.domain.document.rsp.ConceptRsp;

import java.util.List;

public interface ModelService {



    ApiReturn<List<ConceptRsp>> getModel(Integer sceneId, Integer id);

    ApiReturn saveModelConcept(ConceptReq conceptReq);

    ApiReturn saveModelAttr(AttrReq attrReq);

    ApiReturn importGroup(Integer sceneId, Integer id);
}
