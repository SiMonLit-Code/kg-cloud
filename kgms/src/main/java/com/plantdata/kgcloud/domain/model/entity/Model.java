package com.plantdata.kgcloud.domain.model.entity;

import com.plantdata.kgcloud.domain.common.converter.StringListConverter;
import com.plantdata.kgcloud.domain.model.converter.ModelPrfConverter;
import com.plantdata.kgcloud.sdk.req.ModelPrf;
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
@EntityListeners(AuditingEntityListener.class)
public class Model {

    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "user_id")
    private String userId;

    @Basic
    @Column(name = "model_name")
    private String modelName;

    @Basic
    @Column(name = "model_url")
    private String modelUrl;

    @Basic
    @Column(name = "model_type")
    private Integer modelType;

    @Basic
    @Column(name = "prf")
    @Convert(converter = ModelPrfConverter.class)
    private ModelPrf prf;

    @Basic
    @Column(name = "labels")
    @Convert(converter = StringListConverter.class)
    private List<String> labels;

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
