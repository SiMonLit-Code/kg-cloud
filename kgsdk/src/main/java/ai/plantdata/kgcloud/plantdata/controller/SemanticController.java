package ai.plantdata.kgcloud.plantdata.controller;


import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.web.util.ConvertUtils;
import ai.plantdata.kgcloud.plantdata.converter.nlp.NlpConverter;
import ai.plantdata.kgcloud.plantdata.converter.nlp.NlpConverter2;
import ai.plantdata.kgcloud.plantdata.req.data.EntityFileRelationParameter;
import ai.plantdata.kgcloud.plantdata.req.nlp.AnnotationParameter;
import ai.plantdata.kgcloud.plantdata.req.semantic.GremlinParameter;
import ai.plantdata.kgcloud.plantdata.req.semantic.QaKbqaParameter;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.hiekn.pddocument.bean.PdDocument;
import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.plantdata.converter.semantic.GremlinConverter;
import ai.plantdata.kgcloud.plantdata.converter.semantic.QaConverter;
import ai.plantdata.kgcloud.sdk.EntityFileClient;
import ai.plantdata.kgcloud.sdk.NlpClient;
import ai.plantdata.kgcloud.sdk.SemanticClient;
import ai.plantdata.kgcloud.sdk.req.EntityFileRelationAddReq;
import ai.plantdata.kgcloud.sdk.req.app.nlp.EntityLinkingReq;
import ai.plantdata.kgcloud.sdk.req.app.sematic.GremlinReq;
import ai.plantdata.kgcloud.sdk.req.app.sematic.QueryReq;
import ai.plantdata.kgcloud.sdk.rsp.app.GremlinRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.nlp.TaggingItemRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.semantic.QaAnswerDataRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.EntityFileRelationRsp;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("sdk/semantic")
public class SemanticController implements SdkOldApiInterface {

    @Autowired
    private SemanticClient semanticClient;
    @Autowired
    public NlpClient nlpClient;

    @Autowired
    private EntityFileClient entityFileClient;

    @ApiOperation("意图图谱生成")
    @GetMapping("kbqa/init")
    public RestResp kbqaInit(@ApiParam(required = true) @RequestParam("kgName") String kgName) {
        semanticClient.create(kgName);
        return new RestResp();
    }


    @ApiOperation("知识图谱问答")
    @PostMapping("kbqa")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "query", required = true, dataType = "string", paramType = "form", value = "查询问题"),
            @ApiImplicitParam(name = "pageModel", dataType = "string", paramType = "form", value = "pageModel")
    })
    public RestResp<QaAnswerDataRsp> qaKbqa(@Valid @ApiIgnore QaKbqaParameter param) {

        Function<QueryReq, ApiReturn<QaAnswerDataRsp>> returnFunction = a -> semanticClient.qaKbQa(param.getKgName(), a);
        Optional<QaAnswerDataRsp> dataRsp = returnFunction
                .compose(QaConverter::qaKbqaParameterToQueryReq)
                .andThen(BasicConverter::apiReturnData)
                .apply(param);
        return new RestResp<>(dataRsp.orElse(new QaAnswerDataRsp()));
    }

    @ApiOperation("gremlin查询")
    @PostMapping("gremlin")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "gremlin", required = true, dataType = "string", paramType = "form", value = "gremlin查询语句")
    })
    public RestResp<GremlinRsp> qaGremlin(@Valid @ApiIgnore GremlinParameter param) {
        Function<GremlinReq, ApiReturn<GremlinRsp>> returnFunction = a -> semanticClient.gremlinQuery(param.getKgName(), a);
        Optional<GremlinRsp> dataRsp = returnFunction
                .compose(GremlinConverter::gremlinParameterToQueryReq)
                .andThen(BasicConverter::apiReturnData)
                .apply(param);
        return new RestResp<>(dataRsp.orElse(new GremlinRsp()));
    }


    @ApiOperation("文本语义标注")
    @PostMapping("annotation")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", dataType = "string", paramType = "query", value = "kgName"),
            @ApiImplicitParam(name = "text", dataType = "string", paramType = "form", value = "待标注文本"),
            @ApiImplicitParam(name = "conceptIds", dataType = "string", paramType = "form", value = "标注范围，格式为json数组格式的概念列表"),
    })
    public RestResp<PdDocument> annotation(@Valid @ApiIgnore AnnotationParameter param) {
        Function<EntityLinkingReq, ApiReturn<List<TaggingItemRsp>>> returnFunction = a -> nlpClient.tagging(param.getKgName(), a);
        Optional<List<TaggingItemRsp>> opt = returnFunction
                .compose(NlpConverter::annotationParameterToEntityLinkingReq)
                .andThen(BasicConverter::apiReturnData)
                .apply(param);
        PdDocument document = NlpConverter2.annotationToPdDocument(opt.orElse(Collections.emptyList()));
        return new RestResp<>(document);
    }

    @PostMapping("entity/file/add")
    @ApiOperation("实体文件管理-建立标引关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "indexType", required = true, dataType = "int", paramType = "form", value = "标引类型(0：文件标引，1：文本标引，2：链接标引)"),
            @ApiImplicitParam(name = "entityIds", required = true, dataType = "string", paramType = "form", value = "实体id列表"),
            @ApiImplicitParam(name = "fileId", dataType = "string", paramType = "form", value = "文件id(文件标引必需)"),
            @ApiImplicitParam(name = "title", dataType = "string", paramType = "form", value = "标题(文本、链接标引必需)"),
            @ApiImplicitParam(name = "keyword", dataType = "string", paramType = "form", value = "关键词"),
            @ApiImplicitParam(name = "description", dataType = "string", paramType = "form", value = "简介(文本标引必需)"),
            @ApiImplicitParam(name = "url", dataType = "string", paramType = "form", value = "链接(链接标引必需)"),
    })
    public RestResp<EntityFileRelationRsp> add(@Valid @ApiIgnore EntityFileRelationParameter param) {
        EntityFileRelationAddReq req = ConvertUtils.convert(EntityFileRelationAddReq.class).apply(param);
        ApiReturn<EntityFileRelationRsp> apiReturn = entityFileClient.add(param.getKgName(), req);
        if (apiReturn.getErrCode() == 200) {
            return new RestResp<>(apiReturn.getData());
        } else {
            return new RestResp<>(apiReturn.getErrCode(), apiReturn.getMessage());
        }
    }
}
