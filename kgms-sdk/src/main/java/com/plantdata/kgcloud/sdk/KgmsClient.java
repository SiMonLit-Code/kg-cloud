package com.plantdata.kgcloud.sdk;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.bean.BasePage;
import ai.plantdata.cloud.bean.BaseReq;
import com.plantdata.kgcloud.sdk.exection.client.KgmsClientEx;
import com.plantdata.kgcloud.sdk.req.*;
import com.plantdata.kgcloud.sdk.rsp.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "kgms", contextId = "kgms",fallback = KgmsClientEx.class)
public interface KgmsClient {

    @ApiOperation("图谱查找所有")
    @GetMapping("/graph/all")
    ApiReturn<List<GraphRsp>> graphFindAll();

    @ApiOperation("图谱分页查找")
    @GetMapping("/graph/")
    ApiReturn<BasePage<GraphRsp>> graphFindAll(@RequestParam("kw") String kw, @RequestParam("page") Integer page, @RequestParam("size") Integer size);

    @ApiOperation("图谱根据Id查找")
    @GetMapping("/graph/{kgName}")
    ApiReturn<GraphRsp> graphFindById(@PathVariable("kgName") String kgName);

    @ApiOperation("图谱根据kgName查找dbName")
    @GetMapping("/graph/kgName/{kgName}")
    ApiReturn<String> graphFindDbNameByKgName(@PathVariable("kgName") String kgName);

    @ApiOperation("图谱创建")
    @PostMapping("/graph/")
    ApiReturn<GraphRsp> graphInsert(@Valid @RequestBody GraphReq dictionaryReq);

    @ApiOperation("图谱编辑")
    @PatchMapping("/graph/{kgName}")
    ApiReturn<GraphRsp> graphUpdate(@PathVariable("kgName") String kgName, @Valid @RequestBody GraphReq req);

    @ApiOperation("图谱删除")
    @DeleteMapping("/graph/{kgName}")
    ApiReturn graphDelete(@PathVariable("kgName") String kgName);

    @GetMapping("/share/status/{kgName}/{spaId}")
    @ApiOperation("SPA分享状态")
    ApiReturn<LinkShareSpaRsp> shareStatus(@PathVariable("kgName") String kgName, @PathVariable("spaId") String spaId);


    @GetMapping("/share/status")
    @ApiOperation("分享状态分享")
    ApiReturn<SelfSharedRsp> shareSpaStatus(@RequestParam("kgName") String kgName, @RequestParam("spaId") String spaId, @RequestParam("token") String token);

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
     * @param kgName 图谱名称
     * @param page   页 默认1
     * @param size   每页显示数量 默认10
     * @return ApiReturn<BasePage < GraphConfAlgorithmRsp>>
     */
    @GetMapping("/config/algorithm/{kgName}")
    ApiReturn<BasePage<GraphConfAlgorithmRsp>> select(@PathVariable("kgName") String kgName,
                                                      @RequestParam(value = "type", required = false, defaultValue = "1") Integer type,
                                                      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                      @RequestParam(value = "size", required = false, defaultValue = "10") Integer size);

    /**
     * 图谱配置-算法-详情
     *
     * @param id
     * @return
     */
    @GetMapping("/config/algorithm/detail/{id}")
    ApiReturn<GraphConfAlgorithmRsp> detailAlgorithm(@PathVariable("id") Long id);

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
     * @param kgName   图谱名称
     * @param ruleType 规则类型
     * @param page     分页
     * @param size     数量
     * @return
     */
    @GetMapping("config/kgql/{kgName}/{ruleType}")
    ApiReturn<BasePage<GraphConfKgqlRsp>> selectKgql(@PathVariable("kgName") String kgName, @PathVariable("ruleType") Integer ruleType,
                                                     @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                     @RequestParam(value = "size", required = false, defaultValue = "10") Integer size);

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
    ApiReturn<List<GraphConfStatisticalRsp>> updateStatisticalBatch(@RequestBody @Valid List<UpdateGraphConfStatisticalReq> reqs);

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
    @PutMapping("/config/statistical/batch/delete")
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
    @GetMapping("/config/statistical/page/{kgName}")
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
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/config/reason/page/{kgName}")
    ApiReturn<BasePage<GraphConfReasonRsp>> selectReasoningPage(@PathVariable("kgName") String kgName,
                                                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                                @RequestParam(value = "size", required = false, defaultValue = "10") Integer size);

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

}
