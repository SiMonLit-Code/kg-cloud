package com.plantdata.kgcloud.plantdata.controller;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.converter.graph.ExploreReqConverter;
import com.plantdata.kgcloud.plantdata.converter.graph.ExploreRspConverter;
import com.plantdata.kgcloud.plantdata.req.explore.GeneralGraphParameter;
import com.plantdata.kgcloud.plantdata.req.explore.GraphBean;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonExploreReq;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/16 10:20
 */
@RestController
@RequestMapping("sdk/app")
public class GraphExploreController implements SdkOldApiInterface {
    @Autowired
    private AppClient appClient;

    @ApiOperation("普通图探索")
    @PostMapping("graph")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", dataType = "long", paramType = "form", value = "实体id"),
            @ApiImplicitParam(name = "kw", dataType = "string", paramType = "form", value = "实体或概念名称,若id为空时此参数生效"),
            @ApiImplicitParam(name = "distance", dataType = "int", paramType = "form", value = "查询层数"),
            @ApiImplicitParam(name = "direction", dataType = "int", paramType = "form", value = "查询边关系的方向，0表示双向，1表示出发，2表示到达,默认0"),
            @ApiImplicitParam(name = "highLevelSize", dataType = "long", paramType = "form", value = "第二层以上节点查询个数，如果指定，第2层及第2层以上返回的节点以此数为限"),
            @ApiImplicitParam(name = "isRelationMerge", dataType = "boolean", paramType = "form", value = "同节点的关系是否进行合并"),
            @ApiImplicitParam(name = "replaceClassIds", dataType = "strng", paramType = "form", value = "需要被替换后的classId列表，格式为json数组。"),
            @ApiImplicitParam(name = "allowAtts", dataType = "string", paramType = "form", value = "查询指定的属性，格式为json数组，默认为查询全部"),
            @ApiImplicitParam(name = "allowTypes", dataType = "string", paramType = "form", value = "查询指定的概念，格式为json数组，默认为查询全部"),
            @ApiImplicitParam(name = "replaceClassIdsKey", dataType = "strng", paramType = "form", value = "replaceClassIds为空时生效"),
            @ApiImplicitParam(name = "allowAttsKey", dataType = "string", paramType = "form", value = "allowAtts为空时生效"),
            @ApiImplicitParam(name = "allowTypesKey", dataType = "string", paramType = "form", value = "allowTypes为空时生效"),
            @ApiImplicitParam(name = "allowAttrGroups", dataType = "string", paramType = "form", value = "查询指定的属性分组，格式为json数组"),
            @ApiImplicitParam(name = "entityQuery", dataType = "string", paramType = "form", value = "实体节点过滤"),
            @ApiImplicitParam(name = "graphBean", dataType = "string", paramType = "form", value = "后置筛选"),
            @ApiImplicitParam(name = "attAttFilters", dataType = "string", paramType = "form", value = "边附加属性过滤条件，格式 [{\"attrId\":\"数值属性id\",\"seqNo\":\"边数值属性id\",\"$eq\":\"字段全匹配\"},{\"attrId\":\"数值属性id\",\"SeqNo\":\"边数值属性id\",\"$gt\":\"大于\",\"$lt\":\"小于\"}]"),
            @ApiImplicitParam(name = "reservedAttFilters", dataType = "string", paramType = "form", value = "保留属性过滤条件，seqNo说明：3.权重，11.来源，12.置信度，13.批次号，15，自定义名称。格式 [{\"seqNo\":\"边数值属性id\",\"$eq\":\"字段全匹配\"},{\"seqNo\":\"边数值属性id\",\"$gt\":\"大于\",\"$lt\":\"小于\"}]"),
            @ApiImplicitParam(name = "isInherit", dataType = "boolean", paramType = "form", value = "allowTypes字段指定的概念是否继承"),
            @ApiImplicitParam(name = "privateAttRead", dataType = "boolean", paramType = "form", value = "是否读取私有属性，默认读取"),
            @ApiImplicitParam(name = "graphRule", dataType = "string", paramType = "form", value = "图探索规则，设置此参数时，其他筛选条件无效"),
            @ApiImplicitParam(name = "hyponymyDistance", dataType = "int", paramType = "form", value = "上下位关系的读取层数，0表示不读取，默认为0"),
            @ApiImplicitParam(name = "attSorts", dataType = "string", paramType = "form", value = "边附加属性排序例：[{\"attrId\":\"数值属性id\",\"seqNo\":\"边数值属性id\",\"sort\":\"-1或1\"}]"),
            @ApiImplicitParam(name = "pageNo", dataType = "int", paramType = "query", value = "当前页，默认1"),
            @ApiImplicitParam(name = "pageSize", dataType = "int", paramType = "query", value = "每页数，默认10"),
    })
    public RestResp<GraphBean> graph(@Valid @ApiIgnore GeneralGraphParameter generalGraphParameter) {
        CommonExploreReq commonExploreReq = ExploreReqConverter.generalGraphParameterToCommonExploreReq(generalGraphParameter);
        GraphBean graphBean = BasicConverter.convert(appClient.commonGraphExploration(generalGraphParameter.getKgName(), commonExploreReq), ExploreRspConverter::commonBasicGraphExploreRspToGraphBean);
        return new RestResp<>(graphBean);
    }
}
