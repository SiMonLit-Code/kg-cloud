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
    ApiReturn<DataSetRsp> dataSetFindById(@PathVariable("id") Long id);

    @ApiOperation("数据集创建")
    @PostMapping("/dataset/")
    ApiReturn<DataSetRsp> dataSetInsert(@Valid @RequestBody DataSetCreateReq req);

    @ApiOperation("数据集编辑")
    @PatchMapping("/dataset/{id}")
    ApiReturn<DataSetRsp> dataSetUpdate(@PathVariable("id") Long id, @Valid @RequestBody DataSetCreateReq req);

    @ApiOperation("数据集删除")
    @DeleteMapping("/dataset/{id}")
    ApiReturn dataSetDelete(@PathVariable("id") Long id);

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
    ApiReturn<DictionaryRsp> dictionaryFindById(@PathVariable("id") Long id);

    @ApiOperation("词典创建")
    @PostMapping("/dictionary/")
    ApiReturn<DictionaryRsp> dictionaryInsert(@Valid @RequestBody DictionaryReq dictionaryReq);

    @ApiOperation("词典编辑")
    @PatchMapping("/dictionary/{id}")
    ApiReturn<DictionaryRsp> dictionaryUpdate(@PathVariable("id") Long id, @Valid @RequestBody DictionaryReq req);

    @ApiOperation("词典删除")
    @DeleteMapping("/dictionary/{id}")
    ApiReturn dictionaryDelete(@PathVariable("id") Long id);

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

    @ApiOperation("图谱配置-算法-新建")
    @PostMapping("/config/algorithm/{kgName}")
    ApiReturn<GraphConfAlgorithmRsp> save(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfAlgorithmReq req);

    @ApiOperation("图谱配置-算法-更新")
    @PatchMapping("/config/algorithm/{id}")
    public ApiReturn<GraphConfAlgorithmRsp> update(@PathVariable("id") Long id, @RequestBody @Valid GraphConfAlgorithmReq req);

    @ApiOperation("图谱配置-算法-删除")
    @DeleteMapping("/config/algorithm/{id}")
    ApiReturn delete(@PathVariable("id") Long id);

    @ApiOperation("图谱配置-算法-获取")
    @GetMapping("/config/algorithm/{kgName}")
    ApiReturn<Page<GraphConfAlgorithmRsp>> select(@PathVariable("kgName") String kgName, BaseReq baseReq);

    @ApiOperation("图谱配置-焦点-获取")
    @GetMapping("/config/focus/{kgName}")
    ApiReturn<List<GraphConfFocusRsp>> find(@PathVariable("kgName") String kgName);

    @ApiOperation("图谱配置-焦点-保存")
    @PatchMapping("/config/focus/{kgName}")
    ApiReturn<List<GraphConfFocusRsp>> saveFocus(@PathVariable("kgName") String kgName, @RequestBody @Valid List<GraphConfFocusReq> req);

    @ApiOperation("图谱配置-KGQL-新建")
    @PostMapping("/kgql/{kgName}")
    ApiReturn<GraphConfKgqlRsp> saveKgql(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfKgqlReq req);

    @ApiOperation("图谱配置-KGQL-更新")
    @PatchMapping("/config/kgql/{id}")
    ApiReturn<GraphConfKgqlRsp> updateKgql(@PathVariable("id") Long id, @RequestBody @Valid GraphConfKgqlReq req);

    @ApiOperation("图谱配置-KGQL-删除")
    @DeleteMapping("/config/kgql/{id}")
    ApiReturn deleteKgql(@PathVariable("id") Long id);

    @ApiOperation("图谱配置-KGQL-查询")
    @GetMapping("/config/kgql/{kgName}")
    ApiReturn<Page<GraphConfKgqlRsp>> selectKgql(@PathVariable("kgName") String kgName, BaseReq baseReq);

    @ApiOperation("图谱配置-KGQL-详情")
    @GetMapping("/kgql/{id}")
    public ApiReturn<GraphConfKgqlRsp> detailKgql(@PathVariable("id") Long id);

    @ApiOperation("图谱配置-问答-新建")
    @PostMapping("/config/qa/{kgName}")
    ApiReturn<List<GraphConfQaRsp>> save(@PathVariable("kgName") String kgName, @RequestBody @Valid List<GraphConfQaReq> req);

    @ApiOperation("图谱配置-问答-获取")
    @GetMapping("/config/qa/{kgName}")
    ApiReturn<List<GraphConfQaRsp>> selectQa(@PathVariable("kgName") String kgName);

    @ApiOperation("图谱配置-统计-新建")
    @PostMapping("/config/statistical/{kgName}")
    ApiReturn<GraphConfStatisticalRsp> saveStatistical(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfStatisticalReq req);

    @ApiOperation("图谱配置-统计-批量新建")
    @PostMapping("/config/statistical/batch/save")
    ApiReturn<List<GraphConfStatisticalRsp>> saveStatisticalBatch( @RequestBody @Valid List<GraphConfStatisticalReq> listReq);

    @ApiOperation("图谱配置-统计-更新")
    @PatchMapping("/config/statistical/{id}")
    ApiReturn<GraphConfStatisticalRsp> updateStatistical(@PathVariable("id") Long id, @RequestBody @Valid GraphConfStatisticalReq req);

    @ApiOperation("图谱配置-统计-批量更新")
    @PatchMapping("/config/statistical/batch/update")
    ApiReturn<List<GraphConfStatisticalRsp>> updateStatisticalBatch( @RequestBody @Valid List<GraphConfStatisticalReq> reqs);

    @ApiOperation("图谱配置-统计-删除")
    @DeleteMapping("/config/statistical/{id}")
    ApiReturn deleteStatistical(@PathVariable("id") Long id);

    @ApiOperation("图谱配置-统计-批量删除")
    @PatchMapping("/config/statistical/batch")
    ApiReturn deleteStatisticalBatch(@RequestBody List<Long> ids);

    @ApiOperation("图谱配置-统计-查询")
    @GetMapping("/config/statistical/{kgName}")
    public ApiReturn<List<GraphConfStatisticalRsp>> selectStatistical(@PathVariable("kgName") String kgName);

    @ApiOperation("图谱配置-统计-分页")
    @GetMapping("/config/statistical/{kgName}")
    ApiReturn<Page<GraphConfStatisticalRsp>> selectStatisticalPage(@PathVariable("kgName") String kgName , BaseReq baseReq);

    @ApiOperation("图谱配置-推理-新增")
    @PostMapping("/config/reason/{kgName}")
    ApiReturn<GraphConfReasonRsp> saveReasoning(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfReasonReq req);

    @ApiOperation("图谱配置-推理-分页")
    @GetMapping("/config/reason/{kgName}")
    ApiReturn<Page<GraphConfReasonRsp>> selectReasoninglPage(@PathVariable("kgName") String kgName , BaseReq baseReq);

    @ApiOperation("图谱配置-推理-详情")
    @GetMapping("/config/reason/{id}")
    ApiReturn<GraphConfReasonRsp> detailReasoning(@PathVariable("id") Long id);

    @ApiOperation("图谱配置-推理-删除")
    @DeleteMapping("/config/reason/{kgName}/{id}")
    ApiReturn deleteReasoning(@PathVariable("id") Long id);

    @ApiOperation("图谱配置-推理-更新")
    @PatchMapping("/config/reason/{kgName}/{id}")
    ApiReturn<GraphConfReasonRsp> updateReasoning(@PathVariable("id") Long id, @RequestBody @Valid GraphConfReasonReq req);


    /**
     * 模型调用
     *
     * @param id  id
     * @param req req
     * @return .
     */
    @PostMapping("model/call/{id}")
    ApiReturn callJson(@PathVariable("id") Long id, @RequestBody KgmsCallReq req);


}
