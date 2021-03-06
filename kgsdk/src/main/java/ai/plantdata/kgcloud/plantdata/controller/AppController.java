package ai.plantdata.kgcloud.plantdata.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.kgcloud.config.CurrentUser;
import ai.plantdata.kgcloud.plantdata.bean.AttrPromtRemoteBean;
import ai.plantdata.kgcloud.plantdata.converter.app.AppConverter;
import ai.plantdata.kgcloud.plantdata.converter.app.PromptConverter;
import ai.plantdata.kgcloud.plantdata.converter.graph.GraphInitBasicConverter;
import ai.plantdata.kgcloud.plantdata.req.app.*;
import ai.plantdata.kgcloud.plantdata.req.common.KVBean;
import ai.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import ai.plantdata.kgcloud.plantdata.req.entity.EntityMultiModalBean;
import ai.plantdata.kgcloud.plantdata.req.entity.EntityProfileBean;
import ai.plantdata.kgcloud.plantdata.req.entity.ImportEntityBean;
import ai.plantdata.kgcloud.plantdata.rsp.app.ApkBean;
import ai.plantdata.kgcloud.plantdata.rsp.app.InitGraphBean;
import ai.plantdata.kgcloud.plantdata.rsp.app.TreeItemVo;
import ai.plantdata.kgcloud.plantdata.rsp.schema.SchemaBean;
import ai.plantdata.kgcloud.sdk.req.app.*;
import ai.plantdata.kgcloud.sdk.rsp.app.main.*;
import cn.hiboot.mcn.core.model.result.RestResp;
import ai.plantdata.kgcloud.constant.SdkErrorCodeEnum;
import ai.plantdata.kgcloud.plantdata.converter.app.InfoBoxConverter;
import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.plantdata.converter.common.ConceptConverter;
import ai.plantdata.kgcloud.plantdata.converter.common.SchemaBasicConverter;
import ai.plantdata.kgcloud.sdk.AppClient;
import ai.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReqList;
import ai.plantdata.kgcloud.sdk.req.app.infobox.BatchMultiModalReqList;
import ai.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import ai.plantdata.kgcloud.sdk.req.app.infobox.InfoboxMultiModalReq;
import ai.plantdata.kgcloud.sdk.rsp.app.EdgeAttributeRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.PageRsp;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "type", required = true, dataType = "string", paramType = "form", value = "类型"),
    })
    public RestResp<InitGraphBean> graphInit(@RequestParam("kgName")String kgName, @RequestParam("type") String type) {
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
            @ApiImplicitParam(name = "id", dataType = "long", paramType = "form", value = "实体id"),
            @ApiImplicitParam(name = "kw", dataType = "string", paramType = "form", value = "实体名称"),
            @ApiImplicitParam(name = "isRelationAtts", dataType = "boolean", defaultValue = "false", paramType = "form", value = "是否读取对象属性,默认false"),
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
            @ApiImplicitParam(name = "ids", dataType = "string", paramType = "form", value = "实体id列表"),
            @ApiImplicitParam(name = "kws", dataType = "string", paramType = "form", value = "实体名称列表"),
            @ApiImplicitParam(name = "isRelationAtts", dataType = "boolean", defaultValue = "false", paramType = "form", value = "是否读取对象属性,默认false"),
            @ApiImplicitParam(name = "allowAtts", dataType = "string", paramType = "form", value = "查询指定的属性，格式为json数组格式，默认为读取全部"),
            @ApiImplicitParam(name = "allowAttsKey", dataType = "string", paramType = "form", value = "allowAtts为空时生效"),
    })
    public RestResp<List<EntityProfileBean>> infoBoxMore(@Valid @ApiIgnore InfoBoxParameterMore param) {
        Function<BatchInfoBoxReqList, ApiReturn<List<InfoBoxRsp>>> returnFunction = a -> appClient.listInfoBox(param.getKgName(), a);
        List<EntityProfileBean> beanList = returnFunction
                .compose(InfoBoxConverter::infoBoxParameterMoreToBatchInfoBoxReq)
                .andThen(a -> BasicConverter.convert(a, b -> BasicConverter.toListNoNull(b, InfoBoxConverter::infoBoxRspToEntityProfileBean)))
                .apply(param);
        return new RestResp<>(beanList);
    }


    @ApiOperation("读取知识卡片多模态文件")
    @PostMapping("infobox/multi/modal")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", dataType = "long", paramType = "form", value = "实体id"),
            @ApiImplicitParam(name = "kw", dataType = "string", paramType = "form", value = "实体名称"),
    })
    public RestResp<EntityMultiModalBean> infoBoxMultiModal(@Valid @ApiIgnore InfoBoxMultiModalParameter infoBoxMultiModalParameter) {
        Function<InfoboxMultiModalReq, ApiReturn<InfoboxMultiModelRsp>> returnFunction = a -> appClient.infoBoxMultiModal(infoBoxMultiModalParameter.getKgName(), a);
        EntityMultiModalBean entityMultiModalBean = returnFunction
                .compose(InfoBoxConverter::infoBoxMultiModalParameterToInfoBoxReq)
                .andThen(a -> BasicConverter.convert(a, InfoBoxConverter::infoboxMultiModelRspToEntityMultiModalBean))
                .apply(infoBoxMultiModalParameter);
        return new RestResp<>(entityMultiModalBean);
    }

    @ApiOperation("批量读取知识卡片多模态文件")
    @PostMapping("infobox/multi/modal/more")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "ids", dataType = "string", paramType = "form", value = "实体id列表"),
            @ApiImplicitParam(name = "kws", dataType = "string", paramType = "form", value = "实体名称列表"),
    })
    public RestResp<List<EntityMultiModalBean>> infoBoxMultiModalMore(@Valid @ApiIgnore InfoBoxMultiModalParameterMore param) {
        Function<BatchMultiModalReqList, ApiReturn<List<InfoboxMultiModelRsp>>> returnFunction = a -> appClient.listInfoBoxMultiModal(param.getKgName(), a);
        List<EntityMultiModalBean> beanList = returnFunction
                .compose(InfoBoxConverter::infoBoxMultiModalMoreToBatchMultiModalReq)
                .andThen(a -> BasicConverter.convert(a, b -> BasicConverter.toListNoNull(b, InfoBoxConverter::infoboxMultiModelRspToEntityMultiModalBean)))
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
            @ApiImplicitParam(name = "isFuzzy", defaultValue = "true", dataType = "boolean", paramType = "query", value = "是否模糊搜索"),
            @ApiImplicitParam(name = "openExportDate", defaultValue = "true", dataType = "boolean", paramType = "query", value = "是否使用导出实体数据集检索"),
            @ApiImplicitParam(name = "sort", dataType = "Integer", defaultValue = "-1", paramType = "query", value = "按权重排序:-1=desc 1=asc,默认-1"),
            @ApiImplicitParam(name = "pageNo", defaultValue = "1", dataType = "int", paramType = "query", value = "分页页码最小值为1"),
            @ApiImplicitParam(name = "pageSize", defaultValue = "10", dataType = "int", paramType = "query", value = "分页每页最小为1"),
            @ApiImplicitParam(name = "promptType", defaultValue = "0", dataType = "int", paramType = "query", value = "提示类型，0:prompt,1:qa,2:all"),
            @ApiImplicitParam(name = "isReturnTop", defaultValue = "true", dataType = "boolean", paramType = "query", value = "是否返回顶层概念"),
    })
    public RestResp<List<EntityBean>> prompt(@Valid @ApiIgnore PromptParameter promptParameter) {

        Function<PromptReq, ApiReturn<List<PromptEntityRsp>>> returnFunction = a -> appClient.prompt(promptParameter.getKgName(), a);

        List<EntityBean> entityBeans = returnFunction
                .compose(PromptConverter::promptParameterToPromptReq)
                .andThen(a -> BasicConverter.convertList(a, PromptConverter::promptEntityRspToEntityBean))
                .apply(promptParameter);

        return new RestResp<>(entityBeans);
    }

    @ApiOperation("综合搜索")
    @PostMapping("graph/prompt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "kw", required = true, dataType = "string", paramType = "form", value = "搜索关键词"),
            @ApiImplicitParam(name = "type", defaultValue = "11", dataType = "string", paramType = "form", value = "类型，默认11。第一位表示概念，第二位表示实例。。1为有，0为没有"),
            @ApiImplicitParam(name = "allowTypes", dataType = "string", paramType = "form", value = "查询指定的概念，格式为json数组，默认为查询全部"),
            @ApiImplicitParam(name = "allowTypesKey", dataType = "string", paramType = "form", value = "allowTypes为空时此参数生效"),
            @ApiImplicitParam(name = "isInherit", defaultValue = "false", dataType = "boolean", paramType = "form", value = "allowTypes字段指定的概念是否继承"),
            @ApiImplicitParam(name = "isFuzzy", defaultValue = "false", dataType = "boolean", paramType = "form", value = "是否模糊搜索"),
            @ApiImplicitParam(name = "openExportDate", defaultValue = "true", dataType = "boolean", paramType = "form", value = "是否使用导出实体数据集检索"),
            @ApiImplicitParam(name = "sort", dataType = "Integer", defaultValue = "-1", paramType = "form", value = "按权重排序:-1=desc 1=asc,默认-1"),
            @ApiImplicitParam(name = "pageNo", defaultValue = "1", dataType = "int", paramType = "query", value = "分页页码最小值为1"),
            @ApiImplicitParam(name = "pageSize", defaultValue = "10", dataType = "int", paramType = "query", value = "分页每页最小为1"),
            @ApiImplicitParam(name = "isReturnTop", defaultValue = "true", dataType = "boolean", paramType = "query", value = "是否返回顶层概念"),
    })
    public RestResp<List<EntityBean>> promptPost(@Valid @ApiIgnore PromptParameter promptParameter) {
        return prompt(promptParameter);
    }

    @ApiOperation("高级搜索查实体,")
    @PostMapping("senior/prompt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "conceptId", dataType = "long", paramType = "form", value = "实体所属概念ID"),
            @ApiImplicitParam(name = "conceptKey", dataType = "string", paramType = "form", value = "conceptId为空时生效"),
            @ApiImplicitParam(name = "isFuzzy", dataType = "boolean", paramType = "form", value = "是否模糊搜索 false前缀搜索，true支持模糊搜索"),
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
        Function<List<EdgeAttributeRsp>, List<AttrPromtRemoteBean>> rspFunction = a -> BasicConverter.toListNoNull(a, PromptConverter::edgeAttributeRspToAttrPromtRemoteBean);
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
            @ApiImplicitParam(name = "isRangeDisplay", dataType = "boolean", defaultValue = "false", paramType = "form"),
    })
    public RestResp<TreeItemVo> modelStat(@Valid @ApiIgnore ModelStatParameter modelStatParameter) {
        ApiReturn<BasicConceptTreeRsp> rspApiReturn = appClient.visualModels(modelStatParameter.getKgName(), modelStatParameter.getConceptId(), modelStatParameter.getIsDisplay());
        TreeItemVo treeItemVo = ConceptConverter.basicConceptTreeRspToTreeItemVoWithRangeOption(rspApiReturn.getData(), modelStatParameter.getIsRangeDisplay());
        return new RestResp<>(treeItemVo);
    }

    @ApiOperation("知识推荐")
    @PostMapping("recommend/knowledge")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "entityId", dataType = "long", paramType = "form", value = "实体id"),
            @ApiImplicitParam(name = "kw", dataType = "string", paramType = "form", value = "实体名称"),
            @ApiImplicitParam(name = "direction", dataType = "int", paramType = "form", value = "关系方向。默认正向，0表示双向，1表示出发，2表示到达,默认0"),
            @ApiImplicitParam(name = "allowAtts", dataType = "string", paramType = "form", value = "推荐范围，格式为json数组的属性定义id,必须指定范围"),
            @ApiImplicitParam(name = "allowAttsKey", dataType = "string", paramType = "form", value = "allowAtts 为空时生效"),
            @ApiImplicitParam(name = "pageSize", dataType = "int", paramType = "form", value = "显示的数量"),
    })
    public RestResp<List<KVBean<String, List<EntityBean>>>> association(@Valid @ApiIgnore AssociationParameter param) {
        Function<KnowledgeRecommendReqList, ApiReturn<List<ObjectAttributeRsp>>> returnFunction = a -> appClient.knowledgeRecommend(param.getKgName(), a);
        List<KVBean<String, List<EntityBean>>> kvBeanList = returnFunction
                .compose(AppConverter::associationParameterToKnowledgeRecommendReq)
                .andThen(a -> BasicConverter.convert(a, b -> BasicConverter.toListNoNull(b, AppConverter::infoBoxAttrRspToKvBean)))
                .apply(param);
        return new RestResp<>(kvBeanList);
    }

    @ApiOperation("知识推荐（两层）")
    @PostMapping("layer/recommend/knowledge")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "entityId", dataType = "long", paramType = "form", value = "实体id"),
            @ApiImplicitParam(name = "kw", dataType = "string", paramType = "form", value = "实体名称"),
            @ApiImplicitParam(name = "layerFilter", dataType = "string", paramType = "form", value = "Map<Integer,Filter>，key为层数，filter为图探索过滤参数，可指定每一层的过滤条件"),
            @ApiImplicitParam(name = "pageNo", dataType = "int", paramType = "query", value = "当前页，默认1"),
            @ApiImplicitParam(name = "pageSize", dataType = "int", paramType = "query", value = "每页数，默认10"),
    })
    public RestResp<List<KVBean<String, List<EntityBean>>>> layerRecommendKnowledge(@Valid @ApiIgnore LayerAssociationParameter param) {
        Function<LayerKnowledgeRecommendReqList, ApiReturn<List<ObjectAttributeRsp>>> returnFunction = a -> appClient.layerKnowledgeRecommend(param.getKgName(), a);
        List<KVBean<String, List<EntityBean>>> kvBeanList = returnFunction
                .compose(AppConverter::layerAssociationParameterToKnowledgeRecommendReq)
                .andThen(a -> BasicConverter.convert(a, b -> BasicConverter.toListNoNull(b, AppConverter::infoBoxAttrRspToKvBean)))
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
        List<ApkBean> apkBeanList = BasicConverter.convert(statDetail, a -> BasicConverter.toListNoNull(a.getData(), AppConverter::apkRspToApkBean));
        return new RestResp<>(apkBeanList);
    }
}
