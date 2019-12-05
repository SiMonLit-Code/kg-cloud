package com.plantdata.kgcloud.domain.data.obtain.controller;

import ai.plantdata.kg.api.edit.BatchApi;
import ai.plantdata.kg.api.edit.resp.BatchRelationVO;
import ai.plantdata.kg.api.edit.resp.BatchResult;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.converter.RestRespConverter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/5 14:27
 */
@RestController
@RequestMapping("kgData")
public class RelationController {

    @Autowired
    private BatchApi batchApi;

    @ApiOperation("批量关系新增")
    @PostMapping("relation/insert/{kgName}")
    public ApiReturn<BatchRelationVO> importRelation(@PathVariable String kgName,
                                                     @RequestBody BatchRelationVO relation) {
        Optional<BatchResult<BatchRelationVO>> relationOpt = RestRespConverter.convert(batchApi.addRelations(kgName, Lists.newArrayList(relation)));
        List<BatchRelationVO> success = relationOpt.orElse(new BatchResult<>()).getSuccess();
        return ApiReturn.success(CollectionUtils.isEmpty(success) ? new BatchRelationVO() : success.get(0));
    }
    ///todo
}
