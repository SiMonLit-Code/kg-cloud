package com.plantdata.kgcloud.domain.audit.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
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
@Table(name = "api_audit")
@EntityListeners(AuditingEntityListener.class)
public class ApiAudit {
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "url")
    private String url;
    @Basic
    @Column(name = "kg_name")
    private String kgName;
    @Basic
    @Column(name = "page")
    private String page;
    @Basic
    @Column(name = "status")
    private Integer status;
    @Basic
    @Column(name = "invoke_at", updatable = false)
    @CreatedDate
    private Date invokeAt;

}
