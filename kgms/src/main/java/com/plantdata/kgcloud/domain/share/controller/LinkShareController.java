package com.plantdata.kgcloud.domain.share.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.share.rsp.LinkShareRsp;
import com.plantdata.kgcloud.domain.share.rsp.SelfSharedRsp;
import com.plantdata.kgcloud.domain.share.service.LinkShareService;
import com.plantdata.kgcloud.sdk.rsp.LinkShareSpaRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jdm on 2019/12/9 12:06.
 */

@Api(tags = "知识应用分享")
@RestController
@RequestMapping("/share")
public class LinkShareController {

    @Autowired
    private LinkShareService linkShareService;

    @GetMapping("/status/{kgName}")
    @ApiOperation("分享状态列表")
    public ApiReturn<LinkShareRsp> shareStatus(@PathVariable("kgName") String kgName) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(linkShareService.shareStatus(userId, kgName));
    }

    @GetMapping("/status")
    @ApiOperation("分享状态列表")
    public ApiReturn<SelfSharedRsp> shareSpaStatus(@RequestParam("kgName") String kgName,@RequestParam("spaId") String spaId,   @RequestParam("token") String token) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(linkShareService.shareSpaStatus(userId, kgName,spaId,token));
    }

    @GetMapping("/status/{kgName}/{spaId}")
    @ApiOperation("SPA分享状态")
    public ApiReturn<LinkShareSpaRsp> shareStatus(@PathVariable("kgName") String kgName, @PathVariable("spaId") String spaId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(linkShareService.shareStatus(userId, kgName, spaId));
    }

    @GetMapping("/link/{kgName}")
    @ApiOperation("分享链接")
    public ApiReturn shareLink(@PathVariable("kgName") String kgName, @RequestParam("spaId") String spaId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(linkShareService.shareLink(userId, kgName, spaId));
    }

    @GetMapping("/cancel/{kgName}")
    @ApiOperation("取消分享")
    public ApiReturn shareCancel(@PathVariable("kgName") String kgName, @RequestParam("spaId") String spaId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(linkShareService.shareCancel(userId, kgName, spaId));
    }


}
