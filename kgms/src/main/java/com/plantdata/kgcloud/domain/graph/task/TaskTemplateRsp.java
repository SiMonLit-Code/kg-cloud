package com.plantdata.kgcloud.domain.graph.task;

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
@Table(name = "task_template")
public class TaskTemplateRsp {
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "template")
    private String template;
    @Basic
    @Column(name = "create_at", updatable = false)
    private Date createAt;
    @Basic
    @Column(name = "update_at")
    private Date updateAt;

}
