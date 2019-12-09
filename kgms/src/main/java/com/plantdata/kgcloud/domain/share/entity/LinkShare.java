package com.plantdata.kgcloud.domain.share.entity;

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
@Table(name = "link_share")
@EntityListeners(AuditingEntityListener.class)
public class LinkShare {

    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "user_id")
    @CreatedBy
    private String userId;

    @Basic
    @Column(name = "kg_name")
    private String kgName;

    @Basic
    @Column(name = "spa_id")
    private String spaId;

    @Basic
    @Column(name = "share_link")
    private String shareLink;

    @Basic
    @Column(name = "is_shared")
    private Boolean shared;

    @Basic
    @Column(name = "total_scan")
    private Long totalScan;

    @Basic
    @Column(name = "expire_at")
    private Date expireAt;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
