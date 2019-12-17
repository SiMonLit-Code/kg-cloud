package com.plantdata.kgcloud.domain.graph.config.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.plantdata.kgcloud.domain.common.converter.JsonNodeConverter;
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
@NoArgsConstructor
@Entity
@Table(name = "graph_conf_statistical")
@EntityListeners(AuditingEntityListener.class)
public class GraphConfStatistical {

    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "kg_name")
    private String kgName;

    @Basic
    @Column(name = "statis_type")
    private String statisType;

    @Basic
    @Column(name = "statis_rule")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode statisRule;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
