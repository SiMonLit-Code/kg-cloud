package ai.plantdata.kgcloud.sdk.exection.client;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.sdk.req.edit.*;
import ai.plantdata.kgcloud.sdk.EditClient;
import ai.plantdata.kgcloud.sdk.req.EdgeSearchReqList;
import ai.plantdata.kgcloud.sdk.req.app.BatchEntityAttrDeleteReq;
import ai.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import ai.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.*;
import ai.plantdata.kgcloud.sdk.rsp.BasicInfoRsp;
import ai.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import ai.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.data.RelationUpdateReq;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-19 19:17
 **/
@Component
public class EditClientEx implements EditClient {
    @Override
    public ApiReturn<Long> createConcept(String kgName, BasicInfoReq basicInfoReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<BasicInfoRsp>> batchEntityDetails(String kgName, List<Long> ids) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn deleteConcept(String kgName, Long id, Boolean force) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn deleteAttrDefinition(String kgName, Integer id) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<OpenBatchResult<AttrDefinitionBatchRsp>> batchAddAttrDefinition(String kgName, List<AttrDefinitionReq> attrDefinitionReqs) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<AttrDefinitionRsp>> getAttrDefinitionByConceptId(String kgName, @RequestParam Long conceptId) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<AttrDefinitionRsp>> getAttrDefinitionByConceptIds(String kgName, AttrDefinitionConceptsReq attrDefinitionConceptsReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<OpenBatchResult<BatchRelationRsp>> importRelation(String kgName, List<BatchRelationRsp> relationList) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn updateConcept(String kgName, BasicInfoModifyReq basicInfoModifyReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn updateAttrDefinition(String kgName, AttrDefinitionModifyReq modifyReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<BasicInfoVO>> getConceptTree(String kgName, Long conceptId) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<OpenBatchResult<AttrDefinitionBatchRsp>> batchModifyAttrDefinition(String kgName, List<AttrDefinitionModifyReq> attrDefinitionReqs) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<DeleteResult>> batchDeleteEntities(String kgName, List<Long> ids) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn deleteRelations(String kgName, List<String> tripleIds) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<RelationUpdateReq>> updateRelations(String kgName, List<RelationUpdateReq> list) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<OpenEntityRsp>> queryEntityList(String kgName, EntityQueryReq queryReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<OpenBatchResult<OpenBatchSaveEntityRsp>> saveOrUpdate(String kgName, boolean add, List<OpenBatchSaveEntityRsp> batchEntity) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<EdgeSearchRsp>> batchSearchRelation(String kgName, EdgeSearchReqList queryReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn addPrivateData(String kgName, PrivateAttrDataReq privateAttrDataReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<String>> batchAddPrivateRelation(String kgName, BatchPrivateRelationReq batchPrivateRelationReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn executeQl(@Valid KgqlReq kgqlReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn batchDeleteEntityAttr(String kgName, BatchEntityAttrDeleteReq deleteReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<SimpleBasicRsp>> listNames(String kgName, List<String> names) {
        return ApiReturn.fail(500,"请求超时");
    }
}
