package ai.plantdata.kgcloud.domain.data.obtain.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.bean.BasePage;
import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import ai.plantdata.kgcloud.sdk.KgmsClient;
import ai.plantdata.kgcloud.sdk.req.GraphConfSnapshotReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfSnapshotRsp;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author :Admin
 * @description :
 * @create :2020-08-13 11:12:00
 */
@RestController
@RequestMapping("v3/kgdata/config")
public class GraphConfSnapshotController implements GraphDataObtainInterface {

    @Resource
    private KgmsClient kgmsClient;

    @ApiOperation("图谱配置-快照保存")
    @PostMapping("/snapshot")
    public ApiReturn<GraphConfSnapshotRsp> saveSnapShot(@Valid @RequestBody GraphConfSnapshotReq graphConfSnapshotReq) {
        return this.kgmsClient.saveSnapShot(graphConfSnapshotReq);
    }

    @ApiOperation("删除快照")
    @DeleteMapping("/snapshot/{id}")
    public ApiReturn deleteSnapShot(@PathVariable("id") Long id){
        return this.kgmsClient.deleteSnapShot(id);
    }

    @ApiOperation("图谱配置-查询所有快照")
    @GetMapping("/snapshot/{kgName}/{spaId}")
    public ApiReturn<BasePage<GraphConfSnapshotRsp>> findAllSnapShot(@PathVariable("kgName") String kgName,
                                                                     @PathVariable("spaId") String spaId, BaseReq p){
        return this.kgmsClient.findAllSnapShot(kgName,spaId,p.getPage(),p.getSize());
    }

    @ApiOperation("图谱配置-单个快照")
    @GetMapping("/snapshot/{id}")
    public ApiReturn<GraphConfSnapshotRsp> findByIdSnapShot(@PathVariable("id") Long id){
        return this.kgmsClient.findByIdSnapShot(id);
    }
}
