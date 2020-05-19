package com.plantdata.kgcloud.domain.repo.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @date 2020/5/18  14:19
 */
@Getter
@Setter
public class RepositoryMenu {

    private int id;
    /**
     * 组件id
     */
    private int repositoryId;
    /**
     * 菜单id
     */
    private int menuId;
}
