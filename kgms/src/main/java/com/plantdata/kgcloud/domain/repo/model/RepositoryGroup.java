package com.plantdata.kgcloud.domain.repo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
/**
 * @author cjw
 * @date 2020/5/19  17:00
 */
@Getter
@Setter
@Entity
@Table(name = "repo_repository_group")
public class RepositoryGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * 所属分组id
     */
    @Column(name = "group_id")
    private int groupId;

    /**
     * 分组名称
     */
    @Column(name = "group_name")
    private String groupName;

    /**
     * 排序
     */
    @Column(name = "rank")
    private Integer rank;

    /**
     * 分组描述
     */
    @Column(name = "desc")
    private String desc;

}
