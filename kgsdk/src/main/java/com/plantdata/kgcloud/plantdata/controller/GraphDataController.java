package com.plantdata.kgcloud.plantdata.controller;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.plantdata.bean.AttributeConstraintDefinition;
import com.plantdata.kgcloud.plantdata.bean.AttributeDefinition;
import com.plantdata.kgcloud.plantdata.bean.ImportRelationBean;
import com.plantdata.kgcloud.plantdata.converter.common.*;
import com.plantdata.kgcloud.plantdata.req.data.AttributeParameter;
import com.plantdata.kgcloud.plantdata.req.data.ConceptParameter;
import com.plantdata.kgcloud.plantdata.req.data.DelectEntityParameter;
import com.plantdata.kgcloud.plantdata.req.data.DelectRelationParameter;
import com.plantdata.kgcloud.plantdata.req.data.EntityAttrDelectParameter;
import com.plantdata.kgcloud.plantdata.req.data.EntityByDataAttributeParameter;
import com.plantdata.kgcloud.plantdata.req.data.EntityInsertParameter;
import com.plantdata.kgcloud.plantdata.req.data.ImportAttributeParameter;
import com.plantdata.kgcloud.plantdata.req.data.ImportEntityParameter;
import com.plantdata.kgcloud.plantdata.req.data.ImportRelationParameter;
import com.plantdata.kgcloud.plantdata.req.data.InsertConceptParameter;
import com.plantdata.kgcloud.plantdata.req.data.QueryRelationParameter;
import com.plantdata.kgcloud.plantdata.req.data.UpdataConceptParameter;
import com.plantdata.kgcloud.plantdata.req.data.UpdataRelationParameter;
import com.plantdata.kgcloud.plantdata.req.entity.ImportEntityBean;
import com.plantdata.kgcloud.plantdata.rsp.data.RelationBeanScore;
import com.plantdata.kgcloud.plantdata.rsp.data.TreeBean;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.EditClient;
import com.plantdata.kgcloud.sdk.KgDataClient;
import com.plantdata.kgcloud.sdk.MergeClient;
import com.plantdata.kgcloud.sdk.req.EdgeSearchReqList;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryWithConditionReq;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionBatchRsp;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.ConceptAddReq;
import com.plantdata.kgcloud.sdk.req.edit.KgqlReq;
import com.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.data.RelationUpdateReq;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import com.plantdata.kgcloud.sdk.rsp.edit.BatchRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.DeleteResult;
import com.plantdata.kgcloud.sdk.rsp.edit.EdgeSearchRsp;
import com.plantdata.kgcloud.util.JsonUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@RestController("graphDataController-v2")
@RequestMapping("sdk/graph")
public class GraphDataController implements SdkOldApiInterface {

    @Autowired
    private AppClient appClient;
    @Autowired
    private KgDataClient kgDataClient;
    @Autowired
    private EditClient editClient;
    @Autowired
    private MergeClient mergeClient;

