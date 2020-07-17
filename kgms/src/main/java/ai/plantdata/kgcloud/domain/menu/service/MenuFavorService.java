package ai.plantdata.kgcloud.domain.menu.service;

import ai.plantdata.kgcloud.domain.menu.rsp.MenuFavorRsp;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-25 20:17
 **/
public interface MenuFavorService {

    /**
     *
     * @param userId
     * @return
     */
    MenuFavorRsp find(String userId);

    /**
     *
     * @param userId
     * @param menuId
     * @return
     */
    MenuFavorRsp favor(String userId, List<Integer> menuId);
}
