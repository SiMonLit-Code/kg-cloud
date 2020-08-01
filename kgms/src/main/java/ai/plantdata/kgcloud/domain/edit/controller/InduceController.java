package ai.plantdata.kgcloud.domain.edit.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.kgcloud.domain.edit.aop.EditLogOperation;
import ai.plantdata.kgcloud.domain.edit.req.induce.AttrInduceReq;
import ai.plantdata.kgcloud.domain.edit.req.induce.AttrSearchReq;
import ai.plantdata.kgcloud.domain.edit.req.induce.InduceConceptReq;
import ai.plantdata.kgcloud.domain.edit.req.induce.InduceMergeReq;
import ai.plantdata.kgcloud.domain.edit.req.induce.InduceObjectReq;
import ai.plantdata.kgcloud.domain.edit.req.induce.InducePublicReq;
import ai.plantdata.kgcloud.domain.edit.rsp.AttrInduceFindRsp;
import ai.plantdata.kgcloud.domain.edit.rsp.InduceConceptRsp;
import ai.plantdata.kgcloud.domain.edit.service.InduceService;
import ai.plantdata.kgcloud.sdk.req.edit.AttrDefinitionVO;
import com.plantdata.graph.logging.core.ServiceEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static ai.plantdata.kgcloud.constant.AppErrorCodeEnum.NULL_RANGE_VALUE;

/**
 * @Author: LinHo
 * @Date: 2019/11/29 15:21
 * @Description:
 */
@Api(tags = "规约")
@RestController
@RequestMapping("/induce")
public class InduceController {

    @Autowired
    private InduceService induceService;

    @ApiOperation("读取私有属性列表，按数量排序")
    @GetMapping("/{kgName}")
    public ApiReturn<Page<AttrDefinitionVO>> induceAttributeList(@PathVariable("kgName") String kgName,
                                                                 AttrSearchReq attrSearchReq) {
        return ApiReturn.success(induceService.induceAttributeList(kgName, attrSearchReq));
    }

    @ApiOperation("寻找待规约的属性")
    @GetMapping("/{kgName}/find")
    public ApiReturn<List<AttrInduceFindRsp>> induceAttributeFind(@PathVariable("kgName") String kgName,
                                                                  AttrInduceReq attrInduceReq) {
        return ApiReturn.success(induceService.induceAttributeFind(kgName, attrInduceReq));
    }

    @ApiOperation("执行属性公有化")
    @PostMapping("/{kgName}/public")
    @EditLogOperation(serviceEnum = ServiceEnum.ATTR_GUIYUE)
    public ApiReturn inducePublic(@PathVariable("kgName") String kgName,
                                  @Valid @RequestBody InducePublicReq inducePublicReq) {
        induceService.inducePublic(kgName, inducePublicReq);
        return ApiReturn.success();
    }

    @ApiOperation("执行属性对象化")
    @PostMapping("/{kgName}/object")
    @EditLogOperation(serviceEnum = ServiceEnum.ATTR_GUIYUE)
    public ApiReturn induceObject(@PathVariable("kgName") String kgName,
                                  @Valid @RequestBody InduceObjectReq induceObjectReq) {
        induceService.induceObject(kgName, induceObjectReq);
        return ApiReturn.success();
    }

    @ApiOperation("执行属性合并")
    @PostMapping("/{kgName}/merge")
    @EditLogOperation(serviceEnum = ServiceEnum.ATTR_GUIYUE)
    public ApiReturn induceMerge(@PathVariable("kgName") String kgName,
                                 @Valid @RequestBody InduceMergeReq induceMergeReq) {
        induceService.induceMerge(kgName, induceMergeReq);
        return ApiReturn.success();
    }

    @ApiOperation("计算概念待规约列表")
    @GetMapping("/{kgName}/{conceptId}")
    public ApiReturn<List<InduceConceptRsp>> listInduceConcept(@PathVariable("kgName") String kgName,
                                                               @PathVariable("conceptId") Long conceptId) {
        return ApiReturn.success(induceService.listInduceConcept(kgName, conceptId));
    }

    @ApiOperation("执行概念规约")
    @PostMapping("/{kgName}/concept")
    @EditLogOperation(serviceEnum = ServiceEnum.CONCEPT_GUIYUE)
    public ApiReturn induceConcept(@PathVariable("kgName") String kgName,
                                   @Valid @RequestBody InduceConceptReq induceConceptReq) {
        List<Map<Integer, List<String>>> attrData = induceConceptReq.getDataAttributeValues();
        if (attrData != null && !attrData.isEmpty()) {
            for (Map<Integer, List<String>> attrDatum : attrData) {
                for (Map.Entry<Integer, List<String>> entry : attrDatum.entrySet()) {
                    List<String> value = entry.getValue();
                    if (value == null || value.isEmpty()) {
                        throw BizException.of(NULL_RANGE_VALUE);
                    }
                }
            }
        }
        induceService.induceConcept(kgName, induceConceptReq);
        return ApiReturn.success();
    }
}
