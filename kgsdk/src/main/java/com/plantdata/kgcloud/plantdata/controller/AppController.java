package com.plantdata.kgcloud.plantdata.controller;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.config.CurrentUser;
import com.plantdata.kgcloud.constant.SdkErrorCodeEnum;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.plantdata.bean.AttrPromtRemoteBean;
import com.plantdata.kgcloud.plantdata.converter.app.AppConverter;
import com.plantdata.kgcloud.plantdata.converter.app.InfoBoxConverter;
import com.plantdata.kgcloud.plantdata.converter.app.PromptConverter;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.converter.common.ConceptConverter;
import com.plantdata.kgcloud.plantdata.converter.common.SchemaBasicConverter;
import com.plantdata.kgcloud.plantdata.converter.graph.GraphInitBasicConverter;
import com.plantdata.kgcloud.plantdata.req.app.AssociationParameter;
import com.plantdata.kgcloud.plantdata.req.app.AttrPromptParameter;
import com.plantdata.kgcloud.plantdata.req.app.InfoBoxParameter;
import com.plantdata.kgcloud.plantdata.req.app.InfoBoxParameterMore;
import com.plantdata.kgcloud.plantdata.req.app.KgNameByApkParameter;
import com.plantdata.kgcloud.plantdata.req.app.ModelStatParameter;
import com.plantdata.kgcloud.plantdata.req.app.PromptParameter;
import com.plantdata.kgcloud.plantdata.req.app.SeniorPromptParameter;
import com.plantdata.kgcloud.plantdata.req.common.KVBean;
import com.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import com.plantdata.kgcloud.plantdata.req.entity.EntityProfileBean;
import com.plantdata.kgcloud.plantdata.req.entity.ImportEntityBean;
import com.plantdata.kgcloud.plantdata.rsp.app.ApkBean;
import com.plantdata.kgcloud.plantdata.rsp.app.InitGraphBean;
import com.plantdata.kgcloud.plantdata.rsp.app.TreeItemVo;
import com.plantdata.kgcloud.plantdata.rsp.schema.SchemaBean;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import com.plantdata.kgcloud.sdk.req.app.GraphInitRsp;
import com.plantdata.kgcloud.sdk.req.app.KnowledgeRecommendReq;
import com.plantdata.kgcloud.sdk.req.app.ObjectAttributeRsp;
import com.plantdata.kgcloud.sdk.req.app.PromptReq;
import com.plantdata.kgcloud.sdk.req.app.SeniorPromptReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.app.EdgeAttributeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.PageRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.ApkRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.BasicConceptTreeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.function.Function;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/12 11:29
 */
@RestController("appController-v2")
@RequestMapping("sdk/app")
public class AppController implements SdkOldApiInterface {

    @Autowired
    private AppClient appClient;
    @Autowired
    private HttpServletRequest request;

    @ApiOperation("获取当前图实体类型及属性类型的schema")
    @GetMapping("schema")
    public RestResp<SchemaBean> schema(@RequestParam("kgName") String kgName) {
        Function<String, ApiReturn<SchemaRsp>> returnFunction = appClient::querySchema;
        SchemaBean schemaBean = returnFunction
                .andThen(a -> BasicConverter.convert(a, SchemaBasicConverter::schemaRspToSchemaBean))
                .apply(kgName);
        return new RestResp<>(schemaBean);
    }

    @ApiOperation("图探索的初始化")
    @PostMapping("graph/default")
    @ApiParam(name = "kgName", required = true, type = "String", value = "图谱名称")
    public RestResp<InitGraphBean> graphInit(@RequestParam("kgName") String kgName, @RequestParam("type") String type) {
        Function<String, ApiReturn<GraphInitRsp>> returnFunction = a -> appClient.initGraphExploration(kgName, a);
        InitGraphBean initGraphBean = returnFunction
                .andThen(a -> BasicConverter.convert(a, GraphInitBasicConverter::graphInitRspToInitGraphBean))
                .apply(type);
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
        Function<InfoBoxReq, ApiReturn<InfoBoxRsp>> returnFunction = a -> appClient.infoBox(infoBoxParameter.getKgName(), a);
        EntityProfileBean profileBean = returnFunction
                .compose(InfoBoxConverter::infoBoxParameterToInfoBoxReq)
                .andThen(a -> BasicConverter.convert(a, InfoBoxConverter::infoBoxRspToEntityProfileBean))
                .apply(infoBoxParameter);
        return new RestResp<>(profileBean);
    }

