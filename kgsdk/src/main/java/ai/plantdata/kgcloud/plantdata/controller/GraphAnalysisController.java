package ai.plantdata.kgcloud.plantdata.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.plantdata.req.app.CoordinatesParameter;
import ai.plantdata.kgcloud.plantdata.rsp.app.GraphAnalysisaBean;
import cn.hiboot.mcn.core.model.result.RestResp;
import ai.plantdata.kgcloud.plantdata.converter.algorithm.AlgorithmConverter;
import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.sdk.AppClient;
import ai.plantdata.kgcloud.sdk.rsp.app.ComplexGraphVisualRsp;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("sdk/graph/analysis")
public class GraphAnalysisController implements SdkOldApiInterface {

    @Autowired
    private AppClient appClient;

    @ApiOperation("复杂图分析坐标显示")
    @GetMapping("/coordinates")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "azkId", required = true, dataType = "long", paramType = "form", value = "azk任务id"),
            @ApiImplicitParam(name = "type", required = true, dataType = "string", defaultValue = "louvain", paramType = "form", value = "任务类型"),
            @ApiImplicitParam(name = "size", dataType = "integer", defaultValue = "100", paramType = "query", value = "查询数量"),
    })
    public RestResp<GraphAnalysisaBean> coordinates(@Valid @ApiIgnore CoordinatesParameter param) {
        if (param.getSize() < 0) {
            return new RestResp<>(new GraphAnalysisaBean());
        }
        ApiReturn<ComplexGraphVisualRsp> apiReturn
                = appClient.complexGraphVisual(param.getKgName(), param.getAzkId(), param.getType(), param.getSize());
        GraphAnalysisaBean analysisBean = BasicConverter.convert(apiReturn, AlgorithmConverter::complexGraphVisualRspToGraphAnalysisaBean);
        return new RestResp<>(analysisBean);
    }
}