    @ApiOperation("获取概念树")
    @GetMapping("data/concept")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "conceptId", dataType = "long", paramType = "query", value = "概念id"),
            @ApiImplicitParam(name = "conceptIdKey", dataType = "string", paramType = "query", value = "概念唯一标识key,")
    })
    public RestResp<List<TreeBean>> concept(@Valid @ApiIgnore ConceptParameter param) {
        ApiReturn<List<BasicInfoVO>> apiReturn = appClient.conceptTree(param.getKgName(), param.getConceptId(), param.getConceptIdKey());
        List<TreeBean> tree = BasicConverter.convert(apiReturn, a -> BasicConverter.toListNoNull(a, ConceptConverter::basicInfoVoToTreeBean));
        return new RestResp<>(tree);
    }

    @ApiOperation("添加概念")
    @PostMapping("data/concept/insert")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "parentId", required = true, dataType = "long", paramType = "form", value = "所属父概念id"),
            @ApiImplicitParam(name = "name", required = true, dataType = "string", paramType = "form", value = "概念名称"),
            @ApiImplicitParam(name = "key", dataType = "string", paramType = "form", value = "概念唯一标识key"),
            @ApiImplicitParam(name = "meaningTag", dataType = "string", paramType = "form", value = "唯一标识符")
    })
    public RestResp<Long> insertConcept(@Valid @ApiIgnore InsertConceptParameter param) {
        Function<ConceptAddReq, ApiReturn<Long>> returnFunction = a -> kgDataClient.createConcept(param.getKgName(), a);
        Optional<Long> idOpt = returnFunction
                .compose(ConceptConverter::insertConceptParameterToConceptAddReq)
                .andThen(BasicConverter::apiReturnData)
                .apply(param);

        return new RestResp<>(idOpt.orElse(NumberUtils.LONG_MINUS_ONE));
    }

    @ApiOperation("修改概念")
    @PostMapping("data/concept/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "conceptId", required = true, dataType = "long", paramType = "form", value = "概念id"),
            @ApiImplicitParam(name = "name", dataType = "string", paramType = "form", value = "概念名称"),
            @ApiImplicitParam(name = "key", dataType = "string", paramType = "form", value = "概念唯一标识key"),
            @ApiImplicitParam(name = "meaningTag", required = false, dataType = "string", paramType = "form", value = "唯一标识符")
    })
    public RestResp<Long> updataConcept(@Valid @ApiIgnore UpdataConceptParameter param) {
        BasicInfoModifyReq modifyReq = ConceptConverter.updataConceptParameterToBasicInfoModifyReq(param);
        editClient.updateConcept(param.getKgName(), modifyReq);
        return new RestResp<>(param.getConceptId());
    }


    @ApiOperation("删除概念")
    @PostMapping("data/concept/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "conceptId", required = true, dataType = "long", paramType = "query", value = "概念id")
    })
    public RestResp deleteConcept(@Valid @ApiIgnore @RequestParam("kgName") String kgName, @RequestParam("conceptId") Long conceptId) {
        ApiReturnConverter.convert(editClient.deleteConcept(kgName, conceptId));
        return new RestResp();
    }

    @ApiOperation("批量属性定义新增")
    @PostMapping("data/attribute/insert")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "data", required = true, dataType = "string", paramType = "form", value = "数据，是AttributeDefinition 列表的JSON形式")
    })
    public RestResp<Object> importAttribute(@Valid @ApiIgnore ImportAttributeParameter param) {
        Function<List<AttrDefinitionReq>, ApiReturn<OpenBatchResult<AttrDefinitionBatchRsp>>> returnFunction =
                a -> editClient.batchAddAttrDefinition(param.getKgName(), a);
        Function<List<AttributeConstraintDefinition>, List<AttrDefinitionReq>> reqFunction =
                a -> BasicConverter.toListNoNull(a, AttrDefConverter::attributeConstraintDefinitionToAttrDefinitionReq);
        Optional<OpenBatchResult<AttrDefinitionBatchRsp>> batchRsps = returnFunction
                .compose(reqFunction)
                .andThen(BasicConverter::apiReturnData)
                .apply(param.getData());
        if (!batchRsps.isPresent() || CollectionUtils.isEmpty(batchRsps.get().getSuccess())) {
            return new RestResp<>();
        }
        List<Integer> idList = batchRsps.get().getSuccess().stream().map(AttrDefinitionBatchRsp::getId).collect(Collectors.toList());
        return new RestResp<>(idList);
    }

    @ApiOperation("属性修改")
    @PostMapping("data/attribute/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "data", required = true, dataType = "string", paramType = "form", value = "数据，是AttributeDefinition 列表的JSON形式,修改需要设置id")
    })
    public RestResp<Object> updateAttribute(@Valid @ApiIgnore ImportAttributeParameter param) {
        List<AttrDefinitionModifyReq> modifyReqs = BasicConverter.toListNoNull(param.getData(), AttrDefConverter::importAttributeParameterToAttrDefinitionModifyReq);
        Optional<OpenBatchResult<AttrDefinitionBatchRsp>> batchResult = BasicConverter.apiReturnData(editClient.batchModifyAttrDefinition(param.getKgName(), modifyReqs));
        return batchResult.<RestResp<Object>>map(RestResp::new)
                .orElseGet(() -> new RestResp<>(Collections.emptyList()));
    }

    @ApiOperation("删除属性定义")
    @GetMapping("data/attribute/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "query", value = "属性定义ID")
    })
    public RestResp<Object> deleteAttribute(@Valid @ApiIgnore @RequestParam("kgName") String kgName, @RequestParam("id") Integer id) {
        editClient.deleteAttrDefinition(kgName, id);
        return new RestResp<>();
    }


    @ApiOperation("读取概念下的属性定义")
    @GetMapping("data/attribute/get")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "conceptId", dataType = "long", paramType = "query", value = "conceptId 概念id"),
            @ApiImplicitParam(name = "conceptKey", dataType = "string", paramType = "query", value = "conceptId 为空是生效"),
            @ApiImplicitParam(name = "isInherit", dataType = "boolean", paramType = "query", value = "是否继承展示父概念属性 默认继承")
    })
    public RestResp<List<AttributeDefinition>> attribute(@Valid @ApiIgnore AttributeParameter param) {
        ApiReturn<List<AttrDefinitionRsp>> listApiReturn = kgDataClient.searchAttrDefByConcept(param.getKgName(), param.getConceptId(), param.getConceptKey(), param.getIsInherit());
        Function<List<AttrDefinitionRsp>, List<AttributeDefinition>> rspFunction = a -> BasicConverter.toListNoNull(a, AttrDefConverter::attrDefinitionRspToAttributeDefinition);

        Optional<List<AttrDefinitionRsp>> attrDefinitionRspOpt = BasicConverter.apiReturnData(listApiReturn);
        return attrDefinitionRspOpt.map(attrDefinitionRspList ->
                new RestResp<>(rspFunction.apply(attrDefinitionRspList))).orElseGet(() -> new RestResp<>(Collections.emptyList()));
    }


    @ApiOperation("批量关系新增")
    @PostMapping("data/relation/insert")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "entityUpsert", dataType = "boolean", defaultValue = "false", paramType = "form", value = "关系的实体不存在是否新增"),
            @ApiImplicitParam(name = "data", required = true, dataType = "string", paramType = "form", value = "数据，是ImportRelationBean列表的JSON形式")
    })
    public RestResp<OpenBatchResult<ImportRelationBean>> importRelation(@Valid @ApiIgnore ImportRelationParameter param) {
        Function<List<BatchRelationRsp>, ApiReturn<OpenBatchResult<BatchRelationRsp>>> returnFunction = a -> editClient.importRelation(param.getKgName(), a);
        Function<List<ImportRelationBean>, List<BatchRelationRsp>> reqFunction = a -> BasicConverter.toListNoNull(a, RelationConverter::importRelationBeanToBatchRelationRsp);
        OpenBatchResult<ImportRelationBean> resMap = returnFunction
                .compose(reqFunction)
                .andThen(a -> BasicConverter.convert(a, RelationConverter::batchRelationRspToOpenBatchResult))
                .apply(param.getData());
        return new RestResp<>(resMap);
    }

    @ApiOperation("批量关系更新")
    @PostMapping("data/relation/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "data", dataType = "string", required = true, paramType = "form", value = "数据，是ImportRelationBean列表的JSON形式"),
            @ApiImplicitParam(name = "mode", dataType = "int", paramType = "form", value = "0 ")
    })
    public RestResp<String> updataRelation(@Valid @ApiIgnore UpdataRelationParameter param) {
        //mode todo
        Function<List<RelationUpdateReq>, ApiReturn<List<RelationUpdateReq>>> returnFunction = a -> editClient.updateRelations(param.getKgName(), a);
        Function<List<ImportRelationBean>, List<RelationUpdateReq>> reqFunction = a -> BasicConverter.toListNoNull(a, RelationConverter::importRelationBeanToRelationUpdateReq);
        returnFunction
                .compose(reqFunction)
                .andThen(BasicConverter::apiReturnData)
                .apply(param.getData());

        return new RestResp<>("成功");
    }

    @ApiOperation("批量关系删除")
    @PostMapping("data/relation/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "ids", dataType = "string", required = true, paramType = "form", value = "三元组id列表")
    })
    public RestResp<List<String>> delectRelation(@Valid @ApiIgnore DelectRelationParameter param) {
        editClient.deleteRelations(param.getKgName(), param.getIds());
        return new RestResp<>(param.getIds());
    }

    @ApiOperation("批量关系查询")
    @PostMapping("data/relation")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "entityIds", dataType = "string", paramType = "form", value = "实体ID列表的JSON形式,初始节点列表"),
            @ApiImplicitParam(name = "attrIds", dataType = "string", paramType = "form", value = "属性ID列表的JSON形式"),
            @ApiImplicitParam(name = "attrKeys", dataType = "string", paramType = "form", value = "attrIds为空时生效"),
            @ApiImplicitParam(name = "attrValueIds", dataType = "string", paramType = "form", value = "实体ID列表的JSON形式,结束节点列表"),
            @ApiImplicitParam(name = "direction", dataType = "int", paramType = "form", value = "0双向，1正向，-1反向", defaultValue = "0"),
            @ApiImplicitParam(name = "attrTimeFrom", dataType = "string", paramType = "form", value = "边开始时间过滤条件，默认不进行筛选 {\"$gt\" : \"2015-12-01\", \"$lt\" : \"2016-12-01\"}"),
            @ApiImplicitParam(name = "attrTimeTo", dataType = "string", paramType = "form", value = "边结束时间过滤条件，默认不进行筛选 {\"$gt\" : \"2015-12-01\", \"$lt\" : \"2016-12-01\"}"),
            @ApiImplicitParam(name = "query", dataType = "string", paramType = "form", value = "边附加属性过滤条件，格式 [{\"attrId\":\"数值属性id\",\"SeqNo\":\"边数值属性id\",\"$eq\":\"字段全匹配\"},{\"attrId\":\"数值属性id\",\"SeqNo\":\"边数值属性id\",\"$gt\":\"大于\",\"$lt\":\"小于\"}] "),
            @ApiImplicitParam(name = "pageNo", defaultValue = "1", dataType = "int", paramType = "query", value = "分页页码,最小值为1"),
            @ApiImplicitParam(name = "pageSize", defaultValue = "10", dataType = "int", paramType = "query", value = "分页每页数量默认10"),
    })
    public RestResp<List<RelationBeanScore>> queryRelation(@Valid @ApiIgnore QueryRelationParameter query) {
        Function<EdgeSearchReqList, ApiReturn<List<EdgeSearchRsp>>> returnFunction = a -> editClient.batchSearchRelation(query.getKgName(), a);
        List<RelationBeanScore> scoreList = returnFunction
                .compose(RelationConverter::queryRelationParameterToEdgeSearchReq)
                .andThen(a -> BasicConverter.convertList(a, RelationConverter::edgeSearchRspToRelationBeanScore))
                .apply(query);
        return new RestResp<>(scoreList);
    }

    @ApiOperation("批量查询实体")
    @PostMapping("data/entity")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "conceptId", dataType = "long", paramType = "form", value = "实体所属概念ID"),
            @ApiImplicitParam(name = "conceptKey", dataType = "string", paramType = "form", value = "conceptId为空时生效"),
            @ApiImplicitParam(name = "query", dataType = "string", paramType = "form", value = "筛选条件[{\"attrId\":\"数值属性id\",\"$eq\":\"字段全匹配\"},{\"attrId\":\"数值属性id\",\"$gt\":\"大于\",\"$lt\":\"小于\"}]"),
            @ApiImplicitParam(name = "pageNo", defaultValue = "1", dataType = "int", paramType = "query", value = "分页页码最小值为1"),
            @ApiImplicitParam(name = "pageSize", defaultValue = "10", dataType = "int", paramType = "query", value = "分页每页数量默认10"),
    })
    public RestResp<List<ImportEntityBean>> entityByDataAttribute(@Valid @ApiIgnore EntityByDataAttributeParameter param) {
        EntityQueryReq entityQueryReq = EntityConverter.entityByDataAttributeParameterToEntityQueryReq(param);
        ApiReturn<List<OpenEntityRsp>> apiReturn = editClient.queryEntityList(param.getKgName(), entityQueryReq);
        List<ImportEntityBean> entityBeanList = BasicConverter.convertList(apiReturn, EntityConverter::openEntityRspToImportEntityBean);
        return new RestResp<>(entityBeanList);
    }

    @ApiOperation("批量实体添加或修改")
    @PostMapping("data/entity/upsert")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "data", dataType = "string", required = true, paramType = "form", value = "数据，ImportEntityBean"),
            @ApiImplicitParam(name = "upsert", dataType = "boolean", defaultValue = "true", paramType = "form", value = "true 更新 false 修改"),
            @ApiImplicitParam(name = "mode", dataType = "int", paramType = "form", value = "单个实体有部分错误信息时的处理方式，0 表示整条数据不导入，1 表示导入正确的部分"),
    })

    public RestResp<OpenBatchResult<ImportEntityBean>> importEntity(@Valid @ApiIgnore ImportEntityParameter param) {
        Function<List<OpenBatchSaveEntityRsp>, ApiReturn<OpenBatchResult<OpenBatchSaveEntityRsp>>>
                returnFunction = a -> editClient.saveOrUpdate(param.getKgName(), param.getUpsert(), a);
        Function<List<ImportEntityBean>, List<OpenBatchSaveEntityRsp>> repFunction =
                a -> BasicConverter.toListNoNull(param.getData(), EntityConverter::importEntityBeanToOpenBatchSaveEntityRsp);
        Function<OpenBatchResult<OpenBatchSaveEntityRsp>, OpenBatchResult<ImportEntityBean>> rspFunction =
                a -> BasicConverter.convertToOpenBatchResult(a, EntityConverter::OpenBatchSaveEntityRspToImportEntityBean);
        OpenBatchResult<ImportEntityBean> resMap = returnFunction
                .compose(repFunction)
                .andThen(a -> BasicConverter.convert(a, rspFunction))
                .apply(param.getData());
        return new RestResp<>(resMap);
    }

    @ApiOperation("批量实体添加")
    @PostMapping("data/entity/insert")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "data", dataType = "string", required = true, paramType = "form", value = "数据，ImportEntityBean"),
    })
    public RestResp<OpenBatchResult<ImportEntityBean>> entityInsert(@Valid @ApiIgnore EntityInsertParameter param) {
        ImportEntityParameter parameter = new ImportEntityParameter(param.getKgName(), param.getData(), true, NumberUtils.INTEGER_ZERO);
        return importEntity(parameter);
    }

    @ApiOperation("批量实体修改")
    @PostMapping("data/entity/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "data", dataType = "string", required = true, paramType = "form", value = "数据，ImportEntityBean 详细参数参照开放平台"),
    })
    public RestResp<OpenBatchResult<ImportEntityBean>> entityUpdate(@Valid @ApiIgnore EntityInsertParameter param) {
        ImportEntityParameter parameter = new ImportEntityParameter(param.getKgName(), param.getData(), false, NumberUtils.INTEGER_ZERO);
        return importEntity(parameter);
    }

    @ApiOperation("批量删除实体数值属性")
    @PostMapping("data/entity/attr/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "entityIds", dataType = "string", required = true, paramType = "form", value = "实体ID数组的JSON形式"),
            @ApiImplicitParam(name = "attributeIds", dataType = "string", paramType = "form", value = "属性ID数组的JSON形式"),
            @ApiImplicitParam(name = "attrNames", dataType = "string", paramType = "form", value = "私有属性名称数组的JSON形式"),

    })
    public RestResp entityAttrDelect(@Valid @ApiIgnore EntityAttrDelectParameter param) {
        editClient.batchDeleteEntityAttr(param.getKgName(), EntityConverter.entityAttrBatchDelParamToReq(param));
        return new RestResp();
    }

    @ApiOperation("批量实体删除")
    @PostMapping("data/entity/delete")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "ids", dataType = "string", required = true, paramType = "form", example = " [1,2]", value = "需要被删除的实体id")
    })
    public RestResp<List<Map<String, Object>>> delectEntity(@Valid @ApiIgnore DelectEntityParameter param) {
        ApiReturn<List<DeleteResult>> listApiReturn = editClient.batchDeleteEntities(param.getKgName(), param.getIds());
        List<Map<String, Object>> resList = BasicConverter.convertList(listApiReturn, a -> {
            Map<String, Object> tempMap = Maps.newHashMap();
            tempMap.put("entityId", a.getId());
            tempMap.put("message", a.getMessage());
            return tempMap;
        });
        return new RestResp<>(resList);
    }

    @ApiOperation(value = "融合候选集写入", notes = "添加实体到融合候选集")
    @PostMapping("data/entity/merge")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "data", dataType = "string", required = true, paramType = "form", value = "data 实体id,格式[2131,1231]")
    })
    public RestResp<String> entityMerge(@ApiParam(required = true) @RequestParam("kgName") String kgName,
                                        @ApiParam(required = true) @RequestParam("data") List<Long> entityIds) {
        Optional<String> optional = BasicConverter.apiReturnData(mergeClient.createMergeEntity(kgName, entityIds));
        return new RestResp<>(optional.orElse(StringUtils.EMPTY));
    }

    @ApiOperation("根据实体名称返回实体信息")
    @PostMapping("data/entity/get/by/name")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "names", dataType = "string", required = true, paramType = "form", value = "[{\"name\":,\"meaningTag\":}]")
    })
    public RestResp<List<ImportEntityBean>> getEntityByName(@ApiParam(required = true) @RequestParam("kgName") String kgName,
                                                            @ApiIgnore @RequestParam("names") String names) {

        Function<List<EntityQueryWithConditionReq>, ApiReturn<List<OpenEntityRsp>>> returnFunction =
                a -> kgDataClient.queryEntityByNameAndMeaningTag(kgName, a);
        Function<String, List<EntityQueryWithConditionReq>> reqFunction = a -> JsonUtils.jsonToList(a, EntityQueryWithConditionReq.class);
        List<ImportEntityBean> entityBeanList = returnFunction
                .compose(reqFunction)
                .andThen(a -> BasicConverter.convertList(a, EntityConverter::openEntityRspToImportEntityBean))
                .apply(names);
        return new RestResp<>(entityBeanList);
    }

    @ApiOperation(value = "KGQL查询", notes = "KGQL查询，PlantData知识图谱的查询语言查询数据。")
    @PostMapping("data/kgql")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "query", required = true, dataType = "long", paramType = "form", value = "查询语句"),
    })
    public RestResp<Object> execQl(@ApiParam(required = true) @NotBlank @RequestParam("query") String query) {
        return new RestResp<>(editClient.executeQl(new KgqlReq(query)));
    }
}
