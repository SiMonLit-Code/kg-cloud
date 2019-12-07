package com.plantdata.kgcloud.sdk;

import com.fasterxml.jackson.databind.JsonNode;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.sdk.req.*;
import com.plantdata.kgcloud.sdk.rsp.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@FeignClient(value = "kgms", contextId = "kgms")
public interface KgmsClient {

    @ApiOperation("数据集查找所有")
    @GetMapping("/dataset/all")
    ApiReturn<List<DataSetRsp>> dataSetFindAll();

    @ApiOperation("数据集分页查找")
    @GetMapping("/dataset/")
    ApiReturn<Page<DataSetRsp>> dataSetFindAll(DataSetPageReq req);

    @ApiOperation("数据集根据Id查找")
    @GetMapping("/dataset/{id}")
    ApiReturn<DataSetRsp> dataSetFindById(@PathVariable Long id);

    @ApiOperation("数据集创建")
    @PostMapping("/dataset/")
    ApiReturn<DataSetRsp> dataSetInsert(@Valid @RequestBody DataSetReq req);

    @ApiOperation("数据集编辑")
    @PatchMapping("/dataset/{id}")
    ApiReturn<DataSetRsp> dataSetUpdate(@PathVariable Long id, @Valid @RequestBody DataSetReq req);

    @ApiOperation("数据集删除")
    @DeleteMapping("/dataset/{id}")
    ApiReturn dataSetDelete(@PathVariable Long id);

    @ApiOperation("数据集-文件夹-列表")
    @GetMapping("/dataset/folder")
    ApiReturn<List<FolderRsp>> dataSetFolderList();

    @ApiOperation("数据集-文件夹-默认文件夹")
    @GetMapping("/dataset/folder/default")
    ApiReturn<FolderRsp> folderDefault();

    @ApiOperation("数据集-文件夹-创建")
    @PostMapping("/dataset/folder")
    ApiReturn folderInsert(@Valid @RequestBody FolderReq req);

    @ApiOperation("数据集-文件夹-修改")
    @PatchMapping("/dataset/folder/{folderId}")
    ApiReturn folderUpdate(@PathVariable("folderId") Long folderId, @Valid @RequestBody FolderReq req);

    @ApiOperation("数据集-文件夹-删除")
    @DeleteMapping("/dataset/folder/{folderId}")
    ApiReturn folderDelete(@PathVariable("folderId") Long folderId, Boolean deleteData);

    @ApiOperation("数据集-移动-文件夹")
    @PostMapping("/dataset/folder/{folderId}/move")
    ApiReturn folderDataSetMoveto(@PathVariable("folderId") Long folderId, @RequestBody List<Long> datasetIds);

    @ApiOperation("数据集-数据-分页条件查询")
    @GetMapping("/dataset/{datasetId}/data")
    ApiReturn<List<Map<String, Object>>> dataOptFindAll(@PathVariable("datasetId") Long datasetId, DataOptQueryReq baseReq);

    @ApiOperation("数据集-数据-分页查询")
    @GetMapping("/dataset/{datasetId}/data/{dataId}")
    ApiReturn<Map<String, Object>> dataOptFindById(@PathVariable("datasetId") Long datasetId, @PathVariable("dataId") String dataId);

    @ApiOperation("数据集-数据-插入")
    @PostMapping("/dataset/{datasetId}/data")
    ApiReturn<Map<String, Object>> dataOptInsert(@PathVariable("datasetId") Long datasetId, @RequestBody JsonNode data);

    @ApiOperation("数据集-数据-修改")
    @PatchMapping("/dataset/{datasetId}/data/{dataId}")
    ApiReturn<Map<String, Object>> dataOptUpdate(@PathVariable("datasetId") Long datasetId, @PathVariable("dataId") String dataId, @RequestBody JsonNode data);

    @ApiOperation("数据集-数据-根据Id删除")
    @DeleteMapping("/dataset/{datasetId}/data/{dataId}")
    ApiReturn dataOptDelete(@PathVariable("datasetId") Long datasetId, @PathVariable("dataId") String dataId);

