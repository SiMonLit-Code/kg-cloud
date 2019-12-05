package com.plantdata.kgcloud.domain.edit.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.edit.req.merge.EntityMergeReq;
import com.plantdata.kgcloud.domain.edit.req.merge.MergeRecommendedReq;
import com.plantdata.kgcloud.domain.edit.req.merge.RecommendedMergeReq;
import com.plantdata.kgcloud.domain.edit.rsp.EntityMergeRsp;
import com.plantdata.kgcloud.domain.edit.service.MergeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 16:40
 * @Description:
 */
@Api(tags = "实体融合")
@RestController
@RequestMapping("/merge")
public class MergeController {

    @Autowired
    private MergeService mergeService;

    @ApiOperation("实体融合")
    @PostMapping("/{kgName}")
    ApiReturn entityMerge(@PathVariable("kgName") String kgName,
                          @Valid @RequestBody EntityMergeReq entityMergeReq) {
        mergeService.entityMerge(kgName, entityMergeReq);
        return ApiReturn.success();
    }

    @ApiOperation("融合候选集列表")
    @GetMapping("/{kgName}/list")
    ApiReturn<Page<EntityMergeRsp>> listMerges(@PathVariable("kgName") String kgName,
                                               MergeRecommendedReq mergeRecommendedReq) {
        return ApiReturn.success(mergeService.listMerges(kgName, mergeRecommendedReq));
    }

    @ApiOperation("推荐实体融合")
    @PostMapping("/{kgName}/recommend")
    ApiReturn recommendedMerge(@PathVariable("kgName") String kgName,
                               @Valid @RequestBody List<RecommendedMergeReq> recommendedMergeReqs) {
        mergeService.recommendedMerge(kgName, recommendedMergeReqs);
        return ApiReturn.success();
    }
}
