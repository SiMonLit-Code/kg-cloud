package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeAttrStatisticByAttrValueReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByConceptIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByEntityIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByAttrIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByConceptReq;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 14:02
 */
@FeignClient(value = "kgms", path = "kgData", contextId = "kgDataClient")
public interface KgDataClient {

    /**
     * 第三方模型抽取
     *
     * @param modelId    模型id
     * @param input      。
     * @param configList 配置
     * @return 。
     */
    @PostMapping("extract/thirdModel/{modelId}")
    ApiReturn<Object> extractThirdModel(@PathVariable("modelId") Long modelId,
                                        @RequestParam("input") String input, @RequestBody List<Map<String, String>> configList);

    /**
     * 查询实体的关系度数
     *
     * @param kgName
     * @param statisticReq
     * @return
     */
    @PostMapping("kgData/statistic/{kgName}/entity/degree/")
    ApiReturn<List<Map<String, Object>>> statisticCountEdgeByEntity(@PathVariable("kgName") String kgName,
                                                                    @RequestBody EdgeStatisticByEntityIdReq statisticReq);

    /**
     * 统计实体根据概念分组
     *
     * @param kgName
     * @param statisticReq
     * @return
     */
    @PostMapping("kgData/statistic/{kgName}/entity/groupByConcept/")
    ApiReturn<Object> statisticEntityGroupByConcept(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                    @RequestBody EntityStatisticGroupByConceptReq statisticReq);

    /**
     * 实体属性值统计
     *
     * @param kgName
     * @param statisticReq
     * @return
     */
    @PostMapping("kgData/statistic/{kgName}/attr/groupByAttrValue")
    ApiReturn<Object> statisticAttrGroupByConcept(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                  @RequestBody EntityStatisticGroupByAttrIdReq statisticReq);

    /**
     * 对象属性统计，统计对象属性的数量，按关系分组
     *
     * @param kgName
     * @param statisticReq
     * @return
     */
    @PostMapping("kgData/statistic/{kgName}/edge/groupByAttrName")
    ApiReturn<Object> statisticRelation(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                        @RequestBody EdgeStatisticByConceptIdReq statisticReq);

    /**
     * 边数值属性统计，按数值属性值分组
     *
     * @param kgName
     * @param statisticReq
     * @return
     */
    @PostMapping("kgData/statistic/{kgName}/edgeAttr/groupByAttrValue")
    ApiReturn<Object> statEdgeGroupByEdgeValue(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                    EdgeAttrStatisticByAttrValueReq statisticReq);
}
