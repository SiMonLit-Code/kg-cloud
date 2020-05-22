package com.plantdata.kgcloud.domain.repo.service.impl;

import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.repo.checker.ServiceChecker;
import com.plantdata.kgcloud.domain.repo.converter.RepositoryConverter;
import com.plantdata.kgcloud.domain.repo.enums.RepositoryLogEnum;
import com.plantdata.kgcloud.domain.repo.factory.ServiceCheckerFactory;
import com.plantdata.kgcloud.domain.repo.model.Repository;
import com.plantdata.kgcloud.domain.repo.model.RepositoryMenu;
import com.plantdata.kgcloud.domain.repo.model.req.RepositoryReq;
import com.plantdata.kgcloud.domain.repo.model.req.RepositoryUpdateReq;
import com.plantdata.kgcloud.domain.repo.model.rsp.RepositoryRsp;
import com.plantdata.kgcloud.domain.repo.repository.RepositoryMenuRepository;
import com.plantdata.kgcloud.domain.repo.repository.RepositoryRepository;
import com.plantdata.kgcloud.domain.repo.service.RepositoryMenuService;
import com.plantdata.kgcloud.domain.repo.service.RepositoryService;
import com.plantdata.kgcloud.domain.repo.service.RepositoryUseLogService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.rsp.RepositoryLogMenuRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @date 2020/5/18  16:03
 */
@Service
public class RepositoryServiceImpl implements RepositoryService {

    @Autowired
    private RepositoryRepository repositoryRepository;
    @Autowired
    private RepositoryUseLogService repositoryUseLogService;
    @Autowired
    private RepositoryMenuRepository repositoryMenuRepository;
    @Autowired
    private RepositoryMenuService repositoryMenuService;

    @Override
    public List<RepositoryRsp> list(String userId, boolean withBasic) {
        List<RepositoryRsp> repositoryRspList = basicListWithCheck(withBasic);
        //检测是否为最新
        Set<Integer> repositoryIds = repositoryUseLogService.listRepositoryId(userId);
        BasicConverter.consumerIfNoNull(repositoryIds, a -> repositoryRspList.forEach(b -> b.setNewFunction(!a.contains(b.getId()))));
        return repositoryRspList;
    }


    private List<RepositoryRsp> basicListWithCheck(boolean withBasic) {
        List<Repository> all = repositoryRepository.findAll();
        if (CollectionUtils.isEmpty(all)) {
            return Collections.emptyList();
        }
        if (!withBasic) {
            all = all.stream().filter(a -> a.getGroupId() != 0).collect(Collectors.toList());
        }
        List<RepositoryRsp> repositoryRspList = BasicConverter.listToRsp(all, RepositoryConverter::repository2RepositoryRsp);
        //填充组件状态
        Function<Repository, Boolean> health = b -> ServiceCheckerFactory.factory(b.getCheckConfigs()).stream().allMatch(a -> {
            try {
                return a.check();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
        Map<Integer, Boolean> stateMap = all.stream().collect(Collectors.toMap(Repository::getId, health));
        BasicConverter.listConsumerIfNoNull(repositoryRspList, a -> a.setEnable(stateMap.getOrDefault(a.getId(), false)));
        return repositoryRspList;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Integer add(RepositoryReq repositoryReq) {
        Repository repository = repositoryRepository.save(RepositoryConverter.repositoryReq2Repository(repositoryReq));
        repositoryMenuService.saveOrUpdate(repositoryReq.getMenuIds(), repository.getId());
        return repository.getId();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean modify(RepositoryUpdateReq updateReq) {
        //保存
        Repository repository = RepositoryConverter.repositoryUpdateReq2RepoCheckConfig(updateReq);
        repositoryMenuService.saveOrUpdate(updateReq.getMenuIds(), repository.getId());
        //保存菜单信息
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(int id) {
        Optional<Repository> repository = repositoryRepository.findById(id);
        repository.ifPresent(a -> {
            repositoryRepository.deleteById(id);
            repositoryMenuRepository.deleteAllByRepositoryId(id);
        });
        return true;
    }

    @Override
    public boolean updateStatus(Integer id, boolean start) {
        Optional<Repository> repOpt = repositoryRepository.findById(id);
        repOpt.ifPresent(a -> {
            boolean res = ServiceCheckerFactory.factory(a.getCheckConfigs()).stream().anyMatch(ServiceChecker::check);
            if (res) {
                throw new BizException("组件启用失败,健康检测未通过");
            }
            a.setState(start);
            repositoryRepository.save(a);
            //清除使用状态
            repositoryUseLogService.deleteByRepositoryId(id);
        });
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void useLog(RepositoryLogEnum type, Integer id, String userId) {
        repositoryUseLogService.save(type, id, userId);
    }


    @Override
    public List<RepositoryLogMenuRsp> menuLog(String userId) {
        List<RepositoryRsp> repositoryRspList = basicListWithCheck(true);
        Map<Integer, RepositoryRsp> rspMap = repositoryRspList.stream().collect(Collectors.toMap(RepositoryRsp::getId, Function.identity(), (a, b) -> b));
        List<RepositoryMenu> all = repositoryMenuRepository.findAll();
        Set<Integer> menuIds = repositoryUseLogService.listMenuId(userId);
        return all.stream().map(a -> {
            RepositoryRsp rsp = rspMap.get(a.getRepositoryId());
            return new RepositoryLogMenuRsp(a.getMenuId(), !menuIds.contains(a.getMenuId()), rsp.isEnable() && rsp.isState());
        }).collect(Collectors.toList());
    }


}
