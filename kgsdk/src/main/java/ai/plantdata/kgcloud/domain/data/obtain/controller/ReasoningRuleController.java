package ai.plantdata.kgcloud.domain.data.obtain.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.bean.BasePage;
import ai.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import ai.plantdata.kgcloud.sdk.KgmsClient;
import ai.plantdata.kgcloud.sdk.req.GraphConfReasonReq;
import ai.plantdata.kgcloud.sdk.req.app.PageReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfReasonRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/14 12:02
 */
@RestController
@RequestMapping("v3/kgdata/rule/reasoning")
public class ReasoningRuleController implements GraphDataObtainInterface {
    @Autowired
    private KgmsClient kgmsClient;

    @GetMapping("page/{kgName}")
    @ApiOperation("推理规则-列表")
    public ApiReturn<BasePage<GraphConfReasonRsp>> listByPage(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                              PageReq pageReq) {
        return kgmsClient.selectReasoningPage(kgName, pageReq.getPage(), pageReq.getSize());
    }

    @ApiOperation("推理规则-详情")
    @GetMapping("detail/{id}")
    public ApiReturn<GraphConfReasonRsp> detail(@ApiParam("规则id") @PathVariable("id") Long id) {
        return kgmsClient.detailReasoning(id);
    }

    @ApiOperation("推理规则-新增")
    @PostMapping("{kgName}")
    public ApiReturn<GraphConfReasonRsp> add(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                             @RequestBody GraphConfReasonReq reasoningRuleReq) {
        return kgmsClient.saveReasoning(kgName, reasoningRuleReq);
    }

    @ApiOperation("推理规则-删除")
    @DeleteMapping("{id}")
    public ApiReturn delete(@ApiParam("规则id") @PathVariable("id") Long id) {
        return kgmsClient.deleteReasoning(id);
    }

    @ApiOperation("推理规则-修改")
    @PutMapping("{id}")
    public ApiReturn<GraphConfReasonRsp> modify(@ApiParam("规则id") @PathVariable("id") Long id, @RequestBody GraphConfReasonReq reasoningRuleReq) {
        return kgmsClient.updateReasoning(id, reasoningRuleReq);
    }

}
