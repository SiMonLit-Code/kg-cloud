package com.plantdata.kgcloud.domain.graph.config.entity;

import ai.plantdata.kg.api.pub.resp.QuerySetting;
import com.fasterxml.jackson.databind.JsonNode;
import com.plantdata.kgcloud.domain.common.converter.JsonNodeConverter;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

@Data
@Entity
@Table(name = "graph_conf_reasoning")
@EntityListeners(AuditingEntityListener.class)
public class GraphConfReasoning {
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "kg_name")
    private String kgName;

    @Basic
    @Column(name = "rule_name")
    private String ruleName;

    @Basic
    @Column(name = "rule_config")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode ruleConfig;

    @Basic
    @Column(name = "rule_settings")
    private String ruleSettings;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;
}
