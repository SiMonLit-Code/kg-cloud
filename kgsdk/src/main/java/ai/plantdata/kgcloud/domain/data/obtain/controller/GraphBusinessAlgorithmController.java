package ai.plantdata.kgcloud.domain.data.obtain.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.bean.BasePage;
import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import ai.plantdata.kgcloud.sdk.KgmsClient;
import ai.plantdata.kgcloud.sdk.req.GraphConfAlgorithmReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfAlgorithmRsp;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/25 16:48
 */
@RestController
@RequestMapping("v3/kgdata/algorithm")
public class GraphBusinessAlgorithmController implements GraphDataObtainInterface {

    @Resource
    private KgmsClient kgmsClient;

    @ApiOperation("图谱配置-算法-新建")
    @PostMapping("{kgName}")
    public ApiReturn<GraphConfAlgorithmRsp> save(@PathVariable("kgName") String kgName,
                                                 @RequestBody GraphConfAlgorithmReq req) {
        return kgmsClient.save(kgName, req);
    }

    @ApiOperation("图谱配置-算法-更新")
    @PutMapping("{id}")
    public ApiReturn<GraphConfAlgorithmRsp> update(@PathVariable("id") Long id,
                                                   @RequestBody @Valid GraphConfAlgorithmReq req) {
        return kgmsClient.update(id, req);
    }

    @ApiOperation("图谱配置-算法-删除")
    @DeleteMapping("{id}")
    public ApiReturn delete(@PathVariable("id") Long id) {
        kgmsClient.delete(id);
        return ApiReturn.success();
    }

    @ApiOperation("图谱配置-算法-获取")
    @GetMapping("{kgName}")
    public ApiReturn<BasePage<GraphConfAlgorithmRsp>> select(@PathVariable("kgName") String kgName, Integer type, BaseReq baseReq) {
        return kgmsClient.select(kgName,type, baseReq.getPage(), baseReq.getSize());
    }

    @ApiOperation("图谱配置-算法-详情")
    @GetMapping("detail/{id}")
    public ApiReturn<GraphConfAlgorithmRsp> detailAlgorithm(@PathVariable("id") Long id) {
        return kgmsClient.detailAlgorithm(id);
    }
}
