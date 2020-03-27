package com.plantdata.kgcloud.domain.dw.entity;

import com.plantdata.kgcloud.domain.dw.converter.ListStandardTemplateSchemaConverter;
import com.plantdata.kgcloud.domain.dw.converter.MoldSchemaConfigConverter;
import com.plantdata.kgcloud.domain.dw.converter.TableKtrConverter;
import com.plantdata.kgcloud.domain.dw.rsp.ModelSchemaConfigRsp;
import com.plantdata.kgcloud.domain.dw.rsp.StandardTemplateSchema;
import com.plantdata.kgcloud.domain.dw.rsp.TableKtrRsp;
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
@Table(name = "dw_prebuild_model")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class DWPrebuildModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "status")
    private String status;

    @Basic
    @Column(name = "model_type")
    private String modelType;

    @Basic
    @Column(name = "user_id")
    private String userId;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "permission")
    private Integer permission;

    @Basic
    @Column(name = "is_standard_template")
    private Integer isStandardTemplate;

    @Basic
    @Column(name = "database_id")
    private Long databaseId;

    @Basic
    @Column(name = "create_at")
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;


    @Basic
    @Column(name = "ktr")
    @Convert(converter = TableKtrConverter.class)
    private List<TableKtrRsp> ktr;

    @Basic
    @Column(name = "yaml_content")
    private String yamlContent;

    @Basic
    @Column(name = "file_content")
    private String fileContent;

    @Basic
    @Column(name = "tag_json")
    @Convert(converter = MoldSchemaConfigConverter.class)
    private List<ModelSchemaConfigRsp> tagJson;

    @Basic
    @Column(name = "`schemas`")
    @Convert(converter = ListStandardTemplateSchemaConverter.class)
    private List<StandardTemplateSchema> schemas;
}
