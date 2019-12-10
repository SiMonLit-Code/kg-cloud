package com.plantdata.kgcloud.domain.lite;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.graph.manage.service.GraphService;
import com.plantdata.kgcloud.domain.share.service.LinkShareService;
import com.plantdata.kgcloud.sdk.req.GraphReq;
import com.plantdata.kgcloud.sdk.rsp.GraphRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 18:30
 **/
@Api(tags = "kgLite接口")
@RestController
@RequestMapping("/lite")
public class LiteController {

    @Autowired
    private GraphService graphService;
    @Autowired
    private LinkShareService linkShareService;

    @GetMapping("/graph/all")
    @ApiOperation("图谱-图谱查找所有")
    public ApiReturn<List<GraphRsp>> findAll() {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(graphService.findAll(userId));
    }

    @ApiOperation("图谱-图谱创建")
    @PostMapping("/graph")
    public ApiReturn<GraphRsp> insert(@Valid @RequestBody GraphReq dictionaryReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(graphService.insert(userId, dictionaryReq));
    }

    @ApiOperation("图谱-图谱编辑")
    @PatchMapping("/graph/{kgName}")
    public ApiReturn<GraphRsp> update(@PathVariable("kgName") String kgName, @Valid @RequestBody GraphReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(graphService.update(userId, kgName, req));
    }

    @ApiOperation("图谱-图谱删除")
    @DeleteMapping("/graph/{kgName}")
    public ApiReturn delete(@PathVariable("kgName") String kgName) {
        String userId = SessionHolder.getUserId();
        graphService.delete(userId, kgName);
        return ApiReturn.success();
    }

    @GetMapping("/share/status/{kgName}")
    @ApiOperation("分享-分享状态列表")
    public ApiReturn shareStatus(@PathVariable("kgName") String kgName) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(linkShareService.shareStatus(userId, kgName));
    }

    @GetMapping("/share/link/{kgName}")
    @ApiOperation("分享-分享链接")
    public ApiReturn shareLink(@PathVariable("kgName") String kgName, @RequestParam("spaId") String spaId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(linkShareService.shareLink(userId, kgName, spaId));
    }

    @GetMapping("/share/cancel/{kgName}")
    @ApiOperation("分享-取消分享")
    public ApiReturn shareCancel(@PathVariable("kgName") String kgName, @RequestParam("spaId") String spaId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(linkShareService.shareCancel(userId, kgName, spaId));
    }

}
