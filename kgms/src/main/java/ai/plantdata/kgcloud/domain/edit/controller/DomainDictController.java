package ai.plantdata.kgcloud.domain.edit.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.domain.edit.req.dict.DictReq;
import ai.plantdata.kgcloud.domain.edit.req.dict.DictSearchReq;
import ai.plantdata.kgcloud.domain.edit.rsp.DictRsp;
import ai.plantdata.kgcloud.domain.edit.service.DomainDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ApiReturn batchInsert(@PathVariable("kgName") String kgName,
                                 @Valid @RequestBody List<DictReq> dictReqs) {
        domainDictService.batchInsert(kgName, dictReqs);
        return ApiReturn.success();
    }

    @ApiOperation("修改领域词")
    @PostMapping("/{kgName}/{id}/update")
    public ApiReturn update(@PathVariable("kgName") String kgName,
                            @PathVariable("id") String id,
                            @Valid @RequestBody DictReq dictReq) {
        domainDictService.update(kgName, id, dictReq);
        return ApiReturn.success();
    }

    @ApiOperation("批量删除")
    @PostMapping("/{kgName}/batch/delete")
    public ApiReturn batchDelete(@PathVariable("kgName") String kgName,
                                 @RequestBody List<String> ids) {
        domainDictService.batchDelete(kgName, ids);
        return ApiReturn.success();
    }

    @ApiOperation("领域词列表")
    @GetMapping("/{kgName}/list")
    public ApiReturn<Page<DictRsp>> listDict(@PathVariable("kgName") String kgName, DictSearchReq dictSearchReq) {
        return ApiReturn.success(domainDictService.listDict(kgName, dictSearchReq));
    }

    @ApiOperation("获取实体关联的所有领域词")
    @GetMapping("/{kgName}/list/{entityId}")
    public ApiReturn listDictByEntity(@PathVariable("kgName") String kgName,
                                      @PathVariable("entityId") Long entityId) {
        return ApiReturn.success(domainDictService.listDictByEntity(kgName, entityId));
    }

}
