package com.plantdata.kgcloud.domain.repo.service.impl;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.repo.enums.RepositoryLogEnum;
import com.plantdata.kgcloud.domain.repo.model.RepositoryMenu;
import com.plantdata.kgcloud.domain.repo.model.RepositoryUseLog;
import com.plantdata.kgcloud.domain.repo.repository.RepositoryMenuRepository;
import com.plantdata.kgcloud.domain.repo.repository.RepositoryUseLogRepository;
import com.plantdata.kgcloud.domain.repo.service.RepositoryUseLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @date 2020/5/22  12:59
 */
@Service
public class RepositoryUseLogServiceImpl implements RepositoryUseLogService {

    @Autowired
    private RepositoryUseLogRepository repositoryUseLogRepository;
    @Autowired
    private RepositoryMenuRepository repositoryMenuRepository;

    @Override
    public void save(RepositoryLogEnum logEnum, int id, String userId) {
        repositoryUseLogRepository.save(new RepositoryUseLog(id, logEnum, userId));
    }

    @Override
    public void deleteByRepositoryId(Integer repositoryId) {
        repositoryUseLogRepository.deleteByBusinessIdInAndLogType(Lists.newArrayList(repositoryId), RepositoryLogEnum.REPOSITORY);
        List<RepositoryMenu> menus = repositoryMenuRepository.findAllByRepositoryIdIn(Lists.newArrayList(repositoryId));
        BasicConverter.consumerIfNoNull(menus,a->{
            List<Integer> menuIds = a.stream().map(RepositoryMenu::getMenuId).collect(Collectors.toList());
            repositoryUseLogRepository.deleteByBusinessIdInAndLogType(menuIds, RepositoryLogEnum.MENU);
        });
    }

    @Override
    public Set<Integer> listRepositoryId(String userId) {
        Set<Integer> repositoryIds = new HashSet<>();
        List<RepositoryUseLog> repoUseLogs = repositoryUseLogRepository.findAllByUserIdAndLogType(userId, RepositoryLogEnum.REPOSITORY);
        BasicConverter.consumerIfNoNull(repoUseLogs, a -> {
            Set<Integer> repoIds = repoUseLogs.stream().map(RepositoryUseLog::getBusinessId).collect(Collectors.toSet());
            repositoryIds.addAll(repoIds);
        });

        List<RepositoryUseLog> menuUseLogs = repositoryUseLogRepository.findAllByUserIdAndLogType(userId, RepositoryLogEnum.MENU);
        BasicConverter.consumerIfNoNull(repoUseLogs, a -> {
            Set<Integer> menuIds = menuUseLogs.stream().map(RepositoryUseLog::getBusinessId).collect(Collectors.toSet());
            List<RepositoryMenu> repositoryMenus = repositoryMenuRepository.findAllByMenuIdIn(new ArrayList<>(menuIds));
            BasicConverter.consumerIfNoNull(repositoryMenus, b -> {
                Set<Integer> repoIds = repositoryMenus.stream().map(RepositoryMenu::getRepositoryId).collect(Collectors.toSet());
                repositoryIds.addAll(repoIds);
            });
        });
        return repositoryIds;
    }

}
