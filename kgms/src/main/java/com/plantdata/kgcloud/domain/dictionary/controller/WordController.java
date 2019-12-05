package com.plantdata.kgcloud.domain.dictionary.controller;


import com.plantdata.kgcloud.domain.dictionary.service.WordService;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.sdk.req.WordReq;
import com.plantdata.kgcloud.sdk.rsp.WordRsp;
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
 * @create: 2019-11-05 15:00
 **/
@Api(tags = "词典管理")
@RestController
@RequestMapping("/dictionary")
public class WordController {
    @Autowired
    private WordService wordService;

    @ApiOperation("词典-词条查找所有")
    @GetMapping("/{dictId}/word/all")
    public ApiReturn<List<WordRsp>> findAll(@PathVariable("dictId") Long dictId) {
        return ApiReturn.success(wordService.findAll(dictId));
    }

    @ApiOperation("词典-词条分页查找")
    @GetMapping("/{dictId}/word")
    public ApiReturn<Page<WordRsp>> findAll(
            @PathVariable("dictId") Long dictId,
            BaseReq baseReq) {
        return ApiReturn.success(wordService.findAll(dictId,baseReq));
    }

    @ApiOperation("词典-词条根据Id查找")
    @GetMapping("/{dictId}/word/{wordId}")
    public ApiReturn<WordRsp> findById(
            @PathVariable("dictId") Long dictId,
            @PathVariable("wordId") String wordId) {
        return ApiReturn.success(wordService.findById(dictId, wordId));
    }

    @ApiOperation("词典-词条创建")
    @PostMapping("/{dictId}/word")
    public ApiReturn<WordRsp> insert(
            @PathVariable("dictId") Long dictId,
            @Valid @RequestBody WordReq req) {
        return ApiReturn.success(wordService.insert(dictId, req));
    }

    @ApiOperation("词典-词条编辑")
    @PatchMapping("/{dictId}/word/{wordId}")
    public ApiReturn<WordRsp> update(
            @PathVariable("dictId") Long dictId,
            @PathVariable("wordId") String wordId,
            @Valid @RequestBody WordReq req) {
        return ApiReturn.success(wordService.update(dictId, wordId, req));
    }

    @ApiOperation("词典-词条删除")
    @DeleteMapping("/{dictId}/word/{wordId}")
    public ApiReturn delete(@PathVariable("dictId") Long dictId,
                            @PathVariable("wordId") String wordId) {
        wordService.delete(dictId, wordId);
        return ApiReturn.success();
    }

}
