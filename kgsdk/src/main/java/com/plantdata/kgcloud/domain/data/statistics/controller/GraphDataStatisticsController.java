package com.plantdata.kgcloud.domain.data.statistics.controller;

import com.plantdata.kgcloud.domain.common.module.GraphDataStatisticsInterface;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.data.statistics.req.StatEntityGroupByAttributeByConceptIdReq;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/1 15:37
 */
@Slf4j
@RestController
@RequestMapping("kgData/relation/statistic/")
public class GraphDataStatisticsController implements GraphDataStatisticsInterface {

    @ApiOperation("对象属性统计，统计对象属性的数量，按关系分组")
    @GetMapping("/byAttrValue/{kgName}")
    public ApiReturn relationCountByAttrValue(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                              @RequestBody @Valid StatEntityGroupByAttributeByConceptIdReq statEntityGroupByAttributeByConceptIdParameter) {
        return ApiReturn.success(null);
    }

    @ApiOperation("边数值属性统计，按数值属性值分组")
    @GetMapping("byEdgeAttrValue/{kgName}")
    public ApiReturn statEdgeGroupByEdgeValue(@ApiParam("图谱名称") @PathVariable("kgName") String kgName) {
        //todo
        return ApiReturn.success();
    }

}
