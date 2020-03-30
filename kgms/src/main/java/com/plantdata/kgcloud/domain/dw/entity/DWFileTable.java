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
 * @create: 2020-03-30 19:52
 **/
@Data
@Entity
@Builder
@Table(name = "dw_file_table")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class DWFileTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "`name`")
    private String name;

    @Basic
    @Column(name = "create_at")
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

    @Basic
    @Column(name = "`path`")
    private String path;

    @Basic
    @Column(name = "`type`")
    private String type;

    @Basic
    @Column(name = "`file_size`")
    private Long fileSize;

    @Basic
    @Column(name = "`keyword`")
    private String keyword;

    @Basic
    @Column(name = "`description`")
    private String description;

    @Basic
    @Column(name = "`owner`")
    private String owner;

    @Basic
    @Column(name = "`user_id`")
    private String userId;

    @Basic
    @Column(name = "`table_id`")
    private Long tableId;

    @Basic
    @Column(name = "`database_id`")
    private Long dataBaseId;
}
