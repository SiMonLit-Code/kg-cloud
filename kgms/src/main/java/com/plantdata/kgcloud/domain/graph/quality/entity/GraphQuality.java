package com.plantdata.kgcloud.domain.graph.quality.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: LinHo
 * @Date: 2020/3/21 14:03
 * @Description:
 */
@Getter
@Setter
@Entity
@Table(name = "graph_quality")
@EntityListeners(AuditingEntityListener.class)
public class GraphQuality {
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "self_id")
    private Long selfId;

    @Basic
    @Column(name = "kg_name")
    private String kgName;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "concept_id")
    private Long conceptId;

    @Basic
    @Column(name = "entity_count")
    private Long entityCount;

    @Basic
    @Column(name = "entity_total")
    private Long entityTotal;

    @Basic
    @Column(name = "attr_definition_count")
    private Integer attrDefinitionCount;

    @Basic
    @Column(name = "schema_integrity")
    private Double schemaIntegrity;

    @Basic
    @Column(name = "reliability")
    private Double reliability;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
