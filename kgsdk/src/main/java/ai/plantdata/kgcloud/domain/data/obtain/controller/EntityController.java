package ai.plantdata.kgcloud.domain.data.obtain.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.bean.BasePage;
import ai.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import ai.plantdata.kgcloud.sdk.EditClient;
import ai.plantdata.kgcloud.sdk.KgDataClient;
import ai.plantdata.kgcloud.sdk.MergeClient;
import ai.plantdata.kgcloud.sdk.req.app.BatchEntityAttrDeleteReq;
import ai.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import ai.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import ai.plantdata.kgcloud.sdk.req.app.TraceabilityQueryReq;
import ai.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import ai.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.DeleteResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 10:49
 */
@RestController
@RequestMapping("v3/kgdata/entity")
public class EntityController implements GraphDataObtainInterface {

    @Autowired
    private EditClient editClient;
    @Autowired
    private MergeClient mergeClient;
    @Autowired
    private KgDataClient kgDataClient;

    @ApiOperation(value = "批量实体查询", notes = "按数值属性筛选实例")
    @PostMapping("{kgName}/list")
    public ApiReturn<List<OpenEntityRsp>> queryEntityList(@PathVariable("kgName") String kgName,
                                                          @RequestBody EntityQueryReq entityQueryReq) {
        return editClient.queryEntityList(kgName, entityQueryReq);
    }

    @ApiOperation(value = "批量实体新增及更新", notes = "新增实体节点及其数值属性，或更新实体的数值属性")
    @PostMapping("{kgName}")
    public ApiReturn<OpenBatchResult<OpenBatchSaveEntityRsp>> batchAdd(@PathVariable("kgName") String kgName,
                                                                       @ApiParam(value = "true新增", required = true) @RequestParam boolean add,
                                                                       @RequestBody List<OpenBatchSaveEntityRsp> batchEntity) {
        return editClient.saveOrUpdate(kgName, add, batchEntity);
    }

    @ApiOperation(value = "批量实体删除", notes = "删除实体。")
    @PostMapping("{kgName}/batch/delete")
    public ApiReturn<List<DeleteResult>> batchDeleteEntities(@PathVariable("kgName") String kgName,
                                                             @RequestBody List<Long> ids) {
        return editClient.batchDeleteEntities(kgName, ids);
    }

    @ApiOperation(value = "知识融合候选集写入", notes = "手工创建融合实体")
    @PostMapping("wait/create/{kgName}")
    public ApiReturn<String> createMergeEntity(@PathVariable("kgName") String kgName, @RequestBody List<Long> ids) {
        return mergeClient.createMergeEntity(kgName, ids);
    }


    @ApiOperation(value = "实体溯源", notes = "根据溯源信息查询实体的基本信息")
    @PostMapping({"{kgName}/source"})
    public ApiReturn<BasePage<OpenEntityRsp>> queryEntityBySource(@PathVariable("kgName") String kgName,
                                                                  @RequestBody TraceabilityQueryReq req) {
        return kgDataClient.queryEntityBySource(kgName, req);
    }

    @ApiOperation(value = "批量删除实体数值属性", notes = "删除指定实体的多个数值属性")
    @DeleteMapping("entity/attr/{kgName}")
    public ApiReturn batchDeleteEntityAttr(@PathVariable("kgName") String kgName,
                                           @RequestBody BatchEntityAttrDeleteReq deleteReq) {
        return editClient.batchDeleteEntityAttr(kgName, deleteReq);
    }
}
