package com.plantdata.kgcloud.domain.repo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Bovin
 * @description
 * @since 2020-05-23 13:12
 **/
@Getter
@Setter
@Entity
@Table(name = "repo_base_menu")
@NoArgsConstructor
public class RepoBaseMenu {
    @Id
    @Column(name = "menu_id")
    private Integer menuId;

    @Column(name = "repository_id")
    private Integer repositoryId;

}
