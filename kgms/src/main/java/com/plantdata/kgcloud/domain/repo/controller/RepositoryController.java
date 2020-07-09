package com.plantdata.kgcloud.domain.repo.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.web.util.SessionHolder;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.repo.constatn.StringConstants;
import com.plantdata.kgcloud.domain.repo.converter.RepositoryConverter;
import com.plantdata.kgcloud.domain.repo.enums.RepositoryLogEnum;
import com.plantdata.kgcloud.domain.repo.entity.RepoItemGroup;
import com.plantdata.kgcloud.domain.repo.model.req.RepositoryReq;
import com.plantdata.kgcloud.domain.repo.model.req.RepositoryUpdateReq;
import com.plantdata.kgcloud.domain.repo.model.rsp.GroupRsp;
import com.plantdata.kgcloud.domain.repo.model.rsp.RepositoryListRsp;
import com.plantdata.kgcloud.domain.repo.model.rsp.RepoItemRsp;
import com.plantdata.kgcloud.domain.repo.repository.RepoItemGroupRepository;
import com.plantdata.kgcloud.domain.repo.service.RepositoryService;
import com.plantdata.kgcloud.domain.repo.service.RepositoryUseLogService;
import com.plantdata.kgcloud.sdk.rsp.RepositoryLogMenuRsp;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
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
    private RepoItemGroupRepository repositoryGroupRepository;
    @Autowired
    private RepositoryUseLogService repositoryUseLogService;

    @ApiOperation("组件列表")
    @GetMapping
    public ApiReturn<RepositoryListRsp> repositoryRsp() {
        List<RepoItemRsp> list = repositoryService.list(SessionHolder.getUserId());
        List<RepoItemGroup> allGroups = repositoryGroupRepository.findAll();
        allGroups.sort(Comparator.comparing(RepoItemGroup::getRank));
        List<GroupRsp> groupRspList = BasicConverter.listToRsp(allGroups, RepositoryConverter::repositoryGroup2GroupRsp);
        return ApiReturn.success(new RepositoryListRsp(groupRspList, list));
    }

    @ApiOperation("组件新增")
    @PostMapping
    public ApiReturn<Integer> add(@RequestBody RepositoryReq repositoryReq) {
        return ApiReturn.success(repositoryService.add(repositoryReq));
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
        logEnumOpt.ifPresent(a -> repositoryUseLogService.save(a, id, SessionHolder.getUserId()));
        return ApiReturn.success(StringConstants.SUCCESS);
    }


    @ApiOperation("使用记录关联动态导航")
    @GetMapping("log/menu")
    public ApiReturn<List<RepositoryLogMenuRsp>> logMenuRsp() {
        return ApiReturn.success(repositoryService.menuLog(SessionHolder.getUserId()));
    }
}
