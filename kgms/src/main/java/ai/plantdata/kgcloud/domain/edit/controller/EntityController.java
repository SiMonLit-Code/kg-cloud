package ai.plantdata.kgcloud.domain.edit.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.web.util.ConvertUtils;
import ai.plantdata.kgcloud.domain.edit.req.entity.*;
import com.plantdata.graph.logging.core.ServiceEnum;
import ai.plantdata.kgcloud.domain.edit.aop.EditLogOperation;
import ai.plantdata.kgcloud.domain.edit.req.basic.BasicInfoListBodyReq;
import ai.plantdata.kgcloud.domain.edit.req.basic.BasicInfoListReq;
import ai.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import ai.plantdata.kgcloud.domain.edit.service.EntityService;
import ai.plantdata.kgcloud.sdk.req.app.BatchEntityAttrDeleteReq;
import ai.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import ai.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import ai.plantdata.kgcloud.sdk.req.edit.BasicInfoModifyReq;
import ai.plantdata.kgcloud.sdk.req.edit.BatchPrivateRelationReq;
import ai.plantdata.kgcloud.sdk.req.edit.PrivateAttrDataReq;
import ai.plantdata.kgcloud.sdk.rsp.BasicInfoRsp;
import ai.plantdata.kgcloud.sdk.rsp.EntityLinkVO;
import ai.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import ai.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.DeleteResult;
import ai.plantdata.kgcloud.sdk.rsp.edit.EntityModifyReq;
import ai.plantdata.kgcloud.sdk.rsp.edit.MultiModalRsp;
import ai.plantdata.kgcloud.sdk.rsp.vo.EntityAttrValueVO;
import ai.plantdata.kgcloud.sdk.rsp.vo.EntityTagVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/20 10:11
 * @Description:
 */
@Api(tags = "实体编辑")
@RestController
@RequestMapping("/edit/entity")
public class EntityController {

    @Autowired
    private EntityService entityService;

    @Autowired
    private BasicInfoService basicInfoService;

    @ApiOperation("实体-概念-添加")
    @PostMapping("/{kgName}/{conceptId}/{entityId}/add")
    @EditLogOperation(serviceEnum = ServiceEnum.ENTITY_EDIT)
    public ApiReturn addMultipleConcept(@PathVariable("kgName") String kgName,
                                        @PathVariable("conceptId") Long conceptId,
                                        @PathVariable("entityId") Long entityId) {
        entityService.addMultipleConcept(kgName, conceptId, entityId);
        return ApiReturn.success();
    }

    @ApiOperation("实体-概念-删除")
    @PostMapping("/{kgName}/{conceptId}/{entityId}/delete")
    @EditLogOperation(serviceEnum = ServiceEnum.ENTITY_EDIT)
    public ApiReturn deleteMultipleConcept(@PathVariable("kgName") String kgName,
                                           @PathVariable("conceptId") Long conceptId,
                                           @PathVariable("entityId") Long entityId) {
        entityService.deleteMultipleConcept(kgName, conceptId, entityId);
        return ApiReturn.success();
    }

    @ApiOperation("实体-多模态数据-添加")
    @PostMapping("/{kgName}/multi/modal")
    public ApiReturn<MultiModalRsp> addMultiModal(@PathVariable("kgName") String kgName,
                                                  @RequestBody MultiModalReq multiModalReq) {
        return ApiReturn.success(entityService.addMultiModal(kgName, multiModalReq));
    }

    @ApiOperation("实体-多模态数据-批量添加")
    @PostMapping("/{kgName}/multi/modal/batch")
    public ApiReturn batchAddMultiModal(@PathVariable("kgName") String kgName,
                                        @RequestBody List<MultiModalReq> multiModalReqs) {
        entityService.batchAddMultiModal(kgName, multiModalReqs);
        return ApiReturn.success();
    }

