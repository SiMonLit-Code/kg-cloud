package com.plantdata.kgcloud.plantdata.controller;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.hiekn.parser.util.JsonUtils;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.converter.rule.GraphAlgorithmConverter;
import com.plantdata.kgcloud.plantdata.req.rule.BusinessGraphBean;
import com.plantdata.kgcloud.plantdata.req.rule.GraphBusinessAlgorithmBean;
import com.plantdata.kgcloud.plantdata.req.rule.GraphBusinessAlgorithmDelete;
import com.plantdata.kgcloud.plantdata.req.rule.GraphBusinessAlgorithmRequestAdd;
import com.plantdata.kgcloud.plantdata.req.rule.GraphBusinessAlgorithmRequestTest;
import com.plantdata.kgcloud.plantdata.req.rule.GraphBusinessAlgorithmRequestUpdate;
import com.plantdata.kgcloud.plantdata.req.rule.ListByPageParameter;
import com.plantdata.kgcloud.sdk.KgmsClient;
import com.plantdata.kgcloud.sdk.rsp.GraphConfAlgorithmRsp;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import com.plantdata.kgcloud.util.HttpUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Administrator
 */
@RestController("graphBusinessAlgorithmController-v2")
@RequestMapping("sdk/graph/business/algorithm")
public class GraphBusinessAlgorithmController implements SdkOldApiInterface {
    @Autowired
    private KgmsClient kgmsClient;

    @GetMapping("get/list")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "pageNo", defaultValue = "1", dataType = "int", paramType = "query", value = "分页页码最小值为1"),
            @ApiImplicitParam(name = "pageSize", defaultValue = "10", dataType = "int", paramType = "query", value = "分页每页最小为1"),
    })
    public RestResp<RestData<GraphBusinessAlgorithmBean>> listByPage(@Valid @ApiIgnore ListByPageParameter param) {
        ApiReturn<BasePage<GraphConfAlgorithmRsp>> returnList = kgmsClient.select(param.getKgName(), param.getPageNo(), param.getPageSize());
        RestData<GraphBusinessAlgorithmBean> restData = BasicConverter.convert(returnList,
                a -> BasicConverter.basePageToRestData(a, GraphAlgorithmConverter::graphConfAlgorithmRspToGraphBusinessAlgorithmBean));
        return new RestResp<>(restData);
    }


    @GetMapping("get")
    @ApiOperation("详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "query"),
    })
    public RestResp<GraphBusinessAlgorithmBean> get(@RequestParam(value = "kgName", required = false) @NotBlank String kgName,
                                                    @RequestParam @NotNull Integer id) {
        //todo
        return new RestResp<>();
    }

    @PostMapping("add")
    @ApiOperation("新增")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "bean", required = true, dataType = "string", paramType = "form", value = "保存的数据"),
    })
    public RestResp<GraphBusinessAlgorithmBean> add(@Valid @ApiIgnore GraphBusinessAlgorithmRequestAdd graphBusinessAlgorithmRequestAdd) {
        //todo
        return new RestResp<>();
    }

    @PostMapping("delete")
    @ApiOperation("删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "form", value = "id"),
    })
    public RestResp delete(@Valid @ApiIgnore GraphBusinessAlgorithmDelete graphBusinessAlgorithmDelete) {
        //todo
        return new RestResp<>();
    }

    @PostMapping("update")
    @ApiOperation("修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "form", value = "id"),
            @ApiImplicitParam(name = "bean", required = true, dataType = "string", paramType = "form", value = "需要修改的数据"),
    })
    public RestResp<GraphBusinessAlgorithmBean> update(@Valid @ApiIgnore GraphBusinessAlgorithmRequestUpdate graphBusinessAlgorithmRequestUpdate) {
        HttpUtils.isHttp(graphBusinessAlgorithmRequestUpdate.getBean().getUrl());
        //todo
        return new RestResp<>();
    }


    @PostMapping("test")
    @ApiOperation("业务算法测试")
    public RestResp<BusinessGraphBean> test(@Valid @ApiIgnore GraphBusinessAlgorithmRequestTest graphBusinessAlgorithmRequestTest) {
        BusinessGraphBean beans = JsonUtils.fromJson(graphBusinessAlgorithmRequestTest.getGraphBean(), BusinessGraphBean.class);
        beans.getEntityList().forEach(s -> s.setName("test"));
        beans.setMessage("这是一个测试message的<span style='color: #f00;'>字段</span>");
        return new RestResp<>(beans);
    }

}