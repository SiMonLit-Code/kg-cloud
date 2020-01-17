package com.plantdata.kgcloud.domain.graph.manage.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Date;


/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

@Data
@Entity
@IdClass(GraphPk.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Graph {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "kg_name")
    private String kgName;

    @Basic
    @Column(name = "db_name")
    private String dbName;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "icon")
    private String icon;

    @Basic
    @Column(name = "is_privately")
    private Boolean privately;

    @Basic
    @Column(name = "is_editable")
    private Boolean editable;

    @Basic
    @Column(name = "is_deleted")
    private Boolean deleted;

    @Basic
    @Column(name = "remark")
    private String remark;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
