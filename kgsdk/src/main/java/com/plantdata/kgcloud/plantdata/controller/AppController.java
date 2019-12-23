package com.plantdata.kgcloud.plantdata.controller;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.plantdata.converter.app.InfoBoxConverter;
import com.plantdata.kgcloud.plantdata.converter.app.PromptConverter;
import com.plantdata.kgcloud.plantdata.converter.graph.GraphInitBasicConverter;
import com.plantdata.kgcloud.plantdata.converter.common.SchemaBasicConverter;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.req.app.InfoBoxParameter;
import com.plantdata.kgcloud.plantdata.req.app.PromptParameter;
import com.plantdata.kgcloud.plantdata.req.app.SeniorPromptParameter;
import com.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import com.plantdata.kgcloud.plantdata.req.entity.EntityProfileBean;
import com.plantdata.kgcloud.plantdata.req.entity.ImportEntityBean;
import com.plantdata.kgcloud.plantdata.rsp.app.InitGraphBean;
import com.plantdata.kgcloud.plantdata.rsp.schema.SchemaBean;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.req.app.GraphInitRsp;
import com.plantdata.kgcloud.sdk.req.app.PromptReq;
import com.plantdata.kgcloud.sdk.req.app.SeniorPromptReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SeniorPromptRsp;
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

import javax.validation.Valid;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/12 11:29
 */
@RestController
@RequestMapping("sdk/app")
public class AppController implements SdkOldApiInterface {

    @Autowired
    private AppClient appClient;

    @ApiOperation("获取当前图实体类型及属性类型的schema")
    @GetMapping("schema")
    public RestResp<SchemaBean> schema(@RequestParam("kgName") String kgName) {
        SchemaBean schemaBean = BasicConverter.convert(appClient.querySchema(kgName), SchemaBasicConverter::schemaRspToSchemaBean);
        return new RestResp<>(schemaBean);
    }

    @ApiOperation("图探索的初始化")
    @PostMapping("graph/default")
    @ApiParam(name = "kgName", required = true, type = "String", value = "图谱名称")
    public RestResp<InitGraphBean> graphInit(@RequestParam("kgName") String kgName, @RequestParam("type") String type) {
        //remote
        ApiReturn<GraphInitRsp> rspApiReturn = appClient.initGraphExploration(kgName, type);
        //convert
        InitGraphBean initGraphBean = BasicConverter.convert(rspApiReturn, GraphInitBasicConverter::graphInitRspToInitGraphBean);
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
        InfoBoxReq infoBoxReq = InfoBoxConverter.infoBoxParameterToInfoBoxReq(infoBoxParameter);
        //remote
        ApiReturn<InfoBoxRsp> infoBoxRspApiReturn = appClient.infoBox(infoBoxParameter.getKgName(), infoBoxReq);
        //convert
        EntityProfileBean res = BasicConverter.convert(infoBoxRspApiReturn, InfoBoxConverter::infoBoxRspToEntityProfileBean);
        return new RestResp<>(res);
    }

    @ApiOperation("综合搜索")
    @GetMapping("prompt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "kw", required = true, dataType = "string", paramType = "query", value = "搜索关键词"),
            @ApiImplicitParam(name = "type", defaultValue = "11", dataType = "string", paramType = "query", value = "类型，默认11。第一位表示概念，第二位表示实例。。1为有，0为没有"),
            @ApiImplicitParam(name = "allowTypes", dataType = "string", paramType = "query", value = "查询指定的概念，格式为json数组，默认为查询全部"),
            @ApiImplicitParam(name = "allowTypesKey", dataType = "string", paramType = "query", value = "allowTypes为空时此参数生效"),
            @ApiImplicitParam(name = "isInherit", defaultValue = "false", dataType = "boolean", paramType = "query", value = "allowTypes字段指定的概念是否继承"),
            @ApiImplicitParam(name = "isFuzzy", defaultValue = "false", dataType = "boolean", paramType = "query", value = "是否模糊搜索"),
            @ApiImplicitParam(name = "openExportDate", defaultValue = "true", dataType = "boolean", paramType = "query", value = "是否使用导出实体数据集检索"),
            @ApiImplicitParam(name = "sort", dataType = "Integer", defaultValue = "-1", paramType = "query", value = "按权重排序:-1=desc 1=asc,默认-1"),
            @ApiImplicitParam(name = "pageNo", defaultValue = "1", dataType = "int", paramType = "query", value = "分页页码最小值为1"),
            @ApiImplicitParam(name = "pageSize", defaultValue = "10", dataType = "int", paramType = "query", value = "分页每页最小为1"),
            @ApiImplicitParam(name = "promptType", defaultValue = "0", dataType = "int", paramType = "query", value = "提示类型，0:prompt,1:qa,2:all"),
    })
    public RestResp<List<EntityBean>> prompt(@Valid @ApiIgnore PromptParameter promptParameter) {
        PromptReq promptReq = PromptConverter.promptParameterToPromptReq(promptParameter);
        //remote
        ApiReturn<List<PromptEntityRsp>> prompt = appClient.prompt(promptParameter.getKgName(), promptReq);
        //convert
        List<EntityBean> entityBeans = BasicConverter.convertList(prompt, PromptConverter::promptEntityRspToEntityBean);
        return new RestResp<>(entityBeans);
    }

    @ApiOperation("高级搜索查实体,")
    @PostMapping("senior/prompt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "conceptId", dataType = "long", paramType = "form", value = "实体所属概念ID"),
            @ApiImplicitParam(name = "conceptKey", dataType = "string", paramType = "form", value = "conceptId为空时生效"),
            @ApiImplicitParam(name = "kw", dataType = "string", paramType = "form", value = "前缀搜索"),
            @ApiImplicitParam(name = "query", dataType = "string", paramType = "form", value = "筛选条件[{\"attrId\":\"数值属性id\",\"$eq\":\"字段全匹配\"},{\"attrId\":\"数值属性id\",\"$gt\":\"大于\",\"$lt\":\"小于\"}]"),
            @ApiImplicitParam(name = "pageNo", defaultValue = "1", dataType = "int", paramType = "query", value = "分页页码最小值为1"),
            @ApiImplicitParam(name = "pageSize", defaultValue = "10", dataType = "int", paramType = "query", value = "分页每页最小为1"),
    })
    public RestResp<List<ImportEntityBean>> seniorPrompt(@Valid @ApiIgnore SeniorPromptParameter seniorPromptParameter) {
        SeniorPromptReq promptReq = PromptConverter.seniorPromptParameterToSeniorPromptReq(seniorPromptParameter);
        //remote
        ApiReturn<List<SeniorPromptRsp>> listApiReturn = appClient.seniorPrompt(seniorPromptParameter.getKgName(), promptReq);
        //convert
        List<ImportEntityBean> importEntityBeans = BasicConverter.convertList(listApiReturn, PromptConverter::seniorPromptRspToSeniorPromptRsp);
        return new RestResp<>(importEntityBeans);
    }
}
