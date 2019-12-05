package com.plantdata.kgcloud.domain.model.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.model.service.ModelService;
import com.plantdata.kgcloud.sdk.req.WordReq;
import com.plantdata.kgcloud.sdk.rsp.ModelRsp;
import com.plantdata.kgcloud.security.SessionHolder;
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
 * @create: 2019-11-04 18:45
 **/

@Api(tags = "模型管理")
@RestController
@RequestMapping("model")
public class ModelController {

    @Autowired
    private ModelService modelService;

    @ApiOperation("模型查找所有")
    @GetMapping("/all")
    public ApiReturn<List<ModelRsp>> findAll() {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(modelService.findAll(userId));
    }


    @ApiOperation("模型分页查找")
    @GetMapping("/")
    public ApiReturn<Page<ModelRsp>> findAll(BaseReq baseReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(modelService.findAll(userId, baseReq));
    }

    @ApiOperation("模型根据Id查找")
    @GetMapping("/{id}")
    public ApiReturn<ModelRsp> findById(@PathVariable Long id) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(modelService.findById(userId,id));
    }

    @ApiOperation("模型创建")
    @PostMapping("/")
    public ApiReturn<ModelRsp> insert(@Valid @RequestBody WordReq.ModelReq modelReq) {

        return ApiReturn.success(modelService.insert(modelReq));
    }

    @ApiOperation("模型编辑")
    @PatchMapping("/{id}")
    public ApiReturn<ModelRsp> update(@PathVariable Long id, @Valid @RequestBody WordReq.ModelReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(modelService.update(userId,id, req));
    }

    @ApiOperation("模型删除")
    @DeleteMapping("/{id}")
    public ApiReturn delete(@PathVariable Long id) {
        String userId = SessionHolder.getUserId();
        modelService.delete(userId,id);
        return ApiReturn.success();
    }

    @ApiOperation("模型调用")
    @PostMapping("/call/{id}")
    public ApiReturn callJson(@PathVariable Long id, @RequestBody WordReq.ModelCallReq req) {
        return ApiReturn.success(modelService.call(id, req.getInput()));
    }


}
