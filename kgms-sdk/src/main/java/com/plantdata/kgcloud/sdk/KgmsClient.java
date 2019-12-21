package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.sdk.req.DataSetCreateReq;
import com.plantdata.kgcloud.sdk.req.DataSetPageReq;
import com.plantdata.kgcloud.sdk.req.DictionaryReq;
import com.plantdata.kgcloud.sdk.req.FolderReq;
import com.plantdata.kgcloud.sdk.req.GraphConfAlgorithmReq;
import com.plantdata.kgcloud.sdk.req.GraphConfFocusReq;
import com.plantdata.kgcloud.sdk.req.GraphConfKgqlReq;
import com.plantdata.kgcloud.sdk.req.GraphConfQaReq;
import com.plantdata.kgcloud.sdk.req.GraphConfReasonReq;
import com.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import com.plantdata.kgcloud.sdk.req.GraphPageReq;
import com.plantdata.kgcloud.sdk.req.GraphReq;
import com.plantdata.kgcloud.sdk.req.KgmsCallReq;
import com.plantdata.kgcloud.sdk.req.WordReq;
import com.plantdata.kgcloud.sdk.rsp.DataSetRsp;
import com.plantdata.kgcloud.sdk.rsp.DictionaryRsp;
import com.plantdata.kgcloud.sdk.rsp.FolderRsp;
import com.plantdata.kgcloud.sdk.rsp.GraphConfAlgorithmRsp;
import com.plantdata.kgcloud.sdk.rsp.GraphConfFocusRsp;
import com.plantdata.kgcloud.sdk.rsp.GraphConfKgqlRsp;
import com.plantdata.kgcloud.sdk.rsp.GraphConfQaRsp;
import com.plantdata.kgcloud.sdk.rsp.GraphConfReasonRsp;
import com.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;
import com.plantdata.kgcloud.sdk.rsp.GraphRsp;
import com.plantdata.kgcloud.sdk.rsp.WordRsp;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
    ApiReturn<BasePage<DataSetRsp>> dataSetFindAll(DataSetPageReq req);

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
    ApiReturn<BasePage<Map<String, Object>>> dataOptFindAll(@PathVariable("datasetId") Long datasetId, @RequestParam("page") Integer page, @RequestParam("size") Integer size);

    @ApiOperation("数据集-数据-分页查询")
    @GetMapping("/dataset/{datasetId}/data/{dataId}")
    ApiReturn<Map<String, Object>> dataOptFindById(@PathVariable("datasetId") Long datasetId, @PathVariable("dataId") String dataId);

    @ApiOperation("数据集-数据-插入")
    @PostMapping("/dataset/{datasetId}/data")
    ApiReturn<Map<String, Object>> dataOptInsert(@PathVariable("datasetId") Long datasetId, @RequestBody Map<String, Object> data);

    @ApiOperation("数据集-数据-修改")
    @PatchMapping("/dataset/{datasetId}/data/{dataId}")
    ApiReturn<Map<String, Object>> dataOptUpdate(@PathVariable("datasetId") Long datasetId, @PathVariable("dataId") String dataId, @RequestBody Map<String, Object> data);

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
    ApiReturn<BasePage<DictionaryRsp>> dictionaryFindAll(BaseReq baseReq);

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
    ApiReturn<BasePage<WordRsp>> wordFindAll(@PathVariable("dictId") Long dictId, BaseReq baseReq);

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
    ApiReturn<BasePage<GraphRsp>> graphFindAll(GraphPageReq req);

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

    /**
     * 图谱配置-算法-新建
     *
     * @param kgName
     * @param req
     * @return
     */
    @PostMapping("/config/algorithm/{kgName}")
    ApiReturn<GraphConfAlgorithmRsp> save(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfAlgorithmReq req);

    /**
     * 图谱配置-算法-更新
     *
     * @param id
     * @param req
     * @return
     */
    @PutMapping("/config/algorithm/{id}")
    ApiReturn<GraphConfAlgorithmRsp> update(@PathVariable("id") Long id, @RequestBody @Valid GraphConfAlgorithmReq req);

    /**
     * 图谱配置-算法-删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/config/algorithm/{id}")
    ApiReturn delete(@PathVariable("id") Long id);

    /**
     * 图谱配置-算法-获取
     *
     * @param kgName
     * @param baseReq
     * @return
     */
    @GetMapping("/config/algorithm/{kgName}")
    ApiReturn<BasePage<GraphConfAlgorithmRsp>> select(@PathVariable("kgName") String kgName, BaseReq baseReq);

    /**
     * 图谱配置-焦点-获取
     *
     * @param kgName
     * @return
     */
    @GetMapping("/config/focus/{kgName}")
    ApiReturn<List<GraphConfFocusRsp>> find(@PathVariable("kgName") String kgName);

    /**
     * 图谱配置-焦点-保存
     *
     * @param kgName
     * @param req
     * @return
     */
    @PutMapping("/config/focus/{kgName}")
    ApiReturn<List<GraphConfFocusRsp>> saveFocus(@PathVariable("kgName") String kgName, @RequestBody @Valid List<GraphConfFocusReq> req);

    /**
     * 图谱配置-KGQL-新建
     *
     * @param kgName
     * @param req
     * @return
     */
    @PostMapping("config/kgql/{kgName}")
    ApiReturn<GraphConfKgqlRsp> saveKgql(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfKgqlReq req);

    /**
     * 图谱配置-KGQL-更新
     *
     * @param id
     * @param req
     * @return
     */
    @PutMapping("/config/kgql/{id}")
    ApiReturn<GraphConfKgqlRsp> updateKgql(@PathVariable("id") Long id, @RequestBody @Valid GraphConfKgqlReq req);

    /**
     * 图谱配置-KGQL-删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/config/kgql/{id}")
    ApiReturn deleteKgql(@PathVariable("id") Long id);

    /**
     * 图谱配置-KGQL-查询
     *
     * @param kgName
     * @param ruleType
     * @param baseReq
     * @return
     */
    @GetMapping("config/kgql/{kgName}/{ruleType}")
    ApiReturn<BasePage<GraphConfKgqlRsp>> selectKgql(@PathVariable("kgName") String kgName, @PathVariable("ruleType") Integer ruleType,
                                                     BaseReq baseReq);

    /**
     * 图谱配置-KGQL-详情
     *
     * @param id
     * @return
     */
    @GetMapping("config/kgql/detail/{id}")
    ApiReturn<GraphConfKgqlRsp> detailKgql(@PathVariable("id") Long id);

    /**
     * 图谱配置-问答-新建
     *
     * @param kgName
     * @param req
     * @return
     */
    @PostMapping("/config/qa/{kgName}")
    ApiReturn<List<GraphConfQaRsp>> save(@PathVariable("kgName") String kgName, @RequestBody @Valid List<GraphConfQaReq> req);

    /**
     * 图谱配置-问答-获取
     *
     * @param kgName
     * @return
     */
    @GetMapping("/config/qa/{kgName}")
    ApiReturn<List<GraphConfQaRsp>> selectQa(@PathVariable("kgName") String kgName);

    /**
     * 图谱配置-统计-新建
     *
     * @param kgName
     * @param req
     * @return
     */
    @PostMapping("/config/statistical/{kgName}")
    ApiReturn<GraphConfStatisticalRsp> saveStatistical(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfStatisticalReq req);

    /**
     * 图谱配置-统计-批量新建
     *
     * @param listReq
     * @return
     */
    @PostMapping("/config/statistical/batch/save")
    ApiReturn<List<GraphConfStatisticalRsp>> saveStatisticalBatch(@RequestBody @Valid List<GraphConfStatisticalReq> listReq);

    /**
     * 图谱配置-统计-更新
     *
     * @param id
     * @param req
     * @return
     */
    @PutMapping("/config/statistical/{id}")
    ApiReturn<GraphConfStatisticalRsp> updateStatistical(@PathVariable("id") Long id, @RequestBody @Valid GraphConfStatisticalReq req);

    /**
     * 图谱配置-统计-批量更新
     *
     * @param reqs
     * @return
     */
    @PutMapping("/config/statistical/batch/update")
    ApiReturn<List<GraphConfStatisticalRsp>> updateStatisticalBatch(@RequestBody @Valid List<GraphConfStatisticalReq> reqs);

    /**
     * 图谱配置-统计-删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/config/statistical/{id}")
    ApiReturn deleteStatistical(@PathVariable("id") Long id);

    /**
     * 图谱配置-统计-批量删除
     *
     * @param ids
     * @return
     */
    @PutMapping("/config/statistical/batch")
    ApiReturn deleteStatisticalBatch(@RequestBody List<Long> ids);

    /**
     * 图谱配置-统计-查询
     *
     * @param kgName
     * @return
     */
    @GetMapping("/config/statistical/{kgName}")
    ApiReturn<List<GraphConfStatisticalRsp>> selectStatistical(@PathVariable("kgName") String kgName);

    /**
     * 图谱配置-统计-分页
     *
     * @param kgName
     * @param baseReq
     * @return
     */
    @GetMapping("/config/statistical/{kgName}")
    ApiReturn<BasePage<GraphConfStatisticalRsp>> selectStatisticalPage(@PathVariable("kgName") String kgName, BaseReq baseReq);

    /**
     * 图谱配置-推理-新增
     *
     * @param kgName
     * @param req
     * @return
     */
    @PostMapping("/config/reason/{kgName}")
    ApiReturn<GraphConfReasonRsp> saveReasoning(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfReasonReq req);

    /**
     * 图谱配置-推理-分页
     *
     * @param kgName
     * @param baseReq
     * @return
     */
    @GetMapping("/config/reason/page/{kgName}")
    ApiReturn<BasePage<GraphConfReasonRsp>> selectReasoningPage(@PathVariable("kgName") String kgName, BaseReq baseReq);

    /**
     * 图谱配置-推理-详情
     *
     * @param id
     * @return
     */
    @GetMapping("/config/reason/{id}")
    ApiReturn<GraphConfReasonRsp> detailReasoning(@PathVariable("id") Long id);

    /**
     * 图谱配置-推理-删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/config/reason/{id}")
    ApiReturn deleteReasoning(@PathVariable("id") Long id);

    /**
     * 图谱配置-推理-更新
     *
     * @param id
     * @param req
     * @return
     */
    @PutMapping("/config/reason/{id}")
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