    @ApiOperation("实体-多模态数据-删除")
    @PostMapping("/{kgName}/multi/modal/{relationId}/{entityId}/delete")
    public ApiReturn deleteMultiModal(@PathVariable("kgName") String kgName,
                                      @PathVariable("relationId") String relationId,
                                      @PathVariable("entityId") Long entityId) {
        entityService.deleteMultiModal(kgName, relationId, entityId);
        return ApiReturn.success();
    }

    @ApiOperation("实体-修改-实体名称,消歧")
    @PostMapping("/{kgName}/update")
    @EditLogOperation(serviceEnum = ServiceEnum.ENTITY_EDIT)
    public ApiReturn updateEntity(@PathVariable("kgName") String kgName,
                                  @Valid @RequestBody EntityModifyReq entityModifyReq) {
        basicInfoService.updateBasicInfo(kgName, ConvertUtils.convert(BasicInfoModifyReq.class).apply(entityModifyReq));
        return ApiReturn.success();
    }

    @ApiOperation("实体-查询-根据实体Ids批量查询")
    @PostMapping("/{kgName}/entities")
    public ApiReturn<List<BasicInfoRsp>> batchEntityDetails(@PathVariable("kgName") String kgName,
                                                            @RequestBody List<Long> ids) {
        return ApiReturn.success(basicInfoService.listByIds(kgName, ids));
    }

    @ApiOperation("实体-删除-根据实体ids批量删除")
    @PostMapping("/{kgName}/batch/delete")
    public ApiReturn<List<DeleteResult>> batchDeleteEntities(@PathVariable("kgName") String kgName,
                                                             @RequestParam(value = "isTrace", defaultValue = "false") Boolean isTrace,
                                                             @RequestBody List<Long> ids) {
        return ApiReturn.success(entityService.deleteByIds(kgName, isTrace, ids));
    }

    @ApiOperation("实体-删除-根据概念id删除实体")
    @PostMapping("/{kgName}/concept/delete")
    public ApiReturn<Long> deleteByConceptId(@PathVariable("kgName") String kgName,
                                             @Valid @RequestBody EntityDeleteReq entityDeleteReq) {
        return ApiReturn.success(entityService.deleteByConceptId(kgName, entityDeleteReq));
    }

