package com.plantdata.kgcloud.domain.graph.attr.entity;

import com.plantdata.kgcloud.domain.edit.req.attr.AttrTemplateReq;
import com.plantdata.kgcloud.domain.graph.attr.converter.AttrTemplateConverter;
import lombok.Data;
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
@Table(name = "graph_attr_template")
@EntityListeners(AuditingEntityListener.class)
public class GraphAttrTemplate {

    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "abs")
    private String abs;

    @Column(name = "config")
    @Convert(converter = AttrTemplateConverter.class)
    private List<AttrTemplateReq> config;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;
    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
