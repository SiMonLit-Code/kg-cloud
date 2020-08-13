package ai.plantdata.kgcloud.domain.graph.config.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.cloud.web.util.ConvertUtils;
import ai.plantdata.kgcloud.domain.graph.config.entity.GraphConfSnapshot;
import ai.plantdata.kgcloud.domain.graph.config.service.GraphConfSnapshotService;
import ai.plantdata.kgcloud.sdk.req.GraphConfSnapshotReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfSnapshotRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author Bovin
 * @description
 * @since 2020-08-12 16:46
 **/
@Api(tags = "图谱配置")
@RestController
@RequestMapping("/config")
public class GraphConfSnapshotController {
    @Resource
    GraphConfSnapshotService graphConfSnapshotService;

    @ApiOperation("图谱配置-快照保存")
    @PostMapping("/snapshot")
    public ApiReturn<GraphConfSnapshotRsp> saveSnapShot(@Valid @RequestBody GraphConfSnapshotReq graphConfSnapshot) {
        return ApiReturn.success(graphConfSnapshotService.saveSnapShot(graphConfSnapshot));
    }
    @ApiOperation("图谱配置-删除快照")
    @DeleteMapping("/snapshot/{id}")
    public ApiReturn deleteSnapShot(@PathVariable("id") Long id) {
        graphConfSnapshotService.deleteSnapShot(id);
        return ApiReturn.success();
    }

    @ApiOperation("图谱配置-查询所有快照")
    @GetMapping("/snapshot/{kgName}/{spaId}")
    public ApiReturn<Page<GraphConfSnapshotRsp>> findAllSnapShot(@PathVariable("kgName") String kgName,
                                                                 @PathVariable("spaId") String spaId,
                                                                 BaseReq p) {
        return ApiReturn.success(graphConfSnapshotService.findAllSnapShot(kgName,spaId,p));
    }

    @ApiOperation("图谱配置-单个快照")
    @GetMapping("/snapshot/{id}")
    public ApiReturn<GraphConfSnapshotRsp> findByIdSnapShot(@PathVariable("id") Long id) {
        return ApiReturn.success(graphConfSnapshotService.findByIdSnapShot(id));
    }


}
