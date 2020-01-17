package com.plantdata.kgcloud.domain.dataset.entity;

import com.plantdata.kgcloud.domain.dataset.converter.AnnotationConfConverter;
import com.plantdata.kgcloud.sdk.req.AnnotationConf;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
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
import java.util.List;


/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "data_set_annotation")
@EntityListeners(AuditingEntityListener.class)
public class DataSetAnnotation {

    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "user_id")
    private String userId;

    @Basic
    @Column(name = "kg_name")
    private String kgName;

    @Basic
    @Column(name = "task_id")
    private Integer taskId;

    @Basic
    @Column(name = "dataset_id")
    private Long dataId;

    @Basic
    @Column(name = "title")
    private String name;

    @Basic
    @Column(name = "config")
    @Convert(converter = AnnotationConfConverter.class)
    private List<AnnotationConf> config;

    @Basic
    @Column(name = "remark")
    private String description;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
