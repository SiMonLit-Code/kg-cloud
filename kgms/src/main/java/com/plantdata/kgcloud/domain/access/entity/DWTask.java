package com.plantdata.kgcloud.domain.access.entity;


import com.plantdata.kgcloud.domain.common.converter.StringListConverter;
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
import java.util.List;

@Data
@Entity
@Table(name = "dw_task")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class DWTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "user_id")
    private String userId;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "task_type")
    private String taskType;

    @Basic
    @Column(name = "config")
    private String config;

    @Basic
    @Column(name = "status")
    private Integer status;

    @Basic
    @Column(name = "outputs")
    @Convert(converter = StringListConverter.class)
    private List<String> outputs;

    @Basic
    @Column(name = "distribute_original_data")
    @Convert(converter = StringListConverter.class)
    private List<String> distributeOriginalData;

    @Basic
    @Column(name = "create_at")
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
