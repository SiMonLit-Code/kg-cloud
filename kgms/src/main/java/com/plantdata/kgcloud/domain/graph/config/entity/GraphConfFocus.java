package com.plantdata.kgcloud.domain.graph.config.entity;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.plantdata.kgcloud.domain.common.converter.ArrayNodeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "graph_conf_focus")
@IdClass(GraphConfFocusPk.class)
@EntityListeners(AuditingEntityListener.class)
public class GraphConfFocus {

    @Id
    @Column(name = "kg_name")
    private String kgName;

    @Id
    @Column(name = "focus_type")
    private String type;

    @Basic
    @Column(name = "focus_entity")
    @Convert(converter = ArrayNodeConverter.class)
    private ArrayNode entities;

    @Basic
    @Column(name = "focus_config")
    private String focusConfig;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
