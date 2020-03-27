package com.plantdata.kgcloud.domain.dw.entity;


import com.plantdata.kgcloud.domain.dw.converter.ListStandardTemplateSchemaConverter;
import com.plantdata.kgcloud.domain.dw.rsp.StandardTemplateSchema;
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
@Table(name = "dw_standard_template")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class DWStandardTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "ktr")
    private String ktr;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "file_content")
    private String fileContent;

    @Basic
    @Column(name = "status")
    private String status;

    @Basic
    @Column(name = "yaml_content")
    private String yamlContent;

    @Basic
    @Column(name = "tag_json")
    private String tagJson;

    @Basic
    @Column(name = "schemas")
    @Convert(converter = ListStandardTemplateSchemaConverter.class)
    private List<StandardTemplateSchema> stSchemas;

    @Basic
    @Column(name = "create_at")
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;
}
