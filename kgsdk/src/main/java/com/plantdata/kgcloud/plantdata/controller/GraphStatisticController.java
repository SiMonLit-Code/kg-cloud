package com.plantdata.kgcloud.plantdata.controller;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.converter.config.StatisticConverter;
import com.plantdata.kgcloud.plantdata.req.config.InitStatisticalBean;
import com.plantdata.kgcloud.plantdata.req.rule.InitStatisticalAddBatch;
import com.plantdata.kgcloud.plantdata.req.rule.InitStatisticalBeanAdd;
import com.plantdata.kgcloud.plantdata.req.rule.InitStatisticalBeanUpdate;
import com.plantdata.kgcloud.plantdata.req.rule.InitStatisticalDeleteBatch;
import com.plantdata.kgcloud.sdk.KgmsClient;
import com.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import com.plantdata.kgcloud.sdk.req.UpdateGraphConfStatisticalReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;
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
import java.util.List;
import java.util.function.Function;

/**
 * @author Administrator
 */
@RestController("graphStatisticController-v2")
@RequestMapping("sdk/graph/stat/settings")
public class GraphStatisticController implements SdkOldApiInterface {

    @Autowired
    private KgmsClient kgmsClient;

    @GetMapping("get/list")
    @ApiOperation("获取统计配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
    })
    public RestResp<List<InitStatisticalBean>> getByKgName(@RequestParam("kgName") String kgName) {
        ApiReturn<List<GraphConfStatisticalRsp>> apiReturn = kgmsClient.selectStatistical(kgName);
        List<InitStatisticalBean> beanList = StatisticConverter.rspToBean.apply(apiReturn);
        return new RestResp<>(beanList);
    }

    @PostMapping("add/batch")
    @ApiOperation("批量添加配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "rules", required = true, dataType = "string", paramType = "form", value = "保存的数据,json数组格式"),
    })
    public RestResp<List<InitStatisticalBean>> setBatch(@Valid @ApiIgnore InitStatisticalAddBatch addBatch) {
        Function<List<GraphConfStatisticalReq>, ApiReturn<List<GraphConfStatisticalRsp>>> returnFunction = a -> kgmsClient.saveStatisticalBatch(a);
        List<InitStatisticalBean> statisticalBeans = returnFunction
                .compose(StatisticConverter.addBeanToReq)
                .andThen(StatisticConverter.rspToBean)
                .apply(addBatch.getRules());
        return new RestResp<>(statisticalBeans);
    }

    @PostMapping("update/batch")
    @ApiOperation("批量修改配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "rules", required = true, dataType = "string", paramType = "form", value = "保存的数据,json数组格式"),
    })
    public RestResp<List<InitStatisticalBean>> updateBatch(@Valid @ApiIgnore InitStatisticalAddBatch updateBatch) {
        Function<List<UpdateGraphConfStatisticalReq>, ApiReturn<List<GraphConfStatisticalRsp>>> returnFunction = a -> kgmsClient.updateStatisticalBatch(a);
        List<InitStatisticalBean> statisticalBeans = returnFunction
                .compose(StatisticConverter.updateBeanToReq)
                .andThen(StatisticConverter.rspToBean)
                .apply(updateBatch.getRules());
        return new RestResp<>(statisticalBeans);
    }

    @PostMapping("delete/batch")
    @ApiOperation("批量删除配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "ids", required = true, dataType = "string", paramType = "form", value = "需要删除的id列表,json数组格式"),
    })
    public RestResp deleteBatch(@Valid @ApiIgnore InitStatisticalDeleteBatch deleteBatch) {
        kgmsClient.deleteStatisticalBatch(BasicConverter.listToRsp(deleteBatch.getIds(), Integer::longValue));
        return new RestResp();
    }

    @PostMapping("add")
    @ApiOperation("添加配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "type", required = true, dataType = "string", paramType = "form", value = "type"),
            @ApiImplicitParam(name = "rule", required = true, dataType = "string", paramType = "form", value = "rule"),
    })
    public RestResp<InitStatisticalBean> add(@Valid @ApiIgnore InitStatisticalBeanAdd beanAdd) {

        Function<GraphConfStatisticalReq, ApiReturn<GraphConfStatisticalRsp>> returnRsp = a -> kgmsClient.saveStatistical(beanAdd.getKgName(), a);
        InitStatisticalBean statisticalBean = returnRsp
                .compose(StatisticConverter::initStatisticalBeanAddToGraphConfStatisticalReq)
                .andThen(a -> BasicConverter.convert(a, StatisticConverter::graphConfStatisticalRspToInitStatisticalBean))
                .apply(beanAdd);
        return new RestResp<>(statisticalBean);
    }

    @PostMapping("update")
    @ApiOperation("修改配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "form", value = "id"),
            @ApiImplicitParam(name = "type", required = true, dataType = "string", paramType = "form", value = "type"),
            @ApiImplicitParam(name = "rule", required = true, dataType = "string", paramType = "form", value = "rule"),
    })
    public RestResp<InitStatisticalBean> update(@Valid @ApiIgnore InitStatisticalBeanUpdate beanUpdate) {
        Function<GraphConfStatisticalReq, ApiReturn<GraphConfStatisticalRsp>> returnRsp = a -> kgmsClient.updateStatistical(beanUpdate.getId().longValue(), a);
        InitStatisticalBean statisticalBean = returnRsp
                .compose(StatisticConverter::initStatisticalBeanAddToGraphConfStatisticalReq)
                .andThen(a -> BasicConverter.convert(a, StatisticConverter::graphConfStatisticalRspToInitStatisticalBean))
                .apply(beanUpdate);
        return new RestResp<>(statisticalBean);
    }

    @PostMapping("delete")
    @ApiOperation("删除配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "string", paramType = "form", value = "需要删除的id"),
    })
    public RestResp delete(@RequestParam("id") Integer id, @RequestParam(value = "kgName", required = false) String kgName) {
        kgmsClient.deleteStatistical(id.longValue());
        return new RestResp();
    }
}