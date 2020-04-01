package com.plantdata.kgcloud.plantdata.controller;

import cn.hiboot.mcn.core.exception.RestException;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.converter.config.StatisticConverter;
import com.plantdata.kgcloud.plantdata.req.data.StatCountRelationbyEntityParameter;
import com.plantdata.kgcloud.plantdata.req.data.StatEdgeGroupByEdgeValueParameter;
import com.plantdata.kgcloud.plantdata.req.data.StatEntityGroupByAttributeByConceptIdParameter;
import com.plantdata.kgcloud.plantdata.req.data.StatEntityGroupByAttrvalueByAttrIdParameter;
import com.plantdata.kgcloud.plantdata.req.data.StatEntityGroupByConceptParameter;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.KgDataClient;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeAttrStatisticByAttrValueReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByConceptIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByEntityIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByAttrIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByConceptReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * @author spa 图谱应用图统计
 */
@RestController
@RequestMapping("sdk/graph")
@Api(tags = "graph-stat-sdk")
public class GraphStatOpenRestApi {

    @Autowired
    private AppClient appClient;

    @Autowired
    private KgDataClient kgDataClient;

    @ApiOperation("查询实体的关系度数")
    @PostMapping("stat/entity/degree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "isDistinct", dataType = "boolean", paramType = "form", value = "一个节点多个关系是否只算一个，默认false"),
            @ApiImplicitParam(name = "allowAtts", dataType = "string", paramType = "form", value = "查询指定的属性，格式[{'layer':'层数';'ids':[查询的id列表]}]，默认为查询全部"),
            @ApiImplicitParam(name = "allowTypes", dataType = "string", paramType = "form", value = "查询指定的概念，格式[{'layer':'层数';'ids':[查询的id列表]}]，默认为查询全部"),
            @ApiImplicitParam(name = "entityId", required = true, dataType = "long", paramType = "form", value = "实体id")
    })
    public RestResp<List<Map<String, Object>>> statCountRelationbyEntity(@Valid @ApiIgnore StatCountRelationbyEntityParameter param) {
        EdgeStatisticByEntityIdReq req = StatisticConverter.statCountRelationbyEntityParameterToEdgeStatisticByEntityIdReq(param);
        Optional<List<Map<String, Object>>> mapOpt = BasicConverter.apiReturnData(kgDataClient.statisticCountEdgeByEntity(param.getKgName(), req));
        return new RestResp<>(mapOpt.orElse(Collections.emptyList()));
    }

    @ApiOperation("实例统计，统计实例数量，按概念分组")
    @PostMapping("stat/entity/count/group/by/concept")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "entityIds", dataType = "string", paramType = "form", value = "统计指定的实体id;指定实体后可返回具体的实体id"),
            @ApiImplicitParam(name = "sort", dataType = "string", paramType = "form", value = "排序规则 1 升序 -1 降序;默认-1"),
            @ApiImplicitParam(name = "allowTypes", dataType = "string", paramType = "form", value = "统计指定的概念，格式为json数组，默认为统计全部"),
            @ApiImplicitParam(name = "allowTypesKey", dataType = "string", paramType = "form", value = "allowTypes为空时生效"),

            @ApiImplicitParam(name = "returnType", dataType = "int", paramType = "form", value = "返回格式，0Echart;1普通格式;默认 0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "form", value = "10")
    })
    public RestResp statEntityGroupByConcept(@Valid @ApiIgnore StatEntityGroupByConceptParameter param) {
        EntityStatisticGroupByConceptReq req = StatisticConverter.statEntityGroupByConceptParameterToEntityStatisticGroupByConceptReq(param);
        Optional<Object> optional = BasicConverter.apiReturnData(kgDataClient.statisticEntityGroupByConcept(param.getKgName(), req));
        return new RestResp<>(optional.orElse(null));
    }


    @ApiOperation("对象属性统计，统计对象属性的数量，按关系分组")
    @PostMapping("stat/relation/count/group/by/attrname")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "conceptId", dataType = "long", paramType = "form", value = "概念id"),
            @ApiImplicitParam(name = "conceptKey", dataType = "string", paramType = "form", value = "概念唯一标识key,conceptId为空时生效"),
            @ApiImplicitParam(name = "tripleIds", dataType = "string", paramType = "form", value = "统计指定的关系id，指定关系后返回具体三元组id"),
            @ApiImplicitParam(name = "allowAtts", dataType = "string", paramType = "form", value = "统计指定的属性，格式为json数组，默认为统计全部"),
            @ApiImplicitParam(name = "allowAttsKey", dataType = "string", paramType = "form", value = "allowAtts为空时生效"),
            @ApiImplicitParam(name = "returnType", dataType = "int", paramType = "form", value = "返回格式，0Echart;1普通格式;默认 0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "form", value = "10"),
            @ApiImplicitParam(name = "sort", dataType = "int", paramType = "form", value = "排序规则 1 升序 -1 降序;默认-1"),
    })
    public RestResp statEntityGroupByAttributeByConceptId(@Valid @ApiIgnore StatEntityGroupByAttributeByConceptIdParameter param) {
        EdgeStatisticByConceptIdReq req = StatisticConverter.statEntityGroupByAttributeByConceptIdParameterToEdgeStatisticByConceptIdReq(param);
        Optional<Object> optional = BasicConverter.apiReturnData(kgDataClient.statisticRelation(param.getKgName(), req));
        return new RestResp<>(optional.orElse(null));
    }


    @ApiOperation("数值属性统计，统计数值属性的数量，按数值属性值分组")
    @PostMapping("stat/entity/count/group/by/attrvalue")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "attrId", dataType = "int", paramType = "form", value = "属性ID"),
            @ApiImplicitParam(name = "attrKey", dataType = "string", paramType = "form", value = "attrId为空时生效，"),
            @ApiImplicitParam(name = "entityIds", dataType = "string", paramType = "form", value = "统计指定的实体id,指定实体后可返回具体的实体id"),
            @ApiImplicitParam(name = "allowValues", dataType = "string", paramType = "form", value = "统计指定的属性值，格式为json数组，默认为统计全部"),
            @ApiImplicitParam(name = "returnType", dataType = "int", paramType = "form", value = "返回格式，0Echart;1普通格式;默认 0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "form", value = "10"),
            @ApiImplicitParam(name = "sort", dataType = "int", paramType = "form", value = "排序规则 1 升序 -1 降序;默认1"),
            @ApiImplicitParam(name = "isMerge", dataType = "boolean", paramType = "form", value = "当统计数值与时间类型时，是否进行合并"),
            @ApiImplicitParam(name = "dateType", dataType = "string", paramType = "form", value = "时间合并时,类型{\"$lte\":,\"$gte\":,type:},type 1 按年, 2安季度，3按月 ,4按日,5按时，6按分，7按秒")
    })
    public RestResp statEntityGroupByAttrvalueByAttrId(@Valid @ApiIgnore StatEntityGroupByAttrvalueByAttrIdParameter param) {

        EntityStatisticGroupByAttrIdReq req = StatisticConverter.statEntityGroupByAttrvalueByAttrIdParameterToEntityStatisticGroupByAttrIdReq(param);
        Optional<Object> optional = BasicConverter.apiReturnData(kgDataClient.statisticAttrGroupByConcept(param.getKgName(), req));
        return new RestResp<>(optional.orElse(null));
    }


    @ApiOperation("边数值属性统计，按数值属性值分组")
    @PostMapping("stat/relation/count/group/by/attrvalue")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "attrId", dataType = "int", paramType = "form", value = "属性ID"),
            @ApiImplicitParam(name = "attrKey", dataType = "string", paramType = "form", value = "attrId为空时生效，"),
            @ApiImplicitParam(name = "entityIds", dataType = "string", paramType = "form", value = "统计指定的开始节点id"),
            @ApiImplicitParam(name = "allowValues", dataType = "string", paramType = "form", value = "统计指定的属性值，格式为json数组，默认为统计全部"),
            @ApiImplicitParam(name = "returnType", dataType = "int", paramType = "form", value = "返回格式，0Echart;1普通格式;默认 0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "form", value = "10"),
            @ApiImplicitParam(name = "sort", dataType = "int", paramType = "form", value = "排序规则 1 升序 -1 降序;默认-1"),
            @ApiImplicitParam(name = "isMerge", dataType = "boolean", paramType = "form", value = "当统计数值与时间类型时，是否进行合并"),
            @ApiImplicitParam(name = "dateType", dataType = "string", paramType = "form", value = "时间合并时,类型{\"$lte\":,\"$gte\":,type:},type 1 按年, 2安季度，3按月 ,4按日,5按时，6按分，7按秒"),
            @ApiImplicitParam(name = "tripleIds", dataType = "string", paramType = "form", value = "统计指定的关系id，指定关系后返回具体三元组id"),
            @ApiImplicitParam(name = "seqNo", required = true, dataType = "int", paramType = "form", value = "边属性id")
    })
    public RestResp statEdgeGroupByEdgeValue(@Valid @ApiIgnore StatEdgeGroupByEdgeValueParameter param) {

        EdgeAttrStatisticByAttrValueReq req = StatisticConverter.statEdgeGroupByEdgeValueParameterToEdgeAttrStatisticByAttrValueReq(param);
        Optional<Object> optional = BasicConverter.apiReturnData(kgDataClient.statEdgeGroupByEdgeValue(param.getKgName(), req));
        return new RestResp<>(optional.orElse(null));
    }


}
