package ai.plantdata.kgcloud.domain.edit.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.web.util.ConvertUtils;
import ai.plantdata.kg.api.edit.merge.EntityMergeSourceVO;
import ai.plantdata.kg.api.edit.merge.MergeEntity4Edit;
import ai.plantdata.kg.api.edit.merge.MergeFinalEntityFrom;
import ai.plantdata.kg.api.edit.merge.WaitMergeVO;
import ai.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import ai.plantdata.kgcloud.domain.edit.aop.EditLogOperation;
import ai.plantdata.kgcloud.domain.edit.req.merge.MergeFinalEntityReq;
import ai.plantdata.kgcloud.domain.edit.req.merge.WaitMergeReq;
import ai.plantdata.kgcloud.domain.edit.rsp.MergeEntityDetailRsp;
import ai.plantdata.kgcloud.domain.edit.service.MergeService;
import com.plantdata.graph.logging.core.ServiceEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 16:40
 * @Description:
 */
@Api(tags = "实体融合")
@RestController
@RequestMapping("/merge")
public class MergeController {

    @Autowired
    private MergeService mergeService;

    @ApiOperation("获取所有来源")
    @GetMapping("source/{kgName}")
    public ApiReturn<Set<String>> allSource(@PathVariable("kgName") String kgName) {
        return ApiReturn.success(mergeService.allSource(kgName));
    }

    @ApiOperation("获取来源排序")
    @GetMapping("source/get/{kgName}")
    public ApiReturn<List<EntityMergeSourceVO>> getSourceSort(@PathVariable("kgName") String kgName) {
        return ApiReturn.success(mergeService.getSourceSort(kgName));
    }

    @ApiOperation("保存来源排序")
    @PostMapping("source/save/{kgName}")
    public ApiReturn saveSourceSort(@PathVariable("kgName") String kgName, @RequestBody Map<Integer, String> map) {
        mergeService.saveSourceSort(kgName, map);
        return ApiReturn.success();
    }

    @ApiOperation("获取待合并的列表")
    @GetMapping("wait/list/{kgName}")
    public ApiReturn<Page<WaitMergeVO>> waitList(
            @PathVariable("kgName") String kgName, WaitMergeReq req) {
        return ApiReturn.success(mergeService.waitList(kgName, req));
    }

    @ApiOperation("批量删除待合并的列表")
    @DeleteMapping("wait/list/{kgName}")
    public ApiReturn deleteWaitList(
            @PathVariable("kgName") String kgName,
            @RequestBody List<String> ids) {
        mergeService.deleteWaitList(kgName, ids);
        return ApiReturn.success();
    }

    @ApiOperation("手工创建融合实体")
    @PostMapping("wait/entity/create/{kgName}")
    public ApiReturn<String> createMergeEntity(
            @PathVariable("kgName") String kgName,
            @RequestBody List<Long> ids) {
        return ApiReturn.success(mergeService.createMergeEntity(kgName, new HashSet<>(ids)));
    }

    @ApiOperation("添加融合实体")
    @PostMapping("wait/entity/{kgName}")
    public ApiReturn insertMergeEntity(
            @PathVariable("kgName") String kgName,
            @RequestParam("objId") String objId,
            @RequestBody List<Long> entityIds) {
        mergeService.insertMergeEntity(kgName, objId, entityIds);
        return ApiReturn.success();
    }

    @ApiOperation("删除融合实体")
    @DeleteMapping("wait/entity/{kgName}")
    public ApiReturn deleteMergeEntity(
            @PathVariable("kgName") String kgName,
            @RequestParam("objId") String objId,
            @RequestBody List<Long> entityIds) {
        mergeService.deleteMergeEntity(kgName, objId, entityIds);
        return ApiReturn.success();
    }

    @ApiOperation("查看冲突项")
    @GetMapping("wait/entity/different/{kgName}")
    public ApiReturn<MergeEntity4Edit> showDifferent(
            @PathVariable("kgName") String kgName,
            @RequestParam("objId") String objId,
            @RequestParam("mode") Integer mode
    ) {
        return ApiReturn.success(mergeService.showDifferent(kgName, objId, mode));
    }

    @ApiOperation("查看详情列表")
    @GetMapping("wait/entity/{kgName}")
    public ApiReturn<List<MergeEntityDetailRsp>> showEntityList(
            @PathVariable("kgName") String kgName,
            @RequestParam("objId") String objId
    ) {
        return ApiReturn.success(mergeService.showEntityList(kgName, objId));
    }

    @ApiOperation("执行融合")
    @PostMapping("wait/entity/handle/{kgName}")
    @EditLogOperation(serviceEnum = ServiceEnum.MEREGE_EDIT)
    public ApiReturn doMergeEntity(
            @PathVariable("kgName") String kgName,
            @RequestParam("objId") String objId,
            @RequestBody MergeFinalEntityReq entity
    ) {

        if (entity != null && entity.getName() != null && entity.getName().trim().length() > 50) {
            throw BizException.of(KgmsErrorCodeEnum.ENTITY_NAME_LENGTH_ERROR);
        }
        final MergeFinalEntityFrom from = ConvertUtils.convert(MergeFinalEntityFrom.class).apply(entity);
        assert entity != null;
        Double reliability = entity.getReliability();
        if (reliability != null) {
            from.setReliability(reliability.toString());
        }

        mergeService.doMergeEntity(kgName, objId, from);
        return ApiReturn.success();
    }

    @ApiOperation("批量融合")
    @PostMapping("wait/entity/batch/{kgName}")
    @EditLogOperation(serviceEnum = ServiceEnum.MEREGE_EDIT)
    public ApiReturn mergeByObjIds(
            @PathVariable("kgName") String kgName,
            @RequestParam("mode") Integer mode,
            @RequestBody List<String> objIds
    ) {
        mergeService.mergeByObjIds(kgName, mode, objIds);
        return ApiReturn.success();
    }

}
