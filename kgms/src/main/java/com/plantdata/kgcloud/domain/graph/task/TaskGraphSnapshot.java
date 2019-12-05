package com.plantdata.kgcloud.domain.graph.task;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
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
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

@Data
@Entity
@Table(name = "task_graph_snapshot")
@EntityListeners(AuditingEntityListener.class)
public class TaskGraphSnapshot {

    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "kg_name")
    private String kgName;

    @Basic
    @Column(name = "user_id")
    @CreatedBy
    private String userId;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "file_size")
    private String fileSize;

    @Basic
    @Column(name = "status")
    private Integer status;

    @Basic
    @Column(name = "restore_at")
    private Date restoreAt;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
