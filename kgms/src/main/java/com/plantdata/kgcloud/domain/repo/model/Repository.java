package com.plantdata.kgcloud.domain.repo.model;

import com.plantdata.kgcloud.domain.common.converter.JsonObjectConverter;
import com.plantdata.kgcloud.domain.repo.converter.RepoCheckConfigJsonConverter;
import com.plantdata.kgcloud.domain.repo.converter.RepoTypeConverter;
import com.plantdata.kgcloud.domain.repo.enums.RepositoryTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
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
@NoArgsConstructor
public class Repository {
    /**
     * 组件id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    /**
     * 组件名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 组件类型
     */
    @Column(name = "type")
    @Convert(converter = RepoTypeConverter.class)
    private RepositoryTypeEnum type;
    /**
     * 组件分组名称前端定义
     */
    @Column(name = "group_id")
    private int groupId;
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
     * 描述
     */
    @Column(name = "remark")
    private String remark = StringUtils.EMPTY;
    /**
     * 前端自定义配置
     */
    @Column(name = "config")
    @Convert(converter = JsonObjectConverter.class)
    private Map<String, Object> config;
    /**
     * 检测项目
     */
    @Column(name = "check_configs")
    @Convert(converter = RepoCheckConfigJsonConverter.class)
    private List<RepoCheckConfig> checkConfigs;


}
