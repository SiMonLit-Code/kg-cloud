package ai.plantdata.kgcloud.domain.task;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.bean.BasePage;
import ai.plantdata.kgcloud.domain.common.module.TaskOptInterface;
import ai.plantdata.kgcloud.sdk.XxlAdminClient;
import ai.plantdata.kgcloud.sdk.bean.ExecBean;
import ai.plantdata.kgcloud.sdk.bean.ExecListReq;
import ai.plantdata.kgcloud.sdk.bean.RunTaskReq;
import ai.plantdata.kgcloud.sdk.bean.ScheduleReq;
import ai.plantdata.kgcloud.sdk.bean.TaskBean;
import ai.plantdata.kgcloud.sdk.bean.TaskListReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author cjw
 * @date 2020/8/12  15:04
 */
@RestController
@RequestMapping("v3/task/xxl")
public class XxAdminTaskController implements TaskOptInterface {

    @Autowired
    private XxlAdminClient xxlAdminClient;

    @ApiOperation("创建任务")
    @PostMapping(value = "/add")
    public ApiReturn<Integer> taskAdd(@RequestBody TaskBean task) {
        return xxlAdminClient.taskAdd(task);
    }

    @ApiOperation("更新任务")
    @PostMapping("/update")
    public ApiReturn taskUpdate(@RequestBody TaskBean task) {
        return xxlAdminClient.taskUpdate(task);
    }

    @ApiOperation("获取任务详情By Id")
    @GetMapping("/get/{userId}/{id}")
    public ApiReturn<TaskBean> get(@PathVariable("userId") String userId, @PathVariable("id") Integer id) {
        return xxlAdminClient.get(userId, id);
    }

    @ApiOperation("删除任务")
    @PostMapping("/delete/{userId}/{id}")
    public ApiReturn taskDelete(@PathVariable("userId") String userId,
                                @PathVariable("id") Integer id) {
        return xxlAdminClient.taskDelete(userId, id);
    }

    @ApiOperation("执行任务")
    @PostMapping("/run")
    public ApiReturn taskRun(@RequestBody @Valid RunTaskReq req) {
        return xxlAdminClient.taskRun(req);
    }

    @ApiOperation("调度任务")
    @PostMapping("/schedule")
    public ApiReturn taskSchedule(@Valid @RequestBody ScheduleReq req) {
        return xxlAdminClient.taskSchedule(req);
    }

   @ApiOperation("取消调度任务")
    @PostMapping("/unschedule/{userId}/{id}")
    public ApiReturn taskUnschedule(@PathVariable("userId") String userId,
                                    @PathVariable("id") Integer id) {
        return xxlAdminClient.taskUnschedule(userId, id);
    }

    @ApiOperation("停止任务")
    @PostMapping("/stop/{userId}/{execId}")
    public ApiReturn taskStop(@PathVariable("userId") String userId,
                              @PathVariable("execId") Integer execId) {
        return xxlAdminClient.taskStop(userId, execId);
    }


    @ApiOperation("任务列表")
    @PostMapping("/list")
    public ApiReturn<BasePage<TaskBean>> list(@Valid @RequestBody TaskListReq req) {
        return xxlAdminClient.list(req);
    }

    @ApiOperation("任务执行列表")
    @PostMapping("/execlist")
    public ApiReturn<BasePage<ExecBean>> execlist(@Valid @RequestBody ExecListReq req) {
        return xxlAdminClient.execlist(req);
    }
}
