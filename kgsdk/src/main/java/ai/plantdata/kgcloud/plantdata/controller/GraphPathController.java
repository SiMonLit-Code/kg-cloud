package ai.plantdata.kgcloud.plantdata.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.plantdata.converter.graph.ExploreReqConverter;
import ai.plantdata.kgcloud.plantdata.converter.graph.ExploreRspConverter;
import ai.plantdata.kgcloud.plantdata.req.explore.common.GraphBean;
import ai.plantdata.kgcloud.plantdata.req.explore.path.PathGraphParameter;
import ai.plantdata.kgcloud.plantdata.req.explore.path.TimePathGraphParameter;
import cn.hiboot.mcn.core.model.result.RestResp;
import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.sdk.AppClient;
import ai.plantdata.kgcloud.sdk.req.app.explore.PathAnalysisReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.PathTimingAnalysisReqList;
import ai.plantdata.kgcloud.sdk.rsp.app.analysis.PathAnalysisRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.analysis.PathTimingAnalysisRsp;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.function.Function;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/24 10:43
 */
@RestController("graphPathController-v2")
@RequestMapping("sdk/network")
public class GraphPathController implements SdkOldApiInterface {

    @Autowired
    private AppClient appClient;

    @ApiOperation("路径分析")
    @PostMapping("path")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "start", required = true, dataType = "long", paramType = "form", value = "开始实体id"),
            @ApiImplicitParam(name = "degreeLimit", dataType = "int(32)", paramType = "form", value = "边数量限制"),
            @ApiImplicitParam(name = "end", required = true, dataType = "long", paramType = "form", value = "结束实体id"),
            @ApiImplicitParam(name = "distance", dataType = "int", paramType = "form", value = "查询层数"),
            @ApiImplicitParam(name = "isShortest", dataType = "boolean", paramType = "form", value = "是否只查询最短路径"),
            @ApiImplicitParam(name = "replaceClassIds", dataType = "strng", paramType = "form", value = "需要被替换后的classId列表，格式为json数组。"),
            @ApiImplicitParam(name = "isRelationMerge", dataType = "boolean", paramType = "form", value = "同节点的关系是否进行合并"),
            @ApiImplicitParam(name = "allowAtts", dataType = "string", paramType = "form", value = "查询指定的属性，格式为json数组，默认为查询全部"),
            @ApiImplicitParam(name = "allowTypes", dataType = "string", paramType = "form", value = "查询指定的概念，格式为json数组，默认为查询全部"),
            @ApiImplicitParam(name = "replaceClassIdsKey", dataType = "strng", paramType = "form", value = "replaceClassIds为空时生效"),
            @ApiImplicitParam(name = "allowAttsKey", dataType = "string", paramType = "form", value = "allowAtts为空时生效"),
            @ApiImplicitParam(name = "allowTypesKey", dataType = "string", paramType = "form", value = "allowTypes为空时生效"),
            @ApiImplicitParam(name = "privateAttRead", dataType = "boolean", paramType = "form", value = "是否读取私有属性，默认读取"),
            @ApiImplicitParam(name = "allowAttrGroups", dataType = "string", paramType = "form", value = "查询指定的属性分组，格式为json数组"),
            @ApiImplicitParam(name = "entityQuery", dataType = "string", paramType = "form", value = "实体节点过滤"),
            @ApiImplicitParam(name = "graphBean", dataType = "string", paramType = "form", value = "后置筛选"),
            @ApiImplicitParam(name = "attAttFilters", dataType = "string", paramType = "form", value = "边附加属性过滤条件，格式 [{\"attrId\":\"数值属性id\",\"seqNo\":\"边数值属性id\",\"$eq\":\"字段全匹配\"},{\"attrId\":\"数值属性id\",\"SeqNo\":\"边数值属性id\",\"$gt\":\"大于\",\"$lt\":\"小于\"}]"),
            @ApiImplicitParam(name = "reservedAttFilters", dataType = "string", paramType = "form", value = "保留属性过滤条件，seqNo说明：3.权重，11.来源，12.置信度，13.批次号，15，自定义名称。格式 [{\"seqNo\":\"边数值属性id\",\"$eq\":\"字段全匹配\"},{\"seqNo\":\"边数值属性id\",\"$gt\":\"大于\",\"$lt\":\"小于\"}]"),
            @ApiImplicitParam(name = "isInherit", dataType = "boolean", paramType = "form", value = "allowTypes字段指定的概念是否继承"),
            @ApiImplicitParam(name = "statsConfig", dataType = "string", paramType = "form", value = "统计，统计节点的关系数量，默认为不进行图统计。"),
    })
    public RestResp<GraphBean> path(@Valid @ApiIgnore PathGraphParameter generalGraphParameter) {

        Function<PathAnalysisReqList, ApiReturn<PathAnalysisRsp>> rspFunction = a -> appClient.path(generalGraphParameter.getKgName(), a);

        GraphBean apply = rspFunction
                .compose(ExploreReqConverter::pathGraphParameterToPathAnalysisReq)
                .andThen(a -> BasicConverter.convert(a, ExploreRspConverter::statisticRspToGraphBean))
                .apply(generalGraphParameter);

        return new RestResp<>(apply);
    }

    @ApiOperation("时序路径发现")
    @PostMapping("path/timing")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "degreeLimit", dataType = "int(32)", paramType = "form", value = "边数量限制"),
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "start", required = true, dataType = "long", paramType = "form", value = "开始实体id"),
            @ApiImplicitParam(name = "end", required = true, dataType = "long", paramType = "form", value = "结束实体id"),
            @ApiImplicitParam(name = "distance", dataType = "int", paramType = "form", value = "查询层数"),
            @ApiImplicitParam(name = "isShortest", dataType = "boolean", paramType = "form", value = "是否只查询最短路径"),
            @ApiImplicitParam(name = "replaceClassIds", dataType = "strng", paramType = "form", value = "需要被替换后的classId列表，格式为json数组。"),
            @ApiImplicitParam(name = "isRelationMerge", dataType = "boolean", paramType = "form", value = "同节点的关系是否进行合并"),
            @ApiImplicitParam(name = "allowAtts", dataType = "string", paramType = "form", value = "查询指定的属性，格式为json数组，默认为查询全部"),
            @ApiImplicitParam(name = "allowTypes", dataType = "string", paramType = "form", value = "查询指定的概念，格式为json数组，默认为查询全部"),
            @ApiImplicitParam(name = "replaceClassIdsKey", dataType = "strng", paramType = "form", value = "replaceClassIds为空时生效"),
            @ApiImplicitParam(name = "allowAttsKey", dataType = "string", paramType = "form", value = "allowAtts为空时生效"),
            @ApiImplicitParam(name = "allowTypesKey", dataType = "string", paramType = "form", value = "allowTypes为空时生效"),
            @ApiImplicitParam(name = "privateAttRead", dataType = "boolean", paramType = "form", value = "是否读取私有属性，默认读取"),
            @ApiImplicitParam(name = "allowAttrGroups", dataType = "string", paramType = "form", value = "查询指定的属性分组，格式为json数组"),
            @ApiImplicitParam(name = "entityQuery", dataType = "string", paramType = "form", value = "实体节点过滤"),
            @ApiImplicitParam(name = "graphBean", dataType = "string", paramType = "form", value = "后置筛选"),
            @ApiImplicitParam(name = "attAttFilters", dataType = "string", paramType = "form", value = "边附加属性过滤条件，格式 [{\"attrId\":\"数值属性id\",\"seqNo\":\"边数值属性id\",\"$eq\":\"字段全匹配\"},{\"attrId\":\"数值属性id\",\"SeqNo\":\"边数值属性id\",\"$gt\":\"大于\",\"$lt\":\"小于\"}]"),
            @ApiImplicitParam(name = "reservedAttFilters", dataType = "string", paramType = "form", value = "保留属性过滤条件，seqNo说明：3.权重，11.来源，12.置信度，13.批次号，15，自定义名称。格式 [{\"seqNo\":\"边数值属性id\",\"$eq\":\"字段全匹配\"},{\"seqNo\":\"边数值属性id\",\"$gt\":\"大于\",\"$lt\":\"小于\"}]"),
            @ApiImplicitParam(name = "isInherit", dataType = "boolean", paramType = "form", value = "allowTypes字段指定的概念是否继承"),
            @ApiImplicitParam(name = "statsConfig", dataType = "string", paramType = "form", value = "统计，统计节点的关系数量，默认为不进行图统计。"),
            @ApiImplicitParam(name = "fromTime", dataType = "string", paramType = "form", value = "开始时间，格式yyyy-MM-dd"),
            @ApiImplicitParam(name = "toTime", dataType = "string", paramType = "form", value = "结束时间，格式yyyy-MM-dd，默认当前时间"),
            @ApiImplicitParam(name = "sort", dataType = "int", paramType = "form", value = "按时间排序:-1=desc 1=asc"),
            @ApiImplicitParam(name = "timeFilterType", dataType = "int", paramType = "form", value = "时间筛选类型，0 不按时间不筛选, 1以节点的时间筛选,  2 以关系的时间筛选, 3 以关系与节点的时间筛选"),

    })
    public RestResp<GraphBean> pathTiming(@Valid @ApiIgnore TimePathGraphParameter generalGraphParameter) {
        Function<PathTimingAnalysisReqList, ApiReturn<PathTimingAnalysisRsp>> returnFunction = a -> appClient.pathTimingAnalysis(generalGraphParameter.getKgName(), a);
        GraphBean graphBean = returnFunction
                .compose(ExploreReqConverter::timePathGraphParameterToPathTimingAnalysisReq)
                .andThen(a -> BasicConverter.convert(a, ExploreRspConverter::statisticRspToGraphBean))
                .apply(generalGraphParameter);
        return new RestResp<>(graphBean);
    }

    @ApiOperation("最短路径发现")
    @PostMapping("path/shortest")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "degreeLimit", dataType = "int(32)", paramType = "form", value = "边数量限制"),
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "start", required = true, dataType = "long", paramType = "form", value = "开始实体id"),
            @ApiImplicitParam(name = "end", required = true, dataType = "long", paramType = "form", value = "结束实体id"),
            @ApiImplicitParam(name = "distance", dataType = "int", paramType = "form", value = "查询层数"),
            @ApiImplicitParam(name = "replaceClassIds", dataType = "strng", paramType = "form", value = "需要被替换后的classId列表，格式为json数组。"),
            @ApiImplicitParam(name = "isRelationMerge", dataType = "boolean", paramType = "form", value = "同节点的关系是否进行合并"),
            @ApiImplicitParam(name = "allowAtts", dataType = "string", paramType = "form", value = "查询指定的属性，格式为json数组，默认为查询全部"),
            @ApiImplicitParam(name = "allowTypes", dataType = "string", paramType = "form", value = "查询指定的概念，格式为json数组，默认为查询全部"),
            @ApiImplicitParam(name = "replaceClassIdsKey", dataType = "strng", paramType = "form", value = "replaceClassIds为空时生效"),
            @ApiImplicitParam(name = "allowAttsKey", dataType = "string", paramType = "form", value = "allowAtts为空时生效"),
            @ApiImplicitParam(name = "allowTypesKey", dataType = "string", paramType = "form", value = "allowTypes为空时生效"),
            @ApiImplicitParam(name = "privateAttRead", dataType = "boolean", paramType = "form", value = "是否读取私有属性，默认读取"),
            @ApiImplicitParam(name = "allowAttrGroups", dataType = "string", paramType = "form", value = "查询指定的属性分组，格式为json数组"),
            @ApiImplicitParam(name = "entityQuery", dataType = "string", paramType = "form", value = "实体节点过滤"),
            @ApiImplicitParam(name = "graphBean", dataType = "string", paramType = "form", value = "后置筛选"),
            @ApiImplicitParam(name = "attAttFilters", dataType = "string", paramType = "form", value = "边附加属性过滤条件，格式 [{\"attrId\":\"数值属性id\",\"seqNo\":\"边数值属性id\",\"$eq\":\"字段全匹配\"},{\"attrId\":\"数值属性id\",\"SeqNo\":\"边数值属性id\",\"$gt\":\"大于\",\"$lt\":\"小于\"}]"),
            @ApiImplicitParam(name = "reservedAttFilters", dataType = "string", paramType = "form", value = "保留属性过滤条件，seqNo说明：3.权重，11.来源，12.置信度，13.批次号，15，自定义名称。格式 [{\"seqNo\":\"边数值属性id\",\"$eq\":\"字段全匹配\"},{\"seqNo\":\"边数值属性id\",\"$gt\":\"大于\",\"$lt\":\"小于\"}]"),
            @ApiImplicitParam(name = "isInherit", dataType = "boolean", paramType = "form", value = "allowTypes字段指定的概念是否继承"),
            @ApiImplicitParam(name = "statsConfig", dataType = "string", paramType = "form", value = "统计，统计节点的关系数量，默认为不进行图统计。"),
    })
    public RestResp<GraphBean> pathShortest(@Valid @ApiIgnore PathGraphParameter param) {
        param.setIsShortest(true);
        return path(param);
    }

}
