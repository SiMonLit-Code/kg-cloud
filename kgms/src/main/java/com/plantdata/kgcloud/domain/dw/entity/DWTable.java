package com.plantdata.kgcloud.domain.dw.entity;

import com.plantdata.kgcloud.domain.common.converter.StringListConverter;
import com.plantdata.kgcloud.domain.dataset.converter.DataSetSchemaConverter;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
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
@Table(name = "dw_table")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class DWTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "tb_name")
    private String tbName;

    @Basic
    @Column(name = "table_name")
    private String tableName;

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
    @Column(name = "create_type")
    private String createType;

    @Basic
    @Column(name = "dw_database_id")
    private Long dwDatabaseId;

    @Basic
    @Column(name = "query_field")
    private String queryField;

    @Basic
    @Column(name = "cron")
    private String cron;

    @Basic
    @Column(name = "is_all")
    private Integer isAll;

    @Basic
    @Column(name = "create_at")
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
