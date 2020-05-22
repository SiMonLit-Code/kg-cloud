package com.plantdata.kgcloud.domain.repo.service.impl;

import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.repo.checker.ServiceChecker;
import com.plantdata.kgcloud.domain.repo.converter.RepositoryConverter;
import com.plantdata.kgcloud.domain.repo.enums.RepositoryLogEnum;
import com.plantdata.kgcloud.domain.repo.factory.ServiceCheckerFactory;
import com.plantdata.kgcloud.domain.repo.model.Repository;
import com.plantdata.kgcloud.domain.repo.model.RepositoryMenu;
import com.plantdata.kgcloud.domain.repo.model.RepositoryUseLog;
import com.plantdata.kgcloud.domain.repo.model.req.RepositoryReq;
import com.plantdata.kgcloud.domain.repo.model.req.RepositoryUpdateReq;
import com.plantdata.kgcloud.domain.repo.model.rsp.RepositoryRsp;
import com.plantdata.kgcloud.domain.repo.repository.RepositoryMenuRepository;
import com.plantdata.kgcloud.domain.repo.repository.RepositoryRepository;
import com.plantdata.kgcloud.domain.repo.repository.RepositoryUseLogRepository;
import com.plantdata.kgcloud.domain.repo.service.RepositoryMenuService;
import com.plantdata.kgcloud.domain.repo.service.RepositoryService;
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
    private RepositoryUseLogRepository repositoryUseLogRepository;
    @Autowired
    private RepositoryMenuRepository repositoryMenuRepository;
    @Autowired
    private RepositoryMenuService repositoryMenuService;

    @Override
    public List<RepositoryRsp> list(String userId, boolean withBasic) {
        List<Repository> all = repositoryRepository.findAll();
        if (CollectionUtils.isEmpty(all)) {
            return Collections.emptyList();
        }
        if (!withBasic) {
            all = all.stream().filter(a -> a.getGroupId() != 0).collect(Collectors.toList());
        }
        List<RepositoryRsp> repositoryRspList = BasicConverter.listToRsp(all, RepositoryConverter::repository2RepositoryRsp);
        //填充组件状态
        Function<Repository, Boolean> health = b -> ServiceCheckerFactory.factory(b.getCheckConfigs()).stream().allMatch(ServiceChecker::check);
        Map<Integer, Boolean> stateMap = all.stream().collect(Collectors.toMap(Repository::getId, health));
        BasicConverter.listConsumerIfNoNull(repositoryRspList, a -> a.setEnable(stateMap.getOrDefault(a.getId(), true)));
        //检测是否为最新
        List<RepositoryUseLog> useLogs = repositoryUseLogRepository.findAllByUserIdAndRepositoryIdIn(userId, stateMap.keySet());
        BasicConverter.consumerIfNoNull(useLogs, a -> {
            Set<Integer> repositoryIds = useLogs.stream().map(RepositoryUseLog::getRepositoryId).collect(Collectors.toSet());
            repositoryRspList.forEach(b -> b.setNewFunction(!repositoryIds.contains(b.getId())));
        });
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
            repositoryUseLogRepository.deleteByRepositoryId(id);
        });
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void useLog(RepositoryLogEnum type, Integer id, String userId) {
        Optional<Repository> repOpt = repositoryRepository.findById(id);
        repOpt.ifPresent(a -> {
            if (RepositoryLogEnum.MENU == type) {
                saveByMenu(id, userId);
            }
        });
    }

    public void saveByMenu(int menuId, String userId) {
        RepositoryMenu repositoryMenu = repositoryMenuRepository.findByMenuId(menuId);
        List<RepositoryUseLog> useLogs = repositoryUseLogRepository.findAllByUserIdAndRepositoryIdIn(userId, repositoryMenu.getRepositoryId());
        if (CollectionUtils.isEmpty(useLogs)) {
            boolean have = useLogs.stream().anyMatch(a -> a.getMenuId() == menuId);
            if (!have) {
                repositoryUseLogRepository.save(new RepositoryUseLog());
            }
        }
    }


    @Override
    public List<RepositoryLogMenuRsp> menuLog(String userId) {
        List<RepositoryRsp> list = list(userId, true);
        Map<Integer, RepositoryRsp> rspMap = list.stream().collect(Collectors.toMap(RepositoryRsp::getId, Function.identity()));
        List<RepositoryMenu> all = repositoryMenuRepository.findAll();
        return all.stream().map(a -> {
            RepositoryRsp rsp = rspMap.get(a.getRepositoryId());
            return new RepositoryLogMenuRsp(a.getMenuId(), rsp.getNewFunction(), rsp.isEnable() && rsp.isState());
        }).collect(Collectors.toList());
    }


}
