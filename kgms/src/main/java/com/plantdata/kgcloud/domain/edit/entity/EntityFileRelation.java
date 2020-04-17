package com.plantdata.kgcloud.domain.edit.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author EYE
 */
@Data
@Entity
@Builder
@Table(name = "entity_file_relation")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class EntityFileRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "kg_name")
    private String kgName;

    @Basic
    @Column(name = "entity_id")
    private Long entityId;

    @Basic
    @Column(name = "`name`")
    private String name;

    @Basic
    @Column(name = "`type`")
    private String type;

    @Basic
    @Column(name = "`keyword`")
    private String keyword;

    @Basic
    @Column(name = "`description`")
    private String description;

    @Basic
    @Column(name = "dw_file_id")
    private Integer dwFileId;

    @Basic
    @Column(name = "multi_modal_id")
    private String multiModalId;

    @Basic
    @Column(name = "create_at")
    @CreatedDate
    private Date createAt;

}
