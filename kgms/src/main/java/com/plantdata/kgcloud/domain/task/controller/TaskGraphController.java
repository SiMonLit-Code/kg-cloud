package com.plantdata.kgcloud.domain.task.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.task.dto.ReasonBean;
import com.plantdata.kgcloud.domain.task.req.ImportTripleReq;
import com.plantdata.kgcloud.domain.task.req.TaskGraphSnapshotReq;
import com.plantdata.kgcloud.domain.task.rsp.TaskGraphSnapshotRsp;
import com.plantdata.kgcloud.domain.task.rsp.TaskTemplateRsp;
import com.plantdata.kgcloud.domain.task.service.ReasonService;
import com.plantdata.kgcloud.domain.task.service.TaskGraphService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-10 10:28
 **/
@Api(tags = "任务相关")
@RestController
@RequestMapping("/task")
public class TaskGraphController {

    @Autowired
    private TaskGraphService taskGraphService;

    @Autowired
    private ReasonService reasonService;

    @ApiOperation("任务相关-快照-根据id删除")
    @GetMapping("/snapshot")
    public ApiReturn<Page<TaskGraphSnapshotRsp>> listByPage(TaskGraphSnapshotReq req) {
        return ApiReturn.success(taskGraphService.snapshotList(req));
    }

    @ApiOperation("任务相关-快照-根据id删除")
    @DeleteMapping("/snapshot/{id}")
    public ApiReturn delete(@PathVariable("id") Long id) {
        taskGraphService.snapshotDelete(id);
        return ApiReturn.success();
    }

    @GetMapping("/template")
    @ApiOperation("任务相关-模板-组合任务模板")
    public ApiReturn<List<TaskTemplateRsp>> taskTemplateList() {
        return ApiReturn.success(taskGraphService.taskTemplateList());
    }


    @GetMapping("/reason/{kgName}/{taskId}")
    @ApiOperation("任务相关-推理-结果分页")
    public ApiReturn<ReasonBean> listByPage(
            @PathVariable("kgName") String kgName,
            @PathVariable("taskId") Integer taskId,
            BaseReq pageModel) {
        return ApiReturn.success(reasonService.listByPage(kgName, taskId, pageModel));
    }

    @PostMapping("/reason/{kgName}/import")
    @ApiOperation("结果入图")
    public ApiReturn<Map<String, Object>> importTriples(
            @PathVariable("kgName") String kgName,
            @RequestBody ImportTripleReq req) {
        return ApiReturn.success(reasonService.importTriple(req.getType(), kgName, req.getMode(), req.getTaskId(), req.getDataIds()));
    }

    @GetMapping("search/{kgName}")
    @ApiOperation("拼音检索-查看开启状态")
    public ApiReturn<Integer> searchStatus(@PathVariable("kgName") String kgName) {
        return ApiReturn.success(taskGraphService.searchStatus(kgName));
    }

    @PostMapping("/search/open/{kgName}")
    @ApiOperation("拼音检索-开启")
    public ApiReturn<Integer> openSearch(@PathVariable("kgName") String kgName) {
        Integer taskId = taskGraphService.openSearch(kgName);
        return ApiReturn.success(taskId);
    }

    @PostMapping("/search/close/{kgName}")
    @ApiOperation("拼音检索-关闭")
    public ApiReturn closeSearch(@PathVariable("kgName") String kgName) {
        taskGraphService.closeSearch(kgName);
        return ApiReturn.success();
    }

    @PostMapping("/search/flush/{kgName}")
    @ApiOperation("拼音检索-刷新结果")
    public ApiReturn flushSearch(@PathVariable("kgName") String kgName) {
        taskGraphService.flushSearch(kgName);
        return ApiReturn.success();
    }

}
