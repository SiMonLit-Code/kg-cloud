package ai.plantdata.kgcloud.domain.task.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.domain.task.req.TaskGraphSnapshotNameReq;
import ai.plantdata.kgcloud.domain.task.req.TaskGraphSnapshotReq;
import ai.plantdata.kgcloud.domain.task.rsp.TaskGraphSnapshotRsp;
import ai.plantdata.kgcloud.domain.task.service.TaskGraphService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @ApiOperation("任务相关-备份-备份列表")
    @GetMapping("/snapshot")
    public ApiReturn<Page<TaskGraphSnapshotRsp>> listByPage(TaskGraphSnapshotReq req) {
        return ApiReturn.success(taskGraphService.snapshotList(req));
    }

    @ApiOperation("任务相关-备份-根据id删除")
    @DeleteMapping("/snapshot/{id}")
    public ApiReturn delete(@PathVariable("id") Long id) {
        taskGraphService.snapshotDelete(id);
        return ApiReturn.success();
    }

    @ApiOperation("任务相关-备份-新增备份记录")
    @PostMapping("/snapshot/add")
    public ApiReturn<TaskGraphSnapshotRsp> add(@Valid @RequestBody TaskGraphSnapshotNameReq req) {
        return ApiReturn.success(taskGraphService.add(req));
    }

}
