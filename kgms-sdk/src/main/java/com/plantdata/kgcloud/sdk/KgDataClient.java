package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.data.RelationUpdateReq;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 14:02
 */
@FeignClient(value = "kgms", path = "kgData", contextId = "kgDataClient")
public interface KgDataClient {


    /**
     * 实体查询
     *
     * @param kgName         kgName
     * @param entityQueryReq req
     * @return 。
     */
    @GetMapping("entity/{kgName}/list")
    ApiReturn<List<OpenEntityRsp>> queryEntityList(@PathVariable("kgName") String kgName,
                                                   EntityQueryReq entityQueryReq);

    /**
     * 批量新增实体
     *
     * @param kgName      kgName
     * @param add         req
     * @param batchEntity 。
     * @return 。
     */
    @PostMapping("entity/{kgName}")
    ApiReturn saveOrUpdate(@PathVariable("kgName") String kgName, @ApiParam("是否只是更新，默认不是") boolean add,
                           @RequestBody List<OpenBatchSaveEntityRsp> batchEntity);

    /**
     * 批量修改关系
     *
     * @param kgName kgName
     * @param list   req
     * @return .
     */
    @PatchMapping("relations/update/{kgName}")
    ApiReturn<List<RelationUpdateReq>> updateRelations(@PathVariable("kgName") String kgName, @RequestBody List<RelationUpdateReq> list);

    /**
     * 第三方模型抽取
     *
     * @param modelId    模型id
     * @param input      。
     * @param configList 配置
     * @return 。
     */
    @PostMapping("extract/thirdModel/{modelId}")
    ApiReturn<Object> extractThirdModel(@PathVariable("modelId") Long modelId,
                                        String input, @RequestBody List<Map<String, String>> configList);
}
