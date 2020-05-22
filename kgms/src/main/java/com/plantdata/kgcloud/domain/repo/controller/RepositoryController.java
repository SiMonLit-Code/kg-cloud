package com.plantdata.kgcloud.domain.repo.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.repo.constatn.StringConstants;
import com.plantdata.kgcloud.domain.repo.converter.RepositoryConverter;
import com.plantdata.kgcloud.domain.repo.enums.RepositoryLogEnum;
import com.plantdata.kgcloud.domain.repo.model.RepositoryGroup;
import com.plantdata.kgcloud.domain.repo.model.req.RepositoryReq;
import com.plantdata.kgcloud.domain.repo.model.req.RepositoryUpdateReq;
import com.plantdata.kgcloud.domain.repo.model.rsp.RepositoryListRsp;
import com.plantdata.kgcloud.domain.repo.model.rsp.RepositoryRsp;
import com.plantdata.kgcloud.domain.repo.repository.RepositoryGroupRepository;
import com.plantdata.kgcloud.domain.repo.service.RepositoryService;
import com.plantdata.kgcloud.sdk.rsp.RepositoryLogMenuRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author cjw
 * @date 2020/5/18  9:57
 */
@RestController
@RequestMapping("repository")
public class RepositoryController {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RepositoryGroupRepository repositoryGroupRepository;

    @ApiOperation("组件列表")
    @GetMapping
    public ApiReturn<RepositoryListRsp> repositoryRsp() {
        List<RepositoryRsp> list = repositoryService.list(SessionHolder.getUserId(), false);
        List<RepositoryGroup> allGroups = repositoryGroupRepository.findAll();
        return ApiReturn.success(RepositoryConverter.buildRepositoryList(list, allGroups));
    }

    @ApiOperation("组件新增")
    @PostMapping
    public ApiReturn<Integer> add(@RequestBody RepositoryReq repositoryReq) {
        return ApiReturn.success(repositoryService.add(repositoryReq));
    }

    @ApiOperation("组件删除")
    @DeleteMapping("{id}")
    public ApiReturn<Boolean> delete(@PathVariable(name = "id") Integer id) {
        return ApiReturn.success(repositoryService.delete(id));
    }

    @ApiOperation("组件修改")
    @PutMapping
    public ApiReturn<Boolean> modify(@RequestBody RepositoryUpdateReq repositoryReq) {
        return ApiReturn.success(repositoryService.modify(repositoryReq));
    }

    @ApiOperation("组件启用")
    @PostMapping("{id}/start")
    public ApiReturn<Boolean> startRepository(@PathVariable(name = "id") Integer id) {
        return ApiReturn.success(repositoryService.updateStatus(id, true));
    }

    @ApiOperation("组件停用")
    @PostMapping("{id}/stop")
    public ApiReturn<Boolean> stopRepository(@PathVariable(name = "id") Integer id) {
        return ApiReturn.success(repositoryService.updateStatus(id, false));
    }

    @ApiOperation("使用记录")
    @GetMapping("{type}/{id}/log")
    public ApiReturn<String> useLog(@PathVariable(name = "type") String type,
                                    @PathVariable(name = "id") Integer id) {
        Optional<RepositoryLogEnum> logEnumOpt = RepositoryLogEnum.parseByDesc(type);
        logEnumOpt.ifPresent(a -> repositoryService.useLog(a, id, SessionHolder.getUserId()));
        return ApiReturn.success(StringConstants.SUCCESS);
    }


    @ApiOperation("使用记录关联动态导航")
    @GetMapping("log/menu")
    public ApiReturn<List<RepositoryLogMenuRsp>> logMenuRsp() {
        return ApiReturn.success(repositoryService.menuLog(SessionHolder.getUserId()));
    }
}
