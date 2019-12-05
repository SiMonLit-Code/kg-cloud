package com.plantdata.kgcloud.domain.audit;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "api_audit")
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
    @Column(name = "invoke_at")
    private Date invokeAt;

}
