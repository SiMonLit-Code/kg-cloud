package ai.plantdata.kgcloud.domain.structure.analysis;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.domain.common.module.GraphStructureAnalysisInterface;
import ai.plantdata.kgcloud.sdk.AppClient;
import ai.plantdata.kgcloud.sdk.req.app.ComplexGraphVisualReq;
import ai.plantdata.kgcloud.sdk.rsp.app.ComplexGraphVisualRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/14 15:33
 */
@RestController
@RequestMapping("v3/complex/graph")
public class GraphAlgorithmAnalysisController implements GraphStructureAnalysisInterface {

    @Autowired
    private AppClient appClient;

    @ApiOperation("复杂图分析坐标显示")
    @GetMapping("visual/{kgName}")
    public ApiReturn<ComplexGraphVisualRsp> complexGraphVisual(@ApiParam("图谱名称") @PathVariable("kgName") String kgName, ComplexGraphVisualReq visualReq) {
        return appClient.complexGraphVisual(kgName, visualReq.getAzkId(), visualReq.getType(), visualReq.getSize());
    }
}
