package com.plantdata.kgcloud.domain.repo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author cjw
 * @date 2020/5/21  18:49
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "repo_repository_menu")
public class RepositoryMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "menu_id")
    private Integer menuId;
    @Column(name = "repository_id")
    private Integer repositoryId;

    public RepositoryMenu(Integer menuId, Integer repositoryId) {
        this.menuId = menuId;
        this.repositoryId = repositoryId;
    }
}
