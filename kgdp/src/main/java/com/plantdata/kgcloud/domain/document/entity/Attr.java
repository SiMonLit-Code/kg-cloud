package com.plantdata.kgcloud.domain.document.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Builder
@Table(name = "attr")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Attr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "concept_id")
    private Integer conceptId;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "old_name")
    private String oldName;

    @Basic
    @Column(name = "type")
    private Integer type;

    @Basic
    @Column(name = "data_type")
    private Integer dataType;

    @Basic
    @Column(name = "range_to")
    private String rangeTo;

    //状态字段
    @Basic
    @Column(name = "is_ignore")
    private Boolean isIgnore;

    @Basic
    @Column(name = "is_import_graph")
    private Boolean isImportGraph;

    //例式
    @Column(name = "count")
    @Basic
    private Integer count;

    @Column(name = "ps")
    @Basic
    private String ps;

}
