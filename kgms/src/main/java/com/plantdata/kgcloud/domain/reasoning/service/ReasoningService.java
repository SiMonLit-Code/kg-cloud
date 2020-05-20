package com.plantdata.kgcloud.domain.reasoning.service;


import com.plantdata.kgcloud.domain.reasoning.req.*;
import com.plantdata.kgcloud.domain.reasoning.rsp.ReasoningRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public interface ReasoningService {
    Page<ReasoningRsp> list(String userId, ReasoningQueryReq reasoningQueryReq);

    ReasoningRsp add(String userId, ReasoningAddReq reasoningAddReq);

    ReasoningRsp update(String userId, ReasoningUpdateReq reasoningUpdateReq);

    void delete(String userId, Integer id);

    CommonBasicGraphExploreRsp verification(String userId, ReasoningVerifyReq reasoningVerifyReq);

    CommonBasicGraphExploreRsp execute(String userId, ReasoningExecuteReq reasoningExecuteReq);
}
