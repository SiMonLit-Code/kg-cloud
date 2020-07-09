package com.plantdata.kgcloud.domain.menu.service;

import ai.plantdata.cloud.web.util.ConvertUtils;
import com.plantdata.kgcloud.domain.menu.entity.MenuFavor;
import com.plantdata.kgcloud.domain.menu.repository.MenuFavorRepository;
import com.plantdata.kgcloud.domain.menu.rsp.MenuFavorRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-25 20:18
 **/
@Service
public class MenuFavorServiceImpl implements MenuFavorService {

    @Autowired
    private MenuFavorRepository menuFavorRepository;

    @Override
    public MenuFavorRsp find(String userId) {
        return menuFavorRepository
                .findById(userId)
                .map(ConvertUtils.convert(MenuFavorRsp.class))
                .orElse(new MenuFavorRsp());
    }

    @Override
    public MenuFavorRsp favor(String userId, List<Integer> list) {
        MenuFavor menuFavor = new MenuFavor();
        menuFavor.setUserId(userId);
        menuFavor.setMenuId(list);
        MenuFavor save = menuFavorRepository.save(menuFavor);
        return ConvertUtils.convert(MenuFavorRsp.class).apply(save);
    }
}
