package ai.plantdata.kgcloud.domain.graph.config.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

@Data
@Entity
@Table(name = "graph_conf_kgql")
@EntityListeners(AuditingEntityListener.class)
public class GraphConfKgql {

    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "kg_name")
    private String kgName;

    @Basic
    @Column(name = "rule_type")
    private Integer ruleType;

    @Basic
    @Column(name = "kgql_name")
    private String kgqlName;

    @Basic
    @Column(name = "kgql")
    private String kgql;

    @Basic
    @Column(name = "rule_settings")
    private String ruleSettings;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
