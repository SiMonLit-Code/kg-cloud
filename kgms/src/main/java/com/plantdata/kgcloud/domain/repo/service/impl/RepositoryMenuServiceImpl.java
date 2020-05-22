package com.plantdata.kgcloud.domain.repo.service.impl;

import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.repo.model.RepositoryMenu;
import com.plantdata.kgcloud.domain.repo.repository.RepositoryMenuRepository;
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
    private RepositoryMenuRepository repositoryMenuRepository;

    @Override
    public void saveOrUpdate(List<Integer> menuIdList, Integer repositoryId) {
        BasicConverter.consumerIfNoNull(menuIdList, a -> {
            List<RepositoryMenu> menus = a.stream().map(b -> new RepositoryMenu(b, repositoryId)).collect(Collectors.toList());
            repositoryMenuRepository.saveAll(menus);
        });
    }
}
