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
        BasicConverter.consumerIfNoNull(menus, a -> {
            List<Integer> menuIds = a.stream().map(RepositoryMenu::getMenuId).collect(Collectors.toList());
            repositoryUseLogRepository.deleteByBusinessIdInAndLogType(menuIds, RepositoryLogEnum.MENU);
        });
    }

    @Override
    public Set<Integer> listRepositoryId(String userId) {
        List<Integer> first = addFirst(userId, RepositoryLogEnum.REPOSITORY);
        List<Integer> two = addTwo(userId, RepositoryLogEnum.MENU);

        return BasicConverter.flatToSet(Lists.newArrayList(first, two));
    }

    @Override
    public Set<Integer> listMenuId(String userId) {
        List<Integer> first = addFirst(userId, RepositoryLogEnum.MENU);
        List<Integer> two = addTwo(userId, RepositoryLogEnum.REPOSITORY);
        return BasicConverter.flatToSet(Lists.newArrayList(first, two));
    }

    private List<Integer> addFirst(String userId, RepositoryLogEnum logEnum) {
        List<RepositoryUseLog> first = repositoryUseLogRepository.findAllByUserIdAndLogType(userId, logEnum);
        return BasicConverter.listToRsp(first, RepositoryUseLog::getBusinessId);

    }

    private List<Integer> addTwo(String userId, RepositoryLogEnum logEnum) {
        List<RepositoryUseLog> two = repositoryUseLogRepository.findAllByUserIdAndLogType(userId, logEnum);
        Set<Integer> tempIds = two.stream().map(RepositoryUseLog::getBusinessId).collect(Collectors.toSet());
        if (RepositoryLogEnum.REPOSITORY == logEnum) {
            List<RepositoryMenu> repositoryMenus = repositoryMenuRepository.findAllByRepositoryIdIn(new ArrayList<>(tempIds));
            return BasicConverter.listToRsp(repositoryMenus, RepositoryMenu::getRepositoryId);
        }
        List<RepositoryMenu> repositoryMenus = repositoryMenuRepository.findAllByMenuIdIn(new ArrayList<>(tempIds));
        return BasicConverter.listToRsp(repositoryMenus, RepositoryMenu::getMenuId);

    }
}