    @ApiOperation("数据集-数据-全部删除")
    @DeleteMapping("/dataset/{datasetId}/data")
    ApiReturn dataOptDeleteAll(@PathVariable("datasetId") Long datasetId);

    @ApiOperation("数据集-数据-批量删除")
    @PatchMapping("/dataset/{datasetId}/data")
    ApiReturn dataOptBatchDelete(@PathVariable("datasetId") Long datasetId, @RequestBody List<String> ids);

    @ApiOperation("词典查找所有")
    @GetMapping("/dictionary/all")
    ApiReturn<List<DictionaryRsp>> dictionaryFindAll();

    @ApiOperation("词典分页查找")
    @GetMapping("/dictionary/")
    ApiReturn<Page<DictionaryRsp>> dictionaryFindAll(BaseReq baseReq);

    @ApiOperation("词典根据Id查找")
    @GetMapping("/dictionary/{id}")
    ApiReturn<DictionaryRsp> dictionaryFindById(@PathVariable Long id);

    @ApiOperation("词典创建")
    @PostMapping("/dictionary/")
    ApiReturn<DictionaryRsp> dictionaryInsert(@Valid @RequestBody DictionaryReq dictionaryReq);

    @ApiOperation("词典编辑")
    @PatchMapping("/dictionary/{id}")
    ApiReturn<DictionaryRsp> dictionaryUpdate(@PathVariable Long id, @Valid @RequestBody DictionaryReq req);

    @ApiOperation("词典删除")
    @DeleteMapping("/dictionary/{id}")
    ApiReturn dictionaryDelete(@PathVariable Long id);

    @ApiOperation("词典-词条查找所有")
    @GetMapping("/dictionary/{dictId}/word/all")
    ApiReturn<List<WordRsp>> wordFindAll(@PathVariable("dictId") Long dictId);

    @ApiOperation("词典-词条分页查找")
    @GetMapping("/dictionary/{dictId}/word")
    ApiReturn<Page<WordRsp>> wordFindAll(@PathVariable("dictId") Long dictId, BaseReq baseReq);

    @ApiOperation("词典-词条根据Id查找")
    @GetMapping("/dictionary/{dictId}/word/{wordId}")
    ApiReturn<WordRsp> wordFindById(@PathVariable("dictId") Long dictId, @PathVariable("wordId") String wordId);

    @ApiOperation("词典-词条创建")
    @PostMapping("/dictionary/{dictId}/word")
    ApiReturn<WordRsp> wordInsert(@PathVariable("dictId") Long dictId, @Valid @RequestBody WordReq req);

    @ApiOperation("词典-词条编辑")
    @PatchMapping("/dictionary/{dictId}/word/{wordId}")
    ApiReturn<WordRsp> wordUpdate(@PathVariable("dictId") Long dictId, @PathVariable("wordId") String wordId, @Valid @RequestBody WordReq req);

    @ApiOperation("词典-词条删除")
    @DeleteMapping("/dictionary/{dictId}/word/{wordId}")
    ApiReturn wordDelete(@PathVariable("dictId") Long dictId, @PathVariable("wordId") String wordId);

    @ApiOperation("图谱查找所有")
    @GetMapping("/graph/all")
    ApiReturn<List<GraphRsp>> graphFindAll();

    @ApiOperation("图谱分页查找")
    @GetMapping("/graph/")
    ApiReturn<Page<GraphRsp>> graphFindAll(GraphPageReq req);

    @ApiOperation("图谱根据Id查找")
    @GetMapping("/graph/{kgName}")
    ApiReturn<GraphRsp> graphFindById(@PathVariable("kgName") String kgName);

    @ApiOperation("图谱创建")
    @PostMapping("/graph/")
    ApiReturn<GraphRsp> graphInsert(@Valid @RequestBody GraphReq dictionaryReq);

    @ApiOperation("图谱编辑")
    @PatchMapping("/graph/{kgName}")
    ApiReturn<GraphRsp> graphUpdate(@PathVariable("kgName") String kgName, @Valid @RequestBody GraphReq req);

