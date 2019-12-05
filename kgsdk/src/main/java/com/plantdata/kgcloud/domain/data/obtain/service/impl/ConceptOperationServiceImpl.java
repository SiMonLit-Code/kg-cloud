package com.plantdata.kgcloud.domain.data.obtain.service.impl;

import com.plantdata.kgcloud.domain.data.obtain.req.ConceptTreeReq;
import com.plantdata.kgcloud.domain.data.obtain.rsp.ConceptTreeRsp;
import com.plantdata.kgcloud.domain.data.obtain.service.ConceptOperationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/10/31 17:57
 */
@Service
public class ConceptOperationServiceImpl implements ConceptOperationService {
    @Override
    public List<ConceptTreeRsp> queryConceptTree(ConceptTreeReq conceptTreeReq) {
        return null;
    }
}
