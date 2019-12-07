package com.plantdata.kgcloud.domain.document.entity;

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


@Data
@Entity
@Builder
@Table(name = "document")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Document {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Basic
    @Column(name = "scene_id")
    private Integer sceneId;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createTime;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateTime;

    @Basic
    @Column(name = "source")
    private String source;

    @Basic
    @Column(name = "doc_status")
    private Integer docStatus;

    @Basic
    @Column(name = "model_status")
    private Integer modelStatus;

    @Basic
    @Column(name = "doc_type")
    private String docType;

    @Basic
    @Column(name = "doc_size")
    private Long docSize;


}