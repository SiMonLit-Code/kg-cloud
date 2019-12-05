package com.plantdata.kgcloud.domain.menu.service;

import com.google.common.collect.Maps;
import com.plantdata.kgcloud.domain.menu.AESUtil;
import com.plantdata.kgcloud.domain.menu.entity.Menu;
import com.plantdata.kgcloud.domain.menu.repository.MenuRepository;
import com.plantdata.kgcloud.domain.menu.req.MenuReq;
import com.plantdata.kgcloud.domain.menu.rsp.MenuRsp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-25 20:18
 **/
@Service
public class MenuServiceImpl implements MenuService {

    private Map<Integer, List<MenuRsp>> mapMenuTree;

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public void updateMenu(MenuReq bean) {
        menuRepository.save(menuByDecode(bean));
        refresh();
    }

    @Override
    public List<MenuRsp> getMenuById(Integer id) {
        getMenuTree();
        return mapMenuTree.get(id);
    }


    @Override
    public void updateBatch(List<MenuReq> menuReqs) {
        List<Menu> menuEncodeBeans = menuReqs.stream().map(this::menuByDecode).collect(Collectors.toList());
        menuRepository.saveAll(menuEncodeBeans);
        refresh();
    }

    @Override
    public void refresh() {
        List<Menu> menuList = menuRepository.findAll();
        List<MenuRsp> menuTreeList = menuList.stream().map(this::decodeByMenu).collect(Collectors.toList());
        mapMenuTree = buildMapMenuTree(null, menuTreeList);
    }

    private void getMenuTree() {
        if (mapMenuTree == null || mapMenuTree.isEmpty()) {
            refresh();
        }
    }


    private Map<Integer, List<MenuRsp>> buildMapMenuTree(Integer topId, List<MenuRsp> menuTreeList) {
        List<MenuRsp> resultList = new ArrayList<>();
        Map<Integer, List<MenuRsp>> mapTree = Maps.newHashMap();
        //获取顶层元素集合
        Integer pid;
        if (Objects.isNull(topId)) {
            //全查
            for (MenuRsp menuTree : menuTreeList) {
                pid = menuTree.getPid();
                if (Objects.isNull(pid) || pid == 0) {
                    resultList.add(menuTree);
                }
            }
            resultList.sort(Comparator.comparing(MenuRsp::getOrder));
        } else {
            //根据传入的ID进行向下递归
            for (MenuRsp menuTree : menuTreeList) {
                pid = menuTree.getPid();
                if (pid != null && topId.equals(pid)) {
                    resultList.add(menuTree);
                }
            }
            resultList.sort(Comparator.comparing(MenuRsp::getOrder));
        }

        //获取每个顶层元素的子数据集合
        for (MenuRsp menuTree : resultList) {
            List<MenuRsp> subList = getSubList(menuTree.getId(), menuTreeList, mapTree);
            subList.sort(Comparator.comparing(MenuRsp::getOrder));
            menuTree.setChildren(subList);
        }

        return mapTree;
    }


    private List<MenuRsp> getSubList(Integer id, List<MenuRsp> menuTrees, Map<Integer, List<MenuRsp>> mapTree) {
        List<MenuRsp> childList = new ArrayList<>();
        Integer pid;
        for (MenuRsp menuTree : menuTrees) {
            pid = menuTree.getPid();
            if (id.equals(pid)) {
                childList.add(menuTree);
            }
        }
        if (childList.size() != 0) {
            mapTree.put(id, childList);
        }
        for (MenuRsp menuTree : childList) {
            List<MenuRsp> subList = getSubList(menuTree.getId(), menuTrees, mapTree);
            menuTree.setChildren(subList);
        }
        //递归退出条件
        if (childList.size() == 0) {
            return new ArrayList<>();
        }
        childList.sort(Comparator.comparing(MenuRsp::getOrder));
        return childList;
    }


    private Menu menuByDecode(MenuReq bean) {
        String id = bean.getId() + "_" + bean.getPid();
        Menu menu = new Menu();
        BeanUtils.copyProperties(bean, menu);
        String aesId = AESUtil.encryptAES(id.getBytes());
        menu.setId(aesId);
        return menu;
    }

    private MenuRsp decodeByMenu(Menu menu) {
        String encodeId = menu.getId();
        Integer[] enIds = AESUtil.encryptEnid(encodeId);
        MenuRsp menuDecode = new MenuRsp();
        BeanUtils.copyProperties(menu, menuDecode);
        menuDecode.setId(enIds[0]);
        menuDecode.setPid(enIds[1]);
        return menuDecode;
    }

}
