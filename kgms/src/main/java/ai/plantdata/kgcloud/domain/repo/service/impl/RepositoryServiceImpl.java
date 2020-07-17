package ai.plantdata.kgcloud.domain.repo.service.impl;

import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.kgcloud.domain.repo.converter.RepositoryConverter;
import ai.plantdata.kgcloud.domain.app.converter.BasicConverter;
import ai.plantdata.kgcloud.domain.repo.checker.ServiceChecker;
import ai.plantdata.kgcloud.domain.repo.entity.RepoBaseMenu;
import ai.plantdata.kgcloud.domain.repo.entity.RepoItem;
import ai.plantdata.kgcloud.domain.repo.entity.RepoMenu;
import ai.plantdata.kgcloud.domain.repo.factory.ServiceCheckerFactory;
import ai.plantdata.kgcloud.domain.repo.model.req.RepositoryReq;
import ai.plantdata.kgcloud.domain.repo.model.req.RepositoryUpdateReq;
import ai.plantdata.kgcloud.domain.repo.model.rsp.RepoItemRsp;
import ai.plantdata.kgcloud.domain.repo.repository.RepoBaseMenuRepository;
import ai.plantdata.kgcloud.domain.repo.repository.RepoItemRepository;
import ai.plantdata.kgcloud.domain.repo.repository.RepoMenuRepository;
import ai.plantdata.kgcloud.domain.repo.service.RepositoryMenuService;
import ai.plantdata.kgcloud.domain.repo.service.RepositoryService;
import ai.plantdata.kgcloud.domain.repo.service.RepositoryUseLogService;
import ai.plantdata.kgcloud.sdk.rsp.RepositoryLogMenuRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @date 2020/5/18  16:03
 */
@Service
public class RepositoryServiceImpl implements RepositoryService {

    @Autowired
    private RepoBaseMenuRepository repoBaseMenuRepository;

    @Autowired
    private RepoItemRepository repoItemRepository;
    @Autowired
    private RepositoryUseLogService repositoryUseLogService;
    @Autowired
    private RepoMenuRepository repoMenuRepository;
    @Autowired
    private RepositoryMenuService repositoryMenuService;

    private Function<RepoItem, Boolean> health = b -> {
        if (b.getCheckConfigs().isEmpty()) {
            return false;
        }
        try {
            for (ServiceChecker serviceChecker : ServiceCheckerFactory.factory(b.getCheckConfigs())) {
                if (serviceChecker.check()) {
                    continue;
                }
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    };

    @Override
    public List<RepoItemRsp> list(String userId) {
        List<RepoItemRsp> repositoryRspList = basicListWithCheck();
        //检测是否为最新
        Set<Integer> repositoryIds = repositoryUseLogService.listRepositoryId(userId);
        BasicConverter.consumerIfNoNull(repositoryIds, a -> repositoryRspList.forEach(b -> b.setNewFunction(!a.contains(b.getId()))));
        return repositoryRspList;
    }

    public List<RepoItemRsp> basicListWithCheck() {
        List<RepoItem> all = repoItemRepository.findAll();
        if (CollectionUtils.isEmpty(all)) {
            return Collections.emptyList();
        }
        List<RepoItemRsp> repositoryRspList = BasicConverter.listToRsp(all, RepositoryConverter::repository2RepositoryRsp);
        Map<Integer, Boolean> stateMap = stateMap(all);
        BasicConverter.listConsumerIfNoNull(repositoryRspList, a -> a.setEnable(stateMap.getOrDefault(a.getId(), false)));
        return repositoryRspList;
    }

    @Override
    public Map<Integer, Boolean> stateMap(List<RepoItem> all) {
        //填充组件状态

        return all.stream().collect(Collectors.toMap(RepoItem::getId, health));
    }

    @Override
    public Boolean state(Integer repoId) {
        Optional<RepoItem> repoItem = repoItemRepository.findById(repoId);
        if (repoItem.isPresent()) {
            return health.apply(repoItem.get());
        } else {
            return false;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Integer add(RepositoryReq repositoryReq) {
        RepoItem repository = repoItemRepository.save(RepositoryConverter.repositoryReq2Repository(repositoryReq));
        repositoryMenuService.saveOrUpdate(repositoryReq.getMenuIds(), repository.getId());
        return repository.getId();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean modify(RepositoryUpdateReq updateReq) {
        //保存
        RepoItem repository = RepositoryConverter.repositoryUpdateReq2RepoCheckConfig(updateReq);
        repositoryMenuService.saveOrUpdate(updateReq.getMenuIds(), repository.getId());
        //保存菜单信息
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(int id) {
        Optional<RepoItem> repository = repoItemRepository.findById(id);
        repository.ifPresent(a -> {
            repoItemRepository.deleteById(id);
            repoMenuRepository.deleteAllByRepositoryId(id);
        });
        return true;
    }

    @Override
    public boolean updateStatus(Integer id, boolean start) {
        Optional<RepoItem> repOpt = repoItemRepository.findById(id);
        repOpt.ifPresent(a -> {
            boolean res = ServiceCheckerFactory.factory(a.getCheckConfigs()).stream().allMatch(ServiceChecker::check);
            if (!res) {
                throw new BizException("组件启用失败,健康检测未通过");
            }
            a.setState(start);
            repoItemRepository.save(a);
            //清除使用状态
            repositoryUseLogService.deleteByRepositoryId(id);
        });
        return true;
    }

    @Override
    public List<RepositoryLogMenuRsp> menuLog(String userId) {
        ArrayList<RepositoryLogMenuRsp> menuLogRspList = new ArrayList<>();
        //基础版菜单状态
        List<RepoBaseMenu> repoBaseMenuList = repoBaseMenuRepository.findAll();
        for (RepoBaseMenu repoBaseMenu : repoBaseMenuList) {
            RepositoryLogMenuRsp menuLogRsp = new RepositoryLogMenuRsp(repoBaseMenu.getMenuId(), false, true);
            menuLogRspList.add(menuLogRsp);
        }
        //扩展菜单状态
        List<RepoItemRsp> repositoryRspList = basicListWithCheck();
        Map<Integer, RepoItemRsp> rspMap = repositoryRspList.stream().collect(Collectors.toMap(RepoItemRsp::getId, Function.identity(), (a, b) -> b));
        Set<Integer> menuIds = repositoryUseLogService.listMenuId(userId);
        List<RepoMenu> all = repoMenuRepository.findAll();
        for (RepoMenu repoMenu : all) {
            Integer repositoryId = repoMenu.getRepositoryId();
            Integer menuId = repoMenu.getMenuId();
            RepoItemRsp rsp = rspMap.get(repositoryId);
            RepositoryLogMenuRsp menuLogRsp = new RepositoryLogMenuRsp(menuId, !menuIds.contains(menuId), rsp != null && rsp.isEnable() && rsp.isState());
            menuLogRspList.add(menuLogRsp);
        }
        return menuLogRspList;
    }


}
