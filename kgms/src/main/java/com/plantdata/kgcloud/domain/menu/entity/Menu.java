package com.plantdata.kgcloud.domain.menu.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.util.Date;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Menu {

    @Id
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "title")
    private String name;

    @Basic
    @Column(name = "is_enable")
    private Boolean enable;

    @Basic
    @Column(name = "is_checked")
    private Boolean checked;

    @Basic
    @Column(name = "menu_type")
    private String type;

    @Basic
    @Column(name = "rank")
    private Integer order;

    @Basic
    @Column(name = "config")
    private String config;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
