package com.plantdata.kgcloud.domain.repo.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.repo.model.req.D2rReq;
import com.plantdata.kgcloud.domain.repo.model.rsp.D2rRsp;
import com.plantdata.kgcloud.domain.repo.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cjw
 * @date 2020/5/15  11:52
 */
@RestController
@RequestMapping("repo/component")
public class ComponentController {

    @Autowired
    private ComponentService componentService;

    @PostMapping
    public ApiReturn<D2rRsp> d2r(@RequestBody D2rReq d2rReq) {
        return ApiReturn.success(componentService.d2r(d2rReq));
    }
}
