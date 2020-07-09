package com.plantdata.kgcloud.plantdata.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.bean.BasePage;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.hiekn.parser.util.JsonUtils;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.converter.rule.GraphAlgorithmConverter;
import com.plantdata.kgcloud.plantdata.req.rule.BusinessGraphBean;
import com.plantdata.kgcloud.plantdata.req.rule.GraphBusinessAlgorithmAdd;
import com.plantdata.kgcloud.plantdata.req.rule.GraphBusinessAlgorithmBean;
import com.plantdata.kgcloud.plantdata.req.rule.GraphBusinessAlgorithmRequestTest;
import com.plantdata.kgcloud.plantdata.req.rule.GraphBusinessAlgorithmUpdate;
import com.plantdata.kgcloud.plantdata.req.rule.ListByPageParameter;
import com.plantdata.kgcloud.sdk.KgmsClient;
import com.plantdata.kgcloud.sdk.req.GraphConfAlgorithmReq;
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
import javax.validation.constraints.NotNull;
import java.util.function.Function;

/**
 * @author Administrator
 */
@RestController("graphBusinessAlgorithmController-v2")
@RequestMapping("sdk/graph/business/algorithm")
public class GraphAlgorithmConfigController implements SdkOldApiInterface {
    @Autowired
    private KgmsClient kgmsClient;

    @GetMapping("get/list")
    @ApiOperation("算法配置-分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "type", defaultValue = "1", dataType = "int", paramType = "query", value = "算法类型 1面板类 2统计类"),
            @ApiImplicitParam(name = "pageNo", defaultValue = "1", dataType = "int", paramType = "query", value = "分页页码最小值为1"),
            @ApiImplicitParam(name = "pageSize", defaultValue = "10", dataType = "int", paramType = "query", value = "分页每页最小为1"),
    })
    public RestResp<RestData<GraphBusinessAlgorithmBean>> listByPage(@Valid @ApiIgnore ListByPageParameter param) {
        ApiReturn<BasePage<GraphConfAlgorithmRsp>> returnList = kgmsClient.select(param.getKgName(),param.getType(), param.getPageNo(), param.getPageSize());
        RestData<GraphBusinessAlgorithmBean> restData = BasicConverter.convert(returnList,
                a -> BasicConverter.basePageToRestData(a, GraphAlgorithmConverter::graphConfAlgorithmRspToGraphBusinessAlgorithmBean));
        return new RestResp<>(restData);
    }


    @GetMapping("get")
    @ApiOperation("算法配置-详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "query"),
    })
    public RestResp<GraphBusinessAlgorithmBean> get(@RequestParam(value = "kgName", required = false) String kgName,
                                                    @RequestParam @NotNull Long id) {
        ApiReturn<GraphConfAlgorithmRsp> apiReturn = kgmsClient.detailAlgorithm(id);
        GraphBusinessAlgorithmBean algorithmBean = BasicConverter.convert(apiReturn, GraphAlgorithmConverter::graphConfAlgorithmRspToGraphBusinessAlgorithmBean);
        return new RestResp<>(algorithmBean);
    }

    @PostMapping("add")
    @ApiOperation("算法配置-新增")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "bean", required = true, dataType = "string", paramType = "form", value = "保存的数据"),
    })
    public RestResp<GraphBusinessAlgorithmBean> add(@Valid @ApiIgnore GraphBusinessAlgorithmAdd requestAdd) {
        Function<GraphConfAlgorithmReq, ApiReturn<GraphConfAlgorithmRsp>> returnFunction = a -> kgmsClient.save(requestAdd.getKgName(), a);
        GraphBusinessAlgorithmBean algorithmBean = returnFunction
                .compose(GraphAlgorithmConverter::graphBusinessAlgorithmAddToGraphConfAlgorithmReq)
                .andThen(a -> BasicConverter.convert(a, GraphAlgorithmConverter::graphConfAlgorithmRspToGraphBusinessAlgorithmBean))
                .apply(requestAdd.getBean());
        return new RestResp<>(algorithmBean);
    }

    @PostMapping("delete")
    @ApiOperation("算法配置-删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "form", value = "id"),
    })
    public RestResp delete(@RequestParam(value = "kgName", required = false) String kgName, @RequestParam("id") Long id) {
        kgmsClient.delete(id);
        return new RestResp<>();
    }

    @PostMapping("update")
    @ApiOperation("算法配置-修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "form", value = "id"),
            @ApiImplicitParam(name = "bean", required = true, dataType = "string", paramType = "form", value = "需要修改的数据"),
    })
    public RestResp<GraphBusinessAlgorithmBean> update(@Valid @ApiIgnore GraphBusinessAlgorithmUpdate algorithmUpdate) {
        HttpUtils.isHttp(algorithmUpdate.getBean().getUrl());
        Function<GraphConfAlgorithmReq, ApiReturn<GraphConfAlgorithmRsp>> returnFunction = a -> kgmsClient.update(algorithmUpdate.getId(), a);
        returnFunction.compose(GraphAlgorithmConverter::graphBusinessAlgorithmAddToGraphConfAlgorithmReq)
                .andThen(a -> BasicConverter.convert(a, GraphAlgorithmConverter::graphConfAlgorithmRspToGraphBusinessAlgorithmBean))
                .apply(algorithmUpdate.getBean());
        return new RestResp<>();
    }


}