package com.plantdata.kgcloud.domain.repo.model;

import com.plantdata.kgcloud.domain.common.converter.IntegerListConverter;
import com.plantdata.kgcloud.domain.common.converter.ObjectJsonConverter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @date 2020/5/15  11:34
 */
@Getter
@Setter
@Entity
@Table(name = "repo_repository")
@EntityListeners(AuditingEntityListener.class)
public class Repository {
    /**
     * 组件id
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    /**
     * 组件名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 组件分组名称前端定义
     */
    @Column(name = "group")
    private int group;
    /**
     * 启停状态
     */
    @Column(name = "state")
    private boolean state;
    /**
     * 排序
     */
    @Column(name = "rank")
    private int rank;
    /**
     * 菜单id
     */
    @Column(name = "menu_id")
    private int menuId;
    /**
     * 描述
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 前端自定义配置
     */
    @Column(name = "config")
    @Convert(converter = ObjectJsonConverter.class)
    private Map<String, Object> config;
    /**
     * 检测项目
     */
    @Column(name = "check_configs")
    @Convert(converter = ObjectJsonConverter.class)
    private List<RepoCheckConfig> checkConfigs;
}