    @ApiOperation("实体-删除-根据来源 ,批次号删除实体")
    @PostMapping("/{kgName}/delete/meta")
    @EditLogOperation(serviceEnum = ServiceEnum.ENTITY_TRACE)
    public ApiReturn deleteByMeta(@PathVariable("kgName") String kgName,
                                  @Valid @RequestBody EntityMetaDeleteReq entityMetaDeleteReq) {
        entityService.deleteByMeta(kgName, entityMetaDeleteReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体-查询-实体列表")
    @PostMapping("/{kgName}/list")
    public ApiReturn<Page<BasicInfoRsp>> listEntities(@PathVariable("kgName") String kgName,
                                                      BasicInfoListReq basicInfoListReq,
                                                      @RequestBody BasicInfoListBodyReq bodyReq) {
        return ApiReturn.success(entityService.listEntities(kgName, basicInfoListReq, bodyReq));
    }

    @ApiOperation("实体-查询-关系列表")
    @PostMapping("/{kgName}/list/relation")
    public ApiReturn<List<EntityAttrValueVO>> listRelation(@PathVariable("kgName") String kgName,
                                                           @RequestBody EntityAttrReq entityAttrReq) {
        return ApiReturn.success(entityService.listRelations(kgName, entityAttrReq));
    }

    @ApiOperation("实体-修改-更新权重,来源,可信度")
    @PostMapping("/{kgName}/{entityId}/ssr")
    @EditLogOperation(serviceEnum = ServiceEnum.ENTITY_EDIT)
    public ApiReturn updateScoreSourceReliability(@PathVariable("kgName") String kgName,
                                                  @PathVariable("entityId") Long entityId,
                                                  @Valid @RequestBody SsrModifyReq ssrModifyReq) {

        entityService.updateScoreSourceReliability(kgName, entityId, ssrModifyReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体-修改-更新权重")
    @PostMapping("/{kgName}/{entityId}/score")
    @EditLogOperation(serviceEnum = ServiceEnum.ENTITY_EDIT)
    public ApiReturn updateScore(@PathVariable("kgName") String kgName,
                                 @PathVariable("entityId") Long entityId,
                                 @Valid @RequestBody ScoreModifyReq scoreModifyReq) {
        entityService.updateScore(kgName, entityId, scoreModifyReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体-修改-更新来源")
    @PostMapping("/{kgName}/{entityId}/source")
    @EditLogOperation(serviceEnum = ServiceEnum.ENTITY_EDIT)
    public ApiReturn updateSource(@PathVariable("kgName") String kgName,
                                  @PathVariable("entityId") Long entityId,
                                  @Valid @RequestBody SourceModifyReq sourceModifyReq) {
        entityService.updateSource(kgName, entityId, sourceModifyReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体-修改-更新可信度")
    @PostMapping("/{kgName}/{entityId}/reliability")
    @EditLogOperation(serviceEnum = ServiceEnum.ENTITY_EDIT)
    public ApiReturn updateReliability(@PathVariable("kgName") String kgName,
                                       @PathVariable("entityId") Long entityId,
                                       @Valid @RequestBody ReliabilityModifyReq reliabilityModifyReq) {
        entityService.updateReliability(kgName, entityId, reliabilityModifyReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体-修改-更新实体开始截止时间")
    @PostMapping("/{kgName}/{entityId}/time")
    @EditLogOperation(serviceEnum = ServiceEnum.ENTITY_EDIT)
    public ApiReturn updateEntityTime(@PathVariable("kgName") String kgName,
                                      @PathVariable("entityId") Long entityId,
                                      @Valid @RequestBody EntityTimeModifyReq entityTimeModifyReq) {
        entityService.updateEntityTime(kgName, entityId, entityTimeModifyReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体-修改-更新实体gis详情")
    @PostMapping("/{kgName}/{entityId}/gis")
    @EditLogOperation(serviceEnum = ServiceEnum.ENTITY_EDIT)
    public ApiReturn updateGisInfo(@PathVariable("kgName") String kgName,
                                   @PathVariable("entityId") Long entityId,
                                   @Valid @RequestBody GisInfoModifyReq gisInfoModifyReq) {
        entityService.updateGisInfo(kgName, entityId, gisInfoModifyReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体-标签-添加")
    @PostMapping("/{kgName}/{entityId}/tag/add")
    @EditLogOperation(serviceEnum = ServiceEnum.ENTITY_EDIT)
    public ApiReturn addEntityTag(@PathVariable("kgName") String kgName,
                                  @PathVariable("entityId") Long entityId,
                                  @Valid @RequestBody List<EntityTagVO> vos) {
        entityService.addEntityTag(kgName, entityId, vos);
        return ApiReturn.success();
    }

    @ApiOperation("实体-标签-修改")
    @PostMapping("/{kgName}/{entityId}/tag/update")
    @EditLogOperation(serviceEnum = ServiceEnum.ENTITY_EDIT)
    public ApiReturn updateEntityTag(@PathVariable("kgName") String kgName,
                                     @PathVariable("entityId") Long entityId,
                                     @Valid @RequestBody List<EntityTagVO> vos) {
        entityService.updateEntityTag(kgName, entityId, vos);
        return ApiReturn.success();
    }

    @ApiOperation("实体-标签-删除")
    @PostMapping("/{kgName}/{entityId}/tag/delete")
    @EditLogOperation(serviceEnum = ServiceEnum.ENTITY_EDIT)
    public ApiReturn deleteEntityTag(@PathVariable("kgName") String kgName,
                                     @PathVariable("entityId") Long entityId,
                                     @Valid @RequestBody List<String> tagNames) {
        entityService.deleteEntityTag(kgName, entityId, tagNames);
        return ApiReturn.success();
    }

    @ApiOperation("实体-实体关联-添加")
    @PostMapping("/{kgName}/{entityId}/link/add")
    @EditLogOperation(serviceEnum = ServiceEnum.ENTITY_EDIT)
    public ApiReturn addEntityLink(@PathVariable("kgName") String kgName,
                                   @PathVariable("entityId") Long entityId,
                                   @Valid @RequestBody List<EntityLinkVO> vos) {
        entityService.addEntityLink(kgName, entityId, vos);
        return ApiReturn.success();
    }

    @ApiOperation("实体-实体关联-删除")
    @PostMapping("/{kgName}/{entityId}/link/delete")
    @EditLogOperation(serviceEnum = ServiceEnum.ENTITY_EDIT)
    public ApiReturn deleteEntityLink(@PathVariable("kgName") String kgName,
                                      @PathVariable("entityId") Long entityId,
                                      @Valid @RequestBody List<EntityLinkVO> vos) {
        entityService.deleteEntityLink(kgName, entityId, vos);
        return ApiReturn.success();
    }

    @ApiOperation("实体-数值属性-更新")
    @PostMapping("/{kgName}/number/update")
    @EditLogOperation(serviceEnum = ServiceEnum.ENTITY_EDIT)
    public ApiReturn upsertNumericalAttrValue(@PathVariable("kgName") String kgName,
                                              @Valid @RequestBody NumericalAttrValueReq numericalAttrValueReq) {
        entityService.upsertNumericalAttrValue(kgName, numericalAttrValueReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体-关系-添加")
    @PostMapping("/{kgName}/relation")
    @EditLogOperation(serviceEnum = ServiceEnum.RELATION_EDIT)
    public ApiReturn addObjectAttrValue(@PathVariable("kgName") String kgName,
                                        @Valid @RequestBody ObjectAttrValueReq objectAttrValueReq) {
        return ApiReturn.success(entityService.addObjectAttrValue(kgName, objectAttrValueReq));
    }

    @ApiOperation("实体-关系-修改关系的metadata和时间")
    @PostMapping("/{kgName}/relation/update/meta")
    @EditLogOperation(serviceEnum = ServiceEnum.RELATION_EDIT)
    public ApiReturn updateRelationMeta(@PathVariable("kgName") String kgName,
                                        @Valid @RequestBody UpdateRelationMetaReq updateRelationMetaReq) {
        entityService.updateRelationMeta(kgName, updateRelationMetaReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体-关系-删除")
    @PostMapping("/{kgName}/relation/delete")
    @EditLogOperation(serviceEnum = ServiceEnum.RELATION_EDIT)
    public ApiReturn deleteObjAttrValue(@PathVariable("kgName") String kgName,
                                        @Valid @RequestBody DeleteRelationReq deleteRelationReq) {
        entityService.deleteObjAttrValue(kgName, deleteRelationReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体-私有属性-添加或更新私有数值或对象属性值")
    @PostMapping("/{kgName}/private/data")
    public ApiReturn addPrivateData(@PathVariable("kgName") String kgName,
                                    @Valid @RequestBody PrivateAttrDataReq privateAttrDataReq) {
        return ApiReturn.success(entityService.addPrivateData(kgName, privateAttrDataReq));
    }

    @ApiOperation("实体-私有属性值数值或对象-批量删除")
    @PostMapping("/{kgName}/batch/private/data/delete")
    public ApiReturn deletePrivateData(@PathVariable("kgName") String kgName,
                                       @Valid @RequestBody DeletePrivateDataReq deletePrivateDataReq) {
        entityService.deletePrivateData(kgName, deletePrivateDataReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体-边属性-添加或更新边数值属性值")
    @PostMapping("/{kgName}/edge/number")
    @EditLogOperation(serviceEnum = ServiceEnum.RELATION_EDIT)
    public ApiReturn addEdgeNumericAttrValue(@PathVariable("kgName") String kgName,
                                             @Valid @RequestBody EdgeNumericAttrValueReq edgeNumericAttrValueReq) {
        entityService.addEdgeNumericAttrValue(kgName, edgeNumericAttrValueReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体-边关系-添加边对象属性值")
    @PostMapping("/{kgName}/edge/object")
    @EditLogOperation(serviceEnum = ServiceEnum.RELATION_EDIT)
    public ApiReturn addEdgeObjectAttrValue(@PathVariable("kgName") String kgName,
                                            @Valid @RequestBody EdgeObjectAttrValueReq edgeObjectAttrValueReq) {
        entityService.addEdgeObjectAttrValue(kgName, edgeObjectAttrValueReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体-边关系-删除边对象属性值")
    @PostMapping("/{kgName}/edge/object/delete")
    @EditLogOperation(serviceEnum = ServiceEnum.RELATION_EDIT)
    public ApiReturn deleteEdgeObjectAttrValue(@PathVariable("kgName") String kgName,
                                               @Valid @RequestBody DeleteEdgeObjectReq deleteEdgeObjectReq) {
        entityService.deleteEdgeObjectAttrValue(kgName, deleteEdgeObjectReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体-关系-批量添加对象属性值")
    @PostMapping("/{kgName}/batch/object/add")
    @EditLogOperation(serviceEnum = ServiceEnum.RELATION_EDIT)
    public ApiReturn<List<String>> batchAddRelation(@PathVariable("kgName") String kgName,
                                                    @Valid @RequestBody BatchRelationReq batchRelationReq) {
        return ApiReturn.success(entityService.batchAddRelation(kgName, batchRelationReq));
    }

    @ApiOperation("实体-私有关系-批量添加私有对象属性值")
    @PostMapping("/{kgName}/batch/object/add/private")
    @EditLogOperation(serviceEnum = ServiceEnum.RELATION_EDIT)
    public ApiReturn<List<String>> batchAddPrivateRelation(@PathVariable("kgName") String kgName,
                                                           @Valid @RequestBody BatchPrivateRelationReq batchPrivateRelationReq) {
        return ApiReturn.success(entityService.batchAddPrivateRelation(kgName, batchPrivateRelationReq));
    }

    @ApiOperation("实体-标签-实体标签搜索")
    @GetMapping("/{kgName}/entity/tag/prompt")
    public ApiReturn<List<String>> tagSearch(@PathVariable("kgName") String kgName,
                                             EntityTagSearchReq entityTagSearchReq) {
        return ApiReturn.success(entityService.tagSearch(kgName, entityTagSearchReq));
    }


    @ApiOperation("实体-查询-实体查询")
    @PostMapping("/{kgName}/list/search")
    public ApiReturn<List<OpenEntityRsp>> queryEntityList(@PathVariable("kgName") String kgName,
                                                          @RequestBody EntityQueryReq entityQueryReq) {
        return ApiReturn.success(entityService.queryEntityList(kgName, entityQueryReq));
    }


    @ApiOperation("实体-实体-批量新增或更新实体")
    @PostMapping("/{kgName}")
    @EditLogOperation(serviceEnum = ServiceEnum.SDK)
    public ApiReturn<OpenBatchResult<OpenBatchSaveEntityRsp>> saveOrUpdate(@PathVariable("kgName") String kgName,
                                                                           @ApiParam(
                                                                                   "是否只是更新，默认不是") boolean add,
                                                                           @RequestBody List<OpenBatchSaveEntityRsp> batchEntity) {
        return ApiReturn.success(entityService.saveOrUpdate(kgName, add, batchEntity));
    }

    @ApiOperation("实体-属性-批量删除")
    @DeleteMapping("/attr/{kgName}")
    @EditLogOperation(serviceEnum = ServiceEnum.SDK)
    public ApiReturn batchDeleteEntityAttr(
            @PathVariable("kgName") String kgName,
            @RequestBody BatchEntityAttrDeleteReq deleteReq) {
        entityService.batchDeleteEntityAttr(kgName, deleteReq);
        return ApiReturn.success();
    }


}
