package com.plantdata.kgcloud.domain.repo.entity;

import com.plantdata.kgcloud.domain.common.converter.JsonObjectConverter;
import com.plantdata.kgcloud.domain.repo.converter.RepoCheckConfigJsonConverter;
import com.plantdata.kgcloud.domain.repo.converter.RepoItemTypeConverter;
import com.plantdata.kgcloud.domain.repo.enums.RepoItemType;
import com.plantdata.kgcloud.domain.repo.model.RepoCheckConfig;
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
@Table(name = "repo_item")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class RepoItem {
    /**
     * 组件id
     */
    @Id
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
    @Convert(converter = RepoItemTypeConverter.class)
    private RepoItemType type;
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
    private String remark;
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
