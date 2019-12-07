package com.plantdata.kgcloud.domain.document.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Builder
@Table(name = "concept")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Concept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "document_id")
    @Basic
    private Integer documentId;

    @Column(name = "name")
    @Basic
    private String Name;

    @Column(name = "old_name")
    @Basic
    private String oldName;

    @Column(name = "is_import_graph")
    @Basic
    private Boolean isImportGraph;

}
