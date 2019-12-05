package com.plantdata.kgcloud.domain.app.controller;

import com.plantdata.kgcloud.sdk.rsp.app.main.ApkRsp;
import com.plantdata.kgcloud.domain.common.module.GraphApplicationInterface;
import com.plantdata.kgcloud.sdk.req.app.KnowledgeRecommendReq;
import com.plantdata.kgcloud.sdk.req.app.ObjectAttributeRsp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.AppClient;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 14:27
 */
@RestController
@RequestMapping("app")
public class GraphApplicationController implements GraphApplicationInterface {

    @Autowired
    private HttpServletRequest request;
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
    public ApiReturn<List<ApkRsp>> getKgName() {
        return appClient.getKgName(request.getHeader("APK"));
    }

    @ApiOperation("获取模型可视化数据")
    @GetMapping("visualModels/{kgName}/{conceptId}")
    public ApiReturn visualized(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                @ApiParam(value = "概念id", required = true) @PathVariable("conceptId") long conceptId,
                                @ApiParam(value = "是否显示对象属性", defaultValue = "false") @RequestParam("display") boolean display) {
        return ApiReturn.success(appClient.visualModels(kgName, conceptId, display));
    }
}
