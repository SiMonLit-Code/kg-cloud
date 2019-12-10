package com.plantdata.kgcloud.domain.graph.config.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.plantdata.kgcloud.domain.common.converter.JsonNodeConcerter;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
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
    @Convert(converter = JsonNodeConcerter.class)
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
