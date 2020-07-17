package ai.plantdata.kgcloud.plantdata.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.plantdata.req.app.GraphBusinessAlgorithmRequestRun;
import ai.plantdata.kgcloud.plantdata.req.rule.BusinessGraphBean;
import ai.plantdata.kgcloud.plantdata.rsp.app.statistic.AlgorithmStatisticeBean;
import cn.hiboot.mcn.core.model.result.RestResp;
import ai.plantdata.kgcloud.plantdata.converter.algorithm.AlgorithmConverter;
import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.sdk.AppClient;
import ai.plantdata.kgcloud.sdk.req.app.algorithm.BusinessGraphRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.statistic.AlgorithmStatisticeRsp;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.function.Function;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("sdk/graph/business/algorithm")
public class GraphAlgorithmAppController implements SdkOldApiInterface{

    @Autowired
    private AppClient appClient;

    @PostMapping("run")
    @ApiOperation("业务算法算法调用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "form", value = "id"),
            @ApiImplicitParam(name = "graphBean", required = true, dataType = "string", paramType = "form", value = "graphBean"),
    })
    public RestResp<BusinessGraphBean> run(@Valid @ApiIgnore GraphBusinessAlgorithmRequestRun param) {

        Function<BusinessGraphRsp, ApiReturn<BusinessGraphRsp>> returnFunction = a -> appClient.executeAlgorithm(param.getKgName(), param.getId(), a);
        BusinessGraphBean graphBean = returnFunction
                .compose(AlgorithmConverter::businessGraphBeanToRsp)
                .andThen(a -> BasicConverter.convert(a, AlgorithmConverter::businessGraphRspToBean))
                .apply(param.getGraphBean());
        return new RestResp<>(graphBean);
    }

    @PostMapping("statistics/run")
    @ApiOperation("统计业务算法算法调用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "form", value = "id"),
            @ApiImplicitParam(name = "graphBean", required = true, dataType = "string", paramType = "form", value = "graphBean"),
    })
    public RestResp<AlgorithmStatisticeBean> statisticsRun(@Valid @ApiIgnore GraphBusinessAlgorithmRequestRun param) {

        Function<BusinessGraphRsp, ApiReturn<AlgorithmStatisticeRsp>> returnFunction = a -> appClient.executeStatisticsAlgorithm(param.getKgName(), param.getId(), a);
        AlgorithmStatisticeBean graphBean = returnFunction
                .compose(AlgorithmConverter::businessGraphBeanToRsp)
                .andThen(a -> BasicConverter.convert(a, AlgorithmConverter::algorithmStatisticeRspToBean))
                .apply(param.getGraphBean());
        return new RestResp<>(graphBean);
    }
}