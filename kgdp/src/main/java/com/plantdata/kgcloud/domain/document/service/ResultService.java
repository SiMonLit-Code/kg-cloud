package com.plantdata.kgcloud.domain.document.service;

import com.hiekn.pddocument.bean.PdDocument;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.document.rsp.DataCheckRsp;

import java.util.List;

public interface ResultService {
    ApiReturn<List<DataCheckRsp>> check(Integer sceneId, Integer id);

    void importGroup(Integer sceneId, Integer id, Integer model);

    ApiReturn<List<PdDocument>> getPddocument(Integer sceneId, Integer id);

    ApiReturn updatePddocument(Integer sceneId, Integer id, List<PdDocument> pdDocumentList);
}
