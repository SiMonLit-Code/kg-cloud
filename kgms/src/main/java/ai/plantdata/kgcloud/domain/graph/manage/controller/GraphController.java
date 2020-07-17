package ai.plantdata.kgcloud.domain.graph.manage.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.web.util.SessionHolder;
import ai.plantdata.kgcloud.domain.common.util.KGUtil;
import ai.plantdata.kgcloud.domain.graph.manage.service.GraphService;
import ai.plantdata.kgcloud.sdk.req.GraphPageReq;
import ai.plantdata.kgcloud.sdk.req.GraphReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 10:58
 **/
@Api(tags = "图谱管理")
@RestController
@RequestMapping("/graph")
public class GraphController {
    @Autowired
    private GraphService graphService;

    @ApiOperation("图谱查找所有")
    @GetMapping("/all")
    public ApiReturn<List<GraphRsp>> findAll() {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(graphService.findAll(userId));
    }


    @ApiOperation("图谱分页查找")
    @GetMapping("/")
    public ApiReturn<Page<GraphRsp>> findAll(GraphPageReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(graphService.findAll(userId, req));
    }

    @ApiOperation("图谱根据kgname查找")
    @GetMapping("/{kgName}")
    public ApiReturn<GraphRsp> findById(@PathVariable("kgName") String kgName) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(graphService.findById(userId, kgName));
    }

    @ApiOperation("图谱根据kgName查找dbName")
    @GetMapping("/kgName/{kgName}")
    public ApiReturn<String> graphFindDbNameByKgName(@PathVariable("kgName") String kgName) {
        return ApiReturn.success(KGUtil.dbName(kgName));
    }

    @ApiOperation("图谱创建")
    @PostMapping("/")
    public ApiReturn<GraphRsp> insert(@Valid @RequestBody GraphReq dictionaryReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(graphService.insert(userId, dictionaryReq));
    }

    @ApiOperation("图谱默认创建")
    @PostMapping("/default")
    public ApiReturn<GraphRsp> createDefault() {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(graphService.createDefault(userId));
    }

    @ApiOperation("图谱编辑")
    @PatchMapping("/{kgName}")
    public ApiReturn<GraphRsp> update(@PathVariable("kgName") String kgName, @Valid @RequestBody GraphReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(graphService.update(userId, kgName, req));
    }

    @ApiOperation("图谱删除")
    @DeleteMapping("/{kgName}")
    public ApiReturn delete(@PathVariable("kgName") String kgName) {
        String userId = SessionHolder.getUserId();
        graphService.delete(userId, kgName);
        return ApiReturn.success();
    }
}
