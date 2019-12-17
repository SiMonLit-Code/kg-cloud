package com.plantdata.kgcloud.domain.task.entity;

import com.plantdata.kgcloud.domain.edit.converter.JpaMapConverter;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/12/16 17:39
 * @Description:
 */
@Data
@Entity
@Table(name="task_graph_status")
@EntityListeners(AuditingEntityListener.class)
public class TaskGraphStatus {

    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "kg_name")
    private String kgName;


    @Column(name = "params")
    @Convert(converter = JpaMapConverter.class)
    private Map<String,Object> params;

    @Basic
    @Column(name = "task_type")
    private String type;

    @Basic
    @Column(name = "task_status")
    private String status;

    @Basic
    @CreatedDate
    @Column(name = "create_at")
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;
}
