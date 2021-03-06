package ai.plantdata.kgcloud.domain.app.controller;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/20 10:00
 */

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.kgcloud.constant.AppErrorCodeEnum;
import ai.plantdata.kgcloud.domain.edit.service.AttributeService;
import ai.plantdata.kgcloud.domain.app.converter.AttrDefConverter;
import ai.plantdata.kgcloud.domain.app.service.GraphEditService;
import ai.plantdata.kgcloud.domain.app.service.GraphHelperService;
import ai.plantdata.kgcloud.sdk.req.app.AttrDefQueryReq;
import ai.plantdata.kgcloud.sdk.req.edit.ConceptAddReq;
import ai.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("kgdata")
public class GraphEditController {

    @Autowired
    private GraphEditService graphEditService;
    @Autowired
    private AttributeService attributeService;
    @Autowired
    private GraphHelperService graphHelperService;

    @ApiOperation("添加概念")
    @PostMapping("concept/{kgName}")
    public ApiReturn<Long> createConcept(@PathVariable("kgName") String kgName,
                                         @Valid @RequestBody ConceptAddReq conceptAddReq) {
        return ApiReturn.success(graphEditService.createConcept(kgName, conceptAddReq));
    }

    @ApiOperation("根据概念查询属性定义")
    @GetMapping("/{kgName}/attribute/search")
    public ApiReturn<List<AttrDefinitionRsp>> searchAttrDefByConcept(@PathVariable("kgName") String kgName, @Valid AttrDefQueryReq queryReq) {
        if (queryReq.getConceptId() == null && queryReq.getConceptKey() == null) {
            throw BizException.of(AppErrorCodeEnum.NULL_CONCEPT_ID_AND_KEY);
        }
        graphHelperService.replaceByConceptKey(kgName, queryReq);
        List<AttrDefinitionRsp> resList = attributeService.getAttrDefinitionByConceptId(kgName,
                AttrDefConverter.attrDefQueryReqToAttrDefinitionSearchReq(queryReq));
        return ApiReturn.success(resList);
    }
}
