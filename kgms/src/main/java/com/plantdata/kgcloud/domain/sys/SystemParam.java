package com.plantdata.kgcloud.domain.sys;

import lombok.Data;
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
@Table(name = "system_param")
@EntityListeners(AuditingEntityListener.class)
public class SystemParam {

    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "param_group")
    private String paramGroup;

    @Basic
    @Column(name = "param_key")
    private String paramKey;

    @Basic
    @Column(name = "param_value")
    private String paramValue;

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
