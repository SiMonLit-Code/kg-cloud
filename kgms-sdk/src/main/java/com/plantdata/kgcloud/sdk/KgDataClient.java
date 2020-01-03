package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.app.AttrDefQueryReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.DataSetAddReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.NameReadReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeAttrStatisticByAttrValueReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByConceptIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByEntityIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByAttrIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByConceptReq;
import com.plantdata.kgcloud.sdk.req.edit.ConceptAddReq;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 14:02
 */
@FeignClient(value = "kgms", path = "kgdata", contextId = "kgDataClient")
public interface KgDataClient {

    /**
     * 第三方模型抽取
     *
     * @param modelId 模型id
     * @param input   。
     * @param config  配置
     * @return 。
     */
    @PostMapping("extract/thirdModel/{modelId}")
    ApiReturn<Object> extractThirdModel(@PathVariable("modelId") Long modelId,
                                        @RequestParam("input") String input, @RequestParam("config") String config);

    /**
     * 查询实体的关系度数
     *
     * @param kgName
     * @param statisticReq
     * @return
     */
    @PostMapping("statistic/{kgName}/entity/degree/")
    ApiReturn<List<Map<String, Object>>> statisticCountEdgeByEntity(@PathVariable("kgName") String kgName,
                                                                    @RequestBody EdgeStatisticByEntityIdReq statisticReq);

    /**
     * 统计实体根据概念分组
     *
     * @param kgName
     * @param statisticReq
     * @return
     */
    @PostMapping("statistic/{kgName}/entity/groupByConcept/")
    ApiReturn<Object> statisticEntityGroupByConcept(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                    @RequestBody EntityStatisticGroupByConceptReq statisticReq);

    /**
     * 实体属性值统计
     *
     * @param kgName
     * @param statisticReq
     * @return
     */
    @PostMapping("statistic/{kgName}/attr/groupByAttrValue")
    ApiReturn<Object> statisticAttrGroupByConcept(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                  @RequestBody EntityStatisticGroupByAttrIdReq statisticReq);

    /**
     * 对象属性统计，统计对象属性的数量，按关系分组
     *
     * @param kgName
     * @param statisticReq
     * @return
     */
    @PostMapping("statistic/{kgName}/edge/groupByAttrName")
    ApiReturn<Object> statisticRelation(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                        @RequestBody EdgeStatisticByConceptIdReq statisticReq);

    /**
     * 边数值属性统计，按数值属性值分组
     *
     * @param kgName
     * @param statisticReq
     * @return
     */
    @PostMapping("statistic/{kgName}/edgeAttr/groupByAttrValue")
    ApiReturn<Object> statEdgeGroupByEdgeValue(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                               @RequestBody EdgeAttrStatisticByAttrValueReq statisticReq);

    /**
     * 读取数据集
     *
     * @param nameReadReq 搜索参数
     * @return 。
     */
    @PostMapping("dataset/read")
    ApiReturn<RestData<Map<String, Object>>> searchDataSet(@RequestBody NameReadReq nameReadReq);

    /**
     * 批量新增数据集
     *
     * @param addReq
     * @return
     */
    @PostMapping("dataset/name")
    ApiReturn batchSaveDataSetByName(@RequestBody DataSetAddReq addReq);

    /**
     * 查询概念
     *
     * @param kgName        图谱名称
     * @param conceptAddReq req
     * @return 。
     */
    @PostMapping("concept/{kgName}")
    ApiReturn<Long> createConcept(@PathVariable("kgName") String kgName,
                                  @RequestBody ConceptAddReq conceptAddReq);

    /**
     * 根据概念查询属性定义
     *
     * @param kgName   。
     * @param queryReq 。
     * @return 。
     */
    @PostMapping("/{kgName}/attribute/search")
    ApiReturn<List<AttrDefinitionRsp>> searchAttrDefByConcept(@PathVariable("kgName") String kgName, @RequestBody AttrDefQueryReq queryReq);
}
