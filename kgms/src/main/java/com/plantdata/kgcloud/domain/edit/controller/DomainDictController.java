package com.plantdata.kgcloud.domain.edit.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.edit.req.dict.DictReq;
import com.plantdata.kgcloud.domain.edit.rsp.DictRsp;
import com.plantdata.kgcloud.domain.edit.service.DomainDictService;
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
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/12/4 15:15
 * @Description:
 */
@Api(tags = "领域词")
@RestController
@RequestMapping("/edit/dict")
public class DomainDictController {

    @Autowired
    private DomainDictService domainDictService;

    @ApiOperation("批量添加领域词")
    @PostMapping("/{kgName}/batch/add")
    ApiReturn batchInsert(@PathVariable("kgName") String kgName,
                          @Size(min = 1) @RequestBody List<DictReq> dictReqs) {
        domainDictService.batchInsert(kgName, dictReqs);
        return ApiReturn.success();
    }

    @ApiOperation("修改领域词")
    @PostMapping("/{kgName}/{id}/update")
    ApiReturn update(@PathVariable("kgName") String kgName,
                     @PathVariable("id") String id,
                     @Valid @RequestBody DictReq dictReq) {
        domainDictService.update(kgName, id, dictReq);
        return ApiReturn.success();
    }

    @ApiOperation("批量删除")
    @PostMapping("/{kgName}/batch/delete")
    ApiReturn batchDelete(@PathVariable("kgName") String kgName,
                          @RequestBody List<String> ids) {
        domainDictService.batchDelete(kgName, ids);
        return ApiReturn.success();
    }

    @ApiOperation("领域词列表")
    @GetMapping("/{kgName}/list")
    ApiReturn<Page<DictRsp>> listDict(@PathVariable("kgName") String kgName, BaseReq baseReq) {
        return ApiReturn.success(domainDictService.listDict(kgName, baseReq));
    }
}
