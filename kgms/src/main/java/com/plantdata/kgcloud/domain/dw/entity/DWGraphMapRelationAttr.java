package com.plantdata.kgcloud.domain.dw.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-03-21 16:25
 **/
@Data
@Entity
@Builder
@Table(name = "dw_graph_map_relation_attr")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class DWGraphMapRelationAttr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "kg_name")
    private String kgName;

    @Basic
    @Column(name = "create_at")
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

    @Basic
    @Column(name = "model_id")
    private Integer modelId;

    @Basic
    @Column(name = "model_attr_id")
    private Integer modelAttrId;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "table_name")
    private String tableName;

    @Basic
    @Column(name = "attr_id")
    private Integer attrId;

    @Basic
    @Column(name = "data_base_id")
    private Long dataBaseId;

    @Basic
    @Column(name = "data_type")
    private Integer dataType;

    @Column(name = "unit")
    private String unit;

    @Basic
    @Column(name = "scheduling_switch")
    private Integer schedulingSwitch;
}
