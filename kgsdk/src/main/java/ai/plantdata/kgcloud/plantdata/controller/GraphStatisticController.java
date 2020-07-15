package ai.plantdata.kgcloud.plantdata.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.plantdata.req.config.InitStatisticalBean;
import ai.plantdata.kgcloud.plantdata.req.rule.InitStatisticalAddBatch;
import ai.plantdata.kgcloud.plantdata.req.rule.InitStatisticalBeanAdd;
import ai.plantdata.kgcloud.plantdata.req.rule.InitStatisticalBeanUpdate;
import ai.plantdata.kgcloud.plantdata.req.rule.InitStatisticalDeleteBatch;
import cn.hiboot.mcn.core.model.result.RestResp;
import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.plantdata.converter.config.StatisticConverter;
import ai.plantdata.kgcloud.sdk.KgmsClient;
import ai.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import ai.plantdata.kgcloud.sdk.req.UpdateGraphConfStatisticalReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;
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
    @ApiOperation("统计配置-获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
    })
    public RestResp<List<InitStatisticalBean>> getByKgName(@RequestParam("kgName") String kgName) {
        ApiReturn<List<GraphConfStatisticalRsp>> apiReturn = kgmsClient.selectStatistical(kgName);
        List<InitStatisticalBean> beanList = StatisticConverter.rspToBean.apply(apiReturn);
        return new RestResp<>(beanList);
    }

    @PostMapping("add/batch")
    @ApiOperation("统计配置-批量添加")
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
    @ApiOperation("统计配置-批量修改")
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
    @ApiOperation("统计配置-批量删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "ids", required = true, dataType = "string", paramType = "form", value = "需要删除的id列表,json数组格式"),
    })
    public RestResp deleteBatch(@Valid @ApiIgnore InitStatisticalDeleteBatch deleteBatch) {
        kgmsClient.deleteStatisticalBatch(deleteBatch.getIds());
        return new RestResp();
    }

    @PostMapping("add")
    @ApiOperation("统计配置-添加")
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
    @ApiOperation("统计配置-修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "form", value = "id"),
            @ApiImplicitParam(name = "type", required = true, dataType = "string", paramType = "form", value = "type"),
            @ApiImplicitParam(name = "rule", required = true, dataType = "string", paramType = "form", value = "rule"),
    })
    public RestResp<InitStatisticalBean> update(@Valid @ApiIgnore InitStatisticalBeanUpdate beanUpdate) {

        Function<GraphConfStatisticalReq, ApiReturn<GraphConfStatisticalRsp>> returnRsp = a -> kgmsClient.updateStatistical(beanUpdate.getId(), a);
        InitStatisticalBean statisticalBean = returnRsp
                .compose(StatisticConverter::initStatisticalBeanAddToGraphConfStatisticalReq)
                .andThen(a -> BasicConverter.convert(a, StatisticConverter::graphConfStatisticalRspToInitStatisticalBean))
                .apply(beanUpdate);
        return new RestResp<>(statisticalBean);
    }

    @PostMapping("delete")
    @ApiOperation("统计配置-删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "string", paramType = "form", value = "需要删除的id"),
    })
    public RestResp delete(@RequestParam("id") Long id, @RequestParam(value = "kgName", required = false) String kgName) {
        kgmsClient.deleteStatistical(id);
        return new RestResp();
    }
}