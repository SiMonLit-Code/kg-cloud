package com.plantdata.kgcloud.plantdata.controller;

import ai.plantdata.kg.common.bean.AttributeDefinition;
import cn.hiboot.mcn.core.exception.RestException;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.converter.common.ConceptConverter;
import com.plantdata.kgcloud.plantdata.req.data.AttributeParameter;
import com.plantdata.kgcloud.plantdata.req.data.ConceptParameter;
import com.plantdata.kgcloud.plantdata.req.data.DelectEntityParameter;
import com.plantdata.kgcloud.plantdata.req.data.DelectRelationParameter;
import com.plantdata.kgcloud.plantdata.req.data.DeleteAttributeParameter;
import com.plantdata.kgcloud.plantdata.req.data.EntityAttrDelectParameter;
import com.plantdata.kgcloud.plantdata.req.data.EntityByDataAttributeParameter;
import com.plantdata.kgcloud.plantdata.req.data.EntityInsertParameter;
import com.plantdata.kgcloud.plantdata.req.data.ImportAttributeParameter;
import com.plantdata.kgcloud.plantdata.req.data.ImportEntityParameter;
import com.plantdata.kgcloud.plantdata.req.data.ImportRelationParameter;
import com.plantdata.kgcloud.plantdata.req.data.InsertConceptParameter;
import com.plantdata.kgcloud.plantdata.req.data.MergeBean;
import com.plantdata.kgcloud.plantdata.req.data.QueryRelationParameter;
import com.plantdata.kgcloud.plantdata.req.data.UpdataConceptParameter;
import com.plantdata.kgcloud.plantdata.req.data.UpdataRelationParameter;
import com.plantdata.kgcloud.plantdata.req.entity.ImportEntityBean;
import com.plantdata.kgcloud.plantdata.rsp.data.RelationBeanScore;
import com.plantdata.kgcloud.plantdata.rsp.data.TreeBean;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.KgDataClient;
import com.plantdata.kgcloud.sdk.req.edit.ConceptAddReq;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

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

    @ApiOperation("获取概念树")
    @GetMapping("data/concept")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "conceptId", dataType = "long", paramType = "query", value = "概念id"),
            @ApiImplicitParam(name = "conceptIdKey", dataType = "string", paramType = "query", value = "概念唯一标识key,")
    })
    public RestResp<List<TreeBean>> concept(@Valid @ApiIgnore ConceptParameter param) {
        ApiReturn<List<BasicInfoVO>> apiReturn = appClient.conceptTree(param.getKgName(), param.getConceptId(), param.getConceptIdKey());
        List<TreeBean> tree = BasicConverter.convert(apiReturn, a -> BasicConverter.listToRsp(a, ConceptConverter::basicInfoVoToTreeBean));
        return new RestResp<>(tree);
    }

    @ApiOperation("添加概念")
    @PostMapping("data/concept/insert")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "parentId", required = true, dataType = "long", paramType = "form", value = "所属父概念id"),
            @ApiImplicitParam(name = "name", required = true, dataType = "string", paramType = "form", value = "概念名称"),
            @ApiImplicitParam(name = "key", dataType = "string", paramType = "form", value = "概念唯一标识key"),
            @ApiImplicitParam(name = "meaningTag", required = false, dataType = "string", paramType = "form", value = "唯一标识符")
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
    public RestResp<Object> updataConcept(@Valid @ApiIgnore UpdataConceptParameter updataConceptParameter) {
        return new RestResp<>();
    }


    @ApiOperation("删除概念")
    @PostMapping("data/concept/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "conceptId", required = true, dataType = "long", paramType = "query", value = "概念id")
    })
    public RestResp deleteConcept(@Valid @ApiIgnore @RequestParam("kgName") String kgName, @RequestParam("conceptId") Long conceptId) {
        return new RestResp();
    }

    @ApiOperation("批量属性新增")
    @PostMapping("data/attribute/insert")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "data", required = true, dataType = "string", paramType = "form", value = "数据，是AttributeDefinition 列表的JSON形式")
    })
    public RestResp<Object> importAttribute(@Valid @ApiIgnore ImportAttributeParameter importAttributeParameter) {
        return new RestResp<>();
    }

    @ApiOperation("属性修改")
    @PostMapping("data/attribute/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "data", required = true, dataType = "string", paramType = "form", value = "数据，是AttributeDefinition 列表的JSON形式")
    })
    public RestResp<Object> updateAttribute(@Valid @ApiIgnore ImportAttributeParameter importAttributeParameter) {
        return new RestResp<>();
    }

    @ApiOperation("删除属性定义")
    @GetMapping("data/attribute/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "long", paramType = "query", value = "属性定义ID")
    })
    public RestResp<Object> deleteAttribute(@Valid @ApiIgnore DeleteAttributeParameter deleteAttributeParameter) {
        return new RestResp<>();
    }


    @ApiOperation("读取概念下的属性")
    @GetMapping("data/attribute/get")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "conceptId", dataType = "long", paramType = "query", value = "conceptId 概念id"),
            @ApiImplicitParam(name = "conceptKey", dataType = "string", paramType = "query", value = "conceptId 为空是生效"),
            @ApiImplicitParam(name = "isInherit", dataType = "boolean", paramType = "query", value = "是否继承展示父概念属性 默认继承")
    })
    public RestResp<List<AttributeDefinition>> attribute(@Valid @ApiIgnore AttributeParameter attributeParameter) {
        if (attributeParameter.getConceptId() == null) {
            throw RestException.newInstance(57024);
        }
        return new RestResp<>();
    }

    @ApiOperation("KGQL查询")
    @PostMapping("data/kgql")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "query", required = true, dataType = "long", paramType = "form", value = "查询语句"),
    })
    public RestResp<List<Object>> execQl(@ApiParam(required = true) @NotBlank @RequestParam("kgName") String kgName,
                                         @ApiParam(required = true) @NotBlank @RequestParam("query") String query) {
        return new RestResp<>();
    }

    @ApiOperation("批量关系新增")
    @PostMapping("data/relation/insert")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "entityUpsert", dataType = "boolean", defaultValue = "false", paramType = "form", value = "关系的实体不存在是否新增"),
            @ApiImplicitParam(name = "data", required = true, dataType = "string", paramType = "form", value = "数据，是ImportRelationBean列表的JSON形式")
    })
    public RestResp<Map<String, Object>> importRelation(@Valid @ApiIgnore ImportRelationParameter importRelationParameter) {
        return new RestResp<>();
    }

    @ApiOperation("批量关系更新")
    @PostMapping("data/relation/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "data", dataType = "string", required = true, paramType = "form", value = "数据，是ImportRelationBean列表的JSON形式"),
            @ApiImplicitParam(name = "mode", dataType = "int", paramType = "form", value = "为 0 时表示只更新或追加传入数据中的部分，为 1 时表示把原始数据完全替换成传入的数据（传入数据不包含某个附加属性时，把对应原始数据删除）")
    })
    public RestResp<Object> updataRelation(@Valid @ApiIgnore UpdataRelationParameter updataRelationParameter) {
        return new RestResp<>();
    }

    @ApiOperation("批量关系删除")
    @PostMapping("data/relation/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "ids", dataType = "string", required = true, paramType = "form", value = "三元组id列表")
    })
    public RestResp<Object> delectRelation(@Valid @ApiIgnore DelectRelationParameter delectRelationParameter) {
        return new RestResp<>();
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
        return new RestResp<>();
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
    public RestResp<List<ImportEntityBean>> entityByDataAttribute(@Valid @ApiIgnore EntityByDataAttributeParameter entityByDataAttributeParameter) {
        return new RestResp<>();
    }

    @ApiOperation("批量实体添加或修改")
    @PostMapping("data/entity/upsert")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "data", dataType = "string", required = true, paramType = "form", value = "数据，ImportEntityBean"),
            @ApiImplicitParam(name = "upsert", dataType = "boolean", defaultValue = "true", paramType = "form", value = "实体不存在是否新增，默认新增"),
            @ApiImplicitParam(name = "mode", dataType = "int", paramType = "form", value = "单个实体有部分错误信息时的处理方式，0 表示整条数据不导入，1 表示导入正确的部分"),
    })
    public RestResp<Map<String, Object>> importEntity(@Valid @ApiIgnore ImportEntityParameter importEntityParameter) {
        return new RestResp<>();
    }

    @ApiOperation("批量实体添加")
    @PostMapping("data/entity/insert")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "data", dataType = "string", required = true, paramType = "form", value = "数据，ImportEntityBean"),
    })
    public RestResp<Map<String, Object>> entityInsert(@Valid @ApiIgnore EntityInsertParameter entityInsertParameter) {
        return new RestResp<>();
    }

    @ApiOperation("批量实体修改")
    @PostMapping("data/entity/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "data", dataType = "string", required = true, paramType = "form", value = "数据，ImportEntityBean"),
    })
    public RestResp<Map<String, Object>> entityUpdate(@Valid @ApiIgnore EntityInsertParameter entityInsertParameter) {
        return new RestResp<>();
    }

    @ApiOperation("批量修改实体数值属性")
    @PostMapping("data/entity/attr/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "entityIds", dataType = "string", required = true, paramType = "form", value = "实体ID数组的JSON形式"),
            @ApiImplicitParam(name = "attributeIds", dataType = "string", paramType = "form", value = "属性ID数组的JSON形式"),
            @ApiImplicitParam(name = "attrNames", dataType = "string", paramType = "form", value = "私有属性名称数组的JSON形式"),

    })
    public RestResp entityAttrDelect(@Valid @ApiIgnore EntityAttrDelectParameter entityInsertParameter) {
        return new RestResp();
    }

    @ApiOperation("批量实体删除")
    @PostMapping("data/entity/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "ids", dataType = "string", required = true, paramType = "form", value = "需要被删除的实体id")
    })
    public RestResp<List<Map<String, Object>>> delectEntity(@Valid @ApiIgnore DelectEntityParameter delectEntityParameter) {
        return new RestResp<>();
    }


    @ApiOperation("合候选集写入")
    @PostMapping("data/entity/merge")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "data", dataType = "string", required = true, paramType = "form", value = "data")
    })
    public RestResp<String> entityMerge(@ApiParam(required = true) @RequestParam("kgName") String kgName,
                                        @ApiParam(required = true) @RequestParam("data") List<MergeBean> data) {

        return new RestResp<>();
    }

    @ApiOperation("根据实体名称返回实体信息")
    @PostMapping("data/entity/get/by/name")
    public RestResp<List<ImportEntityBean>> getEntityByName(@ApiParam(required = true) @RequestParam("kgName") String kgName,
                                                            @ApiParam(required = true, value = "[{\"name\":,\"meaningTag\":}]") @RequestParam("names") String names) {

        return new RestResp<>();
    }

}
