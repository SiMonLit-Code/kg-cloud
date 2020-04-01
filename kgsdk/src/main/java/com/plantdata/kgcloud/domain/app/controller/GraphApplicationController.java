package com.plantdata.kgcloud.domain.app.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.config.CurrentUser;
import com.plantdata.kgcloud.constant.SdkErrorCodeEnum;
import com.plantdata.kgcloud.domain.common.module.GraphApplicationInterface;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.plantdata.converter.common.ApiReturnConverter;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.EditClient;
import com.plantdata.kgcloud.sdk.KgmsClient;
import com.plantdata.kgcloud.sdk.req.app.KnowledgeRecommendReqList;
import com.plantdata.kgcloud.sdk.req.app.ObjectAttributeRsp;
import com.plantdata.kgcloud.sdk.req.app.dataset.PageReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReqList;
import com.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.app.PageRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.ApkRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.BasicConceptTreeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 14:27
 */
@RestController
@RequestMapping("v3/app")
public class GraphApplicationController implements GraphApplicationInterface {

    @Autowired
    private KgmsClient kgmsClient;
    @Autowired
    private AppClient appClient;

    @ApiOperation(value = "知识推荐", notes = "知识推荐，为实体进行特定多种关系的实体推荐。一般见于搜索引擎的右侧推荐区域。")
    @PostMapping("recommend/knowledge/{kgName}")
    public ApiReturn<List<ObjectAttributeRsp>> knowledgeRecommend(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                  @RequestBody @Valid KnowledgeRecommendReqList recommendParam) {
        return appClient.knowledgeRecommend(kgName, recommendParam);
    }

    @ApiOperation(value = "schema查询", notes = "获取当前图谱的建模（Schema）包含实体类型、属性类型。")
    @GetMapping("schema/{kgName}")
    public ApiReturn<SchemaRsp> querySchema(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName) {
        return appClient.querySchema(kgName);
    }

    @ApiOperation(value = "获取图谱名称", notes = "获取所用图谱的kgName、title、apk")
    @GetMapping("kgName/all")
    public ApiReturn<PageRsp<ApkRsp>> getKgName(PageReq pageReq) {

        if (!CurrentUser.isAdmin()) {
            throw BizException.of(SdkErrorCodeEnum.APK_NOT_IS_ADMIN);
        }
        return appClient.getKgName(pageReq.getPage(), pageReq.getSize());
    }

    @ApiOperation("获取模型可视化数据")
    @GetMapping("visualModels/{kgName}/{conceptId}")
    public ApiReturn<BasicConceptTreeRsp> visualized(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                     @ApiParam(value = "概念id", required = true) @PathVariable("conceptId") long conceptId,
                                                     @ApiParam(value = "是否显示对象属性", defaultValue = "false") @RequestParam("display") boolean display) {
        ApiReturn<BasicConceptTreeRsp> apiReturn = appClient.visualModels(kgName, conceptId, display);
        BasicConceptTreeRsp rsp = ApiReturnConverter.convert(apiReturn);
        return ApiReturn.success(rsp);
    }

    @ApiOperation(value = "批量读取知识卡片", notes = "批量获取知识卡片，实体数值属性及关系构成的相关信息。一般用于搜索引擎或百科的实体卡片。")
    @PostMapping("infoBox/list/{kgName}")
    public ApiReturn<List<InfoBoxRsp>> listInfoBox(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                   @RequestBody BatchInfoBoxReqList batchInfoBoxReq) {
        return appClient.listInfoBox(kgName, batchInfoBoxReq);
    }

    @ApiOperation(value = "读取知识卡片", notes = "知识卡片，实体数值属性及关系构成的相关信息。一般用于搜索引擎或百科的实体卡片。")
    @PostMapping("infoBox/{kgName}")
    public ApiReturn<InfoBoxRsp> infoBox(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                         @RequestBody InfoBoxReq infoBoxReq) {
        return appClient.infoBox(kgName, infoBoxReq);
    }

    @ApiOperation("spa分享-查看")
    @GetMapping("share/{kgName}/{spaId}")
    public ApiReturn share(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                           @ApiParam(value = "spaId", required = true) @PathVariable("spaId") String spaId) {
        return kgmsClient.shareStatus(kgName, spaId);
    }

    @ApiOperation("spa分享")
    @GetMapping("share")
    public ApiReturn shareSpaStatus(@ApiParam(value = "图谱名称", required = true) @RequestParam("kgName") String kgName,
                                    @ApiParam(value = "spaId", required = true) @RequestParam("spaId") String spaId,
                                    @ApiParam(value = "token") @RequestParam("token") String token) {
        return kgmsClient.shareSpaStatus(kgName, spaId, token);
    }

}
