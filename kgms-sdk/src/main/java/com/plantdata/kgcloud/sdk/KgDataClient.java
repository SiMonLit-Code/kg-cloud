package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.sdk.exection.client.KgDataClientEx;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryWithConditionReq;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.req.app.SparQlReq;
import com.plantdata.kgcloud.sdk.req.app.TraceabilityQueryReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.*;
import com.plantdata.kgcloud.sdk.req.edit.ConceptAddReq;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import com.plantdata.kgcloud.sdk.rsp.app.sparql.QueryResultRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 14:02
 */
@FeignClient(value = "kgms", path = "kgdata", contextId = "kgDataClient",fallback = KgDataClientEx.class)
public interface KgDataClient {


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
    @PostMapping("statistic/{kgName}/attr/value")
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
     * @param kgName
     * @param conceptId
     * @param conceptKey
     * @param inherit
     * @return
     */
    @GetMapping("/{kgName}/attribute/search")
    ApiReturn<List<AttrDefinitionRsp>> searchAttrDefByConcept(@PathVariable("kgName") String kgName,
                                                              @RequestParam(value = "conceptId", required = false) Long conceptId,
                                                              @RequestParam(value = "conceptKey", required = false) String conceptKey,
                                                              @RequestParam(value = "inherit", defaultValue = "true") boolean inherit);

    @PostMapping("sparQl/query/{kgName}")
    ApiReturn<QueryResultRsp> sparQlQuery(@PathVariable("kgName") String kgName, @RequestBody SparQlReq sparQlReq);

    /**
     * 根据 name 和消歧标识 查询实例
     *
     * @param kgName           。
     * @param conditionReqList 。
     * @return List<OpenEntityRsp>
     */
    @PostMapping("{kgName}/entity/query")
    ApiReturn<List<OpenEntityRsp>> queryEntityByNameAndMeaningTag(@PathVariable("kgName") String kgName,
                                                                  @RequestBody List<EntityQueryWithConditionReq> conditionReqList);


    /**
     * 根据 溯源信息查询实例
     *
     * @param kgName           。
     * @param req 。
     * @return List<OpenEntityRsp>
     */
    @PostMapping({"{kgName}/source/entity/query"})
    ApiReturn<BasePage<OpenEntityRsp>> queryEntityBySource(@PathVariable("kgName") String kgName,
                                                           @RequestBody TraceabilityQueryReq req);
}
