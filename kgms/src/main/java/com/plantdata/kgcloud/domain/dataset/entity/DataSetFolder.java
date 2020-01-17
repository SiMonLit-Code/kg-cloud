package com.plantdata.kgcloud.domain.dataset.entity;

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
@Table(name = "data_set_folder")
@EntityListeners(AuditingEntityListener.class)
public class DataSetFolder {

    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "user_id")
    private String userId;

    @Basic
    @Column(name = "folder_name")
    private String folderName;

    @Basic
    @Column(name = "is_defaulted")
    private Boolean defaulted;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