    @ApiOperation("图谱删除")
    @DeleteMapping("/graph/{kgName}")
    ApiReturn graphDelete(@PathVariable("kgName") String kgName);

    @ApiOperation("图谱新建算法")
    @PostMapping("/config/algorithm/{kgName}")
    ApiReturn<GraphConfAlgorithmRsp> save(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfAlgorithmReq req);

    @ApiOperation("图谱更新算法")
    @PatchMapping("/config/algorithm/{kgName}/{id}")
    ApiReturn<GraphConfAlgorithmRsp> update(@PathVariable("id") Long id, @RequestBody @Valid GraphConfAlgorithmReq req);

    @ApiOperation("图谱删除算法")
    @DeleteMapping("/config/algorithm/{kgName}/{id}")
    void delete(@PathVariable("id") Long id);

    @ApiOperation("图谱查询算法")
    @GetMapping("/config/algorithm/{kgName}")
    ApiReturn<Page<GraphConfAlgorithmRsp>> select(@PathVariable("kgName") String kgName, BaseReq baseReq);

    @ApiOperation("图谱焦点")
    @GetMapping("/config/focus/{kgName}")
    ApiReturn<List<GraphConfFocusRsp>> find(@PathVariable("kgName") String kgName);

    @ApiOperation("图谱焦点保存")
    @PatchMapping("/config/focus/{kgName}")
    ApiReturn<List<GraphConfFocusRsp>> save(@PathVariable("kgName") String kgName, @RequestBody @Valid List<GraphConfFocusReq> req);

    @ApiOperation("图谱新建业务")
    @PostMapping("/config/kgql/{kgName}")
    ApiReturn<GraphConfKgqlRsp> save(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfKgqlReq req);

    @ApiOperation("图谱更新业务")
    @PatchMapping("/config/kgql/{kgName}/{id}")
    ApiReturn<GraphConfKgqlRsp> update(@PathVariable("id") Long id, @RequestBody @Valid GraphConfKgqlReq req);

    @ApiOperation("图谱删除业务")
    @DeleteMapping("/config/kgql/{kgName}/{id}")
    void deleteKgql(@PathVariable("id") Long id);

    @ApiOperation("图谱查询业务")
    @GetMapping("/config/qa/{kgName}")
    ApiReturn<List<GraphConfQaRsp>> select(@PathVariable("kgName") String kgName);

    @ApiOperation("图谱新建问答")
    @PostMapping("/config/qa/{kgName}")
    ApiReturn<List<GraphConfQaRsp>> saveQa(@PathVariable("kgName") String kgName, @RequestBody @Valid List<GraphConfQaReq> req);

    @ApiOperation("图谱查询问答")
    @GetMapping("/config/qa/{kgName}")
    ApiReturn<List<GraphConfQaRsp>> selectQa(@PathVariable("kgName") String kgName);

    @ApiOperation("图谱新建统计")
    @PostMapping("/config/statistical/{kgName}")
    ApiReturn<GraphConfStatisticalRsp> saveStatistical(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfStatisticalReq req);

    @ApiOperation("图谱更新统计")
    @PatchMapping("/config/statistical/{kgName}/{id}")
    ApiReturn<GraphConfStatisticalRsp> updateStatistical(@PathVariable("id") Long id, @RequestBody @Valid GraphConfStatisticalReq req);

    @ApiOperation("图谱删除统计")
    @DeleteMapping("/config/statistical/{kgName}/{id}")
    void deleteStatistical(@PathVariable("id") Long id);

    @ApiOperation("图谱查询统计")
    @GetMapping("/config/statistical/{kgName}")
    ApiReturn<List<GraphConfStatisticalRsp>> selectStatistical(@PathVariable("kgName") String kgName);

    /**
     * 模型调用
     *
     * @param id  id
     * @param req req
     * @return .
     */
    @PostMapping("model/call/{id}")
    ApiReturn callJson(@PathVariable Long id, @RequestBody WordReq.ModelCallReq req);

}
