package ai.plantdata.kgcloud.domain.prebuilder.service;

import ai.plantdata.cloud.bean.BasePage;
import ai.plantdata.kgcloud.domain.prebuilder.req.*;
import com.alibaba.fastjson.JSONObject;
import ai.plantdata.kgcloud.domain.prebuilder.rsp.PreBuilderMatchAttrRsp;
import ai.plantdata.kgcloud.domain.prebuilder.rsp.PreBuilderSearchRsp;
import ai.plantdata.kgcloud.sdk.req.StandardSearchReq;
import ai.plantdata.kgcloud.sdk.rsp.StandardTemplateRsp;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PreBuilderService {
    Page<PreBuilderSearchRsp> findModel(String userId, PreBuilderSearchReq preBuilderSearchReq);

    Page<PreBuilderMatchAttrRsp> matchAttr(String userId, PreBuilderMatchAttrReq preBuilderMatchAttrReq);

    PreBuilderSearchRsp databaseDetail(String userId, Long databaseId);

    Page<PreBuilderSearchRsp> listManage(String userId, PreBuilderSearchReq preBuilderSearchReq);

    void delete(String userId, Integer id);

    void update(String userId, Integer id, String status);

    List<String> getTypes(String userId,Boolean isManage);

    String create(PreBuilderCreateReq req);

    void pushGraphModel(String userId, ModelPushReq req);

    void updateModel(PreBuilderUpdateReq req);

    JSONObject saveGraphMap(String userId, PreBuilderGraphMapReq preBuilderGraphMapReq);

    PreBuilderCountReq matchAttrCount(String userId, PreBuilderMatchAttrReq preBuilderMatchAttrReq);

    BasePage<StandardTemplateRsp> standardList(String userId, StandardSearchReq req);

    List<StandardTemplateRsp> findIds(List<Integer> ids);
}
