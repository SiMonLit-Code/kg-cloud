package com.plantdata.kgcloud.domain.share.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jdm on 2019/12/7 16:28.
 */

//@Data
//@Entity
//@Builder
//@Table(name = "system_limit")
//@EntityListeners(AuditingEntityListener.class)
//public class SystemLimit {
//
//    @Basic
//    @Column(name = "user_id")
//    @CreatedBy
//    private String userId;
//
//    @Basic
//    @Column(name = "dataset_count")
//    private Integer datasetCount;
//
//    @Basic
//    @Column(name = "graph_count")
//    private Integer graph_count;
//
//    @Basic
//    @Column(name = "task_count")
//    private Integer taskCount;
//
//    @Basic
//    @Column(name = "is_shareable")
//    private Boolean is_shareable;
//
//    @Basic
//    @Column(name = "create_at", updatable = false)
//    @CreatedDate
//    private Date createAt;
//
//    @Basic
//    @Column(name = "update_at")
//    @LastModifiedDate
//    private Date updateAt;
//}
