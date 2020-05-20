package com.plantdata.kgcloud.domain.task.entity;

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
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

@Data
@Entity

@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate

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
