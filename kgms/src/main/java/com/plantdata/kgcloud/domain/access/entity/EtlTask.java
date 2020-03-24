package com.plantdata.kgcloud.domain.access.entity;

import com.plantdata.kgcloud.domain.common.converter.StringListConverter;
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
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "access_etl_task")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class EtlTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "database_id")
    private Long databaseId;

    @Basic
    @Column(name = "table_id")
    private Long tableId;


    @Basic
    @Column(name = "status")
    private Integer status;

    @Basic
    @Column(name = "task_id")
    private Integer taskId;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "config")
    private String config;

    @Basic
    @Column(name = "create_at")
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;
}
