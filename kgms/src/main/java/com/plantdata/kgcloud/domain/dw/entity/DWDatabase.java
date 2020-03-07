package com.plantdata.kgcloud.domain.dw.entity;

import com.plantdata.kgcloud.domain.dataset.converter.AddressConverter;
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
@Table(name = "dw_database")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class DWDatabase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "user_id")
    private String userId;

    @Basic
    @Column(name = "data_type")
    private Integer dataType;

    @Basic
    @Column(name = "data_format")
    private Integer dataFormat;

    @Basic
    @Column(name = "create_way")
    private Integer createWay;

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
    @Column(name = "data_name")
    private String dataName;

    @Basic
    @Column(name = "standard_template_id")
    private Long standardTemplateId;

    @Basic
    @Column(name = "yaml_content")
    private String yamlContent;

    @Basic
    @Column(name = "create_at")
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;


}
