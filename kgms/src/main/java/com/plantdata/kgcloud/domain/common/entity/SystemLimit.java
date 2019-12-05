package com.plantdata.kgcloud.domain.common.entity;

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
@Table(name = "system_limit")
@EntityListeners(AuditingEntityListener.class)
public class SystemLimit {

    @Id
    @Column(name = "user_id")
    @CreatedBy
    private String userId;

    @Basic
    @Column(name = "dataset_count")
    private Integer datasetCount;

    @Basic
    @Column(name = "graph_count")
    private Integer graphCount;

    @Basic
    @Column(name = "task_count")
    private Integer taskCount;

    @Basic
    @Column(name = "is_shareable")
    private Boolean shareable;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
