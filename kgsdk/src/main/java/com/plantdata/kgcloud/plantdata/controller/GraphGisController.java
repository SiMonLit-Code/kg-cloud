package com.plantdata.kgcloud.plantdata.controller;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.converter.graph.GisConverter;
import com.plantdata.kgcloud.plantdata.req.explore.common.GraphBean;
import com.plantdata.kgcloud.plantdata.req.explore.gis.GraphLocusGisParameter;
import com.plantdata.kgcloud.plantdata.req.explore.gis.GraphRectangleParameter;
import com.plantdata.kgcloud.plantdata.rsp.explore.gis.GisLocusOldRsp;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.req.app.GisGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.GisLocusReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisLocusAnalysisRsp;
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
 * @date 2019/12/25 17:27
 */
@RestController("graphGisController-v2")
@RequestMapping("sdk/app/graph/gis")
public class GraphGisController implements SdkOldApiInterface {

    @Autowired
    private AppClient appClient;

    @ApiOperation("gis图探索")
    @PostMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "allowTypes", dataType = "string", paramType = "form", value = "查询指定的概念，格式为json数组，默认为查询全部"),
            @ApiImplicitParam(name = "allowTypesKey", dataType = "string", paramType = "form", value = "allowTypes为空时生效"),
            @ApiImplicitParam(name = "attrId", dataType = "string", paramType = "form", value = "根据属性id查询与其关联的gis坐标"),
            @ApiImplicitParam(name = "attrKey", dataType = "string", paramType = "form", value = "attrId为空时生效"),
            @ApiImplicitParam(name = "isInherit", dataType = "string", paramType = "form", value = "allowTypes字段指定的概念是否继承"),
            @ApiImplicitParam(name = "direction", defaultValue = "0", dataType = "int", paramType = "form", value = "查询边关系的方向，0表示双向，1表示出发，2表示到达,默认0"),
            @ApiImplicitParam(name = "fromTime", dataType = "string", paramType = "form", value = "开始时间，格式yyyy-MM-dd"),
            @ApiImplicitParam(name = "toTime", dataType = "string", paramType = "form", value = "结束时间，格式yyyy-MM-dd，默认当前时间"),
            @ApiImplicitParam(name = "filterType", required = true, dataType = "string", paramType = "form", value = "坐标筛选类型,可选值:$box 矩形区域，$centerSphere 圆形区域"),
            @ApiImplicitParam(name = "gisFilters", required = true, dataType = "string", paramType = "form", value = "经度范围,数组格式，值为2个元素，box时例：[[-75,40],[-70,50]]，表示2个坐标点组成的矩形，center时例：[[-74,40],10]，第一个表示圆心坐标，第二个表示半径"),
    })
    public RestResp<GraphBean> graphRectangle(@Valid @ApiIgnore GraphRectangleParameter param) {
        Function<GisGraphExploreReq, ApiReturn<GisGraphExploreRsp>> returnFunction = a -> appClient.gisGraphExploration(param.getKgName(), a);
        GraphBean graphBean = returnFunction
                .compose(GisConverter::graphRectangleParameterToGisGraphExploreReq)
                .andThen(a -> BasicConverter.convert(a, GisConverter::gisGraphExploreRspToGraphBean))
                .apply(param);
        return new RestResp<>(graphBean);
    }

    @ApiOperation("轨迹分析")
    @PostMapping("locus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "rules", required = true, dataType = "string", paramType = "form", value = "分析实体和规则 格式为[{\"ruleId\":1,\"kql\":\"规则语言\",\"ids\":[\"实体id1\",\"实体id2\"]}]"),
            @ApiImplicitParam(name = "filterType", dataType = "string", paramType = "form", value = "坐标筛选类型,可选值:$box 矩形区域，$centerSphere 圆形区域"),
            @ApiImplicitParam(name = "gisFilters", dataType = "string", paramType = "form", value = "经度范围,数组格式，值为2个元素，box时例：[[-75,40],[-70,50]]，表示2个坐标点组成的矩形，center时例：[[-74,40],10]，第一个表示圆心坐标，第二个表示半径"),
            @ApiImplicitParam(name = "fromTime", dataType = "string", paramType = "form", value = "开始时间，格式yyyy-MM-dd"),
            @ApiImplicitParam(name = "toTime", dataType = "string", paramType = "form", value = "结束时间，格式yyyy-MM-dd，默认当前时间"),
            @ApiImplicitParam(name = "timeFilterType", dataType = "int", paramType = "form", value = "时间筛选类型，0 不按时间不筛选, 1以节点的时间筛选,  2 以关系的时间筛选, 3 以关系与节点的时间筛选"),
    })
    public RestResp<GisLocusOldRsp> graphLocusGis(@Valid @ApiIgnore GraphLocusGisParameter param) {
        Function<GisLocusReq, ApiReturn<GisLocusAnalysisRsp>> returnFunction = a -> appClient.graphLocusGis(param.getKgName(), a);
        GisLocusOldRsp locusRsp = returnFunction
                .compose(GisConverter::graphLocusGisParameterToGisLocusReq)
                .andThen(a -> BasicConverter.convert(a, GisConverter::gisGraphExploreRspToGisLocusRsp))
                .apply(param);
        return new RestResp<>(locusRsp);
    }
}
