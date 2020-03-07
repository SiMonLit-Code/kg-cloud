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

@Data
@Entity
@Builder
@Table(name = "dw_prebuild_model")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class DWPrebuildModel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "status")
    private String status;

    @Basic
    @Column(name = "model_type")
    private String modelType;

    @Basic
    @Column(name = "model_tag")
    private String modelTag;

    @Basic
    @Column(name = "user_id")
    private String userId;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "permission")
    private Integer permission;

    @Basic
    @Column(name = "standard_template_id")
    private Long standardTemplateId;

    @Basic
    @Column(name = "database_id")
    private Long databaseId;

    @Basic
    @Column(name = "create_at")
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;
}
