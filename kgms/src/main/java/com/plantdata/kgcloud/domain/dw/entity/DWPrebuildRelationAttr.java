package com.plantdata.kgcloud.domain.dw.entity;

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
@Builder
@Table(name = "dw_prebuild_relation_attr")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class DWPrebuildRelationAttr {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "unit")
    private String unit;

    @Basic
    @Column(name = "attr_id")
    private Integer attrId;

    @Basic
    @Column(name = "model_id")
    private Integer modelId;

    @Basic
    @Column(name = "data_type")
    private Integer dataType;

    @Basic
    @Column(name = "tables")
    @Convert(converter = StringListConverter.class)
    private List<String> tables;

    @Basic
    @Column(name = "create_at")
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;
}
