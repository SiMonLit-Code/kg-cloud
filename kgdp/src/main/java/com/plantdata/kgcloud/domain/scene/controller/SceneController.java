package com.plantdata.kgcloud.domain.scene.controller;


import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.scene.req.SceneQueryReq;
import com.plantdata.kgcloud.domain.scene.req.SceneReq;
import com.plantdata.kgcloud.domain.scene.rsp.SceneRsp;
import com.plantdata.kgcloud.domain.scene.service.SceneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;


@Api(tags = "场景")
@RestController
@RequestMapping("/scene")
public class SceneController {


    @Autowired
    private SceneService sceneService;

    @ApiOperation("场景列表")
    @PostMapping("/get/list/page")
    public ApiReturn<Page<SceneRsp>> getListPage(SceneQueryReq sceneQueryReq, @ApiIgnore @PageableDefault Pageable pageable) {

        if(sceneQueryReq.getSorts() == null || sceneQueryReq.getSorts().size() == 0){
            pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"createTime"));
        }
        return sceneService.findAll(sceneQueryReq,pageable);
    }

    @ApiOperation("获取场景详情")
    @PatchMapping("/get/{id:\\d+}")
    public ApiReturn<SceneRsp> getDetail(@PathVariable("id") Integer id) {
        return sceneService.getSceneDetail(id);
    }

    @ApiOperation("创建场景")
    @PostMapping("/create")
    public ApiReturn<SceneRsp> createScene(@Valid @RequestBody SceneReq sceneReq) {
        return sceneService.createScene(sceneReq);
    }


}
