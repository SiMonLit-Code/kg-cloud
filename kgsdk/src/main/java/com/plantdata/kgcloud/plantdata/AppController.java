package com.plantdata.kgcloud.plantdata;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.plantdata.common.converter.GraphConverter;
import com.plantdata.kgcloud.plantdata.common.converter.SchemaConverter;
import com.plantdata.kgcloud.plantdata.common.converter.ConverterUtils;
import com.plantdata.kgcloud.plantdata.req.GraphInitReq;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

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
        SchemaBean schemaBean = ConverterUtils.convert(appClient.querySchema(kgName), SchemaConverter::schemaRspToSchemaBean);
        return new RestResp<>(schemaBean);
    }

    @ApiOperation("图探索的初始化")
    @PostMapping("graph/default")
    @ApiParam(name = "kgName", required = true, type = "String", value = "图谱名称")
    public RestResp graphInit(@RequestParam("type") String type) {
        InitGraphBean initGraphBean = ConverterUtils.convert(appClient.initGraphExploration(request.getParameter("kgName"), type), GraphConverter::graphInitRspToInitGraphBean);
        return new RestResp<>(initGraphBean);
    }
}