    @ApiOperation("批量读取知识卡片")
    @PostMapping("infobox/more")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "ids", required = true, dataType = "string", paramType = "form", value = "实体id列表"),
            @ApiImplicitParam(name = "isRelationAtts", dataType = "boolean", defaultValue = "true", paramType = "form", value = "是否读取对象属性,默认true"),
            @ApiImplicitParam(name = "allowAtts", dataType = "string", paramType = "form", value = "查询指定的属性，格式为json数组格式，默认为读取全部"),
            @ApiImplicitParam(name = "allowAttsKey", dataType = "string", paramType = "form", value = "allowAtts为空时生效"),
    })
    public RestResp<List<EntityProfileBean>> infoBoxMore(@Valid @ApiIgnore InfoBoxParameterMore param) {
        Function<BatchInfoBoxReq, ApiReturn<List<InfoBoxRsp>>> returnFunction = a -> appClient.listInfoBox(param.getKgName(), a);
        List<EntityProfileBean> beanList = returnFunction
                .compose(InfoBoxConverter::infoBoxParameterMoreToBatchInfoBoxReq)
                .andThen(a -> BasicConverter.convert(a, b -> BasicConverter.listToRsp(b, InfoBoxConverter::infoBoxRspToEntityProfileBean)))
                .apply(param);
        return new RestResp<>(beanList);
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

        Function<PromptReq, ApiReturn<List<PromptEntityRsp>>> returnFunction = a -> appClient.prompt(promptParameter.getKgName(), a);

        List<EntityBean> entityBeans = returnFunction
                .compose(PromptConverter::promptParameterToPromptReq)
                .andThen(a -> BasicConverter.convertList(a, PromptConverter::promptEntityRspToEntityBean))
                .apply(promptParameter);

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

        Function<SeniorPromptReq, ApiReturn<List<SeniorPromptRsp>>> returnFunction = a -> appClient.seniorPrompt(seniorPromptParameter.getKgName(), a);

        List<ImportEntityBean> entityBeans = returnFunction
                .compose(PromptConverter::seniorPromptParameterToSeniorPromptReq)
                .andThen(a -> BasicConverter.convertList(a, PromptConverter::seniorPromptRspToSeniorPromptRsp))
                .apply(seniorPromptParameter);

        return new RestResp<>(entityBeans);
    }

    @ApiOperation("边属性搜索")
    @PostMapping("attr/prompt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "kw", dataType = "string", paramType = "form", value = "开始时间，格式yyyy-MM-dd"),
            @ApiImplicitParam(name = "attrId", dataType = "int", paramType = "form", value = "属性id"),
            @ApiImplicitParam(name = "attrKey", dataType = "string", paramType = "form", value = "attrId为空是生效"),
            @ApiImplicitParam(name = "seqNo", dataType = "int", paramType = "form", value = "边属性id,为保留字段时3.权重，11.来源，12.置信度，13.批次号，15，自定义名称。"),
            @ApiImplicitParam(name = "isReserved", dataType = "int", paramType = "form", value = "是否为保留字段：1是，0不是"),
            @ApiImplicitParam(name = "dataType", defaultValue = "2", dataType = "int", paramType = "form", value = "查询类型,1.数值属性,2.边数值属性，默认为2"),
            @ApiImplicitParam(name = "searchOption", dataType = "string", paramType = "form", value = "数值属性和日期,大小筛选,kw为空时生效,实例{\"$gt\":\"大于\",\"$lt\":\"小于\"}"),
            @ApiImplicitParam(name = "sort", dataType = "int", paramType = "form", value = "按权重排序:-1=desc 1=asc 0= 不排序,默认0"),
            @ApiImplicitParam(name = "pageNo", dataType = "int", paramType = "query", value = "当前页，默认1"),
            @ApiImplicitParam(name = "pageSize", dataType = "int", paramType = "query", value = "每页数，默认10"),
    })
    public RestResp<List<AttrPromtRemoteBean>> attrPrompt(@Valid @ApiIgnore AttrPromptParameter param) {
        Function<EdgeAttrPromptReq, ApiReturn<List<EdgeAttributeRsp>>> returnFunction = a -> appClient.attrPrompt(param.getKgName(), a);
        Function<List<EdgeAttributeRsp>, List<AttrPromtRemoteBean>> rspFunction = a -> BasicConverter.listToRsp(a, PromptConverter::edgeAttributeRspToAttrPromtRemoteBean);
        List<AttrPromtRemoteBean> remoteBeanList = returnFunction
                .compose(PromptConverter::attrPromptParameterToEdgeAttrPromptReq)
                .andThen(a -> BasicConverter.convert(a, rspFunction))
                .apply(param);
        return new RestResp<>(remoteBeanList);
    }


    @ApiOperation(value = "获取模型可视化数据")
    @PostMapping("/model/stat")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "conceptId", required = true, dataType = "long", paramType = "form", value = "概念id"),
            @ApiImplicitParam(name = "isDisplay", dataType = "boolean", defaultValue = "false", paramType = "form"),
    })
    public RestResp<TreeItemVo> modelStat(@Valid @ApiIgnore ModelStatParameter modelStatParameter) {
        ApiReturn<BasicConceptTreeRsp> rspApiReturn = appClient.visualModels(modelStatParameter.getKgName(), modelStatParameter.getConceptId(), modelStatParameter.getIsDisplay());
        TreeItemVo treeItemVo = BasicConverter.convert(rspApiReturn, ConceptConverter::basicConceptTreeRspToTreeItemVo);
        return new RestResp<>(treeItemVo);
    }

    @ApiOperation("知识推荐")
    @PostMapping("recommend/knowledge")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "entityId", required = true, dataType = "long", paramType = "form", value = "实体id"),
            @ApiImplicitParam(name = "direction", dataType = "int", paramType = "form", value = "关系方向。默认正向，0表示双向，1表示出发，2表示到达,默认0"),
            @ApiImplicitParam(name = "allowAtts", dataType = "string", paramType = "form", value = "推荐范围，格式为json数组的属性定义id,必须指定范围"),
            @ApiImplicitParam(name = "allowAttsKey", dataType = "string", paramType = "form", value = "allowAtts 为空时生效"),
            @ApiImplicitParam(name = "pageSize", dataType = "int", paramType = "form"),
    })
    public RestResp<List<KVBean<String, List<EntityBean>>>> association(@Valid @ApiIgnore AssociationParameter param) {
        Function<KnowledgeRecommendReq, ApiReturn<List<ObjectAttributeRsp>>> returnFunction = a -> appClient.knowledgeRecommend(param.getKgName(), a);
        List<KVBean<String, List<EntityBean>>> kvBeanList = returnFunction
                .compose(AppConverter::associationParameterToKnowledgeRecommendReq)
                .andThen(a -> BasicConverter.convert(a, b -> BasicConverter.listToRsp(b, AppConverter::infoBoxAttrRspToKvBean)))
                .apply(param);
        return new RestResp<>(kvBeanList);
    }


    @ApiOperation(value = "获取所有kgName和图谱名称和apk")
    @PostMapping("/get/kgname")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", defaultValue = "1", dataType = "string", paramType = "query", value = "页数"),
            @ApiImplicitParam(name = "size", defaultValue = "10", dataType = "int", paramType = "query", value = "大小,默认10,最大50"),
    })
    public RestResp<List<ApkBean>> getKgNameByApk(@Valid @ApiIgnore KgNameByApkParameter apkParam) {
        if (!CurrentUser.isAdmin()) {
            throw BizException.of(SdkErrorCodeEnum.APK_NOT_IS_ADMIN);
        }
        ApiReturn<PageRsp<ApkRsp>> statDetail = appClient.getKgName(apkParam.getPage(), apkParam.getSize());
        List<ApkBean> apkBeanList = BasicConverter.convert(statDetail, a -> BasicConverter.listToRsp(a.getData(), AppConverter::apkRspToApkBean));
        return new RestResp<>(apkBeanList);
    }
}
