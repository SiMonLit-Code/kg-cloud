package com.plantdata.kgcloud.domain.app.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.config.CurrentUser;
import com.plantdata.kgcloud.constant.SdkErrorCodeEnum;
import com.plantdata.kgcloud.domain.common.module.GraphApplicationInterface;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.KgmsClient;
import com.plantdata.kgcloud.sdk.req.app.KnowledgeRecommendReq;
import com.plantdata.kgcloud.sdk.req.app.ObjectAttributeRsp;
import com.plantdata.kgcloud.sdk.req.app.dataset.PageReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.app.PageRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.ApkRsp;
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

    @ApiOperation("知识推荐")
    @PostMapping("recommend/knowledge/{kgName}")
    public ApiReturn<List<ObjectAttributeRsp>> knowledgeRecommend(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                  @RequestBody @Valid KnowledgeRecommendReq recommendParam) {
        return appClient.knowledgeRecommend(kgName, recommendParam);
    }

    @ApiOperation("获取当前图实体类型及属性类型的schema")
    @GetMapping("schema/{kgName}")
    public ApiReturn<SchemaRsp> querySchema(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName) {
        return appClient.querySchema(kgName);
    }

    @ApiOperation("获取所有图谱名称")
    @GetMapping("kgName/all")
    public ApiReturn<PageRsp<ApkRsp>> getKgName(PageReq pageReq) {

        if (!CurrentUser.isAdmin()) {
            throw BizException.of(SdkErrorCodeEnum.APK_NOT_IS_ADMIN);
        }
        return appClient.getKgName(pageReq.getPage(), pageReq.getSize());
    }

    @ApiOperation("获取模型可视化数据")
    @GetMapping("visualModels/{kgName}/{conceptId}")
    public ApiReturn visualized(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                @ApiParam(value = "概念id", required = true) @PathVariable("conceptId") long conceptId,
                                @ApiParam(value = "是否显示对象属性", defaultValue = "false") @RequestParam("display") boolean display) {
        return ApiReturn.success(appClient.visualModels(kgName, conceptId, display));
    }

    @ApiOperation("批量读取知识卡片")
    @PostMapping("infoBox/list/{kgName}")
    public ApiReturn<List<InfoBoxRsp>> listInfoBox(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                   @RequestBody BatchInfoBoxReq batchInfoBoxReq) {
        return appClient.listInfoBox(kgName, batchInfoBoxReq);
    }

    @ApiOperation("读取知识卡片")
    @PostMapping("infoBox/{kgName}")
    public ApiReturn<InfoBoxRsp> infoBox(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                         @RequestBody InfoBoxReq infoBoxReq) {
        return appClient.infoBox(kgName, infoBoxReq);
    }

    @ApiOperation("spa分享")
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
        return kgmsClient.shareSpaStatus(kgName, spaId,token);
    }

}
