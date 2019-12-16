package com.plantdata.kgcloud.plantdata;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.plantdata.converter.app.InfoBoxConverter;
import com.plantdata.kgcloud.plantdata.converter.graph.GraphInitBasicConverter;
import com.plantdata.kgcloud.plantdata.converter.common.SchemaBasicConverter;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.req.app.InfoBoxParameter;
import com.plantdata.kgcloud.plantdata.req.entity.EntityProfileBean;
import com.plantdata.kgcloud.plantdata.req.explore.GeneralGraphParameter;
import com.plantdata.kgcloud.plantdata.req.explore.GraphBean;
import com.plantdata.kgcloud.plantdata.rsp.app.InitGraphBean;
import com.plantdata.kgcloud.plantdata.rsp.schema.SchemaBean;
import com.plantdata.kgcloud.sdk.AppClient;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/12 11:29
 */
@RestController
@RequestMapping("sdk/app")
public class AppController {

    @Autowired
    private AppClient appClient;
    @Autowired
    private HttpServletRequest request;

    @ApiOperation("获取当前图实体类型及属性类型的schema")
    @GetMapping("schema")
    public RestResp<SchemaBean> schema(@RequestParam("kgName") String kgName) {
        SchemaBean schemaBean = BasicConverter.convert(appClient.querySchema(kgName), SchemaBasicConverter::schemaRspToSchemaBean);
        return new RestResp<>(schemaBean);
    }

    @ApiOperation("图探索的初始化")
    @PostMapping("graph/default")
    @ApiParam(name = "kgName", required = true, type = "String", value = "图谱名称")
    public RestResp graphInit(@RequestParam("type") String type) {
        InitGraphBean initGraphBean = BasicConverter.convert(appClient.initGraphExploration(request.getParameter("kgName"), type), GraphInitBasicConverter::graphInitRspToInitGraphBean);
        return new RestResp<>(initGraphBean);
    }

    @ApiOperation("读取知识卡片")
    @PostMapping("infobox")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "long", paramType = "form", value = "实体id"),
            @ApiImplicitParam(name = "isRelationAtts", dataType = "boolean", defaultValue = "true", paramType = "form", value = "是否读取对象属性,默认true"),
            @ApiImplicitParam(name = "allowAtts", dataType = "string", paramType = "form", value = "查询指定的属性，格式为json数组格式，默认为读取全部"),
            @ApiImplicitParam(name = "allowAttsKey", dataType = "string", paramType = "form", value = "allowAtts为空时生效"),
    })
    public RestResp<EntityProfileBean> infoBox(@Valid @ApiIgnore InfoBoxParameter infoBoxParameter) {
        EntityProfileBean res = BasicConverter.convert(appClient.infoBox(infoBoxParameter.getKgName(),
                InfoBoxConverter.infoBoxParameterToInfoBoxReq(infoBoxParameter)),
                InfoBoxConverter::infoBoxRspToEntityProfileBean);
        return new RestResp<>(res);
    }

}
