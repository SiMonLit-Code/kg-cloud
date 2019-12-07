package com.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.kg.api.edit.BatchApi;
import ai.plantdata.kg.api.edit.resp.BatchResult;
import ai.plantdata.kg.api.edit.resp.UpdateEdgeVO;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.converter.RestCopyConverter;
import com.plantdata.kgcloud.domain.app.service.KgDataService;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.model.service.ModelService;
import com.plantdata.kgcloud.sdk.req.app.BatchEntityAttrDeleteReq;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.req.app.SparQlReq;
import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.data.RelationUpdateReq;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 10:59
 */
@RestController
@RequestMapping("kgData")
public class KgDataController {

    @Autowired
    private KgDataService kgDataService;
    @Autowired
    private BatchApi batchApi;
    @Autowired
    private ModelService modelService;

    @ApiOperation("实体查询")
    @GetMapping("entity/{kgName}/list")
    public ApiReturn<List<OpenEntityRsp>> queryEntityList(@PathVariable("kgName") String kgName,
                                                          EntityQueryReq entityQueryReq) {
        return ApiReturn.success(kgDataService.queryEntityList(kgName, entityQueryReq));
    }


    @ApiOperation("批量新增或新增实体")
    @PostMapping("entity/{kgName}")
    public ApiReturn<List<OpenBatchSaveEntityRsp>> saveOrUpdate(@PathVariable("kgName") String kgName, @ApiParam("是否只是更新，默认不是") boolean add,
                                                                @RequestBody List<OpenBatchSaveEntityRsp> batchEntity) {
        return ApiReturn.success(kgDataService.saveOrUpdate(kgName, add, batchEntity));
    }

    @ApiOperation("批量实体数值属性删除")
    @DeleteMapping("entity/attr/{kgName}")
    public ApiReturn batchDeleteEntityAttr(@PathVariable("kgName") String kgName, @RequestBody BatchEntityAttrDeleteReq deleteReq) {
        kgDataService.batchDeleteEntityAttr(kgName, deleteReq);
        return ApiReturn.success();
    }

    @ApiOperation("批量修改关系")
    @PatchMapping("relations/update/{kgName}")
    public ApiReturn<List<RelationUpdateReq>> updateRelations(@PathVariable("kgName") String kgName, @RequestBody List<RelationUpdateReq> list) {
        List<UpdateEdgeVO> edgeList = RestCopyConverter.copyToNewList(list, UpdateEdgeVO.class);
        Optional<BatchResult<UpdateEdgeVO>> edgeOpt = RestRespConverter.convert(batchApi.updateRelations(kgName, edgeList));
        return edgeOpt.map(result -> ApiReturn.success(RestCopyConverter.copyToNewList(result.getSuccess(), RelationUpdateReq.class)))
                .orElseGet(() -> ApiReturn.success(Collections.emptyList()));
    }

    @ApiOperation("sparql查询")
    @PostMapping("sparQl/query/{kgName}")
    public ApiReturn<Object> sparQlQuery(@Valid @ApiIgnore SparQlReq sparQlReq) {
        //todo 底层提供api
        return ApiReturn.success();
    }

    @ApiOperation("第三方模型抽取")
    @PostMapping("extract/thirdModel/{modelId}")
    public ApiReturn<Object> extractThirdModel(@PathVariable("modelId") Long modelId,
                                               String input, @RequestBody List<Map<String, String>> configList) {
        //todo kgms实现
        modelService.call(modelId, Lists.newArrayList(input));
        return ApiReturn.success(null);
    }

}
