package com.plantdata.kgcloud.domain.dataset.entity;

import com.plantdata.kgcloud.domain.dataset.constant.DataType;
import com.plantdata.kgcloud.domain.dataset.converter.AddressConverter;
import com.plantdata.kgcloud.domain.common.converter.StringListConverter;
import com.plantdata.kgcloud.domain.dataset.converter.DataSetSchemaConverter;
import com.plantdata.kgcloud.domain.dataset.converter.DataTypeConverter;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
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
@Table(name = "data_set")
@EntityListeners(AuditingEntityListener.class)
public class DataSet {

    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "folder_id")
    private Long folderId;

    @Basic
    @Column(name = "user_id")
    @CreatedBy
    private String userId;

    @Basic
    @Column(name = "data_name")
    private String dataName;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "is_private")
    private Boolean privately;

    @Basic
    @Column(name = "is_editable")
    private Boolean editable;

    @Basic
    @Column(name = "data_type")
    @Convert(converter = DataTypeConverter.class)
    private DataType dataType;

    @Basic
    @Column(name = "create_type")
    private Integer createType;

    @Basic
    @Column(name = "addr")
    @Convert(converter = AddressConverter.class)
    private List<String> addr;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "db_name")
    private String dbName;

    @Basic
    @Column(name = "tb_name")
    private String tbName;

    @Basic
    @Column(name = "create_way")
    private String createWay;

    @Basic
    @Column(name = "fields")
    @Convert(converter = StringListConverter.class)
    private List<String> fields;

    @Basic
    @Column(name = "schema_config")
    @Convert(converter = DataSetSchemaConverter.class)
    private List<DataSetSchema> schema;

    @Basic
    @Column(name = "mapper")
    private String mapper;

    @Basic
    @Column(name = "skill_config")
    private String skillConfig;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;
}
