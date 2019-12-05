package com.plantdata.kgcloud.domain.menu.service;


import com.plantdata.kgcloud.domain.menu.req.MenuReq;
import com.plantdata.kgcloud.domain.menu.rsp.MenuRsp;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-25 20:17
 **/
public interface MenuService {
    /**
     *

     * @param bean
     */
    void updateMenu(MenuReq bean);

    /**
     *
     * @param id
     * @return
     */
    List<MenuRsp> getMenuById(Integer id);

    /**
     * 刷新缓存
     */
    void refresh();

    /**
     *
     * @param menuBeans
     */
    void updateBatch(List<MenuReq> menuBeans);
}
