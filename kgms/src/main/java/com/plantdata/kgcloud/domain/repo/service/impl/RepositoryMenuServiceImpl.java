package com.plantdata.kgcloud.domain.repo.service.impl;

import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.repo.entity.RepoMenu;
import com.plantdata.kgcloud.domain.repo.repository.RepoMenuRepository;
import com.plantdata.kgcloud.domain.repo.service.RepositoryMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @date 2020/5/22  9:41
 */
@Service
public class RepositoryMenuServiceImpl implements RepositoryMenuService {

    @Autowired
    private RepoMenuRepository repoMenuRepository;

    @Override
    public void saveOrUpdate(List<Integer> menuIdList, Integer repositoryId) {
        BasicConverter.consumerIfNoNull(menuIdList, a -> {
            List<RepoMenu> menus = a.stream().map(b -> new RepoMenu(b, repositoryId)).collect(Collectors.toList());
            repoMenuRepository.saveAll(menus);
        });
    }
}
