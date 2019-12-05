package com.plantdata.kgcloud.domain.dictionary.controller;


import com.plantdata.kgcloud.domain.dictionary.service.DictionaryService;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.sdk.req.DictionaryReq;
import com.plantdata.kgcloud.sdk.rsp.DictionaryRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-05 14:17
 **/
@Api(tags = "词典管理")
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @ApiOperation("词典查找所有")
    @GetMapping("/all")
    public ApiReturn<List<DictionaryRsp>> findAll() {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dictionaryService.findAll(userId));
    }


    @ApiOperation("词典分页查找")
    @GetMapping("/")
    public ApiReturn<Page<DictionaryRsp>> findAll(BaseReq baseReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dictionaryService.findAll(userId,baseReq));
    }

    @ApiOperation("词典根据Id查找")
    @GetMapping("/{id}")
    public ApiReturn<DictionaryRsp> findById(@PathVariable Long id) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dictionaryService.findById(userId,id));
    }

    @ApiOperation("词典创建")
    @PostMapping("/")
    public ApiReturn<DictionaryRsp> insert(@Valid @RequestBody DictionaryReq dictionaryReq) {
        return ApiReturn.success(dictionaryService.insert(dictionaryReq));
    }

    @ApiOperation("词典编辑")
    @PatchMapping("/{id}")
    public ApiReturn<DictionaryRsp> update(@PathVariable Long id, @Valid @RequestBody DictionaryReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dictionaryService.update(userId,id, req));
    }

    @ApiOperation("词典删除")
    @DeleteMapping("/{id}")
    public ApiReturn delete(@PathVariable Long id) {
        String userId = SessionHolder.getUserId();
        dictionaryService.delete(userId,id);
        return ApiReturn.success();
    }

}
