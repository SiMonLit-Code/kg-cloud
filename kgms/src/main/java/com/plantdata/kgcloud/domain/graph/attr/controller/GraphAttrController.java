package com.plantdata.kgcloud.domain.graph.attr.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.graph.attr.req.AttrGroupReq;
import com.plantdata.kgcloud.domain.graph.attr.req.AttrGroupSearchReq;
import com.plantdata.kgcloud.domain.graph.attr.service.GraphAttrGroupService;
import com.plantdata.kgcloud.sdk.rsp.GraphAttrGroupRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 10:57
 **/
@Api(tags = "属性分组")
@RestController
@RequestMapping("/attr/group")
public class GraphAttrController {

    @Autowired
    private GraphAttrGroupService graphAttrGroupService;

    @ApiOperation("创建属性分组")
    @PostMapping("/{kgName}/create")
    public ApiReturn<Long> createAttrGroup(@PathVariable("kgName") String kgName,
                                           @Valid @RequestBody AttrGroupReq attrGroupReq) {
        return ApiReturn.success(graphAttrGroupService.createAttrGroup(kgName, attrGroupReq));
    }

    @ApiOperation("删除属性分组")
    @DeleteMapping("/{kgName}/delete/{id}")
    public ApiReturn deleteAttrGroup(@PathVariable("kgName") String kgName,
                                     @PathVariable("id") Long id) {
        graphAttrGroupService.deleteAttrGroup(kgName, id);
        return ApiReturn.success();
    }

    @ApiOperation("修改属性分组名称")
    @PutMapping("/{kgName}/update/{id}")
    public ApiReturn<Long> updateAttrGroup(@PathVariable("kgName") String kgName,
                                           @PathVariable("id") Long id,
                                           @Valid @RequestBody AttrGroupReq attrGroupReq) {
        return ApiReturn.success(graphAttrGroupService.updateAttrGroup(kgName, id, attrGroupReq));
    }

    @ApiOperation("属性分组名称列表")
    @GetMapping("/{kgName}")
    public ApiReturn<List<GraphAttrGroupRsp>> listAttrGroups(@PathVariable("kgName") String kgName,
                                                             AttrGroupSearchReq attrGroupSearchReq) {
        return ApiReturn.success(graphAttrGroupService.listAttrGroups(kgName, attrGroupSearchReq));
    }

    @ApiOperation("向属性分组里面添加属性")
    @PostMapping("/{kgName}/{id}/to/add")
    public ApiReturn<Integer> addAttrToAttrGroup(@PathVariable("kgName") String kgName,
                                        @PathVariable("id") Long id,
                                        @Valid @RequestBody List<Integer> attrIds) {
        return ApiReturn.success(graphAttrGroupService.addAttrToAttrGroup(kgName, id, attrIds));
    }

    @ApiOperation("从属性分组里面删除属性")
    @PostMapping("/{kgName}/{id}/from/delete")
    public ApiReturn deleteAttrFromAttrGroup(@PathVariable("kgName") String kgName,
                                             @PathVariable("id") Long id,
                                             @Valid @RequestBody List<Integer> attrIds) {
        graphAttrGroupService.deleteAttrFromAttrGroup(kgName, id, attrIds);
        return ApiReturn.success();
    }
}
